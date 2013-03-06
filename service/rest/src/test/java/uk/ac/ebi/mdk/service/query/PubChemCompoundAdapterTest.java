/*
 * Copyright (c) 2013. John May <jwmay@users.sf.net>
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

package uk.ac.ebi.mdk.service.query;

import org.junit.Test;
import org.openscience.cdk.interfaces.IAtomContainer;
import sun.jvm.hotspot.utilities.Assert;
import uk.ac.ebi.mdk.domain.identifier.PubChemCompoundIdentifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author John May
 */
public class PubChemCompoundAdapterTest {

    private PubChemCompoundAdapter service = new PubChemCompoundAdapter();

    @Test public void testGetSynonyms() {
        Collection<String> actual = service
                .getSynonyms(new PubChemCompoundIdentifier("12"));
        Collection<String> expected = new ArrayList<String>(Arrays.asList("benzene-1,2,3,5-tetrol",
                                                                          "1,2,3,5-tetrahydroxybenzene",
                                                                          "634-94-6",
                                                                          "C03743",
                                                                          "AC1Q79ZN",
                                                                          "AC1L18A1",
                                                                          "CHEBI:16746",
                                                                          "1235-TETRAHYDROXYBENZENE",
                                                                          "KST-1B6889",
                                                                          "AR-1B5201",
                                                                          "ZINC00901826",
                                                                          "AKOS006312269",
                                                                          "AG-K-73579",
                                                                          "A8823"));
        assertThat(actual, is(expected));
    }

    @Test public void testSearchName() {
        Collection<PubChemCompoundIdentifier> cids = service.searchName("pyruvate", false);
        assertTrue(cids.contains(new PubChemCompoundIdentifier("107735")));
        assertTrue(cids.contains(new PubChemCompoundIdentifier("1060")));
    }

    @Test public void testSearchName_Missing() {
        assertTrue(service.searchName("ermmmm", false).isEmpty());
    }

    @Test public void testSearchName_Space() {
        Collection<PubChemCompoundIdentifier> cids = service.searchName("acetyl coa", false);
        assertTrue(cids.contains(new PubChemCompoundIdentifier("444493")));
        assertTrue(cids.contains(new PubChemCompoundIdentifier("16218868")));
    }

    @Test public void testGetIUPACName() {
        String actual = service
                .getIUPACName(new PubChemCompoundIdentifier("12"));
        String expected = "benzene-1,2,3,5-tetrol";
        assertThat(actual, is(expected));
    }

    @Test public void testGetFormula() {
        String actual = service
                .getMolecularFormula(new PubChemCompoundIdentifier("12"));
        String expected = "C6H6O4";
        assertThat(actual, is(expected));
    }

    @Test public void testGetNames() {
        Collection<String> actual = service
                .getNames(new PubChemCompoundIdentifier("12"));
        Collection<String> expected = new ArrayList<String>(Arrays.asList("1,2,3,5-Benzenetetrol",
                                                                          "benzene-1,2,3,5-tetrol",
                                                                          "1,2,3,5-tetrahydroxybenzene",
                                                                          "634-94-6",
                                                                          "C03743",
                                                                          "AC1Q79ZN",
                                                                          "AC1L18A1",
                                                                          "CHEBI:16746",
                                                                          "1235-TETRAHYDROXYBENZENE",
                                                                          "KST-1B6889",
                                                                          "AR-1B5201",
                                                                          "ZINC00901826",
                                                                          "AKOS006312269",
                                                                          "AG-K-73579",
                                                                          "A8823"));
        assertThat(actual, is(expected));
    }

    @Test public void testGetPreferredName() {
        String actual = service
                .getPreferredName(new PubChemCompoundIdentifier("12"));
        String expected = "1,2,3,5-Benzenetetrol";
        assertThat(actual, is(expected));
    }

    @Test public void testGetStructure() {
        IAtomContainer molecule = service
                .getStructure(new PubChemCompoundIdentifier("12"));
        assertFalse(molecule.isEmpty());
    }


}
