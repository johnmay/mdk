/*
 * Copyright (c) 2014. EMBL, European Bioinformatics Institute
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

package uk.ac.ebi.mdk;

import org.apache.commons.cli.Option;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.isomorphism.StructureUtil;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.smiles.SmilesParser;
import uk.ac.ebi.mdk.apps.CommandLineMain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Runes the neutralise utility against a SMILES file.
 *
 * @author John May
 */
public class TestNeutralise extends CommandLineMain {

    public static void main(String[] args) {
        new TestNeutralise().process(args);
    }

    @Override public void setupOptions() {
        add(new Option("i", "input", true, "a file of SMILES"));
    }

    @Override public void process() {

        File input = getFile("input");

        IChemObjectBuilder bldr = SilentChemObjectBuilder.getInstance();
        SmilesParser smipar = new SmilesParser(bldr);
        SmilesGenerator smigen = SmilesGenerator.unique();

        try {
            BufferedReader br = new BufferedReader(new FileReader(input));
            String line = null;
            while ((line = br.readLine()) != null) {
                IAtomContainer container = smipar.parseSmiles(line);
                String smi = smigen.create(container);
                if (StructureUtil.neutralise(container) > 0) {
                    String neuSsmi = smigen.create(container);
                    System.out.println(smi + ">>" + neuSsmi);
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (CDKException e) {
            System.err.println(e.getMessage());
        }

    }
}
