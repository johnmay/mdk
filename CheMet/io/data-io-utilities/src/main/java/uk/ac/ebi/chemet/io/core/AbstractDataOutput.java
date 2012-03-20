package uk.ac.ebi.chemet.io.core;


import org.apache.commons.lang.mutable.MutableShort;

import java.io.DataOutput;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

/**
 * AbstractDataOutput - 12.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class AbstractDataOutput<M> extends MarshalManager<M> {

    private DataOutput        out;
    private Map<Object,Short> objectIds = new HashMap<Object,Short>();
    private MutableShort      iterator  = new MutableShort(Short.MIN_VALUE + 10);

    public AbstractDataOutput(DataOutput out) {
        this.out = out;
    }

    public void writeClass(Class c) throws IOException{
        if(writeObjectId(c)){
            out.writeUTF(c.getName());
        }
    }

    public DataOutput getDataOutput(){
        return out;
    }

    
    /**
     *
     * @param obj
     * @return whether a new object id was written
     * @throws IOException
     */
    public boolean writeObjectId(Object obj) throws IOException {

        if(objectIds.containsKey(obj)){
            out.writeShort(objectIds.get(obj));
            return false;
        }

        out.writeShort(newObjectId(obj));

        return true;
        
    }
    
    public short newObjectId(Object obj){
        
        if(objectIds.containsKey(obj))
            throw new InvalidParameterException("Object already has id!");
        
        iterator.increment();
        objectIds.put(obj, iterator.toShort());
        return iterator.shortValue();
    }
    
}
