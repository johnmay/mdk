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
package uk.ac.ebi.mdk.domain.entity.reaction;

import java.util.Iterator;
import org.apache.log4j.Logger;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.interfaces.*;
import uk.ac.ebi.mdk.domain.entity.reaction.filter.AbstractParticipantFilter;
import uk.ac.ebi.metabolomes.util.CDKUtils;


/**
 * @name    AtomContainerReaction
 * @date    2011.08.08
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 * @brief   Reaction using AtomContainers from CDK. The class provides basic implementation of getting/setting
 *          reactants, products and their coefficients of the IReaction class.
 *
 */
public class AtomContainerReaction
        extends AbstractReaction<AtomContainerParticipant> {

    public AtomContainerReaction() {
    }


    public AtomContainerReaction(AbstractParticipantFilter filter) {
        super(filter);
    }

    private static final Logger LOGGER = Logger.getLogger(AtomContainerReaction.class);


    public void addReactant(IAtomContainer molecule, Double coef, Compartment compartment) {
        if (CDKUtils.isMoleculeGeneric(molecule)) {
            addReactant(new GenericParticipant(molecule, coef, compartment));
        } else {
            addReactant(new AtomContainerParticipant(molecule, coef, compartment));
        }
    }


    public void addProduct(IAtomContainer molecule, Double coef, Compartment compartment) {
        if (CDKUtils.isMoleculeGeneric(molecule)) {
            addProduct(new GenericParticipant(molecule, coef, compartment));
        } else {
            addProduct(new AtomContainerParticipant(molecule, coef, compartment));
        }
    }


    @Override
    public boolean addReactant(AtomContainerParticipant p) {
        if (p instanceof GenericParticipant) {
            super.setGeneric(Boolean.TRUE);
        }
        return super.addReactant(p);
    }


    @Override
    public boolean addProduct(AtomContainerParticipant p) {
        if (p instanceof GenericParticipant) {
            super.setGeneric(Boolean.TRUE);
        }
        return super.addProduct(p);
    }


    /**
     * Create an instance IReaction object for use with CDK
     * TODO: Write unit test...
     * @return IReaction
     */
    public IReaction getCDKReaction() {

        IReaction reaction = DefaultChemObjectBuilder.getInstance().newInstance(IReaction.class);

        // add the reactants
        Iterator<AtomContainerParticipant> reIt =
                                           getReactantParticipants().
                iterator();
        while (reIt.hasNext()) {
            AtomContainerParticipant p = reIt.next();
            reaction.addReactant(new Molecule(p.getMolecule()));
        }

        // add the producta
        Iterator<AtomContainerParticipant> prIt = getProductParticipants().
                iterator();
        while (prIt.hasNext()) {
            AtomContainerParticipant p = (AtomContainerParticipant) prIt.next();
            reaction.addProduct(new Molecule(p.getMolecule()));
        }

        return reaction;

    }

    /* IMPLEMENTATION OF IREACTION INTERFACE */
//    public IMoleculeSet getReactants() {
//        IMoleculeSet molSet = DefaultChemObjectBuilder.getInstance().newInstance( IMoleculeSet.class );
//        Iterator<IMolecule> reIt = super.reactants.iterator();
//        while ( reIt.hasNext() ) {
//            molSet.addAtomContainer( new Molecule( reIt.next() ) );
//        }
//        return molSet;
//    }
//
//    public void setReactants( IMoleculeSet ims ) {
//        for ( int i = 0; i < ims.getAtomContainerCount(); i++ ) {
    //  super.addReactant( ims.getMolecule
//( i ) );
//        }
//    }
//
//    public IMoleculeSet getProducts() {
//        IMoleculeSet molSet = DefaultChemObjectBuilder.getInstance().newInstance( IMoleculeSet.class );
//        Iterator<IMolecule> reIt = super.products.iterator();
//        while ( reIt.hasNext() ) {
//            molSet.addAtomContainer( new Molecule( reIt.next() ) );
//        }
//        return molSet;
//    }
//
//    public void setProducts( IMoleculeSet ims ) {
//        for ( int i = 0; i < ims.getAtomContainerCount(); i++ ) {
//            super.addProduct( ims.getMolecule( i ) );
//        }
//    }
//
//    @Override
//    public void addReactant(ParticipantImplementation<IAtomContainer, Double, CompartmentImplementation> participant) {
//        super.addReactant(participant);
//    }
}
