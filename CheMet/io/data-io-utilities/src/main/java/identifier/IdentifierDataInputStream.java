package identifier;

import identifier.basic.BasicIdentifierReader;
import identifier.other.TaxonomyReader;
import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.Version;
import uk.ac.ebi.chemet.io.core.AbstractDataInput;
import uk.ac.ebi.chemet.io.identifier.IdentifierInput;
import uk.ac.ebi.chemet.io.identifier.IdentifierReader;
import uk.ac.ebi.interfaces.identifiers.Identifier;
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

    private Version v;
    private DataInput in;
    
    public IdentifierDataInputStream(DataInput in, Version v){
        
        super(in);

        this.in = in;
        this.v  = v;
        
        add(Taxonomy.class, new TaxonomyReader(this.in));
        
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
        
        return hasMarshaller(c, v) ? getMarshaller(c, v) : add(c, newBasicRead(c), v);
                
    }
    
    private IdentifierReader newBasicRead(Class c){
        return new BasicIdentifierReader(c, in);
    }
    
}
