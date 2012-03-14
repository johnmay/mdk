package uk.ac.ebi.chemet.io.observation;

import uk.ac.ebi.interfaces.Observation;

import java.io.IOException;
import java.util.Collection;

/**
 * ObservationInput - 11.03.2012 <br/>
 * <p/>
 * Describes input for multiple observations
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface ObservationInput {

    /**
     * Read the next observation in the input.
     * @param <O>
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public <O extends Observation> O read() throws IOException, ClassNotFoundException;

    public <O extends Observation> O read(Class<O> c) throws IOException, ClassNotFoundException;

    public Class readClass() throws IOException, ClassNotFoundException;

    public Collection<Observation> readCollection() throws IOException, ClassNotFoundException;
    
}
