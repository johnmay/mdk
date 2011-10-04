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
    private String abbreviation = "";
    private String name = "";


    public ReconstructionEntity() {
    }

    /**
     * Full instantiation constructor
     *
     * @param identifier
     * @param abbreviation
     * @param name
     * 
     */
    public ReconstructionEntity(Identifier identifier, String abbreviation, String name) {
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


    public void setIdentifier(Identifier id) {
        this.identifier = id;
    }


//    @Override
//    public Object clone() throws CloneNotSupportedException {
//        return super.clone();
//    }
    public void writeExternal(ObjectOutput out) throws IOException {
        IdentifierFactory.getInstance().write(out, identifier);
        out.writeUTF(abbreviation);
        out.writeUTF(name);
    }


    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        identifier = IdentifierFactory.getInstance().read(in);
        abbreviation = in.readUTF();
        name = in.readUTF();
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
