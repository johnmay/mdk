/**
 * AtomContainerReaction.java
 *
 * 2011.08.08
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
package uk.ac.ebi.chemet.entities.reaction;

import java.util.Iterator;
import org.apache.log4j.Logger;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IReaction;
import org.openscience.cdk.tools.manipulator.AtomContainerComparator;

/**
 * @name    AtomContainerReaction
 * @date    2011.08.08
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public class AtomContainerReaction extends GenericReaction<IAtomContainer , Double , Compartment> {

    private static final Logger LOGGER = Logger.getLogger( AtomContainerReaction.class );

    public AtomContainerReaction() {
        super( new AtomContainerComparator() );
    }

    /**
     * Create an instance IReaction object for use with CDK
     * TODO: Write unit test...
     * @return IReaction
     */
    public IReaction getCDKReaction() {

        IReaction reaction = DefaultChemObjectBuilder.getInstance().newInstance( IReaction.class );

        // add the reactants
        Iterator<IAtomContainer> reIt = reactants.iterator();
        Iterator<Double> rsIt = reactantStoichiometries.iterator();
        while ( reIt.hasNext() ) {
            if ( rsIt.hasNext() ) {
                reaction.addReactant( new Molecule( reIt.next() ) , rsIt.next() );
            } else {
                reaction.addReactant( new Molecule( reIt.next() ) );
            }
        }
        // add the product
        Iterator<IAtomContainer> prIt = products.iterator();
        Iterator<Double> psIt = productStoichiometries.iterator();
        while ( reIt.hasNext() ) {
            if ( rsIt.hasNext() ) {
                reaction.addProduct( new Molecule( reIt.next() ) , rsIt.next() );
            } else {
                reaction.addProduct( new Molecule( reIt.next() ) );
            }
        }

        return reaction;

    }
}
