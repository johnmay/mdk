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

package uk.ac.ebi.chemet.io.entity.marshal;

import org.apache.log4j.Logger;
import org.junit.Test;
import uk.ac.ebi.caf.utility.version.Version;
import uk.ac.ebi.mdk.domain.entity.ReconstructionImpl;
import uk.ac.ebi.mdk.io.AnnotationDataInputStream;
import uk.ac.ebi.mdk.io.AnnotationDataOutputStream;
import uk.ac.ebi.mdk.io.EntityDataInputStream;
import uk.ac.ebi.mdk.io.EntityDataOutputStream;
import uk.ac.ebi.mdk.domain.entity.MetaboliteImpl;
import uk.ac.ebi.mdk.io.EntityInput;
import uk.ac.ebi.mdk.io.EntityOutput;
import uk.ac.ebi.mdk.io.IOConstants;
import uk.ac.ebi.mdk.io.ObservationDataInputStream;
import uk.ac.ebi.mdk.io.ObservationDataOutputStream;
import uk.ac.ebi.mdk.domain.identifier.basic.BasicChemicalIdentifier;
import uk.ac.ebi.mdk.domain.identifier.basic.BasicReactionIdentifier;
import uk.ac.ebi.mdk.domain.entity.DefaultEntityFactory;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipantImplementation;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.reaction.Direction;
import uk.ac.ebi.mdk.domain.entity.reaction.Participant;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * MetaboliteDataWriterTest - 12.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class MetaboliteDataWriterTest {

    private static final Logger LOGGER = Logger.getLogger(MetaboliteDataWriterTest.class);

    @Test
    public void testWrite() throws Exception {


        EntityFactory factory = DefaultEntityFactory.getInstance();

        MetabolicReaction rxn = factory.newInstance(MetabolicReaction.class, BasicReactionIdentifier.nextIdentifier(), "Reaction", "rxn");

        Metabolite duplicate = new MetaboliteImpl(BasicChemicalIdentifier.nextIdentifier(), "adp", "ADP");

        rxn.addReactant(new MetabolicParticipantImplementation(new MetaboliteImpl(BasicChemicalIdentifier.nextIdentifier(), "atp", "ATP")));
        rxn.addReactant(new MetabolicParticipantImplementation(duplicate));
        rxn.addProduct(new MetabolicParticipantImplementation(new MetaboliteImpl(BasicChemicalIdentifier.nextIdentifier(), "gtp", "GTP")));
        rxn.addProduct(new MetabolicParticipantImplementation(new MetaboliteImpl(BasicChemicalIdentifier.nextIdentifier(), "gdp", "GDP")));
        rxn.addProduct(new MetabolicParticipantImplementation(duplicate));
        rxn.setDirection(Direction.FORWARD);

        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bytestream);

        Version v = IOConstants.CURRENT;

        EntityOutput eout = new EntityDataOutputStream(v,
                                                             out,
                                                             DefaultEntityFactory.getInstance(),
                                                             new AnnotationDataOutputStream(out, v),
                                                             new ObservationDataOutputStream(out, v));

        eout.write(rxn);


        DataInputStream din = new DataInputStream(new ByteArrayInputStream(bytestream.toByteArray()));
        EntityInput ein = new EntityDataInputStream(v,
                                                          din,
                                                          DefaultEntityFactory.getInstance(),
                                                          new AnnotationDataInputStream(din, v),
                                                          new ObservationDataInputStream(din, v));


        MetabolicReaction rxn2 = ein.read(new ReconstructionImpl());
        for (Participant<?, ?> r : rxn2.getReactants()) {
            for (Participant<?, ?> p : rxn2.getProducts()) {
                if(r.getMolecule() == p.getMolecule()){
                    System.out.println("object graph is working!");
                }
            }
        }


    }

}
