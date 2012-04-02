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
package uk.ac.ebi.resource;

import org.apache.log4j.Logger;
import uk.ac.ebi.chemet.resource.IdentifierSet;
import uk.ac.ebi.chemet.resource.classification.*;
import uk.ac.ebi.chemet.resource.protein.SwissProtIdentifier;
import uk.ac.ebi.chemet.resource.protein.TrEMBLIdentifier;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.interfaces.identifiers.SequenceIdentifier;
import uk.ac.ebi.metabolomes.identifier.InChI;
import uk.ac.ebi.chemet.resource.basic.*;
import uk.ac.ebi.resource.chemical.*;
import uk.ac.ebi.resource.gene.ChromosomeIdentifier;
import uk.ac.ebi.resource.organism.Taxonomy;
import uk.ac.ebi.chemet.resource.structure.HSSPIdentifier;
import uk.ac.ebi.chemet.resource.structure.PDBIdentifier;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.regex.Pattern;


/**
 * IdentifierFactory.java
 * Factory for identifiers
 *
 * @author johnmay
 * @date May 6, 2011
 */
public class IdentifierFactory {

    private static final Logger logger = Logger.getLogger(IdentifierFactory.class);

    private static final String IDENTIFIER_MAPPING_FILE = "IdentifierResourceMapping.properties";

    private static final Map<Class,Identifier> identifiers = new HashMap<Class,Identifier>(60);

    private List<Identifier> supportedIdentifiers = new ArrayList<Identifier>(Arrays.asList(
            new ChEBIIdentifier(),
            new KEGGCompoundIdentifier(),
            new KEGGDrugIdentifier(),
            new LIPIDMapsIdentifier(),
            new TrEMBLIdentifier(),
            new SwissProtIdentifier(),
            new Taxonomy(),
            new ECNumber(),
            new BasicChemicalIdentifier(),
            new BasicReactionIdentifier(),
            new BasicGeneIdentifier(),
            new BasicRNAIdentifier(),
            new BasicProteinIdentifier(),
            new ReconstructionIdentifier(),
            new ChromosomeIdentifier(),
            new TaskIdentifier(),
            new DrugBankIdentifier(),
            new HMDBIdentifier(),
            new InterPro(),
            new GeneOntologyTerm(),
            new GeneOntologyAnnotation(),
            new HSSPIdentifier(),
            new PDBIdentifier(),
            new EINECSIdentifier(),
            new HSDBIdentifier(),
            new ZINCIdentifier(),
            new EPAPesticideIdentifier(),
            new BRNIdentifier(),
            new BRENDAChemicalIdentifier(),
            new CASIdentifier(),
            new GmelinRegistryIdentifier(),
            new UMBBDIdentifier(),
            new PDBChemIdentifier(),
            new CHEMBLIdentifier(),
            new BioCycChemicalIdentifier(),
            new KeggGlycanIdentifier(),
            new KEGGOrthology(),
            new ChemSpiderIdentifier(),
            new PubChemCompoundIdentifier(),
            new PubChemSubstanceIdentifier(),
            new ChemIDplusIdentifier(),
            new InChI(),
            new ExperimentalFactorOntologyIdentifier(),
            new CellTypeOntologyIdentifier(),
            new FoundationalModelOfAnatomyOntologyIdentifier(),
            new BRENDATissueOntologyIdentifier(),
            new TransportClassificationNumber()));

    private Map<String, Identifier> synonyms = new HashMap();

    private List<SequenceIdentifier> proteinIdentifiers = new ArrayList(Arrays.asList(new BasicProteinIdentifier(),
                                                                                      new TrEMBLIdentifier(),
                                                                                      new SwissProtIdentifier()));

    private Set<Identifier> unmapped = new HashSet<Identifier>();
    private Set<Identifier> mapped   = new HashSet<Identifier>();

    private Map<String, SequenceIdentifier> proteinIdMap = new HashMap();

    private List<String> synonymExclusions = Arrays.asList("uniprotkb");

