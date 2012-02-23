//package uk.ac.ebi.chemet.service.index.query.name;
//
//import org.apache.lucene.search.*;
//import uk.ac.ebi.io.service.index.name.HMDBNameIndex;
//import uk.ac.ebi.resource.chemical.HMDBIdentifier;
//import uk.ac.ebi.service.query.IUPACNameService;
//import uk.ac.ebi.service.query.NameService;
//import uk.ac.ebi.service.query.PreferredNameService;
//import uk.ac.ebi.service.query.SynonymService;
//
//import java.io.IOException;
//import java.util.*;
//
///**
// * ${Name}.java - 21.02.2012 <br/> Description...
// *
// * @author johnmay
// * @author $Author$ (this version)
// * @version $Rev$
// */
//public class HMDBNameQueryService
//        extends AbstractNameService
//        implements IUPACNameService<HMDBIdentifier>,
//        PreferredNameService<HMDBIdentifier>,
//        SynonymService<HMDBIdentifier>,
//        NameService<HMDBIdentifier> {
//
//    private IndexSearcher searcher;
//
//    public HMDBNameQueryService() throws IOException {
//        super(new HMDBNameIndex());
//        searcher = new IndexSearcher(getDirectory());
//    }
//
//
//    /**
//     * @inheritDoc
//     */
//    @Override
//    public Collection<HMDBIdentifier> searchPreferredName(String name, boolean approximate) {
//
//        Query query = create(name, PREFERRED_NAME, approximate);
//
//        return getIdentifiers(searcher, query, getIdentifier());
//
//    }
//
//    /**
//     * @inheritDoc
//     */
//    @Override
//    public String getPreferredName(HMDBIdentifier identifier) {
//
//        TermQuery query = new TermQuery(IDENTIFIER_TERM.createTerm(identifier.getAccession()));
//
//        return getFirstValue(searcher, query, PREFERRED_NAME.field());
//
//    }
//
//    /**
//     * @inheritDoc
//     */
//    @Override
//    public Collection<HMDBIdentifier> searchSynonyms(String name, boolean approximate) {
//
//        Query query = create(name, SYNONYM, approximate);
//
//        return getIdentifiers(searcher, query, getIdentifier());
//
//    }
//
//    /**
//     * @inheritDoc
//     */
//    @Override
//    public Collection<String> getSynonyms(HMDBIdentifier identifier) {
//
//        Query query = new TermQuery(IDENTIFIER_TERM.createTerm(identifier.getAccession()));
//
//        Collection<String> synonyms = new ArrayList<String>();
//
//        TopScoreDocCollector collector = TopScoreDocCollector.create(getMaxResults(), true);
//        try {
//
//            searcher.search(query, collector);
//            ScoreDoc[] hits = collector.topDocs().scoreDocs;
//
//            for (ScoreDoc document : hits) {
//                synonyms.addAll(Arrays.asList(getValues(document, SYNONYM.field())));
//            }
//
//        } catch (IOException ex) {
//
//        }
//
//        return synonyms;
//
//    }
//
//    /**
//     * @inheritDoc
//     */
//    @Override
//    public String getIUPACName(HMDBIdentifier identifier) {
//
//        TermQuery query = new TermQuery(IDENTIFIER_TERM.createTerm(identifier.getAccession()));
//
//        return getFirstValue(searcher, query, IUPAC.field());
//
//    }
//
//    /**
//     * @inheritDoc
//     */
//    @Override
//    public Collection<HMDBIdentifier> searchIUPACName(String name, boolean approximate) {
//
//        Query query = create(name, IUPAC, approximate);
//
//        return getIdentifiers(searcher, query, getIdentifier());
//
//    }
//
//    /**
//     * @inheritDoc
//     */
//    @Override
//    public Collection<HMDBIdentifier> searchName(String name, boolean approximate) {
//
//        Set<HMDBIdentifier> identifiers = new HashSet<HMDBIdentifier>();
//
//        identifiers.addAll(searchIUPACName(name, approximate));
//        identifiers.addAll(searchPreferredName(name, approximate));
//        identifiers.addAll(searchSynonyms(name, approximate));
//
//        return identifiers;
//
//    }
//
//    /**
//     * @inheritDoc
//     */
//    @Override
//    public Collection<String> getNames(HMDBIdentifier identifier) {
//
//        Collection<String> names = getSynonyms(identifier);
//
//        names.add(getPreferredName(identifier));
//        names.add(getIUPACName(identifier));
//
//        return names;
//
//    }
//
//    @Override
//    public HMDBIdentifier getIdentifier() {
//        return new HMDBIdentifier();
//    }
//
//    public static void main(String[] args) throws IOException {
//        IUPACNameService service = new HMDBNameQueryService();
//        service.searchIUPACName("[[[(2S,3S,4R,5R)-5-(6-aminopurin-9-yl)-3,4-dihydroxy-oxolan-2-yl]methoxy-hydroxy-phosphoryl]oxy-hydroxy-phosphoryl]oxyphosphonic acid", false);
//        service.getIUPACName(new HMDBIdentifier("HMDB00538"));
//    }
//
//}
