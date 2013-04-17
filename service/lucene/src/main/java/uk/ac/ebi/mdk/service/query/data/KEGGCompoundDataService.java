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

package uk.ac.ebi.mdk.service.query.data;

import org.apache.log4j.Logger;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;
import uk.ac.ebi.mdk.domain.identifier.KEGGCompoundIdentifier;
import uk.ac.ebi.mdk.service.index.data.KEGGCompoundDataIndex;
import uk.ac.ebi.mdk.service.query.AbstractLuceneService;

import java.util.Collection;

/**
 * KEGGCompoundDataService - 27.02.2012 <br/>
 * <p/>
 * Class provides MolecularChargeService and MolecularFormulaService for the
 * KEGG Compound Database
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class KEGGCompoundDataService
        extends AbstractLuceneService<KEGGCompoundIdentifier>
        implements MolecularFormulaService<KEGGCompoundIdentifier> {

    private static final Logger LOGGER = Logger.getLogger(KEGGCompoundDataService.class);

    private static final IChemObjectBuilder BUILDER = SilentChemObjectBuilder.getInstance();

    public KEGGCompoundDataService() {
        super(new KEGGCompoundDataIndex());
    }

    /**
     * @inheritDoc
     */
    @Override
    public IMolecularFormula getIMolecularFormula(KEGGCompoundIdentifier identifier) {
        String formula = getMolecularFormula(identifier);
        if (formula.isEmpty())
            return BUILDER.newInstance(IMolecularFormula.class);
        return MolecularFormulaManipulator.getMolecularFormula(formula,
                                                               BUILDER);
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getMolecularFormula(KEGGCompoundIdentifier identifier) {
        return firstValue(identifier, MOLECULAR_FORMULA);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<KEGGCompoundIdentifier> searchMolecularFormula(IMolecularFormula formula, boolean approximate) {
        return searchMolecularFormula(MolecularFormulaManipulator.getString(formula), approximate);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<KEGGCompoundIdentifier> searchMolecularFormula(String formula, boolean approximate) {
        if (approximate) {
            LOGGER.error("Approximate search is not yet implemented");
        }
        return getIdentifiers(construct(formula, MOLECULAR_FORMULA));
    }

    /**
     * @inheritDoc
     */
    @Override
    public KEGGCompoundIdentifier getIdentifier() {
        return new KEGGCompoundIdentifier();
    }

}
