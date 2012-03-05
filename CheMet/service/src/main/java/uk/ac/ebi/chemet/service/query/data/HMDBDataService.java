package uk.ac.ebi.chemet.service.query.data;

import org.apache.log4j.Logger;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;
import uk.ac.ebi.chemet.service.index.data.HMDBDataIndex;
import uk.ac.ebi.chemet.service.loader.writer.DefaultDataIndexWriter;
import uk.ac.ebi.chemet.service.query.AbstractQueryService;
import uk.ac.ebi.resource.chemical.HMDBIdentifier;
import uk.ac.ebi.service.query.data.MolecularChargeService;
import uk.ac.ebi.service.query.data.MolecularFormulaService;

import java.util.Collection;

/**
 * HMDBChemicalDataService - 27.02.2012 <br/>
 * <p/>
 * Class provides MolecularChargeService and MolecularFormulaService for the
 * Human Metabolome Database
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class HMDBDataService
        extends AbstractQueryService<HMDBIdentifier>
        implements MolecularChargeService<HMDBIdentifier>,
                   MolecularFormulaService<HMDBIdentifier> {

    private static final Logger LOGGER = Logger.getLogger(HMDBDataService.class);

    private static final IChemObjectBuilder BUILDER = DefaultChemObjectBuilder.getInstance();

    public HMDBDataService() {
        super(new HMDBDataIndex());
    }

    /**
     * @inheritDoc
     */
    @Override
    public IMolecularFormula getIMolecularFormula(HMDBIdentifier identifier) {
        String formula = getMolecularFormula(identifier);
        if(formula.isEmpty())
            return BUILDER.newInstance(IMolecularFormula.class);
        return MolecularFormulaManipulator.getMolecularFormula(formula,
                                                               BUILDER);
    }
    /**
     * @inheritDoc
     */
    @Override
    public String getMolecularFormula(HMDBIdentifier identifier) {
        return firstValue(identifier, MOLECULAR_FORMULA);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<HMDBIdentifier> searchMolecularFormula(IMolecularFormula formula, boolean approximate) {
        return searchMolecularFormula(MolecularFormulaManipulator.getString(formula), approximate);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<HMDBIdentifier> searchMolecularFormula(String formula, boolean approximate) {
        if(approximate){
            LOGGER.error("Approximate search is not yet implemented");
        }
        return getIdentifiers(construct(formula, MOLECULAR_FORMULA));
    }

    /**
     * @inheritDoc
     */
    @Override
    public Double getCharge(HMDBIdentifier identifier) {
        String value = firstValue(identifier, MOLECULAR_CHARGE);
        // reuse method for the write that will get a Double.NAN on fail
        return DefaultDataIndexWriter.getChargeValue(value);
    }

    /**
     * @inheritDoc
     */
    @Override
    public HMDBIdentifier getIdentifier() {
        return new HMDBIdentifier();
    }


}
