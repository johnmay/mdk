package uk.ac.ebi.chemet.io.observation;

import org.apache.log4j.Logger;
import uk.ac.ebi.interfaces.Observation;

/**
 * ObservationReader - 08.03.2012 <br/>
 * <p/>
 * Describes a class that can read observations
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface ObservationReader {

    public Observation readObservation();

}
