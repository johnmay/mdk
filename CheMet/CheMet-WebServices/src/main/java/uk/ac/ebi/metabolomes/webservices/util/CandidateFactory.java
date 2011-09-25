
/**
 * CandidateFactory.java
 *
 * 2011.09.22
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
package uk.ac.ebi.metabolomes.webservices.util;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.Map.Entry;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import uk.ac.ebi.chebi.webapps.chebiWS.model.StarsCategory;
import uk.ac.ebi.metabolomes.webservices.ChEBIWebServiceConnection;
import uk.ac.ebi.metabolomes.webservices.ChemicalDBWebService;
import uk.ac.ebi.reconciliation.ChemicalFingerprintEncoder;
import uk.ac.ebi.reconciliation.StringEncoder;


/**
 *          CandidateFactory – 2011.09.22 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class CandidateFactory {

    private static final Logger LOGGER = Logger.getLogger(CandidateFactory.class);
    private final ChemicalDBWebService webservice;
    private StringEncoder encoder;


    public CandidateFactory(ChemicalDBWebService webservice, StringEncoder encoder) {
        this.webservice = webservice;
        this.encoder = encoder;
    }


    /**
     *
     * Creates a {@see Multimap} of candidate entries ({@see CandidateEntry}) with key as their
     * distance
     *
     * @param queryName
     * @param encoder A StringEncoder such as ChemicalFingerprint
     * @return
     * 
     */
    public Multimap<Integer, SynonymCandidateEntry> getSynonymCandidates(String queryName) {

        Multimap<Integer, SynonymCandidateEntry> map = HashMultimap.create();

        // todo add general search
        for( Entry<String,String> entry : webservice.search(queryName).entrySet() ) {

            String accession = entry.getKey();

            String description = entry.getValue(); // webservice.getName(accession);
            Collection<String> synonyms = webservice.getSynonyms(accession);

            Integer distance = getBestScore(queryName, synonyms);
            map.put(distance,
                    new SynonymCandidateEntry(accession,
                                              description,
                                              synonyms,
                                              distance));

        }

        return map;

    }


    public static void main(String[] args) {
        CandidateFactory factory = new CandidateFactory(new ChEBIWebServiceConnection(
          StarsCategory.THREE_ONLY, 20),
                                                        new ChemicalFingerprintEncoder());
        System.out.println(factory.getSynonymCandidates("Adenosine triphosphate"));
        System.out.println(factory.getSynonymCandidates("GTP"));

    }


    public Integer getBestScore(String query, Collection<String> synonyms) {

        int score = Integer.MAX_VALUE;

        String encodedQuery = encoder.encode(query);

        for( String synonym : synonyms ) {
            int distance = calculateDistance(encodedQuery, synonym);
            score = distance < score ? distance : score;
        }

        return score;

    }


    /**
     * Calculates then Levenshtein distance for the query and subject strings using the set
     * StringEncoder
     * 
     * @param encodedQuery query which is been pre-encoded
     * @param subject
     * @return
     */
    public Integer calculateDistance(String encodedQuery, String subject) {
        return StringUtils.getLevenshteinDistance(encodedQuery,
                                                  encoder.encode(subject));
    }


    public void setEncoder(StringEncoder encoder) {
        this.encoder = encoder;
    }


}

