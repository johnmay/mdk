/**
 * MetabolicReaction.java
 *
 * 2011.09.27
 *
 * This file is part of the CheMet library
 * 
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * CheMet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.core;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.List;
import org.apache.log4j.Logger;
import uk.ac.ebi.chemet.entities.reaction.Reaction;
import uk.ac.ebi.chemet.entities.reaction.participant.Participant;
import uk.ac.ebi.core.product.ProductCollection;
import uk.ac.ebi.interfaces.GeneProduct;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.core.metabolite.MetaboliteCollection;
import uk.ac.ebi.core.reaction.MetabolicParticipant;
import uk.ac.ebi.core.reaction.ReactionType;


/**
 *          MetabolicReaction – 2011.09.27 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class MetabolicReaction extends Reaction<Metabolite, Double, Compartment> {

    private static final Logger LOGGER = Logger.getLogger(MetabolicReaction.class);

    private List<GeneProduct> modifiers = new ArrayList();

    private ReactionType type = ReactionType.ENZYMATIC;


    public MetabolicReaction() {
    }


    public MetabolicReaction(Identifier identifier, String abbreviation, String name) {
        super(identifier, abbreviation, name);
    }


    public void addModifier(GeneProduct product) {
        modifiers.add(product);
    }


    public Collection<GeneProduct> getModifiers() {
        return modifiers;
    }


    public void setType(ReactionType type) {
        this.type = type;
    }


    public ReactionType getType() {
        return type;
    }


    @Override
    public MetabolicReaction newInstance() {
        return new MetabolicReaction();
    }


    public void readExternal(ObjectInput in, MetaboliteCollection metabolites, ProductCollection products) throws IOException, ClassNotFoundException {
        super.readExternal(in, metabolites);
        if (in.readBoolean()) {
            int n = in.readInt();
            while (n > modifiers.size()) {
                String baseType = in.readUTF();
                modifiers.add(products.getAll(baseType).get(in.readInt()));
            }
        }
        type = (ReactionType) in.readObject();
    }


    public void writeExternal(ObjectOutput out, MetaboliteCollection metabolites, ProductCollection products) throws IOException {
        super.writeExternal(out, metabolites);

        out.writeBoolean(!modifiers.isEmpty());
        if (!modifiers.isEmpty()) {
            out.writeInt(modifiers.size());
            for (GeneProduct product : modifiers) {
                out.writeUTF(product.getBaseType());
                out.writeInt(products.getAll(product.getBaseType()).indexOf(product));
            }
        }
        out.writeObject(type);

    }
}
