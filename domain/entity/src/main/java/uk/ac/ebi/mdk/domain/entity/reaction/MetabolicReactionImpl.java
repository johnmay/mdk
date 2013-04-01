/*
 * Copyright (c) 2013. John May <jwmay@users.sf.net>
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

package uk.ac.ebi.mdk.domain.entity.reaction;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.entity.GeneProduct;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.identifier.basic.BasicReactionIdentifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;


/**
 * MetabolicReaction – 2011.09.27 <br> Class description
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
public class MetabolicReactionImpl
        extends AbstractReaction<MetabolicParticipant>
        implements MetabolicReaction {

    private static final Logger LOGGER = Logger.getLogger(MetabolicReactionImpl.class);

    private ReactionType type = ReactionTypeImpl.ENZYMATIC;

    public MetabolicReactionImpl(UUID uuid){
        super(uuid);
    }

    public MetabolicReactionImpl() {
        this(BasicReactionIdentifier.nextIdentifier(), "", "");
    }


    public MetabolicReactionImpl(Identifier identifier, String abbreviation, String name) {
        super(identifier, abbreviation, name);
    }

    public void setType(ReactionType type) {
        this.type = type;
    }


    public ReactionType getType() {
        return type;
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean remove(Metabolite m) {

        if (m == null)
            return false;

        boolean changed = false;
        for (MetabolicParticipant p : getParticipants()) {
            if (p.getMolecule() == m) { // reference equality
                changed = removeReactant(p) || removeProduct(p) || changed;
            }
        }
        return changed;

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

    @Override
    public boolean addReactant(Metabolite reactant) {
        return addReactant(new MetabolicParticipantImplementation(reactant));
    }

    @Override
    public boolean addProduct(Metabolite product) {
        return addProduct(new MetabolicParticipantImplementation(product));
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
