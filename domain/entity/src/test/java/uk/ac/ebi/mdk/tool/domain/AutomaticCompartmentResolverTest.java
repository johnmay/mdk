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

package uk.ac.ebi.mdk.tool.domain;

import org.junit.Assert;
import org.apache.log4j.Logger;
import org.junit.Test;
import uk.ac.ebi.mdk.domain.entity.reaction.compartment.Organelle;
import uk.ac.ebi.mdk.domain.tool.AutomaticCompartmentResolver;

import java.security.InvalidParameterException;

/**
 * @author johnmay
 */
public class AutomaticCompartmentResolverTest {

    private static final Logger LOGGER = Logger.getLogger(AutomaticCompartmentResolverTest.class);

    private AutomaticCompartmentResolver resolver = new AutomaticCompartmentResolver();


    @Test
    public void testIsAmbiguous() throws Exception {
        Assert.assertTrue(resolver.isAmbiguous("a"));
        Assert.assertFalse(resolver.isAmbiguous("e"));
    }

    @Test
    public void testGetCompartment_thrown() throws Exception {
        Assert.assertEquals(Organelle.UNKNOWN, resolver.getCompartment("no such compartment"));
    }

    @Test public void testGetCompartment_abbreviation() throws Exception {
        Assert.assertEquals(Organelle.CYTOPLASM, resolver.getCompartment("c"));
    }

    @Test public void testGetCompartment_abbreviationAlt() throws Exception {
        Assert.assertEquals(Organelle.CYTOPLASM, resolver.getCompartment("[c]"));
    }

    @Test public void testGetCompartment_name() throws Exception {
        Assert.assertEquals(Organelle.CYTOPLASM, resolver.getCompartment("cytoplasm"));
    }

    @Test public void testGetCompartment_nameCapitals() throws Exception {
        Assert.assertEquals(Organelle.CYTOPLASM, resolver.getCompartment("CYTOPLASM"));
    }


}
