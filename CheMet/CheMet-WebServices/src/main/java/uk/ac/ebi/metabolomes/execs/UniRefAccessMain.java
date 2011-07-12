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
package uk.ac.ebi.metabolomes.execs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.commons.lang.StringUtils;


/**
 * Example class for accessing UniRef
 * @author johnmay
 */
public class UniRefAccessMain {

    private static final String base = "http://www.uniprot.org";
    private static final Logger log = Logger.getAnonymousLogger();

    private static void run( String tool , List<NameValuePair> params )
            throws Exception {

        HttpClient client = new DefaultHttpClient();
        URI uri = URIUtils.createURI( "http" , "www.uniprot.org" , -1 , '/' + tool , URLEncodedUtils.format( params , "UTF8" ) , null );
        HttpGet post = new HttpGet( uri );

        HttpResponse response = client.execute( post );
//        if( response.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY ){
//            System.out.println( "moved..." );
//            Header locHeader = response.getFirstHeader( "Location");
//            post.abort();
//            post = new HttpPost(locHeader.getValue());
//            response = client.execute( post );
//        }
        // Get hold of the response entity
        HttpEntity entity = response.getEntity();

        // If the response does not enclose an entity, there is no need
        // to worry about connection release
        if ( entity != null ) {
            InputStream instream = entity.getContent();
            try {

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader( instream ) );
                // do something useful with the response
                String line = "";
                while ( ( line = reader.readLine() ) != null ) {
                    System.out.println( line );
                }

            } catch ( IOException ex ) {

                // In case of an IOException the connection will be released
                // back to the connection manager automatically
                throw ex;

            } catch ( RuntimeException ex ) {

                // In case of an unexpected exception you may want to abort
                // the HTTP request in order to shut down the underlying
                // connection and release it back to the connection manager.
                post.abort();
                throw ex;

            } finally {

                // Closing the input stream will trigger connection release
                instream.close();

            }

            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            client.getConnectionManager().shutdown();
        }
    }

    public static void main( String[] args )
            throws Exception {



        String[] ids = new String[]{ "Q8FG45" ,
                                     "Q7DBF9" ,
                                     "P37791" ,
                                     "O33952" ,
                                     "Q04873" ,
                                     "Q04872" ,
                                     "Q47329" ,
                                     "Q57346" ,
                                     "Q5X9A8" ,
                                     "Q8K5G5" ,
                                     "P0C0F5" ,
                                     "Q92GB1" ,
                                     "Q68VX0" ,
                                     "Q1RKF8" ,
                                     "O05973" ,
                                     "Q4UK39" ,
                                     "O32271" ,
                                     "P96718" ,
                                     "O54068" ,
                                     "O34862" ,
                                     "P11759" ,
                                     "Q887P8" ,
                                     "P59793" ,
                                     "O07299" ,
                                     "Q88NC4" };


        run( "mapping" , Arrays.asList( new NameValuePair[]{
                    new BasicNameValuePair( "from" , "ACC" ) ,
                    new BasicNameValuePair( "to" , "NF50" ) ,
                    new BasicNameValuePair( "format" , "tab" ) ,
                    new BasicNameValuePair( "query" , StringUtils.join( ids, ','))
                 } ) );

    }
}
