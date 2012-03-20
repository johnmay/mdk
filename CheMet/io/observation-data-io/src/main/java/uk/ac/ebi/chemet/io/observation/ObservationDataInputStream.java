package uk.ac.ebi.chemet.io.observation;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.Version;
import uk.ac.ebi.caf.utility.version.VersionMap;
import uk.ac.ebi.chemet.io.core.AbstractDataInput;
import uk.ac.ebi.chemet.io.observation.sequence.LocalAlignmentReader;
import uk.ac.ebi.interfaces.Observation;
import uk.ac.ebi.observation.sequence.LocalAlignment;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * ObservationDataInputStream - 08.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class ObservationDataInputStream
        extends AbstractDataInput<ObservationReader>
        implements ObservationInput {

    private static final Logger LOGGER = Logger.getLogger(ObservationDataInputStream.class);

    public Map<Class, VersionMap<ObservationReader>> readers = new HashMap<Class, VersionMap<ObservationReader>>();

    private DataInputStream in;
    private Version v;

    public ObservationDataInputStream(InputStream in, Version v) {
        super(new DataInputStream(in));

        this.in = new DataInputStream(in);
        this.v  = v;

        // default readers
        add(LocalAlignment.class, new LocalAlignmentReader(this.in));

    }

    @Override
    public Observation read() throws IOException, ClassNotFoundException {
        return read(readClass());
    }

    @Override
    public Observation read(Class c) throws IOException, ClassNotFoundException {
        ObservationReader reader = getReader(c);

        short id = readObjectId();

        // if we have an object for that id, return it or read the new object,
        // store and return
        return hasObject(id)
                ? (Observation) get(id)
                : put(id, reader.readObservation());

    }

    public ObservationReader getReader(Class c) {
        return getMarshaller(c, v);
    }


    public void close() throws IOException {
        in.close();
    }

    @Override
    public Collection<Observation> readCollection() throws IOException, ClassNotFoundException {
        
        int n = in.readInt();

        List<Observation> observations = new ArrayList<Observation>(n + 5);

        for(int i = 0 ; i < n ; i++ ){
            observations.add(read());
        }

        return observations;
    }
}
