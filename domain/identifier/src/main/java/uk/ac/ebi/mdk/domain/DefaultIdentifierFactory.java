/*
 * Copyright (c) 2013. EMBL, European Bioinformatics Institute
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.ebi.mdk.domain;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.deprecated.MIRIAMLoader;
import uk.ac.ebi.mdk.domain.identifier.BRENDAChemicalIdentifier;
import uk.ac.ebi.mdk.domain.identifier.BRNIdentifier;
import uk.ac.ebi.mdk.domain.identifier.BioCycChemicalIdentifier;
import uk.ac.ebi.mdk.domain.identifier.CASIdentifier;
import uk.ac.ebi.mdk.domain.identifier.CHEMBLIdentifier;
import uk.ac.ebi.mdk.domain.identifier.ChEBIIdentifier;
import uk.ac.ebi.mdk.domain.identifier.ChemIDplusIdentifier;
import uk.ac.ebi.mdk.domain.identifier.ChemSpiderIdentifier;
import uk.ac.ebi.mdk.domain.identifier.DrugBankIdentifier;
import uk.ac.ebi.mdk.domain.identifier.EINECSIdentifier;
import uk.ac.ebi.mdk.domain.identifier.EPAPesticideIdentifier;
import uk.ac.ebi.mdk.domain.identifier.GmelinRegistryIdentifier;
import uk.ac.ebi.mdk.domain.identifier.HMDBIdentifier;
import uk.ac.ebi.mdk.domain.identifier.HSDBIdentifier;
import uk.ac.ebi.mdk.domain.identifier.HSSPIdentifier;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.identifier.IdentifierFactory;
import uk.ac.ebi.mdk.domain.identifier.IdentifierSet;
import uk.ac.ebi.mdk.domain.identifier.InChI;
import uk.ac.ebi.mdk.domain.identifier.KEGGCompoundIdentifier;
import uk.ac.ebi.mdk.domain.identifier.KEGGDrugIdentifier;
import uk.ac.ebi.mdk.domain.identifier.KEGGReactionIdentifier;
import uk.ac.ebi.mdk.domain.identifier.KeggGlycanIdentifier;
import uk.ac.ebi.mdk.domain.identifier.LIPIDMapsIdentifier;
import uk.ac.ebi.mdk.domain.identifier.MetaCycIdentifier;
import uk.ac.ebi.mdk.domain.identifier.PDBChemIdentifier;
import uk.ac.ebi.mdk.domain.identifier.PDBIdentifier;
import uk.ac.ebi.mdk.domain.identifier.PubChemCompoundIdentifier;
import uk.ac.ebi.mdk.domain.identifier.PubChemSubstanceIdentifier;
import uk.ac.ebi.mdk.domain.identifier.PubMedIdentifier;
import uk.ac.ebi.mdk.domain.identifier.SwissProtIdentifier;
import uk.ac.ebi.mdk.domain.identifier.Taxonomy;
import uk.ac.ebi.mdk.domain.identifier.TrEMBLIdentifier;
import uk.ac.ebi.mdk.domain.identifier.UMBBDIdentifier;
import uk.ac.ebi.mdk.domain.identifier.ZINCIdentifier;
import uk.ac.ebi.mdk.domain.identifier.basic.BasicChemicalIdentifier;
import uk.ac.ebi.mdk.domain.identifier.basic.BasicGeneIdentifier;
import uk.ac.ebi.mdk.domain.identifier.basic.BasicProteinIdentifier;
import uk.ac.ebi.mdk.domain.identifier.basic.BasicRNAIdentifier;
import uk.ac.ebi.mdk.domain.identifier.basic.BasicReactionIdentifier;
import uk.ac.ebi.mdk.domain.identifier.basic.ChromosomeNumber;
import uk.ac.ebi.mdk.domain.identifier.basic.ReconstructionIdentifier;
import uk.ac.ebi.mdk.domain.identifier.basic.TaskIdentifier;
import uk.ac.ebi.mdk.domain.identifier.classification.BRENDATissueOntologyIdentifier;
import uk.ac.ebi.mdk.domain.identifier.classification.CellTypeOntologyIdentifier;
import uk.ac.ebi.mdk.domain.identifier.classification.ECNumber;
import uk.ac.ebi.mdk.domain.identifier.classification.ExperimentalFactorOntologyIdentifier;
import uk.ac.ebi.mdk.domain.identifier.classification.FoundationalModelOfAnatomyOntologyIdentifier;
import uk.ac.ebi.mdk.domain.identifier.classification.GeneOntologyAnnotation;
import uk.ac.ebi.mdk.domain.identifier.classification.GeneOntologyTerm;
import uk.ac.ebi.mdk.domain.identifier.classification.InterPro;
import uk.ac.ebi.mdk.domain.identifier.classification.KEGGOrthology;
import uk.ac.ebi.mdk.domain.identifier.classification.TransportClassificationNumber;
import uk.ac.ebi.mdk.domain.identifier.type.SequenceIdentifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * IdentifierFactory.java Factory for identifiers
 *
 * @author johnmay
 * @date May 6, 2011
 */
