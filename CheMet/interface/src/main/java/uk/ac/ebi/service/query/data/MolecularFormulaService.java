package uk.ac.ebi.service.query.data;

import org.openscience.cdk.interfaces.IMolecularFormula;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.service.query.QueryService;

import java.util.Collection;

/**
 * MolecularFormulaService - 28.02.2012 <br/>
 * <p/>
 * Describes a service that provides look-up of molecular formula's and
 * searching. The approximate search to search for formulas that have the
 * same number of heavy atoms but different numbers of protons. The formula's
 * can be searched/returned both as a String and {@see IMolecularFormula} for
 * convenience.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface MolecularFormulaService<I extends Identifier>
        extends QueryService<I> {

    /**
     * Search for a identifiers in the data-set which match the provided
     * molecular formula. The approximate flag determines whether the
     * proton count should be matched
     *
     * @param formula     formula to search
     * @param approximate whether to ignore proton differences
     *
     * @return identifiers from this data set that match the query
     */
    public Collection<I> searchMolecularFormula(String formula, boolean approximate);

    /**
     * Retrieve a molecular formula for the specified identifier. This method
     * will look-up the molecular formula for the provided identifier. If no
     * formula is found it will return an empty string
     *
     * @param identifier the identifier to fetch the formula for
     *
     * @return molecular formula as a string
     */
    public String getMolecularFormula(I identifier);

    /**
     * Convenience method for searching with a CDK {@see IMolecularFormula} instance.
     * This method simpily wraps {@see searchMolecularFormula(String, boolean)}
     *
     * @param formula     formula to search
     * @param approximate whether to ignore proton differences
     *
     * @return identifiers from this data set that match the query
     */
    public Collection<I> searchMolecularFormula(IMolecularFormula formula, boolean approximate);

    /**
     * Convenience method for retrieving a CDK {@see IMolecularFormula} instance. See
     * {@see getMolecularFormula(I)} for more details. If the no formula is found an
     * empty {@see IMolecularFormula} is returned.
     *
     * @param identifier query to look-up
     *
     * @return an IMolecularFormula instance (not null)
     */
    public IMolecularFormula getIMolecularFormula(I identifier);


}
