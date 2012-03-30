package uk.ac.ebi.chemet.io.entity.marshal;

import org.apache.log4j.Logger;
import org.junit.Test;
import uk.ac.ebi.caf.utility.version.Version;
import uk.ac.ebi.chemet.io.annotation.AnnotationDataInputStream;
import uk.ac.ebi.chemet.io.annotation.AnnotationDataOutputStream;
import uk.ac.ebi.chemet.io.domain.EntityDataInputStream;
import uk.ac.ebi.chemet.io.domain.EntityDataOutputStream;
import uk.ac.ebi.chemet.io.domain.EntityInput;
import uk.ac.ebi.chemet.io.domain.EntityOutput;
import uk.ac.ebi.chemet.io.observation.ObservationDataInputStream;
import uk.ac.ebi.chemet.io.observation.ObservationDataOutputStream;
import uk.ac.ebi.chemet.resource.basic.BasicChemicalIdentifier;
import uk.ac.ebi.chemet.resource.basic.BasicReactionIdentifier;
import uk.ac.ebi.core.DefaultEntityFactory;
import uk.ac.ebi.core.MetaboliteImplementation;
import uk.ac.ebi.core.reaction.MetabolicParticipantImplementation;
import uk.ac.ebi.interfaces.entities.EntityFactory;
import uk.ac.ebi.interfaces.entities.MetabolicReaction;
import uk.ac.ebi.interfaces.entities.Metabolite;
import uk.ac.ebi.interfaces.reaction.Direction;
import uk.ac.ebi.interfaces.reaction.Participant;

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

        Metabolite duplicate = new MetaboliteImplementation(BasicChemicalIdentifier.nextIdentifier(), "adp", "ADP");

        rxn.addReactant(new MetabolicParticipantImplementation(new MetaboliteImplementation(BasicChemicalIdentifier.nextIdentifier(), "atp", "ATP")));
        rxn.addReactant(new MetabolicParticipantImplementation(duplicate));
        rxn.addProduct(new MetabolicParticipantImplementation(new MetaboliteImplementation(BasicChemicalIdentifier.nextIdentifier(), "gtp", "GTP")));
        rxn.addProduct(new MetabolicParticipantImplementation(new MetaboliteImplementation(BasicChemicalIdentifier.nextIdentifier(), "gdp", "GDP")));
        rxn.addProduct(new MetabolicParticipantImplementation(duplicate));
        rxn.setDirection(Direction.FORWARD);

        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bytestream);

        Version v = new Version("0.9");

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


        MetabolicReaction rxn2 = ein.read();
        for (Participant<?, ?> r : rxn2.getReactants()) {
            for (Participant<?, ?> p : rxn2.getProducts()) {
                if(r.getMolecule() == p.getMolecule()){
                    System.out.println("object graph is working!");
                }
            }
        }


    }

}
