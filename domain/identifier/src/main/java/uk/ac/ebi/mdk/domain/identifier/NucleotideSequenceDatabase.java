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

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.identifier.type.ProteinIdentifier;
import uk.ac.ebi.mdk.deprecated.MIR;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * NCBIReferenceSequence - 14.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@MIR(value =29)
public class NucleotideSequenceDatabase
        extends AbstractIdentifier
        implements ProteinIdentifier {

    private static final Logger LOGGER = Logger.getLogger(NucleotideSequenceDatabase.class);

    private static final Pattern HEADER_CODE = Pattern.compile("gp|ddbj|emb");
    
    public NucleotideSequenceDatabase() {
        super();
    }

    public NucleotideSequenceDatabase(String accession){
        super(accession);
    }

    @Override
    public NucleotideSequenceDatabase ofHeader(Iterator<String> token) {

        String accession = token.hasNext() ? token.next() : "";
        String locus     = token.hasNext() ? token.next() : "";

        return new NucleotideSequenceDatabase(accession);

    }


    @Override
    public Collection<String> getHeaderCodes() {
        return Arrays.asList("gb", "ddbj", "emb");
    }

    @Override
    public Identifier newInstance() {
        return new NucleotideSequenceDatabase();
    }
}
