package uk.ac.ebi.chemet.io.observation;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.Version;
import uk.ac.ebi.chemet.io.core.AbstractDataOutput;
import uk.ac.ebi.chemet.io.observation.sequence.LocalAlignmentWriter;
import uk.ac.ebi.interfaces.Observation;
import uk.ac.ebi.observation.sequence.LocalAlignment;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

/**
 * ObservationDataInputStream - 08.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class ObservationDataOutputStream
        extends AbstractDataOutput<ObservationWriter>
        implements ObservationOutput {

    private static final Logger LOGGER = Logger.getLogger(ObservationDataOutputStream.class);

    private DataOutputStream out;
    private Version v;

    /**
     * Create a new observation input stream for the specified
     * version.
     *
     * @param out stream to write too
     * @param v   version
     */
    public ObservationDataOutputStream(OutputStream out, Version v) {
        this(new DataOutputStream(out), v);
    }

    public ObservationDataOutputStream(DataOutputStream out, Version v) {

        super(out);

        this.out = out;
        this.v   = v;

        // default writers
        add(LocalAlignment.class, new LocalAlignmentWriter(this.out));
    }

    /**
     * Write the class and it's object data to the output stream.
     *
     * @param observation
     *
     * @throws IOException
     */
    public void write(Observation observation) throws IOException {

        Class c = observation.getClass();
        writeClass(c);
        writeData(observation);

    }

    /**
     * Write the object data for the specified observation to the
     * output stream. The written observation should be read with
     * {@see ObservationDataInputStream.read(Class)}
     *
     * @param observation
     *
     * @throws IOException
     */
    public void writeData(Observation observation) throws IOException {

        if (writeObjectId(observation)) {
            ObservationWriter writer = getWriter(observation.getClass());
            writer.write(observation);
        }

    }


    @Override
    public void writeObservations(Collection<Observation> observations) throws IOException {

        out.writeInt(observations.size());

        for(Observation observation : observations) {
            write(observation);
        }

    }

    /**
     * Access the writer for the provided class. This method will
     * fetch a writer for the appropriate version the output stream
     * was initialised with.
     *
     * @param c class to get the writer for
     *
     * @return
     */
    public ObservationWriter getWriter(Class c) {
        return getMarshaller(c, v);
    }


    /**
     * Close the output stream
     *
     * @throws IOException low-level io error
     */
    public void close() throws IOException {
        out.close();
    }

}
