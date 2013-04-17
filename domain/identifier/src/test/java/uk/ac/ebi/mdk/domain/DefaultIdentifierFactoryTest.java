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
package uk.ac.ebi.mdk.domain;

import junit.framework.Assert;
import org.junit.Test;
import uk.ac.ebi.mdk.domain.identifier.BioCycChemicalIdentifier;
import uk.ac.ebi.mdk.domain.identifier.ChEBIIdentifier;
import uk.ac.ebi.mdk.domain.identifier.HMDBIdentifier;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.identifier.IdentifierFactory;
import uk.ac.ebi.mdk.domain.identifier.IdentifierSet;
import uk.ac.ebi.mdk.domain.identifier.MetaCycIdentifier;
import uk.ac.ebi.mdk.domain.identifier.SwissProtIdentifier;
import uk.ac.ebi.mdk.domain.identifier.basic.BasicProteinIdentifier;
import uk.ac.ebi.mdk.domain.identifier.classification.ECNumber;

import java.util.Collection;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


/**
 * @author johnmay
 */
public class DefaultIdentifierFactoryTest {


    @Test
    public void testOfName() {
        DefaultIdentifierFactory factory = DefaultIdentifierFactory.getInstance();
        Identifier identifier = factory.ofName("ChEBI identifier", "ChEBI:12");
        System.out.println(identifier.getSummary());
    }


    @Test
    public void testSynonymLoading() {
        DefaultIdentifierFactory factory = DefaultIdentifierFactory.getInstance();
        Assert.assertEquals(ECNumber.class, factory.ofSynonym("EC").getClass());
        Assert.assertEquals(SwissProtIdentifier.class, factory.ofSynonym("Sprot").getClass());
        Assert.assertEquals(HMDBIdentifier.class, factory.ofSynonym("HMDB").getClass());
    }


    @Test
    public void testMapping() {

        DefaultIdentifierFactory factory = DefaultIdentifierFactory.getInstance();
        System.out.printf("%35s %-35s\n", "Class Name", "Mapped MetaInfo");

        for (Identifier id : factory.getSupportedIdentifiers()) {
            long start = System.currentTimeMillis();
            Class<? extends Identifier> c = id.getClass();

            int mir = IdentifierLoader.getInstance().getMIR(c);

            if (mir != 0) {
                System.out.printf("%35s %-35s\n", c.getSimpleName(), id.getShortDescription());
            }


        }
    }

    IdentifierFactory ids = DefaultIdentifierFactory.getInstance();

    @Test public void ofPattern_MetaCyc() {
        Collection<Class<? extends Identifier>> classes = ids.ofPattern("META:ATP");
        assertThat(classes.size(), is(2));
        assertThat(classes, hasItem(BioCycChemicalIdentifier.class));
        assertThat(classes, hasItem(MetaCycIdentifier.class));
    }

    @Test public void ofPattern_ChEBI() {
        Collection<Class<? extends Identifier>> classes = ids.ofPattern("CHEBI:12");
        assertThat(classes.size(), is(1));
        assertThat(classes, hasItem(ChEBIIdentifier.class));
    }

    @Test
    public void sequenceHeaderResolution() throws Exception {

        System.out.println("testSequenceHeaderResolution");

        // basic features
        String sequenceHeader = "sp|Q197F8|002R_IIV3|sp|Q6GZX1|004R_FRG3G|gnl|ec|1.1.1.1|lcl|chemet-id";
        IdentifierSet ids = DefaultIdentifierFactory.getInstance().resolveSequenceHeader(sequenceHeader);
        Assert.assertTrue(ids.contains(new SwissProtIdentifier("Q197F8")));
        Assert.assertTrue(ids.contains(new SwissProtIdentifier("Q6GZX1")));
        Assert.assertTrue(ids.contains(new ECNumber("1.1.1.1")));
        Assert.assertTrue(ids.contains(new BasicProteinIdentifier("chemet-id")));

    }


    @Test
    public void sequenceHeaderUnsupportedIdentifier() throws Exception {

        // basic features
        String sequenceHeader = "gi|2010202|sp|Q197F8|002R_IIV3|sp|Q6GZX1|004R_FRG3G|gnl|ec|1.1.1.1";
        IdentifierSet ids = DefaultIdentifierFactory.getInstance().resolveSequenceHeader(sequenceHeader);

        Assert.assertTrue(ids.contains(new SwissProtIdentifier("Q197F8")));
        Assert.assertTrue(ids.contains(new SwissProtIdentifier("Q6GZX1")));
        Assert.assertTrue(ids.contains(new ECNumber("1.1.1.1")));

    }


}
