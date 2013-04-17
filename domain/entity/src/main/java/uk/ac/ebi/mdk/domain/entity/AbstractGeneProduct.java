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

package uk.ac.ebi.mdk.domain.entity;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.entity.collection.Genome;
import uk.ac.ebi.mdk.domain.identifier.Identifier;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 * @name GeneProduct - 2011.10.07 <br> Class description
 */
public abstract class AbstractGeneProduct
        extends AbstractAnnotatedEntity implements GeneProduct {

    public AbstractGeneProduct(UUID uuid) {
        super(uuid);
    }

    public AbstractGeneProduct(Identifier identifier, String abbreviation, String name) {
        super(identifier, abbreviation, name);
    }


    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        throw new NotSerializableException();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        throw new NotSerializableException();
    }

    public void readExternal(ObjectInput in, Genome genome) throws IOException, ClassNotFoundException {

    }

    public void writeExternal(ObjectOutput out, Genome genome) throws IOException {

    }
}
