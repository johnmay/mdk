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
package uk.ac.ebi.mdk.deprecated;

import org.apache.log4j.BasicConfigurator;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.ebi.mdk.domain.identifier.ChEBIIdentifier;
import uk.ac.ebi.mdk.deprecated.MIRIAMLoader;

import static org.junit.Assert.*;

/**
 *
 * @author johnmay
 */
public class MIRIAMLoaderTest {

    private MIRIAMLoader loader = MIRIAMLoader.getInstance();

    public MIRIAMLoaderTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        BasicConfigurator.configure();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testGetInstance() {
    }

    @Test
    public void testGetEntry_String() {
    }

    @Test
    public void testGetEntry_Short() {
    }

    @Test
    public void testGetAccession() {
    }

    @Test
    public void testGetIdentifier() {
        ChEBIIdentifier chebiId = new ChEBIIdentifier(12);
        assertEquals(chebiId, loader.getIdentifier("urn:miriam:chebi:CHEBI%3A12"));
    }

    @Test
    public void testGetIdentifier_deprecated() {
        ChEBIIdentifier chebiId = new ChEBIIdentifier(12);
        assertEquals(chebiId, loader.getIdentifier("urn:miriam:obo.chebi:CHEBI%3A12"));
    }
    
    @Test
    public void testGetIdentifier_deprecated_nonescaped() {
        ChEBIIdentifier chebiId = new ChEBIIdentifier(12);
        assertEquals(chebiId, loader.getIdentifier("urn:miriam:obo.chebi:CHEBI:12"));
    }

    @Test
    public void testMain() {
    }
}
