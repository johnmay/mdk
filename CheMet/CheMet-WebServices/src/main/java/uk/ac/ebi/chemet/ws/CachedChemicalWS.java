
package uk.ac.ebi.chemet.ws;

/**
 * CachedChemicalWS.java
 *
 * 2011.08.23
 *
 * This file is part of the CheMet library
 *
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CheMet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import org.apache.log4j.Logger;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.MDLV2000Reader;
import uk.ac.ebi.chemet.ws.exceptions.UnfetchableEntry;
import uk.ac.ebi.chemet.ws.exceptions.MissingStructureException;
import uk.ac.ebi.metabolomes.webservices.ChemicalDBWebService;


/**
 *          CachedChemicalWS – 2011.08.23 <br>
 *          Takes as input a ChemicalDBWebservice
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class CachedChemicalWS {

    private static final Logger LOGGER = Logger.getLogger( CachedChemicalWS.class );
    private final ChemicalDBWebService client;
    private static final String DEFAULT_CACHE_ROOT = System.getProperty( "user.home" ) +
                                                     File.separator +
                                                     ".ebi-chemet" +
                                                     File.separator +
                                                     ".cache";
    private File cacheDir = new File( DEFAULT_CACHE_ROOT );
    private Map<String , Object> objectCache = new HashMap<String , Object>( 400 , 0.9f );
    // default cache sizes
    private static final int DEFAULT_MEMORY_CACHE_SIZE = 300;
    private static final int DEFAULT_DISK_CACHE_SIZE = 1500; // keeps 1500 items in disk folder
    private static final int DEFAULT_DISK_CACHE_EXPIRY = 5;  // remove items older then 5 days
    //
    private ArrayBlockingQueue objectQueue;
    private PriorityBlockingQueue<File> diskQueue;
    // cache id
    private static final String CDK_ATOMCONTAINER = ".cdk-atomcontainer";
    private static final String MDL_STRING = ".mdl-string";
    private static final String NAME_STRING = ".name-string";


    /**
     *
     * Constructor takes a class extending the abstract ChemicalDBWebService and uses
     * this as a client to fetch structural info
     *
     * @param client
     * @param memoryQueueSize Size of the memory queue
     * @param cacheExpiry Number of days to consider an item expired
     * @param diskQueueSize Size of the disk queue, when the max size is reached the oldest items are
     *                      removed from the disk
     *
     */
    public CachedChemicalWS( ChemicalDBWebService client , Integer memoryQueueSize ,
                             Integer diskQueueSize ,
                             Integer cacheExpiry ) {

        this.client = client;

        cacheDir = new File( cacheDir , "." + client.getClass().getSimpleName() );
        cacheDir.mkdirs();

        LOGGER.debug( "Using " + cacheDir + " as cache location" );

        // instantiate the two queues... one in memory one on disk
        objectQueue = new ArrayBlockingQueue<String>( memoryQueueSize );

        Comparator<File> fileModifiedComparator = new Comparator<File>() {

            public int compare( File o1 , File o2 ) {
                Long o1Time = o1.lastModified();
                Long o2Time = o2.lastModified();
                return o1Time.compareTo( o2Time );
            }


        };

        diskQueue = new PriorityBlockingQueue<File>( diskQueueSize , fileModifiedComparator );

        // remove old items
        removeExpiredEntries( cacheExpiry );


    }


    /**
     *
     * Convenience constructor using default cache expiry of 5 days. All disk objects older then 5
     * days will be removed
     *
     * @param client The Chemical DB Client
     * @param memoryQueueSize The size of the queue of memory elements
     * @param diskQueueSize The size of the disk queue
     *
     */
    public CachedChemicalWS( ChemicalDBWebService client , Integer memoryQueueSize ,
                             Integer diskQueueSize ) {
        this( client , memoryQueueSize , diskQueueSize , DEFAULT_DISK_CACHE_EXPIRY );
    }


    /**
     *
     * Convenience constructor using default expiry time and disk queue size of 5 days and 1500 respectively
     *
     *
     * @param client
     * @param memoryQueueSize
     *
     */
    public CachedChemicalWS( ChemicalDBWebService client , Integer memoryQueueSize ) {
        this( client , memoryQueueSize , DEFAULT_DISK_CACHE_SIZE , DEFAULT_DISK_CACHE_EXPIRY );
    }


    public CachedChemicalWS( ChemicalDBWebService client ) {
        this( client , DEFAULT_MEMORY_CACHE_SIZE );
    }


    /**
     *
     * Removes items from the cache directory older then the provided number of days
     *
     * @param days Number of days at which an item is considered expired
     *
     */
    private void removeExpiredEntries( Integer days ) {

        long currentTime = System.currentTimeMillis();
        long expireTime = 86400000 * days;

        for ( File cachedFile : cacheDir.listFiles() ) {

            if ( ( currentTime - cachedFile.lastModified() ) > expireTime ) {
                cachedFile.delete();
            }

            // todo: add to queue (sorted by last modified time)
            diskQueue.add( cachedFile );

        }
    }


    /**
     *
     * Access the MDL file (as a string) for the given id. If the is cached on disk
     * '~/.ebi-chemet/.cache/' then this is loaded, otherwise the provided client
     * is used to fetch the MDL file (which is then stored on disk)
     *
     * @param id
     * @return
     *
     * @throws UnfetchableEntry
     * @throws MissingStructureException
     *
     */
    public String getMDLString( String id ) throws UnfetchableEntry ,
                                                   MissingStructureException {

        File cachedFile = new File( cacheDir , id + MDL_STRING );

        if ( cachedFile.exists() ) {
            cachedFile.setLastModified( System.currentTimeMillis() );
            return ( String ) read( cachedFile );
        }

        // fetcg from client
        String mdlString = client.getMDLString( id );

        write( cachedFile , mdlString );

        return mdlString;
    }


    /**
     *
     * Because CDK AtomContainers are non-serializable they are stored in a HashMap. If no AtomContainer
     * for the id has previously been constructed then the MDL file ({@see getMDLString()}) is fetched
     * and the structure created using MDLV2000Reader. The items of the cache are stored in a queue
     * which discards items if the queue becomes too large
     *
     *
     * @param id
     * @return
     *
     * @throws UnfetchableEntry
     * @throws MissingStructureException
     *
     */
    public IAtomContainer getAtomContainer( String id ) throws UnfetchableEntry ,
                                                               MissingStructureException {

        String objectId = id + CDK_ATOMCONTAINER;

        if ( objectCache.containsKey( objectId ) ) {

            objectQueue.remove( objectId ); // move to front
            objectQueue.add( objectId );

            return ( IAtomContainer ) objectCache.get( objectId );
        }

        String mdlString = this.getMDLString( id );

        MDLV2000Reader reader = new MDLV2000Reader( new StringReader( mdlString ) );
        IMolecule molecule = DefaultChemObjectBuilder.getInstance().newInstance( IMolecule.class );

        try {
            molecule = reader.read( molecule );
            reader.close();
        } catch ( IOException ex ) {
            throw new MissingStructureException( id );
        } catch ( CDKException ex ) {
            throw new MissingStructureException( id );
        }

        if ( molecule == null ) {
            throw new MissingStructureException( id );
        }

        molecule.setProperty( "Name" , getName( id ) );


        // store for to avoid IO overhead recall
        objectCache.put( objectId , molecule );

        // empty the queue if there are less then 10 slots left
        if ( objectQueue.remainingCapacity() < 10 ) {
            // empty until there are more then 100
            while ( objectQueue.remainingCapacity() < 100 ) {
                objectCache.remove( objectQueue.poll() );
            }
        }

        objectQueue.add( objectId );

        return ( AtomContainer ) molecule;

    }


    public String getName( String id ) throws UnfetchableEntry {

        String objectId = id + NAME_STRING;

        if ( objectCache.containsKey( objectId ) ) {

            objectQueue.remove( objectId ); // move to front
            objectQueue.add( objectId );

            return ( String ) objectCache.get( objectId );
        }

        File cachedFile = new File( cacheDir , objectId );

        if ( cachedFile.exists() ) {
            cachedFile.setLastModified( System.currentTimeMillis() );
            return ( String ) read( cachedFile );
        }


        // fetcg from client
        String name = client.getName( id );

        write( cachedFile , name );

        // store for to avoid IO overhead recall
        objectCache.put( objectId , name );

        // empty the queue if there are less then 10 slots left
        if ( objectQueue.remainingCapacity() < 10 ) {
            // empty until there are more then 100
            while ( objectQueue.remainingCapacity() < 100 ) {
                objectCache.remove( objectQueue.poll() );
            }
        }

        objectQueue.add( objectId );
        return name;
    }


    private Object read( File cachedFile ) {
        ObjectInputStream ois = null;
        Object obj = null;

        try {

            ois = new ObjectInputStream( new FileInputStream( cachedFile ) );
            obj = ois.readObject();
        } catch ( Exception ex ) {

            LOGGER.error( "There was a problem reading the cached file" );
        } finally {

            try {
                if ( ois != null ) {
                    ois.close();
                }
            } catch ( IOException ex ) {

                LOGGER.error( "Could not closed cached stream" );
            }
        }

        return obj;

    }


    private void write( File cachedFile , Object obj ) {
        ObjectOutputStream oos = null;

        try {

            oos = new ObjectOutputStream( new FileOutputStream( cachedFile ) );
            oos.writeObject( obj );

        } catch ( Exception ex ) {

            LOGGER.error( "There was a problem reading the cached file" );
        } finally {

            try {
                if ( oos != null ) {
                    oos.close();
                }
            } catch ( IOException ex ) {

                LOGGER.error( "Could not closed cached stream" );
            }
        }

        diskQueue.add( cachedFile );

        // empty the queue if there are less then 10 slots left
        if ( diskQueue.remainingCapacity() < 10 ) {
            // empty until there are more then 100 deleteing stored files as we go
            while ( diskQueue.remainingCapacity() < 100 ) {
                diskQueue.poll().delete();
            }
        }

    }


}

