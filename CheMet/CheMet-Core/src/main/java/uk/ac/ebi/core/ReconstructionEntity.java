/*
 *     This file is part of Metabolic Network Builder
 *
 *     Metabolic Network Builder is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Foobar is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.ebi.core;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import uk.ac.ebi.interfaces.Identifier;
import uk.ac.ebi.metabolomes.identifier.AbstractIdentifier;
import uk.ac.ebi.resource.IdentifierFactory;


/**
 * ReconstructionEntity.java – MetabolicDevelopmentKit – Jun 23, 2011
 *
 * @author johnmay <johnmay@ebi.ac.uk, john.wilkinsonmay@gmail.com>
 */
public class ReconstructionEntity implements Cloneable, Externalizable {

    private static final org.apache.log4j.Logger logger =
                                                 org.apache.log4j.Logger.getLogger(
      ReconstructionEntity.class);
    private Identifier identifier;


    public Identifier getIdentifier() {
        return identifier;
    }


    public void setIdentifier(AbstractIdentifier id) {
        this.identifier = id;
    }


//    @Override
//    public Object clone() throws CloneNotSupportedException {
//        return super.clone();
//    }


    public void writeExternal(ObjectOutput out) throws IOException {
        IdentifierFactory.getInstance().write(out, identifier);
    }


    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        identifier = IdentifierFactory.getInstance().read(in);
    }


    @Override
    public String toString() {
        return identifier.toString();
    }



    @Override
    public boolean equals(Object obj) {
        if( obj == null ) {
            return false;
        }
        if( getClass() != obj.getClass() ) {
            return false;
        }
        final ReconstructionEntity other = (ReconstructionEntity) obj;
        if( this.identifier != other.identifier &&
            (this.identifier == null || !this.identifier.equals(other.identifier)) ) {
            return false;
        }
        return true;
    }


    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + (this.identifier != null ? this.identifier.hashCode() : 0);
        return hash;
    }




}

