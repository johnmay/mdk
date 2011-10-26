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
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import uk.ac.ebi.annotation.crossreference.ChEBICrossReference;
import uk.ac.ebi.annotation.crossreference.CrossReference;
import uk.ac.ebi.annotation.crossreference.KEGGCrossReference;
import uk.ac.ebi.chebi.webapps.chebiWS.model.StarsCategory;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.interfaces.services.NameQueryService;
import uk.ac.ebi.metabolomes.webservices.ChEBIWebServiceConnection;
import uk.ac.ebi.metabolomes.webservices.ChemicalDBWebService;
import uk.ac.ebi.reconciliation.ChemicalFingerprintEncoder;
import uk.ac.ebi.reconciliation.StringEncoder;
import uk.ac.ebi.resource.chemical.ChEBIIdentifier;
import uk.ac.ebi.resource.chemical.KEGGCompoundIdentifier;

/**
 *          CandidateFactory – 2011.09.22 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class CandidateFactory<I extends Identifier> {

    private static final Logger LOGGER = Logger.getLogger(CandidateFactory.class);
    private final NameQueryService<I> service;
    private StringEncoder encoder;

    public CandidateFactory(NameQueryService<I> service, StringEncoder encoder) {
        this.service = service;
        this.encoder = encoder;
    }

    /**
     *
     * Creates a {@see Multimap} of candidate entries ({@see CandidateEntry}) with key as their
     * distance
     *
     * @param name
     * @param encoder A StringEncoder such as ChemicalFingerprint
     * @return
     * 
     */
    public Multimap<Integer, SynonymCandidateEntry> getSynonymCandidates(String name) {

        Multimap<Integer, SynonymCandidateEntry> map = HashMultimap.create();

        // todo add general search
        for (I id : service.searchForName(name)) {

            Collection<String> names = service.getNames(id);

            Integer distance = getBestScore(name, names);
            map.put(distance,
                    new SynonymCandidateEntry(id.getAccession(),
                    names.size() > 0 ? names.iterator().next() : "",
                    names,
                    distance));

        }

        return map;

    }

    public CrossReference getCrossReference(CandidateEntry entry) {
        Identifier id = service.getIdentifier();
        id.setAccession(entry.getId());
        if (id instanceof ChEBIIdentifier) {
            return new ChEBICrossReference((ChEBIIdentifier) id);
        }
        if (id instanceof KEGGCompoundIdentifier) {
            return new KEGGCrossReference((KEGGCompoundIdentifier) id);
        }
        return new CrossReference(id);
    }

    /**
     * Creates a multimap of possible candidates entries which are keyed by their distance
     * @param name
     * @return
     */
    public Multimap<Integer, CandidateEntry> getCandidates(String name) {

        Multimap<Integer, CandidateEntry> map = HashMultimap.create();

        System.out.println("Sending candidate search");

        throw new UnsupportedOperationException();

//        // todo add general search
//        for (I id : service.searchWithName(name)) {
//
//            String subject = service.getNames(id);
//            System.out.println("Got candidate name.." + id + ":" +  subject);
//
//
//            Integer distance = calculateDistance(encoder.encode(name), encoder.encode(subject));
//            map.put(distance,
//                    new CandidateEntry(accession,
//                    subject,
//                    distance, ""));
//
//        }

        //return map;

    }

//    public static void main(String[] args) {
//        CandidateFactory factory = new CandidateFactory(new ChEBINameService(),
//                new ChemicalFingerprintEncoder());
//        System.out.println(factory.getSynonymCandidates("Adenosine triphosphate"));
//        System.out.println(factory.getSynonymCandidates("GTP"));
//
//    }

    public Integer getBestScore(String query, Collection<String> synonyms) {

        int score = Integer.MAX_VALUE;

        String encodedQuery = encoder.encode(query);

        for (String synonym : synonyms) {
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
