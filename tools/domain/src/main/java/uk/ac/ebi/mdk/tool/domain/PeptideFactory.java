/*
 * Copyright (C) 2012  John May and Pablo Moreno
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package uk.ac.ebi.mdk.tool.domain;

import org.apache.log4j.Logger;
import org.openscience.cdk.Atom;
import org.openscience.cdk.Bond;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.normalize.SMSDNormalizer;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import uk.ac.ebi.mdk.domain.annotation.AtomContainerAnnotation;
import uk.ac.ebi.chemet.resource.basic.BasicChemicalIdentifier;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.Metabolite;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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

    private EntityFactory factory;


    public PeptideFactory(EntityFactory factory) {
        this.factory = factory;
    }


    public IAtomContainer generateStructure(AminoAcid... aminoacids) throws IOException, CDKException, Exception {
        IAtomContainer peptide = DefaultChemObjectBuilder.getInstance().newInstance(IAtomContainer.class);


        IAtomContainer current = SMSDNormalizer.makeDeepCopy(aminoacids[0].getMolecule());
        IAtomContainer next = null;;

        peptide.add(current);
//        for (IBond bond : AtomContainerManipulator.getBondArray(current)) {
//            bond.setStereo(bond.getStereo() == Stereo.UP ? Stereo.DOWN : bond.getStereo() == Stereo.DOWN ? Stereo.UP : bond.getStereo());
//        }

        // N terminus
        removeAtom(peptide, aminoacids[0].getNTerminal(current));

        for (int i = 0; i < aminoacids.length; i++) {


            if (i + 1 < aminoacids.length) {

                next = SMSDNormalizer.makeDeepCopy(aminoacids[i + 1].getMolecule());


//                // required to sort out stereo-chem
//                if (i % 2 != 0) {
//                    for (IBond bond : AtomContainerManipulator.getBondArray(next)) {
//                        bond.setStereo(bond.getStereo() == Stereo.UP ? Stereo.DOWN : bond.getStereo() == Stereo.DOWN ? Stereo.UP : bond.getStereo());
//                    }
//                }

                peptide.add(next);

                IAtom c = removeAtom(peptide, aminoacids[i].getCTerminal(current));

                IAtom n = removeAtom(peptide, aminoacids[i + 1].getNTerminal(next));


                peptide.addBond(new Bond(n, c));

            }




            if (i == aminoacids.length - 1) {
                IAtom oxygen = new Atom("O");
                peptide.addAtom(oxygen);
                peptide.addBond(new Bond(oxygen,
                                         removeAtom(peptide, aminoacids[i].getCTerminal(current))));
            }

            current = next;

        }


        // required to sort out stereo-chem
//        new StructureDiagramGenerator(new Molecule(peptide)).generateCoordinates();

        return peptide;
    }


    public Metabolite generateMetabolite(AminoAcid... aminoacids) throws IOException, CDKException, Exception {

        Metabolite m = factory.newInstance(Metabolite.class,
                                           BasicChemicalIdentifier.nextIdentifier(),
                                           generateName(aminoacids),
                                           generateAbbreviation(aminoacids));

        m.addAnnotation(new AtomContainerAnnotation(generateStructure(aminoacids)));

        return m;

    }


    public String generateName(AminoAcid... aminoacids) {

        StringBuilder nameBuilder = new StringBuilder(aminoacids.length * 5);

        for (int i = 0; i < aminoacids.length; i++) {

            nameBuilder.append(aminoacids[i].names.iterator().next());
            if (i + 1 < aminoacids.length) {
                nameBuilder.append("-");
            }
        }

        return nameBuilder.toString();
    }


    public String generateAbbreviation(AminoAcid... aminoacids) {

        StringBuilder abbrvBuilder = new StringBuilder(aminoacids.length * 10);

        for (int i = 0; i < aminoacids.length; i++) {

            abbrvBuilder.append(aminoacids[i].getDisplayName());
            if (i + 1 < aminoacids.length) {
                abbrvBuilder.append("-");
            }
        }

        return abbrvBuilder.toString();
    }


    public AminoAcid[] guessPeptide(String name) {

        String peptide = name.toLowerCase();

        List<AminoAcid> aas = new ArrayList<AminoAcid>();

        while (!peptide.isEmpty()) {

            boolean found = false;
            for (AminoAcid aa : AminoAcid.values()) {

                Matcher matcher = aa.getPattern().matcher(peptide);
                if (matcher.find() && matcher.start() == 0) {
                    found = true;

                    // add aa
                    aas.add(aa);
                    peptide = matcher.replaceFirst("");

                }
            }
            if (!found) {
                return aas.toArray(new AminoAcid[0]);
            }

        }
        return aas.toArray(new AminoAcid[0]);


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

    /**
     * Similar to
     * {@see IAtomContainer#removeAtomAndConnectedElectronContainers(IAtom)} but
     * returns a list of the atoms that the molecule was connected to
     * @param molecule
     * @param atom
     *
     * @return
     */
    public static List<IAtom> removeAtomAndGetConnected(IAtomContainer molecule, IAtom atom) {

        List<IAtom> atoms = molecule.getConnectedAtomsList(atom);

        molecule.removeAtomAndConnectedElectronContainers(atom);

        return atoms;
    }


    /**
     * This method is similar to
     * {@see IAtomContainer#removeAtomAndConnectedElectronContainers(IAtom)}
     * however the method returns a single atom. If the atom to remove is
     * connected to more then one a RuntimeException is thrown
     *
     *
     * @param molecule
     * @param atom
     *
     * @return
     */
    public static IAtom removeAtom(IAtomContainer molecule, IAtom atom) {

        List<IAtom> connected = removeAtomAndGetConnected(molecule, atom);

        if (connected.size() != 1) {
            throw new UnsupportedOperationException("There must be exactly 1 connected atom");
        }

        return connected.iterator().next();

    }
    public static enum AminoAcid {

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

        private String displayName;

        private List<String> names;

        private IAtomContainer molecule;

        private Pattern pattern;


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



            displayName = name().replaceAll("_", "-");
            displayName = name().equals("GLY") ? displayName.substring(0, 1) + displayName.substring(1).toLowerCase() : displayName.substring(0, 3) + displayName.substring(3).toLowerCase();


            String stereo = displayName.substring(0, 1).toLowerCase();
            String code = name().equals("GLY") ? "gly" : displayName.substring(2, 5).toLowerCase();

            pattern = name().equals("GLY") ? Pattern.compile("(?:" + code + ")-?") : Pattern.compile("(?:" + stereo + "-" + code + "|" + code + ")-?");

        }


        public Pattern getPattern() {
            return pattern;
        }


        public String getDisplayName() {
            return displayName;
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


        @Override
        public String toString() {
            return displayName;
        }
    };
}
