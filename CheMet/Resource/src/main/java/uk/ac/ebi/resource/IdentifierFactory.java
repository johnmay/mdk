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
import uk.ac.ebi.core.IdentifierSet;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.interfaces.identifiers.SequenceIdentifier;
import uk.ac.ebi.metabolomes.identifier.AbstractIdentifier;
import uk.ac.ebi.metabolomes.identifier.InChI;
import uk.ac.ebi.metabolomes.resource.Resource;
import uk.ac.ebi.resource.chemical.*;
import uk.ac.ebi.resource.classification.*;
import uk.ac.ebi.resource.gene.BasicGeneIdentifier;
import uk.ac.ebi.resource.gene.ChromosomeIdentifier;
import uk.ac.ebi.resource.organism.Taxonomy;
import uk.ac.ebi.resource.protein.BasicProteinIdentifier;
import uk.ac.ebi.resource.protein.SwissProtIdentifier;
import uk.ac.ebi.resource.protein.TrEMBLIdentifier;
import uk.ac.ebi.resource.reaction.BasicReactionIdentifier;
import uk.ac.ebi.resource.rna.BasicRNAIdentifier;
import uk.ac.ebi.resource.structure.HSSPIdentifier;
import uk.ac.ebi.resource.structure.PDBIdentifier;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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

    private static final Identifier[] identifiers = new Identifier[Byte.MAX_VALUE];

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
            new BRENDATissueOntologyIdentifier()));

    private Map<String, Identifier> synonyms = new HashMap();

    private List<SequenceIdentifier> proteinIdentifiers = new ArrayList(Arrays.asList(new BasicProteinIdentifier(),
                                                                                      new TrEMBLIdentifier(),
                                                                                      new SwissProtIdentifier()));

    private Map<String, SequenceIdentifier> proteinIdMap = new HashMap();

    private List<String> synonymExclusions = Arrays.asList("uniprotkb");


    /**
     * Access a subset of all available identifiers that the accession matches
     * the pattern provided by {@see uk.ac.ebi.interfaces.Resource#getCompiledPattern()}
     *
     * @return Unmodifiable List of identifier
     */
    public List<Identifier> getMatchingIdentifiers(String accession) {
        List<Identifier> subset = new ArrayList<Identifier>();
        for (Identifier identifier : supportedIdentifiers) {
            Pattern pattern = identifier.getResource().getCompiledPattern();
            if (pattern.matcher(accession).matches()) {
                subset.add(identifier);
            }
        }
        return subset;
    }


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
            identifiers[identifier.getIndex()] = identifier;
        }

        for (Identifier identifier : supportedIdentifiers) {

            synonyms.put(identifier.getShortDescription().toLowerCase(Locale.ENGLISH), identifier);
            for (String synonym : identifier.getDatabaseSynonyms()) {

                String key = synonym.toLowerCase(Locale.ENGLISH);

                if (synonymExclusions.contains(key) == Boolean.FALSE) {

                    if (synonyms.containsKey(key)) {
                        logger.warn("Clashing synonym names in map: " + key + " appears more then once");
                    }

                    synonyms.put(key, identifier);

                }

            }
        }

        // TODO Build proteinIdentifiers list from supported identifiers list
        for (SequenceIdentifier id : proteinIdentifiers) {
            for(String code : id.getHeaderCodes()){
                if(proteinIdMap.containsKey(code)) {
                    System.err.println("Clashing header codes");
                }
                proteinIdMap.put(code, id);
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
     * Builds an identifier given the accession
     * Uses the identifier parse method to validate ids (slower)
     *
     * @param resource
     * @param accession
     *
     * @deprecated do not use
     */
    @Deprecated
    public static AbstractIdentifier getIdentifier(Resource resource, String accession) {

        Constructor constructor = resource.getIdentifierConstructor();
        if (constructor != null) {
            try {
                return (AbstractIdentifier) constructor.newInstance(accession, true);
            } catch (InstantiationException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
            }
        }
        return new BasicProteinIdentifier(accession);
    }

    /*     
     * @param resource
     * @param accession
     * @return
     * @deprecated do not use
     */

    @Deprecated
    public static AbstractIdentifier getUncheckedIdentifier(Resource resource, String accession) {

        Constructor constructor = resource.getIdentifierConstructor();
        if (constructor != null) {
            try {
                return (AbstractIdentifier) constructor.newInstance(accession, false);
            } catch (InstantiationException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
            }
        }
        return new BasicProteinIdentifier(accession);
    }


    /**
     * Builds a list of identifiers from a string that may
     * or maynot contain multiple identifiers
     * atm: handle gi|39327|sp|398339 etc..
     *
     * @param idsString
     *
     * @return
     *
     * @Deprecated Use resolveSequenceHeader
     */
    @Deprecated
    public static List<AbstractIdentifier> getIdentifiers(String idsString) {

        List<AbstractIdentifier> hitIdentifiers = new ArrayList<AbstractIdentifier>();

        if (idsString.contains(ID_SEPERATOR)) {

            ListIterator<String> it = Arrays.asList(idsString.split(ID_ESCAPED_SEPERATOR)).
                    listIterator();

            // db identifiers , gi,sp,tr etc..
            while (it.hasNext()) {

                String dbid = it.next();

                if (dbid.length() <= DBID_MAX_LENGTH) {
                    Resource r = Resource.getResource(dbid);

                    if (r != Resource.UNKNOWN) {
                        hitIdentifiers.add(IdentifierFactory.getIdentifier(r, it.next()));
                    } else if (it.hasNext()) {
                        dbid = it.next();
                        r = Resource.getResource(dbid);
                        if (r != Resource.UNIPROT) {
                            hitIdentifiers.add(IdentifierFactory.getIdentifier(r, it.next()));
                        } else {
                            it.previous();
                        }
                    }

                } else {

                    hitIdentifiers.add(new BasicProteinIdentifier(dbid));
                }
            }
        } else {
            hitIdentifiers.add(new BasicProteinIdentifier(idsString));
        }

        return hitIdentifiers;
    }


    /**
     * Returns and identifier
     *
     * @param type
     *
     * @return
     */
    public Identifier ofClass(Class type) {
        return ofIndex(IdentifierLoader.getInstance().getIndex(type));
    }


    /**
     * Main factory method. this returns a new identifier of the given index. The indicies are specified in the
     * IdentifierDescription.propertiers file (see. src/main/resources)
     *
     * @param index
     *
     * @return
     */
    public Identifier ofIndex(Byte index) {
        return identifiers[index].newInstance();
    }


    /**
     * Create an identifier of the given synonym. for example "EC" for ECNumber.
     * The synonyms are loaded from the MIRIAM registry with custom synonyms
     * specified in the IdentifierDescription properites resource file.
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

        throw new InvalidParameterException("No matching identifier synonym found for: " + synonym);
    }

    public Identifier ofSynonym(String synonym, String accession) {

        Identifier id = ofSynonym(synonym);
        id.setAccession(accession);
        return id;
        
    }

    public boolean hasSynonym(String synonym){

        String key = synonym.toLowerCase(Locale.ENGLISH);

        if (synonyms.containsKey(key)) {
            return true;
        }

        return false;

    }

    /**
     * Utility method for reading an identifier to an ObjectOutput
     */
    public Identifier read(ObjectInput in) throws IOException, ClassNotFoundException {
        Identifier identifier = ofIndex(in.readByte());
        identifier.readExternal(in);
        return identifier;
    }


    /**
     * Utility method for writing an identifier to an ObjectOutput
     */
    public void write(ObjectOutput out, Identifier identifier) throws IOException {
        out.writeByte(identifier.getIndex());
        identifier.writeExternal(out);
    }

    private static final String ID_SEPERATOR = "|";

    private static final String ID_ESCAPED_SEPERATOR = "\\|";

    private static final int DBID_MAX_LENGTH = 3;
}
