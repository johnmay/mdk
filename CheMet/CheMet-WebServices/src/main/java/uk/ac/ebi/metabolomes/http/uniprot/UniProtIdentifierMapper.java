/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.metabolomes.http.uniprot;

import au.com.bytecode.opencsv.CSVReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Appender;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;

/**
 * UniProtIdentifierMapper.java
 * A class to map UniPort Identifiers
 *
 * @author johnmay
 * @date May 18, 2011
 */
public class UniProtIdentifierMapper {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( UniProtIdentifierMapper.class );
    // tokens for identifier classes
    public static final String UNIREF_50 = "NF50";
    public static final String UNIREF_90 = "NF90";
    public static final String UNIREF_100 = "NF100";
    public static final String UNIPROT_ACC_ID = "ACC+ID";
    public static final String UNIPROT_ACC = "ACC";
    public static final String UNIPROT_ID = "ID";
    public static final String UNIPARC = "UPARC";
    public static final String KEGG = "KEGG_ID";
    // formats
    public static final String TAB_FORMAT = "tab";
    // param names
    private static final String FROM_PARAM = "from";
    private static final String TO_PARAM = "to";
    private static final String QUERY_PARAM = "query";
    private static final String FORTMAT_PARAM = "format";
    // uri
    private static final String uniprotAddress = "www.uniprot.org";
    private static final String mappingToolSubDomain = "mapping";
    // instance variabless
    private HttpClient client;

    private UniProtIdentifierMapper() {
        // set up the HTTPClient
        client = new DefaultHttpClient();
    }

    /**
     * Singleton access method
     * @return Instance of the Identifier Mapper
     */
    public static UniProtIdentifierMapper getInstance() {
        return UniprotIdentifierMappingHolder.INSTANCE;
    }

    /**
     * Singleton holder
     */
    private static class UniprotIdentifierMappingHolder {

        private static UniProtIdentifierMapper INSTANCE = new UniProtIdentifierMapper();
    }

    public Map getMapping( String to ,
                           List uniprotAccessions ) {
        return getMapping( UNIPROT_ACC , to , uniprotAccessions );
    }

    public Map getMapping( String from ,
                           String to ,
                           List identifiers ) {

        if ( identifiers.isEmpty()
             || identifiers.size() == 1 ) {
            logger.error( "Will not get http for one identifier as this is very wastefull. "
                          + "Please provide a longer list for more efficentcy" );
            return createMap( null );
        }

        // create the param list
        List params = new ArrayList<NameValuePair>();
        params.add( new BasicNameValuePair( FROM_PARAM , from ) );
        params.add( new BasicNameValuePair( TO_PARAM , to ) );
        params.add( new BasicNameValuePair( FORTMAT_PARAM , TAB_FORMAT ) );
        params.add( new BasicNameValuePair( QUERY_PARAM , joinList( identifiers ) ) );

        return getMapping( params );
    }

    public Map getMapping( List<NameValuePair> params ) {
        HttpEntity entity = null;
        try {
            // create the Get method
            URI uri = URIUtils.createURI( "http" ,
                                          uniprotAddress ,
                                          -1 ,
                                          '/' + mappingToolSubDomain ,
                                          URLEncodedUtils.format( params , "UTF8" ) ,
                                          null );
            System.out.println( uri );
            HttpGet get = new HttpGet( uri );
            // execute the method and get the entity
            HttpResponse response = client.execute( get );
            entity = response.getEntity();

        } catch ( ClientProtocolException ex ) {
            ex.printStackTrace();
        } catch ( IOException ex ) {
            ex.printStackTrace();
        } catch ( URISyntaxException ex ) {
            ex.printStackTrace();
        }

        // returns empty if the entity is null
        return createMap( entity );
    }

    private Map createMap( HttpEntity entity ) {
        ConcurrentHashMap<String , List<String>> identiferMap = new ConcurrentHashMap<String , List<String>>();
        if ( entity == null ) {
            return identiferMap;
        }
        try {
            CSVReader reader = new CSVReader( new BufferedReader( new InputStreamReader( entity.getContent() ) ) ,
                                              '\t' );
            // reads the headers
            String[] row = reader.readNext();

            while ( ( row = reader.readNext() ) != null ) {
                if ( row.length == 2 ) {
                    identiferMap.putIfAbsent( row[0] , new ArrayList<String>() );
                    identiferMap.get( row[0] ).add( row[1] );
                }

            }
            reader.close();
        } catch ( IOException ex ) {
            logger.error( "unable to parse returned entitiy" , ex );
        } catch ( IllegalStateException ex ) {
            logger.error( "illegal state exception" , ex );
        }
        return identiferMap;
    }

    /**
     * concatenates a list using commas
     * @param items
     * @return
     */
    private String joinList( List items ) {
        StringBuilder sb = new StringBuilder( 8 * items.size() );
        // only get unique items
        List unique = new ArrayList<String>( new HashSet<String>( items ) );
        for ( int i = 0; i < unique.size(); i++ ) {
            sb.append( unique.get( i ).toString() );
            if ( i != unique.size() - 1 ) {
                sb.append( ", " );
            }
        }
        return sb.toString();
    }

    public static void main( String[] args ) {
        org.apache.log4j.BasicConfigurator.configure();
        logger.setLevel( Level.ERROR );
        List upids = new ArrayList();
        upids.add( "P96718" );
        upids.add( "P96718" );
        UniProtIdentifierMapper mapper = UniProtIdentifierMapper.getInstance();
        Map<String , List<String>> map = mapper.getMapping( UniProtIdentifierMapper.UNIREF_50 , upids );
        for ( Entry e : map.entrySet() ) {
            System.out.println( e.getKey() + " -> " + e.getValue() );
        }
    }
}
