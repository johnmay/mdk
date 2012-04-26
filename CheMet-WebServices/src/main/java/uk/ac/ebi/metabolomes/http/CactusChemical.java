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

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import uk.ac.ebi.annotation.crossreference.CrossReference;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.metabolomes.webservices.ICrossReferenceProvider;
import uk.ac.ebi.resource.DefaultIdentifierFactory;
import uk.ac.ebi.chemet.resource.chemical.BRNIdentifier;
import uk.ac.ebi.chemet.resource.chemical.CASIdentifier;
import uk.ac.ebi.chemet.resource.chemical.ChEBIIdentifier;
import uk.ac.ebi.chemet.resource.chemical.EINECSIdentifier;
import uk.ac.ebi.chemet.resource.chemical.EPAPesticideIdentifier;
import uk.ac.ebi.chemet.resource.chemical.HSDBIdentifier;
import uk.ac.ebi.chemet.resource.chemical.KEGGCompoundIdentifier;
import uk.ac.ebi.chemet.resource.chemical.ZINCIdentifier;

/**
 * @name    CactusChemical
 * @date    2011.07.29
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   Helper for accessing data from the NCI CADD Group Chemoinformatics Tools and User Services
 * chemical structure website http://cactus.nci.nih.gov/chemical/structure/
 *
 */
public class CactusChemical implements ICrossReferenceProvider {

    private static final Logger LOGGER = Logger.getLogger( CactusChemical.class );
    private HttpClient client;
    private static final String BASE_DOMAIN = "cactus.nci.nih.gov";
    private static final String CHEMICAL_STRUCTURE_PATH = "/chemical/structure/";
    private final DefaultIdentifierFactory factory = DefaultIdentifierFactory.getInstance();
    //private static final String NAME_SEARCH = "/names";
    
    /**
     * The cactvs representation is the format specified as the output of the query.
     */
    public enum CactvsRepresentation {
        StdInChI, StdInChIKey, Smiles, 
        Ficts, Ficus, uuuuu, SDF, Names, hashisy,
        Image;
        
