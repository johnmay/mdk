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

package uk.ac.ebi.metabolomes.core;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.InvocationTargetException;
import uk.ac.ebi.metabolomes.identifier.AbstractIdentifier;
import uk.ac.ebi.metabolomes.identifier.GenericIdentifier;


/**
 * MetabolicReconstructionObject.java – MetabolicDevelopmentKit – Jun 23, 2011
 *
 * @author johnmay <johnmay@ebi.ac.uk, john.wilkinsonmay@gmail.com>
 */
public class MetabolicReconstructionObject implements Cloneable, Externalizable {

    private static final org.apache.log4j.Logger logger =
                                                 org.apache.log4j.Logger.getLogger(
      MetabolicReconstructionObject.class);
    private AbstractIdentifier identifier;


    public AbstractIdentifier getIdentifier() {
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
        out.writeObject(identifier);
    }


    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        identifier = (AbstractIdentifier) in.readObject();
    }


    @Override
    public String toString() {
        return identifier.toString();
    }


}

