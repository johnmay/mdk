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

package uk.ac.ebi.mdk.service.query;

import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.Test;
import uk.ac.ebi.mdk.domain.identifier.KEGGCompoundIdentifier;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author John May
 */
public class KEGGRestClientTest {

    @Test public void testMDLMolFile() {
        KEGGRestClient client = new KEGGRestClient();
        String mol = client.getMDLMol(new KEGGCompoundIdentifier("C00009"));
        assertTrue(mol.contains("V2000"));
    }

    @Test public void testFormula() {
        KEGGRestClient client = new KEGGRestClient();
        String formula = client.getMolecularFormula(new KEGGCompoundIdentifier("C00002"));
        assertThat(formula, is("C10H16N5O13P3"));
    }

    @Test public void testMDLMolFile_BadId() {
        KEGGRestClient client = new KEGGRestClient();
        String mol = client.getMDLMol(new KEGGCompoundIdentifier("C00000"));
        assertTrue(mol.isEmpty());
    }

}