        public String toURLString() {
            return toString().toLowerCase();
        }
    }
    
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
        return this.getRepresentationForStructIdentifier(compoundName, CactvsRepresentation.Names);
    }
    
    /**
     * Given a std inchi key (with or without the prefix), this returns a set of names (which in cactvs also includes
     * cross references). Honestly, any cactvs recognizable structure identifier could be given instead of just inchi key.
     * 
     * @param stdInChIKey
     * @return 
     */
    public List<String> getNamesForInChIKey(String stdInChIKey) {
        return getRepresentationForStructIdentifier(stdInChIKey, CactvsRepresentation.Names);
    }
    
    public List<String> getRepresentationForStructIdentifier(String structIdentifier, CactvsRepresentation rep) {
        List<String> names = new ArrayList<String>();
        try {

            // construct the URI, the URI has to be constructed inparts the the compoundName gets formated
            // in accordance with RFC (See. URI API)
            URI uri = new URI( "http" , BASE_DOMAIN , CHEMICAL_STRUCTURE_PATH + structIdentifier + "/" + rep.toURLString() , null );
            HttpEntity entity = getPlainTextResponse( uri );

            // if an entity was returned
            if ( entity != null ) {

                InputStream stream = entity.getContent();
                Scanner scanner = new Scanner( stream ).useDelimiter("\n");
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
    
    private final Pattern chebiPat = Pattern.compile("(CHEBI):(\\d+)");
    private final Pattern keggCmpPat = Pattern.compile("(C\\d{5})");
    private final Pattern casRegNumPat = Pattern.compile("\\d+-\\d+-\\d+");
    // BRN : I think is the Belstein Registry Number
    private final Pattern BrnRegNumPat = Pattern.compile("BRN\\s{0,1}(\\d+)");
    // The EPA database of pesticide chemicals
    private final Pattern epaPesticidePat = Pattern.compile("EPA Pesticide Chemical Code (\\d+)");
    // Zinc is a database of comercially available compounds for virtual screening.
    private final Pattern zincDBPat = Pattern.compile("ZINC(\\d+)");
    // Einecs is a european database of comercialy available compounds.
    private final Pattern einecDBPat = Pattern.compile("EINECS\\s*(\\d+-\\d+-\\d+)");
    // The HSDB Database is the Hazardous substance database.
    private final Pattern HSDBPat = Pattern.compile("HSDB\\s{0,1}(\\d+)");
    
    /**
     * For the Cactvs implementation of this interface, a structure identifier needs to be provided as query.
     * According to the nci resolver web page: http://cactus.nci.nih.gov/chemical/structure/documentation
     * this identifier can be SMILES, InChI/Standard InChI, different CACTVS formats like CACTVS Minimol, 
     * CACTVS Serialized Object String, NCI/CADD Identifiers (FICTS, FICuS, uuuuu), CACTVS HASHISY hashcodes and
     * Chemical names.
     * 
     * @param query - a valid NCI/Cactvs structure identifier
     * @return a list of CrossReferences for selected databases
     */
    public List<CrossReference> getCrossReferences(Identifier identifier) {
        List<CrossReference> crossReferences = new ArrayList<CrossReference>();
        for (Identifier externalReference : getCrossReferences(identifier.getAccession())) {
            //Identifier crIdent = factory.ofSynonym(externalReference.getDbName());
            //crIdent.setAccession(externalReference.getExternalID());
            CrossReference ref = new CrossReference(externalReference);
            crossReferences.add(ref);
        }
        return crossReferences;
    }
    /**
     * For the Cactvs implementation of this interface, a structure identifier needs to be provided as query.
     * According to the nci resolver web page: http://cactus.nci.nih.gov/chemical/structure/documentation
     * this identifier can be SMILES, InChI/Standard InChI, different CACTVS formats like CACTVS Minimol, 
     * CACTVS Serialized Object String, NCI/CADD Identifiers (FICTS, FICuS, uuuuu), CACTVS HASHISY hashcodes and
     * Chemical names.
     * 
     * TODO This could use John's CrossReference instead of the ExternalReference object.
     * 
     * @param query - a valid NCI/Cactvs structure identifier
     * @return a list of CrossReferences for selected databases
     */
    public List<Identifier> getCrossReferences(String query) {
        List<Identifier> results=new ArrayList<Identifier>();
        List<String> namesAndCrossRefs = this.getRepresentationForStructIdentifier(query, CactvsRepresentation.Names);
        for (String nameCR : namesAndCrossRefs) {
            Matcher matcher = chebiPat.matcher(nameCR);
            if(matcher.matches()) {
                results.add(new ChEBIIdentifier(matcher.group(2)));
                //results.add(new ExternalReference("CHEBI", matcher.group(2)));
                continue;
            }
            matcher = keggCmpPat.matcher(nameCR);
            if(matcher.matches()) {
                results.add(new KEGGCompoundIdentifier(nameCR));
                //results.add(new ExternalReference("LIGAND-CPD", nameCR));
                continue;
            }
            matcher = einecDBPat.matcher(nameCR);
            if(matcher.matches()) {
                results.add(new EINECSIdentifier(matcher.group(1)));
                //results.add(new ExternalReference("EINECS", matcher.group(1)));
                continue;
            }
            matcher = zincDBPat.matcher(nameCR);
            if(matcher.matches()) {
                results.add(new ZINCIdentifier(matcher.group(1)));
                //results.add(new ExternalReference("ZINC", matcher.group(1)));
                continue;
            }
            matcher = HSDBPat.matcher(nameCR);
            if(matcher.matches()) {
                results.add(new HSDBIdentifier(matcher.group(1)));
                //results.add(new ExternalReference("HSDB", matcher.group(1)));
                continue;
            }
            matcher = epaPesticidePat.matcher(nameCR);
            if(matcher.matches()) {
                results.add(new EPAPesticideIdentifier(matcher.group(1)));
                //results.add(new ExternalReference("EPA_PESTICIDE", matcher.group(1)));
                continue;
            }
            matcher = BrnRegNumPat.matcher(nameCR);
            if(matcher.matches()) {
                results.add(new BRNIdentifier(matcher.group(1)));
                //results.add(new ExternalReference("BRN", matcher.group(1)));
                continue;
            }
            matcher = casRegNumPat.matcher(nameCR);
            if(matcher.matches()) {
                results.add(new CASIdentifier(nameCR));
                //results.add(new ExternalReference("CAS", nameCR));
                continue;
            }
        }
        return results;
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
