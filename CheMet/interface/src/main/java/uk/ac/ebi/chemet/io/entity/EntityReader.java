package uk.ac.ebi.chemet.io.entity;

import uk.ac.ebi.interfaces.entities.Entity;
import uk.ac.ebi.interfaces.identifiers.Identifier;

import java.io.IOException;

/**
 * EntityReader - 12.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface EntityReader<E extends Entity> {
    
    public E readEntity() throws IOException, ClassNotFoundException;
    
}
