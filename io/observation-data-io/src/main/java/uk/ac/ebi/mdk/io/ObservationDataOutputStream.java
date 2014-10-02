/*
 * Copyright (c) 2013. EMBL, European Bioinformatics Institute
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.ebi.mdk.io;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.Version;
import uk.ac.ebi.mdk.io.observation.LocalAlignmentWriter;
import uk.ac.ebi.mdk.domain.observation.Observation;
import uk.ac.ebi.mdk.domain.observation.sequence.LocalAlignment;

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

        super(out, v);

        this.out = out;

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
    @SuppressWarnings("unchecked")
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
        return getMarshaller(c, getVersion());
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
