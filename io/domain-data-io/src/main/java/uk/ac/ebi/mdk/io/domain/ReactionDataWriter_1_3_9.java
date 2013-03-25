package uk.ac.ebi.mdk.io.domain;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.domain.entity.AnnotatedEntity;
import uk.ac.ebi.mdk.domain.entity.GeneProduct;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.io.EntityOutput;
import uk.ac.ebi.mdk.io.EntityWriter;
import uk.ac.ebi.mdk.io.EnumWriter;

import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;

/**
 * ProteinProductDataWriter - 12.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@CompatibleSince("1.3.9")
public class ReactionDataWriter_1_3_9
        implements EntityWriter<MetabolicReaction> {

    private static final Logger LOGGER = Logger.getLogger(ReactionDataWriter_1_3_9.class);

    private DataOutput out;
    private EntityOutput entityOut;
    private EnumWriter enumWriter;

    public ReactionDataWriter_1_3_9(DataOutput out, EntityOutput entityOut){
        this.out = out;
        this.entityOut = entityOut;
        this.enumWriter = new EnumWriter(out);
    }

    public void write(MetabolicReaction rxn) throws IOException {

        out.writeUTF(rxn.uuid().toString());

        out.writeByte(rxn.getReactantCount());

        for (MetabolicParticipant p : rxn.getReactants()) {
            out.writeDouble(p.getCoefficient());
            enumWriter.writeEnum((Enum)p.getCompartment()); // throw error about compartment not being an enum
            entityOut.writeData(p.getMolecule());
        }

        out.writeByte(rxn.getProductCount());

        for (MetabolicParticipant p : rxn.getProducts()) {
            out.writeDouble(p.getCoefficient());
            enumWriter.writeEnum((Enum)p.getCompartment());
            entityOut.writeData(p.getMolecule());
        }

        enumWriter.writeEnum( (Enum) rxn.getDirection());

        // write modifiers
        Collection<GeneProduct> modifiers = rxn.getModifiers();
        out.writeByte(modifiers.size());
        for(AnnotatedEntity entity : modifiers ){
            entityOut.write(entity);
        }


    }

}
