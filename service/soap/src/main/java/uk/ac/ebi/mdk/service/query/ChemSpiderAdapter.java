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

import com.chemspider.CompoundInfo;
import com.chemspider.SearchLocator;
import com.chemspider.SearchSoap;
import org.apache.log4j.Logger;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.inchi.InChIGeneratorFactory;
import org.openscience.cdk.inchi.InChIToStructure;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import uk.ac.ebi.caf.utility.preference.type.StringPreference;
import uk.ac.ebi.mdk.domain.identifier.ChemSpiderIdentifier;
import uk.ac.ebi.mdk.service.ServicePreferences;
import uk.ac.ebi.mdk.service.query.structure.StructureService;

import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;

/**
 * @author John May
 */
public class ChemSpiderAdapter
        extends AbstractSoapService<ChemSpiderIdentifier>
        implements StructureService<ChemSpiderIdentifier> {

    private static final Logger LOGGER = Logger.getLogger(ChemSpiderAdapter.class);

    private static final IChemObjectBuilder BUILDER = SilentChemObjectBuilder.getInstance();

    private SearchSoap service;
    private SearchLocator locator = new SearchLocator();

    @Override
    public ChemSpiderIdentifier getIdentifier() {
        return new ChemSpiderIdentifier();
    }

    @Override
    public IAtomContainer getStructure(ChemSpiderIdentifier identifier) {

        try {

            StringPreference key = ServicePreferences.getInstance().getPreference("CHEM_SPIDER_KEY");

            if (key.get().isEmpty()) {
                System.err.println("No ChemSpider key available: please input the key via preferences!");
                return BUILDER.newInstance(IAtomContainer.class);
            }

            CompoundInfo info = service.getCompoundInfo(Integer.parseInt(identifier.getAccession()),
                                                        key.get());

            InChIGeneratorFactory inchiFactory = InChIGeneratorFactory.getInstance();
            InChIToStructure inchi2structure = inchiFactory.getInChIToStructure(info.getInChI(), BUILDER);

            return inchi2structure.getAtomContainer();


        } catch (CDKException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return BUILDER.newInstance(IAtomContainer.class);

    }

    @Override
    public boolean startup() {
        if (service != null) return true;
        try {
            service = locator.getSearchSoap();
        } catch (ServiceException ex) {
            LOGGER.error("Startup failed on SOAP Web Service: " + ex.getMessage());
        }
        return service != null;
    }

}
