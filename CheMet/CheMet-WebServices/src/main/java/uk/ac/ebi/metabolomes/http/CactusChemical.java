/**
 * CactusChemical.java
 *
 * 2011.07.29
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
package uk.ac.ebi.metabolomes.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

/**
 * @name    CactusChemical
 * @date    2011.07.29
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 * @brief   Helper for accessing data from the NCI CADD Group Chemoinformatics Tools and User Services
 * chemical structure website http://cactus.nci.nih.gov/chemical/structure/
 *
 */
public class CactusChemical {

    private static final Logger LOGGER = Logger.getLogger( CactusChemical.class );
    private HttpClient client;
    private static final String BASE_DOMAIN = "cactus.nci.nih.gov";
    private static final String CHEMICAL_STRUCTURE_PATH = "/chemical/structure/";
    private static final String NAME_SEARCH = "/names";

    private CactusChemical() {
        client = new DefaultHttpClient();
    }

    private static class CactusChemicalHolder {

        private static final CactusChemical INSTANCE = new CactusChemical();
    }

    /**
     * Accessor method for instance of the object
     * @return Singleton instance of the class
     */
    public static CactusChemical getInstance() {
        return CactusChemicalHolder.INSTANCE;
    }

    /**
     * Method submits a name to the service and returns a list of synonyms/identifies
     * the address is http://cactus.nci.nih.gov/chemical/structure/<chemical-name>/names
     *
     * @param compoundName a chemical name to search
     * @return a list of synonyms/identifiers
     */
    public List<String> getNames( String compoundName ) {
        List<String> names = new ArrayList<String>();
        try {

            // construct the URI, the URI has to be constructed inparts the the compoundName gets formated
            // in accordance with RFC (See. URI API)
            URI uri = new URI( "http" , BASE_DOMAIN , CHEMICAL_STRUCTURE_PATH + compoundName + NAME_SEARCH , null );
            HttpEntity entity = getPlainTextResponse( uri );

            // if an entity was returned
            if ( entity != null ) {

                InputStream stream = entity.getContent();
                Scanner scanner = new Scanner( stream );
                while ( scanner.hasNext() ) {

                    // unescape the response and add the list of returned names
                    names.add( StringEscapeUtils.unescapeHtml( scanner.next() ) );
                }
                // trigger connection release
                stream.close();
                scanner.close();
            }

        } catch ( IOException ex ) {
            LOGGER.error( "Problem reading response: " + ex.getMessage() );
        } catch ( IllegalStateException ex ) {
            LOGGER.error( "IllegalStateException: " + ex.getMessage() );
        } catch ( URISyntaxException ex ) {
            LOGGER.error( "Invalid URL  made: " + ex.getMessage() );
        }
        return names;
    }

    /**
     * Filters responses for only those which have content-type: text/plain; charset=UTF-8
     * @param uri
     * @return
     */
    private HttpEntity getPlainTextResponse( URI uri ) {
        HttpGet request = null;
        try {
            request = new HttpGet( uri );
            HttpResponse response = client.execute( request );
            String contentTypeValue = response.getEntity().getContentType().getValue();

            if ( contentTypeValue.equals( "text/plain; charset=UTF-8" ) ) {
                return response.getEntity();
            } else {
                request.abort();
                // no matches found
                return null;
            }
        } catch ( ClientProtocolException ex ) {
            LOGGER.error( "Could not execute request: " + ex.getMessage() );
        } catch ( IOException ex ) {
            LOGGER.error( "Could not execute request: " + ex.getMessage() );
        } finally {
            request.abort();
        }
        return null;
    }

    /**
     * Queries the supplied name to find the IUPAC name. If the IUPAC name is found
     * the method returns it. An empty string is return if nothing is found
     */
    public String getIUPACName( String name ) {
        return "IUPAC";
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        client.getConnectionManager().shutdown();

    }

    public static void main( String[] args ) {
        List<String> names = CactusChemical.getInstance().getNames( "Adenosine Diphosphate" );
        for ( String string : names ) {
            System.out.println( string );
        }
    }
}
