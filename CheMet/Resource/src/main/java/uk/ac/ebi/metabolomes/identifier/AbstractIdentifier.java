/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Lesser GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package uk.ac.ebi.metabolomes.identifier;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.net.URL;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.core.AbstractDescriptor;
import uk.ac.ebi.interfaces.Resource;
import uk.ac.ebi.resource.IdentifierLoader;


/**
 *
 * Abstract class for that all identifiers should extend.
 * If the sub-class has more then one component (e.g. InChI)
 * the developer should decide which is or what should the
 * 'main' identifier be.
 *
 * In some case you may want a concatenated identifier where
 * the multiple sub-class variables are concatenated together
 * to form a string which identifies that object
 *
 * @author johnmay
 * @date   6 Apr 2011
 *
 */
public abstract class AbstractIdentifier
  extends AbstractDescriptor
  implements Identifier,
             Externalizable {

    public static final IdentifierLoader IDENTIFIER_LOADER = IdentifierLoader.getInstance();
    private String accession;


    public AbstractIdentifier() {
        super(IdentifierLoader.getInstance());
    }


    public AbstractIdentifier(String accession) {
        super(IdentifierLoader.getInstance());
        this.accession = accession;
    }


    /**
     * @inheritDoc
     */
    public String getAccession() {
        return accession;
    }


    /**
     * @param accession
     * @inheritDoc
     */
    public void setAccession(String accession) {
        this.accession = accession;
    }


    /**
     *
     * @return
     */
    public Resource getResource() {
        return IDENTIFIER_LOADER.getEntry(getClass());
    }


    /**
     * @inheritDoc
     */
    @Override
    public String toString() {
        return accession;
    }


    /**
     * @inheritDoc
     */
    @Override
    public boolean equals(Object obj) {
        if( obj == null ) {
            return false;
        }
        if( getClass() != obj.getClass() ) {
            return false;
        }
        final AbstractIdentifier other = (AbstractIdentifier) obj;
        if( (this.accession == null) ? (other.accession != null) : !this.accession.equals(
          other.accession) ) {
            return false;
        }
        return true;
    }


    /**
     * @inheritDoc
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + (this.accession != null ? this.accession.hashCode() : 0);
        return hash;
    }


    /**
     * @inheritDoc
     */
    public String getURN() {
        return getResource().getURN(accession);
    }


    /**
     * @inheritDoc
     */
    public URL getURL() {
        return getResource().getURL(getAccession());
    }


    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(accession);
    }


    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.accession = in.readUTF();
    }



}

