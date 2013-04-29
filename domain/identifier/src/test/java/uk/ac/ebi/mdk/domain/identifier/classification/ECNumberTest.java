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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.mdk.domain.identifier.classification;

import org.junit.Test;
import uk.ac.ebi.mdk.domain.identifier.Identifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/** @author johnmay */
public class ECNumberTest {

    @Test
    public void testUnderterminedParsing() {
        Identifier id = new ECNumber("EC-Undertermined");
        assertEquals("-.-.-.-", id.getAccession());
    }

    @Test
    public void testPartial() {
        assertFalse(new ECNumber("1.1.1.1").isPartial());
        assertTrue(new ECNumber("1.1.1.-").isPartial());
    }

    @Test
    public void testNormalParsing() {
        Identifier id = new ECNumber("EC-5.3.1.23");
        assertEquals("5.3.1.23", id.getAccession());
    }

    /** test for E.C. from UniProt-KB P94368 */
    @Test public void testP94368() {
        Identifier id = new ECNumber();
        id.setAccession("4.2.1.136");
        assertEquals("4.2.1.136", id.getAccession());
    }
}

