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

package uk.ac.ebi.mdk.io;

import org.junit.Test;
import uk.ac.ebi.caf.utility.version.Version;
import uk.ac.ebi.mdk.domain.DefaultIdentifierFactory;
import uk.ac.ebi.mdk.domain.annotation.crossreference.ChEBICrossReference;
import uk.ac.ebi.mdk.domain.annotation.crossreference.CrossReference;
import uk.ac.ebi.mdk.domain.annotation.crossreference.KEGGCrossReference;
import uk.ac.ebi.mdk.domain.entity.DefaultEntityFactory;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.Gene;
import uk.ac.ebi.mdk.domain.entity.GeneProduct;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.entity.collection.Metabolome;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.domain.entity.reaction.compartment.Organelle;
import uk.ac.ebi.mdk.domain.identifier.BioCycChemicalIdentifier;
import uk.ac.ebi.mdk.domain.identifier.ChEBIIdentifier;
import uk.ac.ebi.mdk.domain.identifier.HMDBIdentifier;
import uk.ac.ebi.mdk.domain.identifier.IdentifierFactory;
import uk.ac.ebi.mdk.domain.identifier.KEGGCompoundIdentifier;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

/**
 * @author John May
 */
public class BackwardsCompatibility {

    @Test public void test_1_0_0() throws IOException, ClassNotFoundException {
        check(load("test_recon_1_0_0"));
    }

    @Test public void test_1_0_1() throws IOException, ClassNotFoundException {
        check(load("test_recon_1_0_1"));
    }

    @Test public void test_1_0_2() throws IOException, ClassNotFoundException {
        check(load("test_recon_1_0_2"));
    }

    @Test public void test_1_0_2_1() throws IOException,
                                            ClassNotFoundException {
        check(load("test_recon_1_0_2_1"));
    }


