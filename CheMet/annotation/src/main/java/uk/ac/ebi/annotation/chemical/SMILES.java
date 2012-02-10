/**
 * SMILES.java
 *
 * 2012.02.03
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
package uk.ac.ebi.annotation.chemical;

import org.apache.log4j.Logger;
import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.annotation.base.AbstractStringAnnotation;
import uk.ac.ebi.annotation.util.AnnotationLoader;
import uk.ac.ebi.core.Description;
import uk.ac.ebi.interfaces.Annotation;
import uk.ac.ebi.interfaces.annotation.ChemicalStructure;
import uk.ac.ebi.interfaces.annotation.Context;
import uk.ac.ebi.interfaces.entities.Metabolite;

/**
 * @name    SMILES
 * @date    2012.02.03
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
@Context(Metabolite.class)
public class SMILES
    extends AbstractStringAnnotation
    implements ChemicalStructure {
    
    private static final Logger LOGGER = Logger.getLogger(SMILES.class);
    
    private static Description description = AnnotationLoader.getInstance().getMetaInfo(
            SMILES.class);

    public SMILES() {
    }
    
    public SMILES(String smiles) {
        super.setValue(smiles);
    }
    
    public Annotation getInstance() {
        return new SMILES();
    }

    public Annotation getInstance(String value) {
        return new SMILES(value);
    }

    @Override
    public String getShortDescription() {
        return description.shortDescription;
    }

    @Override
    public String getLongDescription() {
        return description.longDescription;
    }


    public IAtomContainer getStructure() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public void setStructure(IAtomContainer structure) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
}
