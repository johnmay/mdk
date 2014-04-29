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

package uk.ac.ebi.mdk.domain.annotation;

import org.apache.log4j.Logger;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.smiles.Beam;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.smiles.SmilesParser;
import uk.ac.ebi.mdk.domain.annotation.primitive.AbstractStringAnnotation;
import uk.ac.ebi.mdk.lang.annotation.Brief;
import uk.ac.ebi.mdk.lang.annotation.Description;
import uk.ac.ebi.mdk.lang.annotation.Context;
import uk.ac.ebi.mdk.domain.entity.Metabolite;


/**
 * @name    SMILES
 * @date    2012.02.03
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   ...class metaInfo...
 *
 */
@Context(Metabolite.class)
@Brief("SMILES")
@Description("The simplified molecular-input line-entry specification representation of chemical structure")
public class SMILES
        extends AbstractStringAnnotation
        implements ChemicalStructure {

    private static final Logger LOGGER = Logger.getLogger(SMILES.class);

    private static SmilesParser    SMILES_PARSER;
    private static SmilesGenerator SMILES_GENERATOR;

    
    private IAtomContainer atomContainer;


    public SMILES() {
    }


    public SMILES(String smiles) {
        setValue(smiles);
    }

    public SMILES(IAtomContainer structure) {
        setStructure(structure);
    }


    public Annotation newInstance() {
        return new SMILES();
    }


    public SMILES getInstance(String value) {
        return new SMILES(value);
    }
    
    @Override
    public void setValue(String SMILES){
        super.setValue(SMILES);
        // parseSMILES();
    }


    public IAtomContainer getStructure() {
        return atomContainer == null ? parseSMILES() : atomContainer;
    }
    
    private IAtomContainer parseSMILES(){
        return atomContainer = Beam.fromSMILES(getValue());
    }

    public void setStructure(IAtomContainer structure) {
        this.atomContainer = structure;
        setValue(Beam.toSMILES(structure));
    }

    @Override
    public String toInChI() {
        return "";
    }
}
