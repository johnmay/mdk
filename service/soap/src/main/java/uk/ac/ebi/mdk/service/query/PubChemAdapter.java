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

import gov.nih.nlm.ncbi.pubchem.ws.*;
import gov.nih.nlm.ncbi.pubchem.ws.holders.DataBlobTypeHolder;
import org.apache.log4j.Logger;
import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.mdk.domain.identifier.PubChemCompoundIdentifier;
import uk.ac.ebi.mdk.service.query.structure.StructureSearch;
import uk.ac.ebi.mdk.service.query.structure.StructureService;
import uk.ac.ebi.mdk.service.query.structure.SubstructureSearch;
import uk.ac.ebi.mdk.service.query.structure.SuperstructureSearch;

import javax.xml.rpc.ServiceException;
import javax.xml.rpc.holders.StringHolder;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author John May
 */
public class PubChemAdapter
        extends AbstractSoapService<PubChemCompoundIdentifier>
        implements QueryService<PubChemCompoundIdentifier>,
                   StructureService<PubChemCompoundIdentifier>,
                   StructureSearch<PubChemCompoundIdentifier>,
                   SubstructureSearch<PubChemCompoundIdentifier>,
                   SuperstructureSearch<PubChemCompoundIdentifier> {

    private static final Logger LOGGER = Logger.getLogger(PubChemAdapter.class);

    private PUGLocator locator = new PUGLocator();
    private PUGSoap service;

    public PubChemAdapter() {
    }

    @Override
    public IAtomContainer getStructure(PubChemCompoundIdentifier identifier) {

        try {

            ArrayOfInt aoi = new ArrayOfInt(new int[]{Integer.parseInt(identifier.getAccession())});

            String listKey = service.inputList(aoi, PCIDType.eID_CID);
            StringHolder sh = new StringHolder();
            DataBlobTypeHolder blob = new DataBlobTypeHolder();
            service.download(listKey, FormatType.eFormat_SDF, CompressType.eCompress_None, false, 0, Boolean.TRUE, sh, blob);

            return mol2Structure(new String(blob.value.getData()));


        } catch (NumberFormatException ex) {
            LOGGER.error("invalid PubChem-Compound identifier - accession must be a number");
        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return mol2Structure("");
    }

    @Override
    public Collection<PubChemCompoundIdentifier> structureSearch(IAtomContainer molecule, boolean approximate) {


        try {
            String structurekey = service.inputStructureBase64(structure2Mol(molecule).getBytes(), FormatType.eFormat_SDF);
            return collectIdentifiers(service.similaritySearch2D(structurekey,
                                                                 new SimilaritySearchOptions(),
                                                                 new LimitsType(2, getMaxResults(), null)));
        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        // return empty list
        return collectIdentifiers("");
    }

    @Override
    public Collection<PubChemCompoundIdentifier> substructureSearch(IAtomContainer molecule) {

        try {
            String structurekey = service.inputStructureBase64(structure2Mol(molecule).getBytes(), FormatType.eFormat_SDF);
            // need to configure options
            return collectIdentifiers(service.substructureSearch(structurekey,
                                                                 new StructureSearchOptions(),
                                                                 new LimitsType(2, getMaxResults(), null)));
        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        // return empty list
        return collectIdentifiers("");
    }

    @Override
    public Collection<PubChemCompoundIdentifier> superstructureSearch(IAtomContainer molecule) {
        try {
            String structurekey = service.inputStructureBase64(structure2Mol(molecule).getBytes(), FormatType.eFormat_SDF);
            // need to configure options
            return collectIdentifiers(service.superstructureSearch(structurekey,
                                                                   new StructureSearchOptions(),
                                                                   new LimitsType(2, getMaxResults(), null)));
        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        // return empty list
        return collectIdentifiers("");
    }

    public Collection<PubChemCompoundIdentifier> collectIdentifiers(String listKey) {

        Collection<PubChemCompoundIdentifier> cids = new ArrayList<PubChemCompoundIdentifier>();

        if (listKey == null || listKey.isEmpty())
            return cids;

        try {
            while (service.getOperationStatus(listKey) == StatusType.eStatus_Running) {
                try {
                    Thread.sleep(2000l);
                } catch (InterruptedException ex) {
                    LOGGER.info("Sleep was interrupted");
                }
            }

            ArrayOfInt aoi = service.getIDList(listKey, 0, service.getListItemsCount(listKey));
            for (int id : aoi.get_int()) {
                cids.add(getIdentifier(Integer.toString(id)));
            }
        } catch (RemoteException ex) {
            LOGGER.error("Remote exception occurred: " + ex.getMessage());
        }

        return cids;
    }

    @Override
    public PubChemCompoundIdentifier getIdentifier() {
        return new PubChemCompoundIdentifier();
    }

    @Override
    public boolean startup() {
        if (service != null) return true && reachable("http://pubchem.ncbi.nlm.nih.gov/");
        try {
            service = locator.getPUGSoap();
        } catch (ServiceException ex) {
            LOGGER.error("Startup failed on SOAP Web Service: " + ex.getMessage());
        }
        return service != null && reachable("http://pubchem.ncbi.nlm.nih.gov/");
    }
}
