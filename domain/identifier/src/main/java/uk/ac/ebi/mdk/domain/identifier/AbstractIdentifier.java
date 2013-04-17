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

package uk.ac.ebi.mdk.domain.identifier;

import uk.ac.ebi.mdk.deprecated.MIRIAMEntry;
import uk.ac.ebi.mdk.domain.AbstractDescriptor;
import uk.ac.ebi.mdk.domain.IdentifierLoader;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.net.URL;
import java.util.Collection;
import java.util.regex.Pattern;

/**
 * Abstract class for that all identifiers should extend. If the sub-class has
 * more then one component (e.g. InChI) the developer should decide which is or
 * what should the 'main' identifier be. <p/> In some case you may want a
 * concatenated identifier where the multiple sub-class variables are
 * concatenated together to form a string which identifies that object
 *
 * @author johnmay
 * @date 6 Apr 2011
 */
public abstract class AbstractIdentifier
        extends AbstractDescriptor
        implements Identifier {

    public static final IdentifierLoader IDENTIFIER_LOADER = IdentifierLoader
            .getInstance();
    private String accession;

    public AbstractIdentifier() {
        super(IDENTIFIER_LOADER);
        this.accession = ""; // avoid NullPointerExceptions
    }

    public AbstractIdentifier(String accession) {
        super(IdentifierLoader.getInstance());
        this.accession = accession;
    }

    /** @inheritDoc */
    @Override
    public String getAccession() {
        return accession;
    }

    /** @inheritDoc */
    @Override
    public void setAccession(String accession) {
        if (accession == null)
            throw new NullPointerException("Provided accession was null");
        this.accession = accession;
    }


    @Override public Pattern pattern() {
        return IDENTIFIER_LOADER.getMetaInfo(getClass()).pattern();
    }

    /** @inheritDoc */
    @Override
    public Resource getResource() {
        return IDENTIFIER_LOADER.getEntry(getClass());
    }

    /** @inheritDoc */
    @Override public boolean isValid() {
        Resource resource = getResource();
        if (resource != null) {
            Pattern p = pattern();
            if (p != null)
                return p.matcher(accession).matches();
        }
        // no resource, we cannot check if the accession is valid
        return true;
    }

    /** @inheritDoc */
    @Override
    public String toString() {
        return accession;
    }

    /**
     * Returns a string summary of the Identifier, consisting of the short
     * description of the database and the accession. Added to avoid changing
     * the behaviour of toString().
     *
     * @return dbName + accession.
     */
    @Override
    public String getSummary() {
        return getShortDescription() + " " + getAccession();
    }

    /** @inheritDoc */
    @Override
    public int hashCode() {
        int hash = 257;
        hash = 37 * hash + getClass().hashCode();
        hash = 37 * hash + (this.accession != null ? this.accession.hashCode()
                                                   : 0);
        return hash;
    }

    /** @inheritDoc */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractIdentifier other = (AbstractIdentifier) obj;
        if ((this.accession == null) ? (other.accession != null)
                                     : !this.accession.equals(
                                             other.accession)) {
            return false;
        }
        return true;
    }

    /** @inheritDoc */
    @Override
    public String getURN() {
        return getResource().getURN(getAccession());
    }

    /** @inheritDoc */
    @Override
    public URL getURL() {
        return getResource().getURL(getAccession());
    }


    @Override
    @Deprecated
    public String getResolvableURL() {
        // XXX very bad but need it in place until a better solution can be implemented
        return "http://identifiers.org/" + ((MIRIAMEntry) getResource()).namespace + "/" + accession + "/";
    }

    /** @inheritDoc */
    @Override
    public Collection<String> getSynonyms() {
        return IDENTIFIER_LOADER.getDatabaseSynonyms(getClass());
    }

    @Override public int compareTo(Identifier o) {
        int cmp = getResource().getName().compareTo(o.getResource().getName());
        if (cmp != 0)
            return cmp;
        return accession.compareTo(o.getAccession());
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(accession);
    }

    public void readExternal(ObjectInput in) throws IOException,
                                                    ClassNotFoundException {
        this.accession = in.readUTF();
    }
}
