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
package uk.ac.ebi.mdk.domain.entity;

import com.google.common.base.Objects;
import uk.ac.ebi.mdk.domain.identifier.Identifier;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;


/**
 * ReconstructionEntity.java – MetabolicDevelopmentKit – Jun 23, 2011
 *
 * @author johnmay <johnmay@ebi.ac.uk, john.wilkinsonmay@gmail.com>
 */
public abstract class AbstractEntity implements Entity, Cloneable, Externalizable {

    private static final org.apache.log4j.Logger logger =
                                                 org.apache.log4j.Logger.getLogger(
            AbstractEntity.class);

    private Identifier identifier;

    private String abbreviation = "";

    private String name = "";


    public AbstractEntity() {
    }


    /**
     * Full instantiation constructor
     *
     * @param identifier
     * @param abbreviation
     * @param name
     * 
     */
    public AbstractEntity(Identifier identifier, String abbreviation, String name) {
        this.identifier = identifier;
        this.abbreviation = abbreviation;
        this.name = name;
    }


    /**
     * Access the abbreviation of the entity. The abbreviation is normally a
     * couple of letters that give the author a quick reference of what the object
     * is
     * @return abbreviation (null) if no abbreviation is present
     */
    public String getAbbreviation() {
        return abbreviation;
    }


    /**
     * Access the name of the entity
     * @return name of entity (null) if no name is given
     */
    public String getName() {
        return name;
    }


    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }


    public void setName(String name) {
        this.name = name;
    }


    /**
     * Return the accession of the entity
     */
    public String getAccession() {
        return getIdentifier().getAccession();
    }


    public Identifier getIdentifier() {
        return identifier;
    }


    public void setIdentifier(Identifier identifier) {
        this.identifier = identifier;
    }


//    @Override
//    public Object clone() throws CloneNotSupportedException {
//        return super.clone();
//    }
    public void writeExternal(ObjectOutput out) throws IOException {
        throw new UnsupportedOperationException("No longer supported, use entity-io module");
    }


    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        throw new UnsupportedOperationException("No longer supported, use entity-io module");
    }


    @Override
    public String toString() {
        return name;
    }

}
