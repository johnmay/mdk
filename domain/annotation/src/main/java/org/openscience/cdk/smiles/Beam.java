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

package org.openscience.cdk.smiles;

import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import uk.ac.ebi.beam.Functions;
import uk.ac.ebi.beam.Graph;

/**
 * Utility for reading and writing SMILES until the CDK is patched with Beam.
 *
 * @author John May
 */
public final class Beam {

  private static final IChemObjectBuilder builder = SilentChemObjectBuilder.getInstance();
  private static final SmilesParser       smipar  = new SmilesParser(builder);
  private static final SmilesGenerator    smigen  = new SmilesGenerator(SmiFlavor.Isomeric | SmiFlavor.CxSmiles);

  /**
   * Read the molecule from the SMILES string - if the string could not be
   * read (invalid SMILES) and empty molecule is returned.
   *
   * @param smi SMILES string
   * @return a molecule (empty if a parse exception occurred)
   */
  public static IAtomContainer fromSMILES(String smi)
  {
    try {
      return smipar.parseSmiles(smi);
    } catch (InvalidSmilesException e) {
      return builder.newAtomContainer();
    }
  }

  /**
   * Read the molecule from the SMILES string - if the string could not be
   * generated an empty string is returned.
   *
   * @param m molecule
   * @return a molecule (empty if a parse exception occurred)
   */
  public static String toSMILES(IAtomContainer m)
  {
    try {
      return smigen.create(m);
    } catch (Exception e) {
      return "";
    }
  }
}
