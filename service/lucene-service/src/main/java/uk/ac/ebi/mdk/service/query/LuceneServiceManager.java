package uk.ac.ebi.mdk.service.query;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.service.query.crossreference.ChEBICrossReferenceService;
import uk.ac.ebi.mdk.service.query.data.ChEBIDataService;
import uk.ac.ebi.mdk.service.query.data.HMDBDataService;
import uk.ac.ebi.mdk.service.query.data.KEGGCompoundDataService;
import uk.ac.ebi.mdk.service.query.name.ChEBINameService;
import uk.ac.ebi.mdk.service.query.name.HMDBNameService;
import uk.ac.ebi.mdk.service.query.name.KEGGCompoundNameService;
import uk.ac.ebi.mdk.service.query.structure.ChEBIStructureService;
import uk.ac.ebi.mdk.service.query.structure.HMDBStructureService;
import uk.ac.ebi.mdk.service.query.structure.KEGGCompoundStructureService;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.service.ServiceManager;

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
     *
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
    public <S extends QueryService<I>, I extends Identifier> boolean hasService(Class<? extends I> identifier, Class<S> c) {

        for (QueryService service : services.get(identifier)) {
            if (c.isAssignableFrom(service.getClass()) && service.startup()) {
                return true;
            }
        }

        return false;
    }

    /**
     * @inheritDoc
     */
    @Override
    public <S extends QueryService<I>, I extends Identifier> S getService(Class<? extends I> identifier, Class<S> c) {

        for (QueryService service : services.get(identifier)) {
            if (c.isAssignableFrom(service.getClass())) {
                return (S) service;
            }
        }

        // may want to do something better
        throw new InvalidParameterException("No " + c + "service available for " + identifier.getSimpleName());
    }

    @Override
    public <S extends QueryService<I>, I extends Identifier> boolean hasService(I identifier, Class<S> serviceClass) {
        return hasService((Class<? extends I>) identifier.getClass(), serviceClass);
    }

    @Override
    public <S extends QueryService<I>, I extends Identifier> S getService(I identifier, Class<S> serviceClass) {
        return getService((Class<? extends I>) identifier.getClass(), serviceClass);
    }

    /**
     * Access a service that implements multiple interfaces
     *
     * @param identifier
     * @param classes
     * @param <S>
     * @param <I>        Example:
     *                   <pre>{@code
     *                   QueryService<ChEBIIdentifier> service = ServiceManager.getService(ChEBIIdentifier.class,
     *                                                                                     BrandNameService.class,
     *                                                                                     IUPACNameService.class,
     *                                                                                     PreferredNameService.class);
     *                   <p/>
     *                   <p/>
     *                   BrandNameService<ChEBIIdentifier> brandService    = (BrandNameService<ChEBIIdentifier>)
     *                   service;
     *                   IUPACNameService<ChEBIIdentifier> iupacService    = (IUPACNameService<ChEBIIdentifier>)
     *                   service;
     *                   PreferredNameService<ChEBIIdentifier> prefService = (PreferredNameService<ChEBIIdentifier>)
     *                   service;
     *                   <p/>
     *                   ChEBIIdentifier chebiId = new ChEBIIdentifier("CHEBI:15422");
     *                   <p/>
     *                   System.out.println("Brand: "     + brandService.getBrandName(chebiId));
     *                   System.out.println("IUPAC: "     + iupacService.getIUPACName(chebiId));
     *                   System.out.println("Preferred: " + prefService.getPreferredName(chebiId));
     *                   }</pre>
     *
     * @return
     */
    public <S extends QueryService<I>,
            I extends Identifier> S getService(Class<I> identifier, Class... classes) {

        for (QueryService service : services.get(identifier)) {
            for (Class c : classes) {
                if (!c.isAssignableFrom(service.getClass())) {
                    break;
                }
                return (S) service;
            }
        }

        // may want to do something better
        throw new InvalidParameterException("No " + classes + "service available for " + identifier.getSimpleName());

    }

}
