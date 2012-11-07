package uk.ac.ebi.mdk.apps.tool;

import au.com.bytecode.opencsv.CSVWriter;
import org.apache.commons.cli.Option;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.iterator.IteratingMDLReader;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import uk.ac.ebi.mdk.apps.CommandLineMain;
import uk.ac.ebi.mdk.domain.DefaultIdentifierFactory;
import uk.ac.ebi.mdk.domain.annotation.AtomContainerAnnotation;
import uk.ac.ebi.mdk.domain.annotation.Synonym;
import uk.ac.ebi.mdk.domain.entity.DefaultEntityFactory;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.identifier.IdentifierFactory;
import uk.ac.ebi.mdk.service.DefaultServiceManager;
import uk.ac.ebi.mdk.service.ServiceManager;
import uk.ac.ebi.mdk.service.query.name.NameService;
import uk.ac.ebi.mdk.service.query.name.PreferredNameService;
import uk.ac.ebi.mdk.service.query.structure.StructureService;
import uk.ac.ebi.mdk.tool.MappedEntityAligner;
import uk.ac.ebi.mdk.prototype.hash.seed.AtomicNumberSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.BondOrderSumSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.ChargeSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.ConnectedAtomSeed;
import uk.ac.ebi.mdk.prototype.hash.seed.StereoSeed;
import uk.ac.ebi.mdk.tool.match.EntityAligner;
import uk.ac.ebi.mdk.tool.match.EntityMatcher;
import uk.ac.ebi.mdk.tool.match.MetaboliteHashCodeMatcher;
import uk.ac.ebi.mdk.tool.match.NameMatcher;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Aligns provided IDs to a provided DataSet (SDF file). The sets are expanded
 * to include structures and names for all entries. The matching cannot
 * currently be configured. This utility was created to help with assigning
 * ChEBI identifiers to HMDB of Mark Williams (Reactome/MetaboLights).
 *
 * @author John May
 */
public class Align extends CommandLineMain {

    private Map<Identifier, Metabolite> queryCache = new HashMap<Identifier, Metabolite>();


    public static void main(String[] args) {
        Logger.getLogger(MappedEntityAligner.class).setLevel(Level.DEBUG);
        new Align().process(args);

    }


    @Override
    public void setupOptions() {
        add(new Option("q", "query", true, "List of ids"));
        add(new Option("s", "source", true, "Source of IDs (HMDB, ChEBI, KEGG Compound, MetaCyc or LIPID Maps)"));
        add(new Option("r", "reference", true, "An SDF file to align too"));
        add(new Option("i", "id", true, "Property the id is in: e.g. <ChEBI_ID> would be 'ChEBI_ID'"));
        add(new Option("rs", "reference-source", true, "The source of the reference id (HMDB, ChEBI, KEGG Compound, MetaCyc or LIPID Maps)"));
        add(new Option("o", "out", true, "The output file (tsv)"));
    }


