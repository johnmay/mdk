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

import uk.ac.ebi.mdk.io.identifier.BasicIdentifierWriter;
import uk.ac.ebi.mdk.io.identifier.DynamicIdentifierWriter;
import uk.ac.ebi.mdk.io.identifier.TaxonomyWriter;
import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.Version;
import uk.ac.ebi.mdk.domain.identifier.DynamicIdentifier;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.identifier.Taxonomy;

import java.io.DataOutput;
import java.io.IOException;

/**
 * IdentifierDataOutputStream - 13.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class IdentifierDataOutputStream
        extends AbstractDataOutput<IdentifierWriter<? extends Identifier>>
        implements IdentifierOutput {

    private static final Logger LOGGER = Logger.getLogger(IdentifierDataOutputStream.class);

    private BasicIdentifierWriter defaultWriter;
    private DataOutput out;

    public IdentifierDataOutputStream(DataOutput out, Version v){
        super(out, v);
        this.out = out;
        defaultWriter= new BasicIdentifierWriter(out);
        
        
        add(Taxonomy.class, new TaxonomyWriter(out));
        add(DynamicIdentifier.class, new DynamicIdentifierWriter(out));

    }

    @Override
    @SuppressWarnings("unchecked")
    public void writeData(Identifier identifier) throws IOException {
        Class c = identifier.getClass();
        getWriter(c).write(identifier);
    }

    @Override
    public void write(Identifier identifier) throws IOException {
        writeClass(identifier.getClass());
        writeData(identifier);
    }
    
    public IdentifierWriter getWriter(Class c){
        if(hasMarshaller(c, getVersion())){
            return getMarshaller(c, getVersion());
        }
        return defaultWriter;
    }
    
}