    public void check(Reconstruction reconstruction) throws IOException,
                                                            ClassNotFoundException {

        assertThat(reconstruction.genome().genes().size(), is(6));
        assertThat(reconstruction.proteome().size(), is(6));
        assertThat(reconstruction.metabolome().size(), is(5));
        assertThat(reconstruction.reactome().size(), is(4));

        // ensure each gene/proteins are linked
        for (Gene gene : reconstruction.genome().genes()) {
            assertFalse(reconstruction.productsOf(gene).isEmpty());
        }
        for (GeneProduct gp : reconstruction.proteome()) {
            assertFalse(reconstruction.genesOf(gp).isEmpty());
        }

        // metabolome
        Metabolome metabolome = reconstruction.metabolome();
        Metabolite atp = metabolome.ofName("atp").iterator().next();
        Metabolite gtp = metabolome.ofName("gtp").iterator().next();
        Metabolite adp = metabolome.ofName("adp").iterator().next();
        Metabolite gdp = metabolome.ofName("gdp").iterator().next();
        Metabolite pi = metabolome.ofName("pi").iterator().next();

        IdentifierFactory ids = DefaultIdentifierFactory.getInstance();

        // atp cross-references
        Collection<CrossReference> xrefs = atp
                .getAnnotationsExtending(CrossReference.class);
        assertThat(xrefs, hasItem(new ChEBICrossReference(new ChEBIIdentifier("CHEBI:30616"))));
        assertThat(xrefs, hasItem(new ChEBICrossReference(new ChEBIIdentifier("CHEBI:57299"))));
        assertThat(xrefs, hasItem(new ChEBICrossReference(new ChEBIIdentifier("CHEBI:15422"))));
        assertThat(xrefs, hasItem(new KEGGCrossReference(new KEGGCompoundIdentifier("C00002"))));
        assertThat(xrefs, hasItem(CrossReference
                                          .create(new HMDBIdentifier("HMDB00538"))));
        assertThat(xrefs, hasItem(CrossReference
                                          .create(new BioCycChemicalIdentifier("META", "ATP"))));

        assertFalse(reconstruction.participatesIn(atp).isEmpty());
        assertFalse(reconstruction.participatesIn(gtp).isEmpty());
        assertFalse(reconstruction.participatesIn(gdp).isEmpty());
        assertFalse(reconstruction.participatesIn(adp).isEmpty());
        assertFalse(reconstruction.participatesIn(pi).isEmpty());

        Map<String, MetabolicReaction> rxnMap = new TreeMap<String, MetabolicReaction>();
        for (MetabolicReaction rxn : reconstruction.reactome()) {
            rxnMap.put(rxn.getName(), rxn);
        }

        MetabolicReaction trx1 = rxnMap.get("trx1");
        MetabolicReaction trx2 = rxnMap.get("trx2");
        MetabolicReaction rx1 = rxnMap.get("rx1");
        MetabolicReaction rx2 = rxnMap.get("rx2");

        // trx1

        MetabolicParticipant trx1_p1 = trx1.getReactants().get(0);
        MetabolicParticipant trx1_p2 = trx1.getProducts().get(0);

        assertEquals(trx1_p1.getCompartment(), Organelle.CYTOPLASM);
        assertEquals(trx1_p2.getCompartment(), Organelle.EXTRACELLULAR);
        assertEquals(trx1_p1.getMolecule(), gdp);
        assertEquals(trx1_p2.getMolecule(), gdp);
        assertEquals(trx1_p1.getCoefficient(), 1.0, 0.01);
        assertEquals(trx1_p2.getCoefficient(), 1.0, 0.01);

        // trx2
        MetabolicParticipant trx2_p1 = trx2.getReactants().get(0);
        MetabolicParticipant trx2_p2 = trx2.getProducts().get(0);

        assertEquals(trx2_p1.getCompartment(), Organelle.EXTRACELLULAR);
        assertEquals(trx2_p2.getCompartment(), Organelle.CYTOPLASM);
        assertEquals(trx2_p1.getMolecule(), adp);
        assertEquals(trx2_p2.getMolecule(), adp);
        assertEquals(trx2_p1.getCoefficient(), 2.0, 0.01);
        assertEquals(trx2_p2.getCoefficient(), 2.0, 0.01);

        // rx1
        MetabolicParticipant rx1_p1 = rx1.getReactants().get(0);
        MetabolicParticipant rx1_p2 = rx1.getReactants().get(1);
        MetabolicParticipant rx1_p3 = rx1.getProducts().get(0);

        assertThat(rx1_p1.getMolecule(), is(adp));
        assertThat(rx1_p2.getMolecule(), is(pi));
        assertThat(rx1_p3.getMolecule(), is(atp));

        assertEquals(rx1_p1.getCompartment(), Organelle.CYTOPLASM);
        assertEquals(rx1_p2.getCompartment(), Organelle.CYTOPLASM);
        assertEquals(rx1_p3.getCompartment(), Organelle.CYTOPLASM);

        assertEquals(rx1_p1.getCoefficient(), 1.0, 0.01);
        assertEquals(rx1_p2.getCoefficient(), 1.0, 0.01);
        assertEquals(rx1_p3.getCoefficient(), 1.0, 0.01);

        // rx2
        MetabolicParticipant rx2_p1 = rx2.getReactants().get(0);
        MetabolicParticipant rx2_p2 = rx2.getReactants().get(1);
        MetabolicParticipant rx2_p3 = rx2.getProducts().get(0);

        assertThat(rx2_p1.getMolecule(), is(gdp));
        assertThat(rx2_p2.getMolecule(), is(pi));
        assertThat(rx2_p3.getMolecule(), is(gtp));

        assertEquals(rx2_p1.getCompartment(), Organelle.CYTOPLASM);
        assertEquals(rx2_p2.getCompartment(), Organelle.CYTOPLASM);
        assertEquals(rx2_p3.getCompartment(), Organelle.CYTOPLASM);

        assertEquals(rx2_p1.getCoefficient(), 1.0, 0.01);
        assertEquals(rx2_p2.getCoefficient(), 1.0, 0.01);
        assertEquals(rx2_p3.getCoefficient(), 1.0, 0.01);

    }

    public Reconstruction load(String path) throws IOException,
                                                   ClassNotFoundException {


        Properties properties = new Properties();
        InputStream in = getClass()
                .getResourceAsStream(path + "/info.properties");
        properties.load(in);
        String stringVersion = properties.getProperty("chemet.version");
        in.close();

        Version version = stringVersion == null ? IOConstants.VERSION
                                                : new Version(stringVersion);

        // open data input stream
        DataInputStream annotationStream = new DataInputStream(getClass()
                                                                       .getResourceAsStream(path + "/entity-annotations"));
        DataInputStream observationStream = new DataInputStream(getClass()
                                                                        .getResourceAsStream(path + "/entity-observations"));
        DataInputStream entityStream = new DataInputStream(getClass()
                                                                   .getResourceAsStream(path + "/entities"));

        EntityFactory factory = DefaultEntityFactory.getInstance();

        ObservationInput observationInput = new ObservationDataInputStream(observationStream, version);
        AnnotationInput annotationInput = new AnnotationDataInputStream(annotationStream, version);
        EntityInput entityInput = new EntityDataInputStream(version, entityStream, factory, annotationInput, observationInput);

        Reconstruction reconstruction = entityInput.read(null);

        annotationStream.close();
        observationStream.close();
        entityStream.close();

        return reconstruction;
    }

}
