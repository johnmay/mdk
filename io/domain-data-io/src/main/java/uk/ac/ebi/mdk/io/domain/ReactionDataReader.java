package uk.ac.ebi.mdk.io.domain;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.io.EnumReader;
import uk.ac.ebi.mdk.io.EntityInput;
import uk.ac.ebi.mdk.io.EntityReader;
import uk.ac.ebi.mdk.domain.entity.reaction.Compartment;
import uk.ac.ebi.mdk.domain.entity.reaction.Direction;
import uk.ac.ebi.mdk.domain.entity.GeneProduct;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;

import java.io.DataInput;
import java.io.IOException;

/**
 * ProteinProductDataWriter - 12.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@CompatibleSince("0.9")
public class ReactionDataReader
        implements EntityReader<MetabolicReaction> {

    private static final Logger LOGGER = Logger.getLogger(ReactionDataReader.class);

    private DataInput in;
    private EntityInput entityIn;
    private EnumReader enumReader;
    private EntityFactory factory;

    public ReactionDataReader(DataInput in, EntityFactory factory, EntityInput entityIn){
        this.in = in;
        this.entityIn = entityIn;
        this.enumReader = new EnumReader(in);
        this.factory = factory;
    }

    public MetabolicReaction readEntity() throws IOException, ClassNotFoundException {

        MetabolicReaction rxn = factory.newInstance(MetabolicReaction.class);

        int nReactants = in.readByte();
       
        for(int i = 0; i < nReactants; i++){
            MetabolicParticipant p = factory.newInstance(MetabolicParticipant.class);
            p.setCoefficient(in.readDouble());
            p.setCompartment((Compartment)enumReader.readEnum());
            p.setMolecule(entityIn.read(Metabolite.class));
            rxn.addReactant(p);
        }

        int nProducts = in.readByte();

        for(int i = 0; i < nProducts; i++){
            MetabolicParticipant p = factory.newInstance(MetabolicParticipant.class);
            p.setCoefficient(in.readDouble());
            p.setCompartment((Compartment)enumReader.readEnum());
            p.setMolecule(entityIn.read(Metabolite.class));
            rxn.addProduct(p);
        }

        rxn.setDirection((Direction)enumReader.readEnum());

        // read modifiers
        int nModifiers = in.readByte();
        for(int i = 0; i < nModifiers; i++){
            rxn.addModifier( (GeneProduct) entityIn.read());
        }


        return rxn;

    }

}
