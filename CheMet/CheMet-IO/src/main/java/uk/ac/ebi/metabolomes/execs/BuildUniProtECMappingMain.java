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

import uk.ac.ebi.metabolomes.http.uniprot.UniProtSearchQuery;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import uk.ac.ebi.metabolomes.http.uniprot.UniProtSearch;
import uk.ac.ebi.metabolomes.identifier.ECNumber;
import uk.ac.ebi.metabolomes.io.flatfile.IntEnzXML;

/**
 * Builds a table of every EC Number and UniProtKB/SwissProt and UniProtKB/TrEMBL
 * @author johnmay
 */
public class BuildUniProtECMappingMain {

    /**
     * @param args the command line arguments
     */
    public static void main( String[] args ) throws IOException {

        IntEnzXML intenz = IntEnzXML.getLoadedInstance();
        List<ECNumber> ecCodes = new ArrayList<ECNumber>( intenz.getEnzymeSequenceMap().keySet() );
        CSVWriter writer = new CSVWriter( new BufferedWriter( new FileWriter( "/nfs/nobackup/research/steinbeck/johnmay/uniprot_all_ec.tsv" ) ) );

        double size = ecCodes.size();

        for ( int i = 0; i < ecCodes.size(); i++ ) {

            System.out.printf("[%.2f]", i/size );

            List params = new ArrayList<NameValuePair>();
            UniProtSearchQuery query = new UniProtSearchQuery("");
            query.setAndEnzymeCommissionNumber(ecCodes.get( i ).toString() );
            params.add( new BasicNameValuePair( "query" , query.buildQuery()));
            params.add( new BasicNameValuePair( "format" , "tab" ) );
            params.add( new BasicNameValuePair( "columns" , "id,ec,reviewed" ) );
            HttpEntity entity = UniProtSearch.getInstance().search( params );
            CSVReader reader = new CSVReader( new BufferedReader( new InputStreamReader( entity.getContent() ) ) );
            writer.writeAll( reader.readAll() );

        }
    }
}
