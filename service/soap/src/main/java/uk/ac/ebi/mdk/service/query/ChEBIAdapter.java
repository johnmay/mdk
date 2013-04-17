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

import org.apache.log4j.Logger;
import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.mdk.domain.identifier.ChEBIIdentifier;
import uk.ac.ebi.mdk.service.query.name.IUPACNameService;
import uk.ac.ebi.mdk.service.query.name.NameService;
import uk.ac.ebi.mdk.service.query.name.PreferredNameService;
import uk.ac.ebi.mdk.service.query.name.SynonymService;
import uk.ac.ebi.mdk.service.query.structure.StructureService;
import uk.ac.ebi.ws.chebi.*;

import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author John May
 */
public class ChEBIAdapter
        extends AbstractSoapService<ChEBIIdentifier>
        implements SynonymService<ChEBIIdentifier>,
                   NameService<ChEBIIdentifier>,
                   IUPACNameService<ChEBIIdentifier>,
                   PreferredNameService<ChEBIIdentifier>,
                   StructureService<ChEBIIdentifier> {

    private static final Logger LOGGER             = Logger.getLogger(ChEBIAdapter.class);
    private static       int    DEFAULT_CACHE_SIZE = 200;
    private int cacheSize;

    private Map<ChEBIIdentifier, Entity> entites = new LinkedHashMap<ChEBIIdentifier, Entity>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry<ChEBIIdentifier, Entity> eldest) {
            return size() > cacheSize;
        }
    };

    private ChebiWebServiceServiceLocator locator = new ChebiWebServiceServiceLocator();
    private ChebiWebServicePortType service;

    public ChEBIAdapter() {
        this(DEFAULT_CACHE_SIZE);
    }

    public ChEBIAdapter(int cacheSize) {
        this.cacheSize = cacheSize;
    }

    @Override
    public String getIUPACName(ChEBIIdentifier identifier) {

        Collection<String> names = new ArrayList<String>();

        for (DataItem item : getCompleteEntity(identifier).getIupacNames()) {
            names.add(item.getData());
        }

        return names.isEmpty() ? "" : names.iterator().next(); // first only

    }

    public Entity getCompleteEntity(ChEBIIdentifier identifier) {

        if (entites.containsKey(identifier)) {
            return entites.get(identifier);
        }

        try {
            Entity entity = service.getCompleteEntity(identifier.getAccession());
            entites.put(identifier, entity);
            return entity;
        } catch (RemoteException ex) {
            LOGGER.error("Remote exception occurred " + ex.getMessage());
        }

        Entity entity = new Entity();

        // make sure we get no null pointers
        entity.setChebiAsciiName("");
        entity.setSynonyms(new DataItem[0]);
        entity.setIupacNames(new DataItem[0]);
        entity.setChemicalStructures(new StructureDataItem[0]);
        entity.setCharge("0");
        entity.setFormulae(new DataItem[0]);

        entites.put(identifier, entity);

        return entity;

    }

    @Override
    public Collection<ChEBIIdentifier> searchIUPACName(String name, boolean approximate) {
        return searchBy(name, SearchCategory.value6);
    }

    @Override
    public Collection<ChEBIIdentifier> searchPreferredName(String name, boolean approximate) {
        return searchBy(name, SearchCategory.value3);
    }

    @Override
    public String getPreferredName(ChEBIIdentifier identifier) {
        Entity entity = getCompleteEntity(identifier);
        return entity.getChebiAsciiName();
    }

    @Override
    public Collection<ChEBIIdentifier> searchName(String name, boolean approximate) {
        return searchBy(name, SearchCategory.value5);
    }

    @Override
    public Collection<String> getNames(ChEBIIdentifier identifier) {

        Collection<String> names = new ArrayList<String>();
        names.add(getCompleteEntity(identifier).getChebiAsciiName());

        if (getCompleteEntity(identifier).getSynonyms() != null) {
            for (DataItem item : getCompleteEntity(identifier).getSynonyms()) {
                names.add(item.getData());
            }
        }
        if (getCompleteEntity(identifier).getIupacNames() != null) {
            for (DataItem item : getCompleteEntity(identifier).getIupacNames()) {
                names.add(item.getData());
            }
        }

        return names;
    }

    @Override
    public Collection<ChEBIIdentifier> searchSynonyms(String name, boolean approximate) {
        return searchBy(name, SearchCategory.value5);
    }

    @Override
    public Collection<String> getSynonyms(ChEBIIdentifier identifier) {

        Collection<String> synonyms = new ArrayList<String>();
        for (DataItem item : getCompleteEntity(identifier).getSynonyms()) {
            synonyms.add(item.getData());
        }

        return synonyms;
    }

    private Collection<ChEBIIdentifier> searchBy(String name, SearchCategory a) {

        Collection<ChEBIIdentifier> identifiers = new ArrayList<ChEBIIdentifier>();

        try {
            LiteEntityList ents = service.getLiteEntity(name, a, getMaxResults(), StarsCategory.value3);
            if (ents != null && ents.getListElement() != null) {
                for (LiteEntity entity : ents.getListElement()) {
                    identifiers.add(getIdentifier(entity.getChebiId()));
                }
            }

        } catch (RemoteException ex) {
            LOGGER.error("Problems getting lite entity from ChEBI Web service", ex);
        }

        return identifiers;

    }

    @Override
    public ChEBIIdentifier getIdentifier() {
        return new ChEBIIdentifier();
    }

    private static final String address = "http://www.ebi.ac.uk/chebi/";

    @Override
    public boolean startup() {
        if (service != null)
            return reachable(address);
        try {
            service = locator.getChebiWebServicePort();
        } catch (ServiceException ex) {
            LOGGER.error("Startup failed on SOAP Web Service: " + ex.getMessage());
        }
        return service != null && reachable(address);
    }

    @Override
    public IAtomContainer getStructure(ChEBIIdentifier identifier) {

        Entity entity = getCompleteEntity(identifier);

        if (entity.getChemicalStructures() != null) {
            for (StructureDataItem item : entity.getChemicalStructures()) {
                if (item.getType().equals("mol")) {
                    return mol2Structure(item.getStructure());
                }
            }
        }


        return mol2Structure(null);

    }
}
