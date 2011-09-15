/*
 *     This file is part of Metabolic Network Builder
 *
 *     Metabolic Network Builder is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Foobar is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.ebi.resource;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import org.apache.log4j.Logger;
import uk.ac.ebi.chemet.interfaces.entities.Identifier;
import uk.ac.ebi.metabolomes.identifier.AbstractIdentifier;
import uk.ac.ebi.metabolomes.identifier.GenericIdentifier;
import uk.ac.ebi.metabolomes.identifier.InChI;
import uk.ac.ebi.metabolomes.identifier.MIRIAMEntry;
import uk.ac.ebi.metabolomes.resource.Resource;
import uk.ac.ebi.resource.chemical.ChEBIIdentifier;
import uk.ac.ebi.resource.chemical.KEGGCompoundIdentifier;
import uk.ac.ebi.resource.organism.Taxonomy;
import uk.ac.ebi.resource.protein.SwissProtIdentifier;
import uk.ac.ebi.resource.protein.TrEMBLIdentifier;
import uk.ac.ebi.resource.protein.UniProtIdentifier;


/**
 * IdentifierFactory.java
 *
 *
 * @author johnmay
 * @date May 6, 2011
 */
public class IdentifierFactory {

    private static final Logger logger = Logger.getLogger(IdentifierFactory.class);
    private static final String IDENTIFIER_MAPPING_FILE = "IdentifierResourceMapping.properties";
    private static final Identifier[] identifiers = new Identifier[Byte.MAX_VALUE];
    private List<Identifier> supportedIdentifiers = new ArrayList<Identifier>(Arrays.asList(
      new ChEBIIdentifier(),
      new KEGGCompoundIdentifier(),
      new UniProtIdentifier(),
      new TrEMBLIdentifier(),
      new SwissProtIdentifier(),
      new Taxonomy(),
      new InChI()));


    public List<Identifier> getSupportedIdentifiers() {
        return supportedIdentifiers;
    }


    private IdentifierFactory() {
        for( Identifier identifier : supportedIdentifiers ) {
            identifiers[identifier.getIndex()] = identifier;
        }
    }


    public static class IdentifierFactoryHolder {

        public static IdentifierFactory INSTANCE = new IdentifierFactory();
    }


    public static IdentifierFactory getInstance() {
        return IdentifierFactoryHolder.INSTANCE;
    }


    /**
     * Builds an identifier given the accession
     * Uses the identifier parse method to validate ids (slower)
     * @param resource
     * @param accession
     */
    public static AbstractIdentifier getIdentifier(Resource resource, String accession) {

        Constructor constructor = resource.getIdentifierConstructor();
        if( constructor != null ) {
            try {
                return (AbstractIdentifier) constructor.newInstance(accession, true);
            } catch( InstantiationException ex ) {
                ex.printStackTrace();
            } catch( IllegalAccessException ex ) {
                ex.printStackTrace();
            } catch( IllegalArgumentException ex ) {
                ex.printStackTrace();
            } catch( InvocationTargetException ex ) {
                ex.printStackTrace();
            }
        }
        return new GenericIdentifier(accession);
    }


    public static AbstractIdentifier getUncheckedIdentifier(Resource resource, String accession) {

        Constructor constructor = resource.getIdentifierConstructor();
        if( constructor != null ) {
            try {
                return (AbstractIdentifier) constructor.newInstance(accession, false);
            } catch( InstantiationException ex ) {
                ex.printStackTrace();
            } catch( IllegalAccessException ex ) {
                ex.printStackTrace();
            } catch( IllegalArgumentException ex ) {
                ex.printStackTrace();
            } catch( InvocationTargetException ex ) {
                ex.printStackTrace();
            }
        }
        return new GenericIdentifier(accession);
    }
//
//    public static void main( String[] args ) {
//
//        GeneProduct product = new GeneProteinProduct();
//        UniProtIdentifier id = ( UniProtIdentifier ) IdentifierFactory.getIdentifier( Resource.UNIPROT , "QYEUEE.1" );
//        System.out.println( "Created Id:" + id );
//
//        // multiple id parsing
//        String ids = "gi|254777906|sp|B9E8Z7.1|DNAA_MACCJ";
//        List idList = IdentifierFactory.getIdentifiers( ids );
//        for ( Object object : idList ) {
//            System.out.println( object.getClass() + " " + object );
//        }
//
//    }


    /**
     * Builds a list of identifiers from a string that may
     * or maynot contain multiple identifiers
     * atm: handle gi|39327|sp|398339 etc..
     * @param idsString
     * @return
     */
    public static List<AbstractIdentifier> getIdentifiers(String idsString) {

        List<AbstractIdentifier> hitIdentifiers = new ArrayList<AbstractIdentifier>();

        if( idsString.contains(ID_SEPERATOR) ) {

            ListIterator<String> it = Arrays.asList(idsString.split(ID_ESCAPED_SEPERATOR)).
              listIterator();

            // db identifiers , gi,sp,tr etc..
            while( it.hasNext() ) {

                String dbid = it.next();

                if( dbid.length() <= DBID_MAX_LENGTH ) {
                    Resource r = Resource.getResource(dbid);

                    if( r != Resource.UNKNOWN && r != Resource.GENERAL ) {
                        hitIdentifiers.add(IdentifierFactory.getIdentifier(r, it.next()));
                    } else if( r == Resource.GENERAL && it.hasNext() ) {
                        dbid = it.next();
                        r = Resource.getResource(dbid);
                        if( r != Resource.UNIPROT ) {
                            hitIdentifiers.add(IdentifierFactory.getIdentifier(r, it.next()));
                        } else {
                            it.previous();
                        }
                    }

                } else {

                    hitIdentifiers.add(new GenericIdentifier(dbid));
                }
            }
        } else {
            hitIdentifiers.add(new GenericIdentifier(idsString));
        }

        return hitIdentifiers;
    }


    /**
     *
     * Access the MIRIAMEntry for the given identifier. If no MIRIAMResouce is available then null is returned
     *
     * @param idClass
     * @return
     */
    public static MIRIAMEntry getResouce(Class<? extends AbstractIdentifier> idClass) {
//        if(idClass == ChEBIIdentifier.class){
//            return MIRIAMEntry
//        }
        return null;
    }


    /**
     *
     * Returns and identifier
     *
     * @param <T>
     * @param type
     * @return
     */
    public Identifier ofClass(Class<? extends Identifier> type) {
        return ofIndex(IdentifierLoader.getInstance().getIndex(type));
    }


    public Identifier ofIndex(Byte index) {
        return identifiers[index].newInstance();
    }



    private static final String ID_SEPERATOR = "|";
    private static final String ID_ESCAPED_SEPERATOR = "\\|";
    private static final int DBID_MAX_LENGTH = 3;
}

