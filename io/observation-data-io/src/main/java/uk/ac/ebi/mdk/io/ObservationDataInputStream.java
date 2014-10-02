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
import uk.ac.ebi.caf.utility.version.VersionMap;
import uk.ac.ebi.mdk.io.observation.LocalAlignmentReader;
import uk.ac.ebi.mdk.domain.observation.Observation;
import uk.ac.ebi.mdk.domain.observation.sequence.LocalAlignment;

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

    public ObservationDataInputStream(InputStream in, Version v) {
        super(new DataInputStream(in), v);

        this.in = new DataInputStream(in);

        // default readers
        add(LocalAlignment.class, new LocalAlignmentReader(this.in));

    }

    @Override
    @SuppressWarnings("unchecked")
    public Observation read() throws IOException, ClassNotFoundException {
        return read(readClass());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Observation read(Class c) throws IOException, ClassNotFoundException {
        ObservationReader reader = getReader(c);

        Integer id = readObjectId();

        // if we have an object for that id, return it or read the new object,
        // store and return
        return hasObject(id)
                ? (Observation) get(id)
                : put(id, reader.readObservation());

    }

    public ObservationReader getReader(Class c) {
        return getMarshaller(c, getVersion());
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
