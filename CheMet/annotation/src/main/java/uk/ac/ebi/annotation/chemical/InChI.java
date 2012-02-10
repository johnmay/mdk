/**
 * SMILESAnnotation.java
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

import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.inchi.InChIGenerator;
import org.openscience.cdk.inchi.InChIGeneratorFactory;
import org.openscience.cdk.inchi.InChIToStructure;
import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.annotation.base.AbstractStringAnnotation;
import uk.ac.ebi.annotation.util.AnnotationLoader;
import uk.ac.ebi.core.Description;
import uk.ac.ebi.interfaces.Annotation;
import uk.ac.ebi.interfaces.annotation.ChemicalStructure;
import uk.ac.ebi.interfaces.annotation.Context;
import uk.ac.ebi.interfaces.annotation.Descriptor;
import uk.ac.ebi.interfaces.entities.Metabolite;


/**
 * @name    SMILESAnnotation
 * @date    2012.02.03
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
@Context(Metabolite.class)
@Descriptor(brief = "InChI",
            description = "The IUPAC International Chemical Identifier string representation of chemical structure")
public class InChI
        extends AbstractStringAnnotation
        implements ChemicalStructure {

    private static final Logger LOGGER = Logger.getLogger(InChI.class);

    private static Description description = AnnotationLoader.getInstance().getMetaInfo(
            InChI.class);

    private IAtomContainer structure;


    public InChI() {
    }


    public InChI(String inchi) {
        super.setValue(inchi);
    }


    public Annotation getInstance() {
        return new InChI();
    }


    public Annotation getInstance(String value) {
        return new InChI(value);
    }


    @Override
    public void setValue(String value) {
        super.setValue(value);
        generateStructure();
    }


    @Override
    public String getShortDescription() {
        return description.shortDescription;
    }


    @Override
    public String getLongDescription() {
        return description.longDescription;
    }


    @Override
    public Byte getIndex() {
        return description.index;
    }


    public IAtomContainer getStructure() {

        if (structure == null) {
            generateStructure();
        }

        return structure;

    }


    /**
     * Generate the structure from the stored InChI value
     */
    private void generateStructure() {

        if (getValue().isEmpty()) {
            return;
        }

        try {
            InChIGeneratorFactory inchiFactory = InChIGeneratorFactory.getInstance();
            InChIToStructure inchi2structure = inchiFactory.getInChIToStructure(getValue(), DefaultChemObjectBuilder.getInstance());
            structure = inchi2structure.getAtomContainer();
        } catch (CDKException ex) {
            LOGGER.error("Unable to generate structure from provided inchi: " + ex.getMessage());
        }

    }


    public void setStructure(IAtomContainer structure) {
        try {
            InChIGeneratorFactory inchiFactory = InChIGeneratorFactory.getInstance();
            InChIGenerator inchiGenerator = inchiFactory.getInChIGenerator(structure);
            setValue(inchiGenerator.getInchi());
        } catch (CDKException ex) {
            LOGGER.error("Unable to generate InChI from provided structure: " + ex.getMessage());
        }
    }
}
