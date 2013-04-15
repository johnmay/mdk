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

package uk.ac.ebi.mdk.io.identifier;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.io.EnumWriter;
import uk.ac.ebi.mdk.io.IdentifierWriter;
import uk.ac.ebi.mdk.domain.identifier.Taxonomy;

import java.io.DataOutput;
import java.io.IOException;

/**
 * TaxonomyWriter - 13.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@CompatibleSince("0.9")
public class TaxonomyWriter implements IdentifierWriter<Taxonomy> {

    private static final Logger LOGGER = Logger.getLogger(TaxonomyWriter.class);

    private DataOutput out;
    private EnumWriter enumWriter;

    public TaxonomyWriter(DataOutput out) {
        this.out = out;
        this.enumWriter = new EnumWriter(out);
    }

    @Override
    public void write(Taxonomy identifier) throws IOException {
        out.writeInt(identifier.getTaxon());
        out.writeUTF(identifier.getCode());
        out.writeUTF(identifier.getCommonName());
        out.writeUTF(identifier.getOfficialName());
        enumWriter.writeEnum(identifier.getKingdom());
    }
}
