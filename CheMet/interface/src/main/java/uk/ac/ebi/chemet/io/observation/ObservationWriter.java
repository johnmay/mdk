package uk.ac.ebi.chemet.io.observation;

import uk.ac.ebi.interfaces.Observation;

import java.io.IOException;

/**
 * ObservationReader - 08.03.2012 <br/>
 * <p/>
 * Describes a class that can write observations
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface ObservationWriter<O extends Observation> {

    public void write(O observation) throws IOException;

}
