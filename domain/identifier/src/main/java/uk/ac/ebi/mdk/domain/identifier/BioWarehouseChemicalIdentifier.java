
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

package uk.ac.ebi.mdk.domain.identifier;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.lang.annotation.Brief;
import uk.ac.ebi.mdk.deprecated.MIRIAMEntry;
import uk.ac.ebi.mdk.domain.identifier.type.BioWarehouseIdentifier;
import uk.ac.ebi.mdk.domain.IdentifierMetaInfo;


/**
 *
 * @name    BRENDAChemicalIdentifier – 2011.08.16
 *          An identifier for BRENDA Compounds
 *
 * @version $Rev$ : Last Changed $Date$
 *
 * @author  pmoreno
 * @author  $Author$ (this version)
 *
 */
@Brief("BioWarehouse Chemical")
public class BioWarehouseChemicalIdentifier
  extends AbstractChemicalIdentifier implements BioWarehouseIdentifier {

    private static final Logger LOGGER = Logger.getLogger(BioWarehouseChemicalIdentifier.class);
    private static final IdentifierMetaInfo DESCRIPTION = IDENTIFIER_LOADER.getMetaInfo(
      BioWarehouseChemicalIdentifier.class);
    private Long wid;
    private Long dataSetWid;

    public BioWarehouseChemicalIdentifier() {
    }


    public BioWarehouseChemicalIdentifier(String accession) {
        super(accession);
    }
    
    public BioWarehouseChemicalIdentifier(Long wid, Long dataSetWID) {
        super("WID:"+wid+"_DWID:"+dataSetWID);
        this.wid = wid;
        this.dataSetWid = dataSetWID;
    }


    /**
     * @inheritDoc
     */
    @Override
    public BioWarehouseChemicalIdentifier newInstance() {
        return new BioWarehouseChemicalIdentifier();
    }




    /**
     * @inheritDoc
     */
    @Override
    public String getShortDescription() {
        return DESCRIPTION.brief;
    }


    /**
     * @inheritDoc
     */
    @Override
    public String getLongDescription() {
        return DESCRIPTION.description;
    }


    /**
     * @inheritDoc
     */
    @Override
    public MIRIAMEntry getResource() {
        return DESCRIPTION.resource;
    }

    public void setWID(Long wid) {
        this.wid = wid;
    }

    public void setDataSetWID(Long dataSetWID) {
        this.dataSetWid = dataSetWID;
    }

    public Long getWID() {
        return wid;
    }

    public Long getDataSetWID() {
        return dataSetWid;
    }


}

