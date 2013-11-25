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

import org.junit.Test;
import uk.ac.ebi.mdk.domain.entity.DefaultEntityFactory;
import uk.ac.ebi.mdk.domain.entity.Metabolite;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/** @author John May */
public class MetaboliteMakerTest {

    @Test public void withName() throws Exception {
        MetaboliteMaker maker = new MetaboliteMaker(DefaultEntityFactory.getInstance());
        Metabolite metabolite = maker.fromSmiles("CCO ethanol");
        assertThat(metabolite.getName(), is("ethanol"));
        assertThat(metabolite.getStructures().size(), is(1));
    }

    @Test public void noName() throws Exception {
        MetaboliteMaker maker = new MetaboliteMaker(DefaultEntityFactory.getInstance());
        Metabolite metabolite = maker.fromSmiles("CCO");
        assertThat(metabolite.getName(), is(""));
        assertThat(metabolite.getStructures().size(), is(1));
    }    
}
