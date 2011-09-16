
/**
 * PubChemCompoundIdentifier.java
 *
 * 2011.09.15
 *
 * This file is part of the CheMet library
 *
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CheMet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.resource.chemical;

import org.apache.log4j.Logger;
import uk.ac.ebi.metabolomes.identifier.MIRIAMEntry;
import uk.ac.ebi.resource.IdentifierDescription;
import uk.ac.ebi.resource.IdentifierLoader;


/**
 *          PubChemCompoundIdentifier – 2011.09.15 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class PubChemSubstanceIdentifier
  extends ChemicalIdentifier {

    private static final Logger LOGGER = Logger.getLogger(PubChemSubstanceIdentifier.class);
    private static final IdentifierDescription DESCRIPTION = IdentifierLoader.getInstance().getMetaInfo(
      PubChemSubstanceIdentifier.class);


    public PubChemSubstanceIdentifier() {
    }


    public PubChemSubstanceIdentifier(String accession) {
        super(accession);
    }


    /**
     * @inheritDoc
     */
    public PubChemSubstanceIdentifier newInstance() {
        return new PubChemSubstanceIdentifier();
    }


    /**
     * @inheritDoc
     */
    @Override
    public Byte getIndex() {
        return DESCRIPTION.index;
    }


    /**
     * @inheritDoc
     */
    @Override
    public String getShortDescription() {
        return DESCRIPTION.shortDescription;
    }


    /**
     * @inheritDoc
     */
    @Override
    public String getLongDescription() {
        return DESCRIPTION.longDescription;
    }


    /**
     * @inheritDoc
     */
    @Override
    public MIRIAMEntry getResource() {
        return DESCRIPTION.resource;
    }



    public static void main(String[] args) {
        System.out.println(new PubChemSubstanceIdentifier("123").equals(new PubChemSubstanceIdentifier("123")));
    }



}

