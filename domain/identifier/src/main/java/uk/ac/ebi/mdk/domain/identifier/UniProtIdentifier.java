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

package uk.ac.ebi.mdk.domain.identifier;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collection;
import java.util.Iterator;

import uk.ac.ebi.mdk.deprecated.MIRIAMEntry;
import uk.ac.ebi.mdk.domain.identifier.type.ProteinIdentifier;
import uk.ac.ebi.mdk.domain.IdentifierMetaInfo;
import uk.ac.ebi.mdk.deprecated.MIR;
import uk.ac.ebi.mdk.domain.identifier.type.SequenceIdentifier;


/**
 * UniProtIdentifier.java
 *
 *
 * @author johnmay
 * @date Mar 21, 2011
 */
@MIR(5)
public class UniProtIdentifier
        extends AbstractIdentifier
        implements ProteinIdentifier, Externalizable {

    private static final org.apache.log4j.Logger logger =
                                                 org.apache.log4j.Logger.getLogger(
            UniProtIdentifier.class);

    private static final IdentifierMetaInfo META_INFO = IDENTIFIER_LOADER.getMetaInfo(
            UniProtIdentifier.class);

    private static final String UNIPROT_ACCESSION_SCHEMA = "[A-Z][A-Z0-9]{5}";

    private static final String UNIPROT_WITH_SPECIES_ACCESSION_SCHEMA = UNIPROT_ACCESSION_SCHEMA
                                                                        + "_[A-Z]{5}";


    public enum Status {

        REVIEWED, UNREVIEWED, UNKNOWN
    };

    private String name = "";


    public UniProtIdentifier() {
        //  setResource( Resource.UNIPROT );
    }

    @Override public Identifier newInstance() {
        return new UniProtIdentifier();
    }

    @Override public Collection<String> getHeaderCodes() {
        throw new UnsupportedOperationException("");
    }

    @Override public SequenceIdentifier ofHeader(Iterator<String> token) {
        throw new UnsupportedOperationException("");
    }

    public UniProtIdentifier(String identifier, Boolean check) {
        this();

        if (check) {
            setAccession(parse(identifier));
        } else {
            setAccession(identifier);
        }
    }


    public UniProtIdentifier(String identifier) {
        this(identifier, Boolean.TRUE);
    }


    public final String parse(String identifier) {
        String parsedIdentifier = identifier.trim();

        // remove the version
        if (parsedIdentifier.contains(".")) {
            // could store if needed
            parsedIdentifier = parsedIdentifier.substring(0, parsedIdentifier.indexOf("."));
        }

        // if not matching schema warn
        if (parsedIdentifier.matches(UNIPROT_ACCESSION_SCHEMA)) {
            return parsedIdentifier;
        }
        if (parsedIdentifier.matches(UNIPROT_WITH_SPECIES_ACCESSION_SCHEMA)) {
            return parsedIdentifier;
        }

        logger.warn(getResource() + " accession '" + identifier
                    + "' doesn't look like a valid uniprot identifier");

        return parsedIdentifier;

    }


    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        name = in.readUTF();
    }


    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeUTF(name);
    }


    /**
     * @inheritDoc
     */
    @Override
    public String getShortDescription() {
        return META_INFO.brief;
    }


    /**
     * @inheritDoc
     */
    @Override
    public String getLongDescription() {
        return META_INFO.description;
    }





    /**
     * @inheritDoc
     */
    @Override
    public MIRIAMEntry getResource() {
        return META_INFO.resource;
    }


    /**
     * Returns the status of the entry
     */
    public UniProtIdentifier.Status getStatus(){
        return Status.UNKNOWN;
    }


    public void setName(String name) {
        this.name = name;
    }
}
