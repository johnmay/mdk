package uk.ac.ebi.chemet.io.observation;

import uk.ac.ebi.interfaces.Observation;

import java.io.IOException;
import java.util.Collection;

/**
 * ObservationOutput - 11.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface ObservationOutput {

    public void write(Observation observation) throws IOException;

    public void writeData(Observation observation) throws IOException;

    public void writeClass(Class c) throws IOException;
    
    public void writeObservations(Collection<Observation> observations) throws IOException;

}