    /**
     * Access a list of all available identifiers
     *
     * @return Unmodifiable List of identifier
     */
    public List<Identifier> getSupportedIdentifiers() {
        return Collections.unmodifiableList(supportedIdentifiers);
    }


    private IdentifierFactory() {

        for (Identifier identifier : supportedIdentifiers) {
            
            identifiers.put(identifier.getClass(), identifier);

            // add to the mapped/unmapped set
            Set set = identifier.getResource().isMapped() ? mapped : unmapped;
            set.add(identifier);

            synonyms.put(identifier.getShortDescription().toLowerCase(Locale.ENGLISH), identifier);
            for (String synonym : identifier.getSynonyms()) {

                String key = synonym.toLowerCase(Locale.ENGLISH);

                if (synonymExclusions.contains(key) == Boolean.FALSE) {

                    if (synonyms.containsKey(key)) {
                        logger.warn("Clashing synonym names in map: " + key + " appears more then once");
                    }

                    synonyms.put(key, identifier);

                }

            }

            if(identifier instanceof SequenceIdentifier){

                SequenceIdentifier sequenceIdentifier = (SequenceIdentifier) identifier;

                for(String code : sequenceIdentifier.getHeaderCodes()){
                    if(proteinIdMap.containsKey(code)) {
                        System.err.println("Clashing header codes");
                    }
                    proteinIdMap.put(code, sequenceIdentifier);
                }

            }


        }

        // sort by resource name
        Collections.sort(supportedIdentifiers, new Comparator<Identifier>() {
            public int compare(Identifier o1, Identifier o2) {
                return o1.getResource().getName().compareTo(o2.getResource().getName());
            }
        });

    }


    public static class IdentifierFactoryHolder {

        public static IdentifierFactory INSTANCE = new IdentifierFactory();
    }


    public static IdentifierFactory getInstance() {
        return IdentifierFactoryHolder.INSTANCE;
    }


    /**
     * Resolves a sequence header e.g. sp|Q38483|EKFF_EKH to one of our identifiers
     */
    public IdentifierSet resolveSequenceHeader(String header) {

        IdentifierSet    resolved = new IdentifierSet();
        Iterator<String> token    = Arrays.asList(header.split("\\|")).iterator();

        while (token.hasNext()) {

            String code = token.next();

            if(code.equals("gnl")){

                String db        = token.hasNext() ? token.next() : "";
                String accession = token.hasNext() ? token.next() : "";

                if(hasSynonym(db)){
                    resolved.add(ofSynonym(db, accession));
                }

            } else if(proteinIdMap.containsKey(code)){
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
    public <I extends Identifier> I ofClass(Class<I> type) {
        return (I) identifiers.get(type).newInstance();
    }

    public <I extends Identifier> I ofClass(Class<I> type, String accession) {
        I identifier = ofClass(type);
        identifier.setAccession(accession);
        return identifier;
    }


    public Collection<Class<? extends Identifier>> ofPattern(String accession){
        Collection<Class<? extends Identifier>> matched = new ArrayList<Class<? extends Identifier>>();
        for(Identifier identifier : mapped){
            Pattern pattern = identifier.getResource().getCompiledPattern();
            if(pattern.matcher(accession).matches()){
                matched.add(identifier.getClass());
            }
        }
        return matched;
    }


    public boolean hasSynonym(String synonym){

        String key = synonym.toLowerCase(Locale.ENGLISH);

        if (synonyms.containsKey(key)) {
            return true;
        }

        return false;

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
    public Identifier ofSynonym(String synonym) {

        String key = synonym.toLowerCase(Locale.ENGLISH);

        if (synonyms.containsKey(key)) {
            return synonyms.get(key).newInstance();
        }

        throw new InvalidParameterException("No matching identifier synonym found for: " + synonym + " please invoke hasSynonym() to avoid this error");
    }

    public Identifier ofSynonym(String synonym, String accession) {

        Identifier id = ofSynonym(synonym);
        id.setAccession(accession);
        return id;

    }

}
