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
import java.util.Arrays;
import java.util.Collection;

/**
 * LuceneServiceManager - 28.02.2012 <br/>
 * <p/>
 * Manager of lucene index based services
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@Deprecated
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
    public <S extends QueryService<I>, I extends Identifier> boolean hasService(Class<? extends I> identifier,
                                                                                Class<? extends S> c) {

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
    @SuppressWarnings("unchecked")
    public <S extends QueryService<I>, I extends Identifier> S getService(Class<? extends I> identifier, Class<? extends S> c) {

        for (QueryService service : services.get(identifier)) {
            if (c.isAssignableFrom(service.getClass())) {
                return (S) service;
            }
        }

        // may want to do something better
        throw new InvalidParameterException("No " + c + "service available for " + identifier.getSimpleName());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S extends QueryService<I>, I extends Identifier> boolean hasService(I identifier, Class<? extends S> serviceClass) {
        return hasService((Class<? extends I>) identifier.getClass(), serviceClass);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S extends QueryService<I>, I extends Identifier> S getService(I identifier, Class<? extends S> serviceClass) {
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
    @SuppressWarnings("unchecked")
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
        throw new InvalidParameterException("No " + Arrays.toString(classes) + "service available for " + identifier.getSimpleName());

    }

    @Override
    public <I extends Identifier, S extends QueryService> S createService(Class<? extends I> identifierClass, Class<? extends S> service) {
        return null;
    }

    @Override
    public Collection<Identifier> getIdentifiers(Class<? extends QueryService> c) {
        return null;
    }
}
