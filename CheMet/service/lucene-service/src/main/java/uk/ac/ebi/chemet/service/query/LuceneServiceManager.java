package uk.ac.ebi.chemet.service.query;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.apache.log4j.Logger;
import uk.ac.ebi.chemet.service.query.crossreference.ChEBICrossReferenceService;
import uk.ac.ebi.chemet.service.query.data.ChEBIDataService;
import uk.ac.ebi.chemet.service.query.data.HMDBDataService;
import uk.ac.ebi.chemet.service.query.data.KEGGCompoundDataService;
import uk.ac.ebi.chemet.service.query.name.ChEBINameService;
import uk.ac.ebi.chemet.service.query.name.HMDBNameService;
import uk.ac.ebi.chemet.service.query.name.KEGGCompoundNameService;
import uk.ac.ebi.chemet.service.query.structure.ChEBIStructureService;
import uk.ac.ebi.chemet.service.query.structure.HMDBStructureService;
import uk.ac.ebi.chemet.service.query.structure.KEGGCompoundStructureService;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.service.ServiceManager;
import uk.ac.ebi.service.query.QueryService;

import java.security.InvalidParameterException;

/**
 * LuceneServiceManager - 28.02.2012 <br/>
 * <p/>
 * Manager of lucene index based services
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class LuceneServiceManager
        implements ServiceManager {

    private static final Logger LOGGER = Logger.getLogger(LuceneServiceManager.class);

    private Multimap<Class<? extends Identifier>, QueryService> services = HashMultimap.create();

    private LuceneServiceManager() {

        /* ChEBI */
        add(new ChEBINameService());
        add(new ChEBIDataService());
        add(new ChEBIStructureService());
        add(new ChEBICrossReferenceService());

        /* HMDB */
        add(new HMDBDataService());
        add(new HMDBNameService());
        add(new HMDBStructureService());

        /* KEGG */
        add(new KEGGCompoundDataService());
        add(new KEGGCompoundNameService());
        add(new KEGGCompoundStructureService());


    }

    private void add(QueryService service) {
        services.put(service.getIdentifier().getClass(), service);
    }

    /**
     * Acces the singleton instance of this {@see ServiceManager}
     * @return instance of the manager
     */
    public static LuceneServiceManager getInstance() {
        return DefaultServiceManagerHolder.INSTANCE;
    }

    private static class DefaultServiceManagerHolder {
        private static LuceneServiceManager INSTANCE = new LuceneServiceManager();
    }

    /**
     * @inheritDoc
     */
    @Override
    public <S extends QueryService<I>, I extends Identifier> boolean hasService(Class<I> identifier, Class<S> c) {

        for (QueryService service : services.get(identifier)) {
            if (c.isAssignableFrom(service.getClass()) && service.isAvailable()) {
                return true;
            }
        }

        return false;
    }

    /**
     * @inheritDoc
     */
    @Override
    public <S extends QueryService<I>, I extends Identifier> S getService(Class<I> identifier, Class<S> c) {

        for (QueryService service : services.get(identifier)) {
            if (c.isAssignableFrom(service.getClass())) {
                return (S) service;
            }
        }

        // may want to do something better
        throw new InvalidParameterException("No " + c + "service available for " + identifier.getSimpleName());
    }

}
