package uk.ac.ebi.chemet.service.query.name;

import uk.ac.ebi.chemet.service.index.name.ChEBINameIndex;
import uk.ac.ebi.chemet.service.loader.location.SystemLocation;
import uk.ac.ebi.chemet.service.loader.location.ZIPSystemLocation;
import uk.ac.ebi.chemet.service.loader.name.ChEBINameLoader;
import uk.ac.ebi.chemet.service.query.AbstractQueryService;
import uk.ac.ebi.resource.chemical.ChEBIIdentifier;
import uk.ac.ebi.service.ResourceLoader;
import uk.ac.ebi.service.query.name.*;

import java.util.Collection;
import java.util.HashSet;

/**
 * ChEBINameService - 23.02.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class ChEBINameService
        extends AbstractQueryService<ChEBIIdentifier>
        implements IUPACNameService<ChEBIIdentifier>,
                   PreferredNameService<ChEBIIdentifier>,
                   SynonymService<ChEBIIdentifier>,
                   BrandNameService<ChEBIIdentifier>,
                   InternationalNonproprietaryNameService<ChEBIIdentifier>,
                   NameService<ChEBIIdentifier> {

    public ChEBINameService() {
        super(new ChEBINameIndex());
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<ChEBIIdentifier> searchName(String name, boolean approximate) {
        // use set as to avoid duplicates
        Collection<ChEBIIdentifier> identifiers = new HashSet<ChEBIIdentifier>();

        // efficiency could be improved with multifield search
        identifiers.addAll(searchPreferredName(name, approximate));
        identifiers.addAll(searchSynonyms(name, approximate));
        identifiers.addAll(searchIUPACName(name, approximate));
        identifiers.addAll(searchBrandName(name, approximate));
        identifiers.addAll(searchINN(name, approximate));

        return identifiers;

    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<String> getNames(ChEBIIdentifier identifier) {
        // use set as to avoid duplicates
        Collection<String> names = new HashSet<String>();

        names.add(getIUPACName(identifier));
        names.add(getPreferredName(identifier));
        names.add(getBrandName(identifier));
        names.add(getINN(identifier));
        names.addAll(getSynonyms(identifier));

        names.remove(""); // remove empty results

        return names;

    }

    /**
     * @inheritDoc
     */
    @Override
    public String getIUPACName(ChEBIIdentifier identifier) {
        return firstValue(identifier, IUPAC);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<ChEBIIdentifier> searchIUPACName(String name, boolean approximate) {
        return getIdentifiers(construct(name, IUPAC, approximate));
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<ChEBIIdentifier> searchPreferredName(String name, boolean approximate) {
        return getIdentifiers(construct(name, PREFERRED_NAME, approximate));
    }

    public Collection<ChEBIIdentifier> searchPreferredName_split(String name, boolean approximate) {
        return getIdentifiers(construct(name, PREFERRED_NAME, approximate));
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getPreferredName(ChEBIIdentifier identifier) {
        return firstValue(identifier, PREFERRED_NAME);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<ChEBIIdentifier> searchSynonyms(String name, boolean approximate) {
        return getIdentifiers(construct(name, SYNONYM, approximate));
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<String> getSynonyms(ChEBIIdentifier identifier) {
        return firstValues(identifier, SYNONYM);
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getBrandName(ChEBIIdentifier identifier) {
        return firstValue(identifier, BRAND_NAME);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<ChEBIIdentifier> searchBrandName(String name, boolean approximate) {
        return getIdentifiers(construct(name, BRAND_NAME, approximate));
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getINN(ChEBIIdentifier identifier) {
        return firstValue(identifier, INN);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<ChEBIIdentifier> searchINN(String name, boolean approximate) {
        return getIdentifiers(construct(name, INN, approximate));
    }

    /**
     * @inheritDoc
     */
    @Override
    public ChEBIIdentifier getIdentifier() {
        return new ChEBIIdentifier();
    }

    public static void main(String[] args) {

        try {

            ResourceLoader loader = new ChEBINameLoader();
            loader.addLocation("ChEBI Compounds", new ZIPSystemLocation("/databases/chebi/flatfiles/compounds.zip"));
            loader.addLocation("ChEBI Names", new SystemLocation("/databases/chebi/flatfiles/names.tsv"));
          //  loader.backup(); loader.update();
        } catch (Exception ex) {

        }

        ChEBINameService service = new ChEBINameService();


        System.out.println(service.values(service.construct("chebi:15390", IDENTIFIER, false), SYNONYM));


        //        Collection<String> names = new HashSet<String>();
        //        for (int i = 0; i < 8000; i++) {
        //            ChEBIIdentifier id = new ChEBIIdentifier(i);
        //            String preferredName = service.getPreferredName(id);
        //            if (!preferredName.isEmpty()) {
        //                names.add(preferredName);
        //            }
        //        }
        //
        //        for (String name : names) {
        //            service.searchPreferredName_split(name, false);
        //            service.searchPreferredName(name, false);
        //        }
        //
        //
        //        {
        //            System.out.print("getIdentifiers_split(\"...\", false) : ");
        //            long start = System.currentTimeMillis();
        //            ChEBINameService newService = new ChEBINameService();
        //            for (String name : names) {
        //                newService.searchPreferredName_split(name, false);
        //            }
        //            long end = System.currentTimeMillis();
        //            System.out.println(end - start);
        //        }
        //
        //        {
        //            ChEBINameService newService = new ChEBINameService();
        //            System.out.print("getIdentifiers(\"...\", false) : ");
        //            long start = System.currentTimeMillis();
        //            for (String name : names) {
        //                newService.searchPreferredName(name, false);
        //            }
        //            long end = System.currentTimeMillis();
        //            System.out.println(end - start);
        //        }


        //        for (String name : Arrays.asList("(R)-Acetoin", "Coenzyme A")) {
        //            Collection<ChEBIIdentifier> results = service.searchPreferredName(name, false);
        //            System.out.println(name + ":");
        //            for (ChEBIIdentifier id : results) {
        //                System.out.println(id + "\t" + service.getPreferredName(id));
        //            }
        //        }


        //          Collection<ChEBIIdentifier> results = service.searchName("Coenzyme A", true);
        //        Collection<ChEBIIdentifier> results = service.searchName("D-glucose 6-phosphate", true);
        //        Collection<ChEBIIdentifier> results = service.searchName("phenyl lactate", true);
        //        System.out.println(results.size() + " results");
        //        for(ChEBIIdentifier id : results){
        //            System.out.println(id + "\t" + service.getNames(id));
        //        }
        //        long end = System.currentTimeMillis();
        //        System.out.println("time: " + (end - start));
    }
}