public class DefaultIdentifierFactory implements IdentifierFactory {

    private static final Logger logger = Logger.getLogger(
        DefaultIdentifierFactory.class);

    private static final String IDENTIFIER_MAPPING_FILE = "IdentifierResourceMapping.properties";


    private static final Map<Class, Identifier>  identifiers     = new HashMap<Class, Identifier>(
        60);
    private static final Map<String, Identifier> identifierNames = new HashMap<String, Identifier>(
        60);

    private List<Identifier> supportedIdentifiers = new ArrayList<Identifier>(
        Arrays.asList(new ChEBIIdentifier(), new KEGGCompoundIdentifier(),
                      new KEGGDrugIdentifier(), new LIPIDMapsIdentifier(),
                      new TrEMBLIdentifier(), new SwissProtIdentifier(),
                      new Taxonomy(), new ECNumber(),
                      new BasicChemicalIdentifier(),
                      new BasicReactionIdentifier(), new BasicGeneIdentifier(),
                      new BasicRNAIdentifier(), new BasicProteinIdentifier(),
                      new ReconstructionIdentifier(), new ChromosomeNumber(),
                      new TaskIdentifier(), new DrugBankIdentifier(),
                      new HMDBIdentifier(), new InterPro(),
                      new GeneOntologyTerm(), new GeneOntologyAnnotation(),
                      new HSSPIdentifier(), new PDBIdentifier(),
                      new EINECSIdentifier(), new HSDBIdentifier(),
                      new ZINCIdentifier(), new EPAPesticideIdentifier(),
                      new BRNIdentifier(), new BRENDAChemicalIdentifier(),
                      new CASIdentifier(), new GmelinRegistryIdentifier(),
                      new UMBBDIdentifier(), new PDBChemIdentifier(),
                      new CHEMBLIdentifier(), new BioCycChemicalIdentifier(),
                      new MetaCycIdentifier(), new KeggGlycanIdentifier(),
                      new KEGGOrthology(), new ChemSpiderIdentifier(),
                      new PubChemCompoundIdentifier(),
                      new PubChemSubstanceIdentifier(), new PubMedIdentifier(),
                      new ChemIDplusIdentifier(), new InChI(),
                      new ExperimentalFactorOntologyIdentifier(),
                      new CellTypeOntologyIdentifier(),
                      new FoundationalModelOfAnatomyOntologyIdentifier(),
                      new BRENDATissueOntologyIdentifier(),
                      new TransportClassificationNumber(),
                      new KEGGReactionIdentifier()));

    private Map<String, Identifier> synonyms = new HashMap<String, Identifier>();

    private List<SequenceIdentifier> proteinIdentifiers = new ArrayList<SequenceIdentifier>(
        Arrays.asList(new BasicProteinIdentifier(), new TrEMBLIdentifier(),
                      new SwissProtIdentifier()));

    private Set<Identifier> unmapped = new HashSet<Identifier>();
    private Set<Identifier> mapped   = new HashSet<Identifier>();

    private Map<String, SequenceIdentifier> proteinIdMap = new HashMap<String,SequenceIdentifier>();

    private List<String> synonymExclusions = Arrays.asList("uniprotkb");

    /**
     * Access a list of all available identifiers
     *
     * @return Unmodifiable List of identifier
     */
    public List<Identifier> getSupportedIdentifiers() {
        return Collections.unmodifiableList(supportedIdentifiers);
    }


    private DefaultIdentifierFactory() {

        for (Identifier identifier : supportedIdentifiers) {

            identifiers.put(identifier.getClass(), identifier);
            identifierNames.put(identifier.getShortDescription().toLowerCase(
                Locale.ENGLISH), identifier);

            // add to the mapped/unmapped set
            Set<Identifier> set = identifier.getResource().isMapped() ? mapped : unmapped;
            set.add(identifier);

            synonyms.put(identifier.getShortDescription().toLowerCase(
                Locale.ENGLISH), identifier);
            for (String synonym : identifier.getSynonyms()) {

                String key = synonym.toLowerCase(Locale.ENGLISH);

                if (synonymExclusions.contains(key) == Boolean.FALSE) {

                    if (synonyms.containsKey(key)) {
                        logger.warn(
                            "Clashing synonym names in map: " + key + " appears more then once");
                    }

                    synonyms.put(key, identifier);

                }

            }

            if (identifier instanceof SequenceIdentifier) {

                SequenceIdentifier sequenceIdentifier = (SequenceIdentifier) identifier;

                for (String code : sequenceIdentifier.getHeaderCodes()) {
                    if (proteinIdMap.containsKey(code)) {
                        System.err.println("Clashing header codes");
                    }
                    proteinIdMap.put(code, sequenceIdentifier);
                }

            }


        }

        // sort by resource name
        Collections.sort(supportedIdentifiers, new Comparator<Identifier>() {
            public int compare(Identifier o1, Identifier o2) {
                return o1.getShortDescription().compareTo(
                    o2.getShortDescription());
            }
        });

    }


