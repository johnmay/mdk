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

package uk.ac.ebi.mdk.io;

import uk.ac.ebi.mdk.io.identifier.BasicIdentifierReader;
import uk.ac.ebi.mdk.io.identifier.DynamicIdentifierReader;
import uk.ac.ebi.mdk.io.identifier.TaxonomyReader;
import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.Version;
import uk.ac.ebi.mdk.domain.identifier.DynamicIdentifier;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.identifier.Taxonomy;

import java.io.DataInput;
import java.io.IOException;

/**
 * IdentifierDataInputStream - 13.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class IdentifierDataInputStream
        extends AbstractDataInput<IdentifierReader>
        implements IdentifierInput {

    private static final Logger LOGGER = Logger.getLogger(IdentifierDataInputStream.class);

    private DataInput in;
    
    public IdentifierDataInputStream(DataInput in, Version v){
        
        super(in, v);

        this.in = in;

        // specialised readers
        add(Taxonomy.class,          new TaxonomyReader(this.in));
        add(DynamicIdentifier.class, new DynamicIdentifierReader(this.in));

    }

    @Override
    public <I extends Identifier> I read() throws IOException, ClassNotFoundException {
        Class c = readClass();
        return (I) read(c);
    }

    @Override
    public <I extends Identifier> I read(Class<I> c) throws IOException, ClassNotFoundException {
        return (I) getReader(c).readIdentifier();
    }

    public IdentifierReader getReader(Class c){
        
        return hasMarshaller(c, getVersion()) ? getMarshaller(c, getVersion()) : add(c, newBasicRead(c), getVersion());
                
    }
    
    private IdentifierReader newBasicRead(Class c){
        return new BasicIdentifierReader(c, in);
    }
    
}
