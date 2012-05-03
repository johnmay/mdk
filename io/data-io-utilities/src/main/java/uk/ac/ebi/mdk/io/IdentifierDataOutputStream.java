package uk.ac.ebi.mdk.io;

import uk.ac.ebi.mdk.io.identifier.BasicIdentifierWriter;
import uk.ac.ebi.mdk.io.identifier.DynamicIdentifierWriter;
import uk.ac.ebi.mdk.io.identifier.TaxonomyWriter;
import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.Version;
import uk.ac.ebi.chemet.resource.base.DynamicIdentifier;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.resource.organism.Taxonomy;

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
        extends AbstractDataOutput<IdentifierWriter>
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
    public void writeData(Identifier identifier) throws IOException {
        Class c = identifier.getClass();
        getWriter(c).write(identifier);
    }

    @Override
    public void write(Identifier identifier) throws IOException {
        Class c = identifier.getClass();
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


