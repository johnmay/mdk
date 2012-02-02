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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.openscience.cdk.interfaces.IMolecularFormula;
import uk.ac.ebi.annotation.Source;
import uk.ac.ebi.annotation.chemical.MolecularFormula;
import uk.ac.ebi.annotation.crossreference.ChEBICrossReference;
import uk.ac.ebi.annotation.crossreference.CrossReference;
import uk.ac.ebi.annotation.crossreference.KEGGCrossReference;
import uk.ac.ebi.interfaces.entities.EntityFactory;
import uk.ac.ebi.interfaces.entities.Metabolite;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.interfaces.services.ChemicalDataQueryService;
import uk.ac.ebi.interfaces.services.NameQueryService;
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

            SynonymCandidateEntry entry = getBestScore(name, names);
            entry.setId(id.getAccession());

            map.put(entry.getDistance(), entry);

        }

        return map;

    }


    /**
     * Generates 'dummy' metabolites for a list of candidates that can easily be displayed
     * in MoleculeTable (chemet-vis)
     */
    public List<Metabolite> getMetaboliteList(List<? extends CandidateEntry> candidates,
                                              I xrefid,
                                              ChemicalDataQueryService<I> dataservice,
                                              String source,
                                              EntityFactory factory) {

        List<uk.ac.ebi.interfaces.entities.Metabolite> metabolites = new ArrayList<uk.ac.ebi.interfaces.entities.Metabolite>(candidates.size());

        for (CandidateEntry candidate : candidates) {

            I id = (I) xrefid.newInstance();
            id.setAccession(candidate.getId());

            Metabolite m = factory.newInstance(Metabolite.class, id, "", "");
            m.setName(candidate.getDescription());

            m.setCharge(dataservice.getCharge(id));
            Collection<IMolecularFormula> mfs = dataservice.getFormulas(id);
            for (IMolecularFormula imf : mfs) {
                m.addAnnotation(new MolecularFormula(imf));
            }

            m.addAnnotation(new Source(source));

            metabolites.add(m);

        }

        return metabolites;
    }


    public <T extends CandidateEntry> List<T> getSortedList(Multimap<Integer, T> map) {

        List<T> entries = new ArrayList<T>();
        List<Integer> scores = new ArrayList<Integer>(map.keySet());
        Collections.sort(scores);
        for (Integer score : scores) {
            entries.addAll(map.get(score));
        }
        return entries;

    }


    /**
     * Uses fuzzy match to search
     * @param name
     * @return
     */
    public Multimap<Integer, SynonymCandidateEntry> getFuzzySynonymCandidates(String name) {
        Multimap<Integer, SynonymCandidateEntry> map = HashMultimap.create();

        // todo add general search
        for (I id : service.fuzzySearchForName(name)) {

            Collection<String> names = service.getNames(id);

            SynonymCandidateEntry entry = getBestScore(name, names);
            entry.setId(id.getAccession());

            map.put(entry.getDistance(), entry);

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

    public SynonymCandidateEntry getBestScore(String query, Collection<String> synonyms) {

        int score = Integer.MAX_VALUE;
        int index = -1;

        String encodedQuery = encoder.encode(query);

        List<String> synonymList = new ArrayList(synonyms);

        for (int i = 0; i < synonyms.size(); i++) {
            int distance = calculateDistance(encodedQuery, synonymList.get(i));

            if (distance < score) {
                score = distance;
                index = i;
            }

        }


        return new SynonymCandidateEntry("",
                                         index != -1 ? synonymList.get(index) : "",
                                         synonyms,
                                         score,
                                         index);


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
