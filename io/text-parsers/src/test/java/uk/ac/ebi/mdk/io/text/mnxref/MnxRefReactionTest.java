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

package uk.ac.ebi.mdk.io.text.mnxref;

import org.hamcrest.Matcher;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/** @author John May */
public class MnxRefReactionTest {

    @Test
    public void testParse() throws Exception {
        MnxRefReaction r = MnxRefReaction
                .parse(new String[]{"MNXR1",
                                    "1 MNXM3428 = 1 MNXM3428",
                                    "1 10-formyltetrahydrofolate-[Glu](5) = 1 10-formyltetrahydrofolate-[Glu](5)",
                                    "true",
                                    "",
                                    "bigg:10FTHF5GLUtl"});
        assertThat(r.id(), is("MNXR1"));
        assertThat(r.equation(), is("1 MNXM3428 = 1 MNXM3428"));
        assertThat(r.description(), is("1 10-formyltetrahydrofolate-[Glu](5) = 1 10-formyltetrahydrofolate-[Glu](5)"));
        assertThat(r.balanced(), is(true));
        assertThat(r.enzymeCode(), is(""));
        assertThat(r.source(),  is("bigg:10FTHF5GLUtl"));
    }


}
