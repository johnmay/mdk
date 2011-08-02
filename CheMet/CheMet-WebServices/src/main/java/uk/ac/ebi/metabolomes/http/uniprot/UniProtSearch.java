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
import java.util.Arrays;
import java.util.List;
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

/**
 * UniProtSearch.java – MetabolicDevelopmentKit – Jun 22, 2011
 *
 * @author johnmay <johnmay@ebi.ac.uk, john.wilkinsonmay@gmail.com>
 */
public class UniProtSearch {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( UniProtSearch.class );
    private HttpClient client;
    private static final String baseDomain = "www.uniprot.org";
    private static final String toolDomain = "uniprot";

    private UniProtSearch() {
        // set up the HTTPClient
        client = new DefaultHttpClient();
    }

    /**
     * Singleton access method
     * @return Instance of the Identifier Mapper
     */
    public static UniProtSearch getInstance() {
        return UniProtSearchHolder.INSTANCE;
    }

    /**
     * Singleton holder
     */
    private static class UniProtSearchHolder {

        private static UniProtSearch INSTANCE = new UniProtSearch();
    }

    public HttpEntity search( List<NameValuePair> params ) {

        HttpEntity entity = null;

        try {
            // create the Get method
            URI uri = URIUtils.createURI( "http" ,
                                          baseDomain ,
                                          -1 ,
                                          '/' + toolDomain ,
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

        return entity;
    }

    public static void main( String[] args ) throws IOException {

        List params = new ArrayList<NameValuePair>();

        UniProtSearchQuery query = new UniProtSearchQuery( "" );
        query.setAndEnzymeCommissionNumber( "1.1.1.1" );
        params.add( new BasicNameValuePair( "query" , query.buildQuery() ) );
        params.add( new BasicNameValuePair( "format" , "tab" ) );
        params.add( new BasicNameValuePair( "columns" , "id,ec" ) );
        HttpEntity entity = UniProtSearch.getInstance().search( params );
        CSVReader reader = new CSVReader( new BufferedReader( new InputStreamReader( entity.getContent() ) ) );

        String[] values = null;
        while ( ( values = reader.readNext() ) != null ) {
            System.out.println( Arrays.asList( values ) );
        }

    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        client.getConnectionManager().shutdown();
    }


}
