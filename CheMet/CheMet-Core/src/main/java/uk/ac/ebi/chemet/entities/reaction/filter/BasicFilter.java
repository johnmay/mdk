
/**
 * BasicFilter.java
 *
 * 2011.08.22
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
package uk.ac.ebi.chemet.entities.reaction.filter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.MDLReader;
import uk.ac.ebi.chemet.entities.reaction.participant.AtomContainerParticipant;
import uk.ac.ebi.chemet.entities.reaction.participant.ParticipantImplementation;


/**
 *          BasicFilter – 2011.08.22 <br>
 *          For use with reaction using AtomContainerParticipants this filters
 *          Hydrogen Ions (H<sup>+</sup>), Water (H<sub>2</sub>O) and Carbon Dioxide (CO<sub>2</sub>)
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class BasicFilter extends AbstractParticipantFilter {

    private static final Logger LOGGER = Logger.getLogger( BasicFilter.class );
    // default molecule resource file names
    private static final String carbondDioxideResource = "CO2_ChEBI_16526.mol";
    private static final String waterResource = "H2O_ChEBI_15377.mol";
    private static final String hydronResource = "H_ChEBI_15378.mol";
    // storage of the rejections
    private Set<AtomContainerParticipant> rejections = new HashSet<AtomContainerParticipant>();
    // CDK object builder
    private static final IChemObjectBuilder CHEM_OBJECT_BUILDER = DefaultChemObjectBuilder.
      getInstance();


    /**
     *
     * Instantiates the class with the default molecules from {@see defaultMolecules()}
     *
     */
    public BasicFilter() {
        this( defaultMolecules() );
    }


    /**
     *
     * Instantiation that allows specification of the molecules to reject
     *
     * @param rejectedMolecules The molecules to reject/ignore
     *
     */
    public BasicFilter( List<IAtomContainer> rejectedMolecules ) {

        for ( IAtomContainer mol : rejectedMolecules ) {

            rejections.add( new AtomContainerParticipant( mol ) );
        }

    }


    /**
     *
     * Build the collection of default molecules H<sup>+</sup>, H<sub>2</sub>O and CO<sub>2</sub>
     * from the resources stored in uk.ac.ebi.chemet.entities.reaction.filter
     *
     * @return List of {@see IAtomContainer} instances
     *
     */
    public static List<IAtomContainer> defaultMolecules() {

        List<IAtomContainer> molecules = new ArrayList<IAtomContainer>();


        for ( String molResource : Arrays.asList( carbondDioxideResource ,
                                                  waterResource ,
                                                  hydronResource ) ) {
            InputStream is = BasicFilter.class.getResourceAsStream( molResource );
            MDLReader reader = new MDLReader( is );
            IMolecule mol = CHEM_OBJECT_BUILDER.newInstance( IMolecule.class );
            try {

                molecules.add( reader.read( mol ) );

            } catch ( CDKException ex ) {

                LOGGER.error( "Could not read mol file: " + molResource );
            } finally {

                try {

                    is.close();
                } catch ( IOException ex ) {

                    LOGGER.error( "Could not close input stream for molfile: " + molResource );
                }

            }

        }


        return molecules;
    }


    /**
     *
     * Wrapper for a call to {@see reject( AtomContainerParticipant)}. The method checks whether the
     * participant is an {@see AtomContainerParticipant} first before delegating to the aforementioned
     * reject method. If the participant is not an instance of AtomContainerParticipant then it
     * is rejected (true returned).
     *
     * @param p Participant to test
     *
     * @return Whether to reject the molecule
     *
     */
    @Override
    public boolean reject( ParticipantImplementation p ) {
        if ( p instanceof AtomContainerParticipant ) {
            return reject( new AtomContainerParticipant( ( IAtomContainer ) p.getMolecule() ) );
        }
        return true;
    }


    /**
     *
     * Rejects AtomContainerParticipants using their internal hashCode and equals methods. If the
     * atom container participant is contained within the rejections list it is rejected. For this
     * reason the parameter p should be instantiated without stoichiometric coefficients or a
     * compartment (by default). The {@see reject(Participant)} method takes care of this by
     * making a shallow copy using on the IAtomContainer molecule.
     *
     * @param p Participant to test for rejection
     *
     * @return Whether the molecule should be rejected
     *
     */
    public boolean reject( AtomContainerParticipant p ) {
        return rejections.contains( p );
    }


    /**
     *
     * Adds a rejection to the underlying rejection set
     *
     * @param molecule
     *
     * @return Whether the collection was modified
     *
     */
    public boolean addRejection( IAtomContainer molecule ) {
        return rejections.add( new AtomContainerParticipant( molecule ) );
    }


    /**
     *
     * Remove a rejection to the underlying rejection set
     *
     * @param molecule
     *
     * @return Whether the collection was modified
     *
     */
    public boolean removeRejection( IAtomContainer molecule ) {
        return rejections.remove( new AtomContainerParticipant( molecule ) );
    }


}

