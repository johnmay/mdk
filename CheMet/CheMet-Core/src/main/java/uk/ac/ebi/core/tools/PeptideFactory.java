/**
 * DipeptideFactory.java
 *
 * 2012.01.13
 *
 * This file is part of the CheMet library
 *
 * The CheMet library is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * CheMet is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet. If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.core.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;
import org.openscience.cdk.*;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IBond.Stereo;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.io.MDLV2000Writer;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.normalize.SMSDNormalizer;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import uk.ac.ebi.annotation.chemical.ChemicalStructure;
import uk.ac.ebi.core.Metabolite;
import uk.ac.ebi.metabolomes.util.CDKUtils;
import uk.ac.ebi.resource.chemical.BasicChemicalIdentifier;


/**
 *
 * DipeptideFactory 2012.01.13
 *
 * @version $Rev$ : Last Changed $Date$
 * @author johnmay
 * @author $Author$ (this version)
 *
 * Class description
 *
 */
public class PeptideFactory {

    private static final Logger LOGGER = Logger.getLogger(PeptideFactory.class);


    public Metabolite generatePeptide(AminoAcid... aminoacids) throws IOException, CDKException, Exception {

        IAtomContainer peptide = DefaultChemObjectBuilder.getInstance().newInstance(IAtomContainer.class);


        IAtomContainer current = SMSDNormalizer.makeDeepCopy(aminoacids[0].getMolecule());
        IAtomContainer next = null;;

        peptide.add(current);
//        for (IBond bond : AtomContainerManipulator.getBondArray(current)) {
//            bond.setStereo(bond.getStereo() == Stereo.UP ? Stereo.DOWN : bond.getStereo() == Stereo.DOWN ? Stereo.UP : bond.getStereo());
//        }

        // N terminus
        CDKUtils.removeAtom(peptide, aminoacids[0].getNTerminal(current));

        StringBuilder nameBuilder = new StringBuilder(aminoacids.length * 5);
        StringBuilder abbrvBuilder = new StringBuilder(aminoacids.length * 10);

        for (int i = 0; i < aminoacids.length; i++) {

            nameBuilder.append(aminoacids[i].names.iterator().next());
            abbrvBuilder.append(aminoacids[i].name());


            if (i + 1 < aminoacids.length) {

                next = SMSDNormalizer.makeDeepCopy(aminoacids[i + 1].getMolecule());


//                // required to sort out stereo-chem
//                if (i % 2 != 0) {
//                    for (IBond bond : AtomContainerManipulator.getBondArray(next)) {
//                        bond.setStereo(bond.getStereo() == Stereo.UP ? Stereo.DOWN : bond.getStereo() == Stereo.DOWN ? Stereo.UP : bond.getStereo());
//                    }
//                }

                peptide.add(next);

                IAtom c = CDKUtils.removeAtom(peptide, aminoacids[i].getCTerminal(current));

                IAtom n = CDKUtils.removeAtom(peptide, aminoacids[i + 1].getNTerminal(next));


                peptide.addBond(new Bond(n, c));

                nameBuilder.append("-");
                abbrvBuilder.append("-");

            }




            if (i == aminoacids.length - 1) {
                IAtom oxygen = new Atom("O");
                peptide.addAtom(oxygen);
                peptide.addBond(new Bond(oxygen,
                                         CDKUtils.removeAtom(peptide, aminoacids[i].getCTerminal(current))));
            }

            current = next;

        }

        Metabolite m = new Metabolite(BasicChemicalIdentifier.nextIdentifier(),
                                      abbrvBuilder.toString(),
                                      nameBuilder.toString());

        // required to sort out stereo-chem
        new StructureDiagramGenerator(new Molecule(peptide)).generateCoordinates();

        m.addAnnotation(new ChemicalStructure(peptide));

        System.out.println(m);

        return m;

    }


    private static int getAtomIndex(IAtomContainer structure,
                                    String query,
                                    String neighbour) {
        for (int i = 0; i < structure.getAtomCount(); i++) {

            IAtom atom = structure.getAtom(i);
            String symbol = atom.getSymbol();

            if (symbol.equals(query)) {
                for (IAtom connected : structure.getConnectedAtomsList(atom)) {
                    if (connected.getSymbol().equals(neighbour)) {
                        return i;
                    }
                }
            }

        }

        throw new InvalidParameterException("No match found!");
    }


    public enum AminoAcid {

