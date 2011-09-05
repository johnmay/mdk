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
package uk.ac.ebi.metabolomes.descriptor.annotation;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * ObjectLink.java – MetabolicDevelopmentKit – Jun 23, 2011
 * Class defines a link between two objects
 * @author johnmay <johnmay@ebi.ac.uk, john.wilkinsonmay@gmail.com>
 */
public class ObjectLink
    implements Externalizable{

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( ObjectLink.class );

    private Object itemOne;
    private Object itemTwo;

    // todo indicate relationship

    public ObjectLink( Object itemOne , Object itemTwo ) {
        this.itemOne = itemOne;
        this.itemTwo = itemTwo;
    }

    public Object getItemOne() {
        return itemOne;
    }

    public Object getIremTwo() {
        return itemTwo;
    }


    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        itemOne = in.readObject();
        itemTwo = in.readObject();
    }


    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(itemOne);
        out.writeObject(itemTwo);
    }




}