    @Override
    public void process() {

        List<Identifier> identifiers = getIdentifiers();
        List<EntityMatcher<Metabolite, ?>> matchers = getMatchers();
        List<Metabolite> reference = Collections.EMPTY_LIST;

        System.out.println("Loading reference:");
        {
            long start = System.currentTimeMillis();
            reference = getReference();
            long end = System.currentTimeMillis();
            System.out.println((end - start) + " ms");
        }

        System.out.println("Loading queries:");
        {
            long start = System.currentTimeMillis();
            for (Identifier identifier : identifiers) {
                build(identifier);
            }
            long end = System.currentTimeMillis();
            System.out.println((end - start) + " ms");
        }


        // create an entity aligner - no cache and non greedy
        EntityAligner<Metabolite> aligner = new MappedEntityAligner<Metabolite>(reference, false, false);


        try {
            CSVWriter writer = new CSVWriter(new FileWriter(getFile("o", "align", ".tsv")), '\t', '\0');

            for (EntityMatcher<Metabolite, ?> matcher : matchers) {

                Set<Identifier> matched = new HashSet<Identifier>(identifiers.size());

                aligner.push(matcher);

                System.out.println(identifiers.size());

                for (Identifier identifier : identifiers) {

                    Metabolite query = build(identifier);
                    for (Metabolite match : aligner.getMatches(query)) {
                        writer.writeNext(new String[]{
                                query.getAccession(),
                                match.getAccession(),
                                matcher.toString(),
                                matcher.calculatedMetric(query).toString(),
                                matcher.calculatedMetric(match).toString()
                        });
                        matched.add(identifier);
                    }

                }

                for (Identifier match : matched) {
                    identifiers.remove(match);
                }
                matched.clear();

            }

            System.out.println(identifiers.size());

            writer.close();

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }


    public List<EntityMatcher<Metabolite, ?>> getMatchers() {
        return new ArrayList<EntityMatcher<Metabolite, ?>>(Arrays.asList(
                new MetaboliteHashCodeMatcher(AtomicNumberSeed.class,
                                              ConnectedAtomSeed.class,
                                              BondOrderSumSeed.class,
                                              StereoSeed.class,
                                              ChargeSeed.class),
                new MetaboliteHashCodeMatcher(AtomicNumberSeed.class,
                                              ConnectedAtomSeed.class,
                                              BondOrderSumSeed.class,
                                              StereoSeed.class),
                new MetaboliteHashCodeMatcher(AtomicNumberSeed.class,
                                              ConnectedAtomSeed.class,
                                              BondOrderSumSeed.class),
                new NameMatcher<Metabolite>(false, false),
                new NameMatcher<Metabolite>(true, false),
                new NameMatcher<Metabolite>(false, true),
                new NameMatcher<Metabolite>(true, true)
                                                                        ));
    }


    public Metabolite build(Identifier identifier) {

        if (queryCache.containsKey(identifier)) {
            return queryCache.get(identifier);
        }

        ServiceManager manager = DefaultServiceManager.getInstance();

        Metabolite metabolite = DefaultEntityFactory.getInstance().newInstance(Metabolite.class);

        metabolite.setIdentifier(identifier);

        if (manager.hasService(identifier, StructureService.class)) {

            StructureService service = manager.getService(identifier, StructureService.class);

            IAtomContainer container = service.getStructure(identifier);

            if (container.getAtomCount() != 0) {
                metabolite.addAnnotation(new AtomContainerAnnotation(container));
            }

        } else {
            System.err.println("No structure service available for given"
                                       + " identifier source: "
                                       + identifier.getShortDescription());
        }

        // adds preferred names and synonyms
        assignNames(metabolite);

        queryCache.put(identifier, metabolite);

        return metabolite;


    }


    public void assignNames(Metabolite metabolite) {

        ServiceManager manager = DefaultServiceManager.getInstance();
        Identifier identifier = metabolite.getIdentifier();

        // preferred name
        if (manager.hasService(identifier, PreferredNameService.class)) {

            PreferredNameService service = manager.getService(identifier, PreferredNameService.class);

            metabolite.setName(service.getPreferredName(identifier));

        } else {
            System.err.println("No preferred service available for given"
                                       + " identifier source: "
                                       + identifier.getShortDescription());
        }

        // synonyms
        if (manager.hasService(identifier, NameService.class)) {

            NameService<Identifier> service = manager.getService(identifier, NameService.class);

            for (String name : service.getNames(identifier)) {
                metabolite.addAnnotation(new Synonym(name));
            }

        } else {
            System.err.println("No preferred service available for given"
                                       + " identifier source: "
                                       + identifier.getShortDescription());
        }

    }


    public List<Metabolite> getReference() {

        List<Metabolite> metabolites = new ArrayList<Metabolite>();

        if (!has("r")) {
            System.err.println("No reference SDF");
            return metabolites;
        }

        String idProperty = get("i", "na");
        String idSource = get("rs", "na");

        IteratingMDLReader reader = null;
        try {

            reader = new IteratingMDLReader(new FileInputStream(getFile("r")),
                                            SilentChemObjectBuilder.getInstance(),
                                            true);

            while (reader.hasNext()) {

                IAtomContainer container = reader.next();

                String id = (String) container.getProperty(idProperty);
                id = id != null ? id : "Unknown Id";

                Metabolite metabolite = DefaultEntityFactory.getInstance().newInstance(Metabolite.class);

                Identifier identifier = DefaultIdentifierFactory.getInstance().ofName(idSource, id);
                if(identifier == IdentifierFactory.EMPTY_IDENTIFIER)
                    System.err.println("could not find identifier for name: " + idSource);
                metabolite.setIdentifier(identifier);

                metabolite.addAnnotation(new AtomContainerAnnotation(container));

                // adds preferred names and synonyms
                assignNames(metabolite);

                metabolites.add(metabolite);


            }


        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }

        return metabolites;


    }


    public List<Identifier> getIdentifiers() {

        List<Identifier> identifiers = new ArrayList<Identifier>();

        if (!has("q") && !has("s")) {
            System.err.println("Missing query id list or query source");
        }

        String source = get("s", "na");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(getFile("q")));
        } catch (FileNotFoundException e) {
            System.err.println("Could not find file to open");
        }

        IdentifierFactory idFactory = DefaultIdentifierFactory.getInstance();


        try {
            String line = "";
            while ((line = reader.readLine()) != null) {
                Identifier identifier = idFactory.ofName(source, line.trim());
                if(identifier != IdentifierFactory.EMPTY_IDENTIFIER)
                    identifiers.add(identifier);
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.err.println("Could not close file");
                }
            }
        }

        return identifiers;

    }

}