        /*
         * Positive
         */
        L_ARG("aminoacid/L-Arg.mol", "L-arginine"),
        D_ARG("aminoacid/D-Arg.mol", "D-arginine"),
        L_HIS("aminoacid/L-His.mol", "L-histidine"),
        D_HIS("aminoacid/D-His.mol", "D-histidine"),
        L_LYS("aminoacid/L-Lys.mol", "L-lysine"),
        D_LYS("aminoacid/D-Lys.mol", "D-lysine"),
        /*
         * Negative
         */
        L_ASP("aminoacid/L-Asp.mol", "L-aspartate"),
        D_ASP("aminoacid/D-Asp.mol", "D-aspartate"),
        L_GLU("aminoacid/L-Glu.mol", "L-glutamic acid"),
        D_GLU("aminoacid/D-Glu.mol", "D-glutamic acid"),
        /*
         * Polar
         */
        L_SER("aminoacid/L-Ser.mol", "L-serine"),
        D_SER("aminoacid/D-Ser.mol", "D-serine"),
        L_THR("aminoacid/L-Thr.mol", "L-threonine"),
        D_THR("aminoacid/D-Thr.mol", "D-threonine"),
        L_ASN("aminoacid/L-Asn.mol", "L-asparagine"),
        D_ASN("aminoacid/D-Asn.mol", "D-asparagine"),
        L_GLN("aminoacid/L-Gln.mol", "L-glutami"),
        D_GLN("aminoacid/D-Gln.mol", "D-glutami-ne"),
        /*
         * Special
         */
        L_CYS("aminoacid/D-Cys.mol", "L-cysteine"),
        D_CYS("aminoacid/D-Cys.mol", "D-cysteine"),
        L_SEC("aminoacid/L-Sec.mol", "L-selenocysteinate"),
        D_SEC("aminoacid/D-Sec.mol", "D-selenocysteinate"),
        GLY("aminoacid/Gly.mol", "Glycine"),
        L_PRO("aminoacid/L-Pro.mol", "L-proline"),
        D_PRO("aminoacid/D-Pro.mol", "D-proline"),
        /*
         * Hydrophobic
         */
        L_ALA("aminoacid/L-Ala.mol", "L-alanine"),
        D_ALA("aminoacid/D-Ala.mol", "D-alanine"),
        L_VAL("aminoacid/L-Val.mol", "L-valine"),
        D_VAL("aminoacid/D-Val.mol", "D-valine"),
        L_ILE("aminoacid/L-Ile.mol", "L-isoleucine"),
        D_ILE("aminoacid/D-Ile.mol", "D-isoleucine"),
        L_LEU("aminoacid/L-Lue.mol", "L-leucine"),
        D_LEU("aminoacid/D-Lue.mol", "D-leucine"),
        L_MET("aminoacid/L-Met.mol", "L-methionine"),
        D_MET("aminoacid/D-Met.mol", "D-methionine"),
        L_PHE("aminoacid/L-Phe.mol", "L-phenylalanine"),
        D_PHE("aminoacid/D-Phe.mol", "D-phenylalanine"),
        L_TYR("aminoacid/L-Tyr.mol", "L-tyrosine"),
        D_TYR("aminoacid/D-Tyr.mol", "D-tyrosine"),
        L_TRP("aminoacid/L-Trp.mol", "L-tryptophan"),
        D_TRP("aminoacid/D-Trp.mol", "D-tryptophan"),;

        private List<String> names;

        private IAtomContainer molecule;


        private AminoAcid(String resource, String... names) {

            this.names = Arrays.asList(names);

            if (resource.isEmpty()) {
                return;
            }

            try {
                InputStream stream = getClass().getResourceAsStream(resource);
                MDLV2000Reader reader = new MDLV2000Reader(stream);
                this.molecule = DefaultChemObjectBuilder.getInstance().newInstance(IMolecule.class);
                reader.read(molecule);
                AtomContainerManipulator.percieveAtomTypesAndConfigureUnsetProperties(molecule);
            } catch (Exception ex) {
                LOGGER.error("Could not load structure for "
                             + name() + ": " + ex.getMessage());
            }

        }


        public IAtomContainer getMolecule() {
            return molecule;
        }


        public List<String> getNames() {
            return names;
        }


        public IAtom getCTerminal(IAtomContainer container) {
            return container.getAtom(getAtomIndex(molecule, "R", "C"));
        }


        public IAtom getNTerminal(IAtomContainer container) {
            return container.getAtom(getAtomIndex(molecule, "R", "N"));
        }
    };
}
