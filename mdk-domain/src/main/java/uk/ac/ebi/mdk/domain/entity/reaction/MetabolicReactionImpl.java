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
package uk.ac.ebi.mdk.domain.entity.reaction;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.entity.GeneProduct;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.identifier.Identifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 *          MetabolicReaction – 2011.09.27 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class MetabolicReactionImpl
        extends AbstractReaction<MetabolicParticipant>
        implements MetabolicReaction {

    private static final Logger LOGGER = Logger.getLogger(MetabolicReactionImpl.class);

    private List<GeneProduct> modifiers = new ArrayList();

    private ReactionType type = ReactionTypeImpl.ENZYMATIC;


    public MetabolicReactionImpl() {
    }


    public MetabolicReactionImpl(Identifier identifier, String abbreviation, String name) {
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
    public MetabolicReactionImpl newInstance() {
        return new MetabolicReactionImpl();
    }


    public List<Metabolite> getAllReactionMolecules() {
        List<Metabolite> molecules = new ArrayList<Metabolite>();
        for (MetabolicParticipant p : getReactantParticipants()) {
            molecules.add(p.getMolecule());
        }
        for (MetabolicParticipant p : getProductParticipants()) {
            molecules.add(p.getMolecule());
        }
        return molecules;
    }
//    public void readExternal(ObjectInput in, MetaboliteCollection metabolites, ProductCollection products) throws IOException, ClassNotFoundException {
//        super.readExternal(in, metabolites);
//        if (in.readBoolean()) {
//            int n = in.readInt();
//            while (n > modifiers.size()) {
//                String baseType = in.readUTF();
//                modifiers.add(products.getAll(baseType).get(in.readInt()));
//            }
//        }
//        type = (ReactionType) in.readObject();
//    }
//
//
//    public void writeExternal(ObjectOutput out, MetaboliteCollection metabolites, ProductCollection products) throws IOException {
//        super.writeExternal(out, metabolites);
//
//        out.writeBoolean(!modifiers.isEmpty());
//        if (!modifiers.isEmpty()) {
//            out.writeInt(modifiers.size());
//            for (GeneProduct product : modifiers) {
//                out.writeUTF(product.getBaseType());
//                out.writeInt(products.getAll(product.getBaseType()).indexOf(product));
//            }
//        }
//        out.writeObject(type);
//
//    }
}
