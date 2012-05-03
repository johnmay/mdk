package uk.ac.ebi.mdk.io;

import uk.ac.ebi.chemet.io.identifier.basic.BasicIdentifierReader;
import uk.ac.ebi.chemet.io.identifier.other.DynamicIdentifierReader;
import uk.ac.ebi.chemet.io.identifier.other.TaxonomyReader;
import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.Version;
import uk.ac.ebi.chemet.resource.base.DynamicIdentifier;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.resource.organism.Taxonomy;

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
