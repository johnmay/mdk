package uk.ac.ebi.chemet.io.observation;

import uk.ac.ebi.interfaces.Observation;

import java.io.IOException;

/**
 * ObservationReader - 08.03.2012 <br/>
 * <p/>
 * Describes a class that can read observations
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface ObservationReader<O extends Observation> {

    public O readObservation() throws IOException;

}