    public static class IdentifierFactoryHolder {

        public static DefaultIdentifierFactory INSTANCE = new DefaultIdentifierFactory();
    }


    public static DefaultIdentifierFactory getInstance() {
        return IdentifierFactoryHolder.INSTANCE;
    }


    /**
     * Resolves a sequence header e.g. sp|Q38483|EKFF_EKH to one of our
     * identifiers
     */
    public IdentifierSet resolveSequenceHeader(String header) {

        IdentifierSet resolved = new IdentifierSet();
        Iterator<String> token = Arrays.asList(header.split("\\|")).iterator();

        while (token.hasNext()) {

            String code = token.next();

            if (code.equals("gnl")) {

                String db = token.hasNext() ? token.next() : "";
                String accession = token.hasNext() ? token.next() : "";

                if (hasSynonym(db)) {
                    resolved.add(ofSynonym(db, accession));
                }

            } else if (proteinIdMap.containsKey(code)) {
                resolved.add(proteinIdMap.get(code).ofHeader(token));
            }

        }

        return resolved;

    }

    /**
     * Construct an identifier of a given class
     *
     * @param type
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public <I extends Identifier> I ofClass(Class<I> type) {
        return (I) identifiers.get(type).newInstance();
    }

    public <I extends Identifier> I ofClass(Class<I> type, String accession) {
        I identifier = ofClass(type);
        identifier.setAccession(accession);
        return identifier;
    }

    /** @inheritDoc */
    @Override
    public Identifier ofName(String name) {

        String normalisedName = name.toLowerCase(Locale.ENGLISH).trim();

        if (identifierNames.containsKey(normalisedName)) {
            return identifierNames.get(normalisedName).newInstance();
        }

        // could simplify to a loop which removes common postfix words

        if (normalisedName.contains("accession")) {
            return ofName(normalisedName.replaceAll("accession", ""));
        }

        if (normalisedName.contains("identifier")) {
            return ofName(normalisedName.replaceAll("identifier", ""));
        }

        if (normalisedName.contains("id")) {
            return ofName(normalisedName.replaceAll("id", ""));
        }

        return EMPTY_IDENTIFIER;

    }

    /** @inheritDoc */
    @Override
    public Identifier ofName(String name, String accession) {
        Identifier identifier = ofName(name);
        identifier.setAccession(accession);
        return identifier;
    }


    @Override
    public Collection<Class<? extends Identifier>> ofPattern(String accession) {
        Collection<Class<? extends Identifier>> matched = new ArrayList<Class<? extends Identifier>>();
        for (Identifier identifier : mapped) {
            Pattern pattern = identifier.pattern();
            if (pattern != null) {
                if (pattern.matcher(accession).matches()) {
                    matched.add(identifier.getClass());
                }
            }
        }
        return matched;
    }


    @Override public boolean hasSynonym(String synonym) {

        String key = synonym.toLowerCase(Locale.ENGLISH);

        return synonyms.containsKey(key);

    }

    /**
     * Create an identifier of the given synonym. for example "EC" for ECNumber.
     * The synonyms are loaded from the MIRIAM registry with custom synonyms
     * specified in the IdentifierMetaInfo properites resource file.
     *
     * @param synonym
     *
     * @return
     */
    @Override public Identifier ofSynonym(String synonym) {

        String key = synonym.toLowerCase(Locale.ENGLISH);

        if (synonyms.containsKey(key)) {
            return synonyms.get(key).newInstance();
        }

        return EMPTY_IDENTIFIER;

    }

    @Override public Identifier ofSynonym(String synonym, String accession) {

        Identifier id = ofSynonym(synonym);
        id.setAccession(accession);
        return id;

    }

    @Override public Identifier ofURL(String url) {
        Matcher matcher = IDENTIFIERS_DOT_ORG.matcher(url);
        if (matcher.matches()) {
            String namespace = matcher.group(1);
            String accession = matcher.group(2);
            return MIRIAMLoader.getInstance().ofNamespace(namespace,
                                                          accession);
        }
        return EMPTY_IDENTIFIER;
    }

    private static final Pattern IDENTIFIERS_DOT_ORG = Pattern.compile(
        "http://(?:www.)?identifiers.org/([^/]+)/([^/]+)/?");
}
