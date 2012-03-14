/**
 * TrEMBLIdentifier.java
 *
 * 2011.09.14
 *
 * This file is part of the CheMet library
 *
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CheMet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.resource.protein;

import org.apache.log4j.Logger;
import uk.ac.ebi.resource.IdentifierDescription;
import uk.ac.ebi.resource.MIRIAMIdentifier;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;


/**
 *          TrEMBLIdentifier – 2011.09.14 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
@MIRIAMIdentifier(mir = 5)
public class TrEMBLIdentifier
        extends UniProtIdentifier {

    private static final Logger LOGGER = Logger.getLogger(TrEMBLIdentifier.class);

    private static final IdentifierDescription DESCRIPTION = IDENTIFIER_LOADER.getMetaInfo(
            TrEMBLIdentifier.class);


    public TrEMBLIdentifier() {
    }


    public TrEMBLIdentifier(String identifier) {
        super(identifier);
    }


    /**
     * @inheritDoc
     */
    @Override
    public TrEMBLIdentifier newInstance() {
        return new TrEMBLIdentifier();
    }


    /**
     * @inheritDoc
     */
    @Override
    public Byte getIndex() {
        return DESCRIPTION.index;
    }


    @Override
    public UniProtIdentifier.Status getStatus() {
        return UniProtIdentifier.Status.UNREVIEWED;
    }


    @Override
    public TrEMBLIdentifier ofHeader(Iterator<String> token) {

        String accession = token.hasNext() ? token.next() : "";
        String name      = token.hasNext() ? token.next() : "";

        return new TrEMBLIdentifier(accession);

    }


    /**
     * @inheritDoc
     */
    @Override
    public Collection<String> getHeaderCodes() {
        return Arrays.asList("tr");
    }
}