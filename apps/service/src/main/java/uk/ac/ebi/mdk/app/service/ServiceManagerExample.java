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

package uk.ac.ebi.mdk.app.service;

import org.apache.log4j.Logger;
import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.mdk.domain.identifier.BioCycChemicalIdentifier;
import uk.ac.ebi.mdk.domain.identifier.ChEBIIdentifier;
import uk.ac.ebi.mdk.domain.identifier.ChemSpiderIdentifier;
import uk.ac.ebi.mdk.domain.identifier.HMDBIdentifier;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.identifier.KEGGCompoundIdentifier;
import uk.ac.ebi.mdk.domain.identifier.LIPIDMapsIdentifier;
import uk.ac.ebi.mdk.domain.identifier.PubChemCompoundIdentifier;
import uk.ac.ebi.mdk.service.DefaultServiceManager;
import uk.ac.ebi.mdk.service.ServiceManager;
import uk.ac.ebi.mdk.service.query.structure.StructureService;

import java.util.Arrays;
import java.util.List;

/**
 * @author John May
 */
public class ServiceManagerExample {

    private static final Logger LOGGER = Logger.getLogger(ServiceManagerExample.class);


    public static void main(String[] args) {

        ServiceManager manager = DefaultServiceManager.getInstance();

        List<? extends Identifier> identifiers = Arrays.asList(new ChEBIIdentifier("CHEBI:15422"),
                                                               new KEGGCompoundIdentifier("C00009"),
                                                               new HMDBIdentifier("HMDB00538"),
                                                               new KEGGCompoundIdentifier("C00010"),
                                                               new ChEBIIdentifier("CHEBI:57299"),
                                                               new LIPIDMapsIdentifier("LMFA01010004"),
                                                               new BioCycChemicalIdentifier("META", "ATP"),
                                                               new PubChemCompoundIdentifier("5957"),
                                                               new ChemSpiderIdentifier("5742"));


        System.out.printf("%-30s %-15s %-20s %-5s %s\n",
                          "Resource",
                          "Identifier",
                          "Service Type",
                          "Atoms",
                          "Time (s)");
        System.out.println();

        for (Identifier identifier : identifiers) {
            if (manager.hasService(identifier, StructureService.class)) {
                long start = System.currentTimeMillis();
                StructureService<Identifier> service = manager.getService(identifier,
                                                                          StructureService.class);
                long end = System.currentTimeMillis();
                IAtomContainer structure = service.getStructure(identifier);
                System.out.printf("%-30s %-15s %-20s %02d    %.3f\n",
                                  identifier.getShortDescription(),
                                  identifier,
                                  service.getServiceType(),
                                  structure.getAtomCount(),
                                  (end - start) / 1000f);

            }
        }

    }


}
