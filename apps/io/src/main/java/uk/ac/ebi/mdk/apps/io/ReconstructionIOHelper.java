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

package uk.ac.ebi.mdk.apps.io;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.preference.type.IntegerPreference;
import uk.ac.ebi.caf.utility.version.Version;
import uk.ac.ebi.mdk.domain.DomainPreferences;
import uk.ac.ebi.mdk.domain.entity.DefaultEntityFactory;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.io.AnnotationDataInputStream;
import uk.ac.ebi.mdk.io.AnnotationDataOutputStream;
import uk.ac.ebi.mdk.io.AnnotationInput;
import uk.ac.ebi.mdk.io.AnnotationOutput;
import uk.ac.ebi.mdk.io.EntityDataInputStream;
import uk.ac.ebi.mdk.io.EntityDataOutputStream;
import uk.ac.ebi.mdk.io.EntityInput;
import uk.ac.ebi.mdk.io.EntityOutput;
import uk.ac.ebi.mdk.io.IOConstants;
import uk.ac.ebi.mdk.io.ObservationDataInputStream;
import uk.ac.ebi.mdk.io.ObservationDataOutputStream;
import uk.ac.ebi.mdk.io.ObservationInput;
import uk.ac.ebi.mdk.io.ObservationOutput;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

/**
 * @author John May
 */
public class ReconstructionIOHelper {

    private static final Logger LOGGER = Logger.getLogger(ReconstructionIOHelper.class);


    public static boolean matchingUUID(Reconstruction reconstruction, File file) throws
                                                                                 IOException {
        if (!file.getName().endsWith(".mr"))
            throw new IllegalArgumentException("Expected '.mr' folder");

        File info = new File(file, "info.properties");
        if (!info.exists())
            return true;

        Properties properties = new Properties();
        properties.load(new FileReader(info));
        String uuidString = properties.getProperty("uuid");

        // info exists but no UUID -> we indicate it doesn't match
        if (uuidString == null)
            return false;

        UUID uuid = UUID.fromString(uuidString);

        // check whether the UUIDs are equal
        return uuid.equals(reconstruction.uuid());

    }

    /**
     * Store a reconstruction, but back it up first incase something goes wrong.
     * @param reconstruction
     * @param file
     * @throws IOException
     */
    public static void writeSafe(Reconstruction reconstruction, File file) throws IOException {
        String path = file.getPath();
        File backup = new File(path + ".bak");
        if (file.renameTo(backup)) {
            ReconstructionIOHelper.write(reconstruction, new File(path));
            delete(backup);
            System.out.println("[INFO] update successful.");
        } else {
            System.err.println("[ERROR] could not backup existing save.");
        }
    }

    static void delete(File f) throws IOException {
        if (f == null) return;
        if (f.isDirectory()) {
            for (File c : f.listFiles())
                delete(c);
        }
        if (!f.delete())
            throw new FileNotFoundException("Failed to delete file: " + f);
    }
    
    public static void write(Reconstruction reconstruction, File file) throws
                                                                       IOException {

        if (!matchingUUID(reconstruction, file))
            throw new IOException("cannot overwrite another reconstruction, please delete existing reconstruction first");

        reconstruction.setContainer(file);
        file.mkdirs();

        IntegerPreference bufferPref = DomainPreferences.getInstance().getPreference("BUFFER_SIZE");

        reconstruction.getContainer().mkdirs();

        File entities = new File(reconstruction.getContainer(), "entities");
        File annotations = new File(reconstruction.getContainer(), "entity-annotations");
        File observations = new File(reconstruction.getContainer(), "entity-observations");
        File info = new File(reconstruction.getContainer(), "info.properties");

        Version version = IOConstants.CURRENT;

        Properties properties = new Properties();

        if (info.exists()) {
            FileInputStream propInput = new FileInputStream(info);
            properties.load(propInput);
            propInput.close();
            String value = properties.getProperty("chemet.version");
            version = value == null ? version : new Version(value);
        }

        // XXX: update SaveAs also..
        properties.put("chemet.version", version.toString());
        properties.put("uuid", reconstruction.uuid().toString());
        FileWriter writer = new FileWriter(info);
        properties.store(writer, "Project info");
        writer.close();

        DataOutputStream entityDataOut = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(entities), bufferPref.get()));
        DataOutputStream annotationDataOut = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(annotations), bufferPref.get()));
        DataOutputStream observationDataOut = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(observations), bufferPref.get()));


        EntityFactory factory = DefaultEntityFactory.getInstance();

        AnnotationOutput annotationOutput = new AnnotationDataOutputStream(annotationDataOut, version);
        ObservationOutput observationOutput = new ObservationDataOutputStream(observationDataOut, version);
        EntityOutput entityOutput = new EntityDataOutputStream(version,
                                                               entityDataOut,
                                                               factory,
                                                               annotationOutput,
                                                               observationOutput);

        long start = System.currentTimeMillis();
        entityOutput.write(reconstruction);
        long end = System.currentTimeMillis();

        LOGGER.info("Wrote reconstruction in " + (end - start) + " ms");

        entityDataOut.close();
        annotationDataOut.close();
        observationDataOut.close();

    }

    public static Reconstruction read(File file) throws IOException,
                                                        ClassNotFoundException {

        File entities = new File(file, "entities");
        File annotations = new File(file, "entity-annotations");
        File observations = new File(file, "entity-observations");
        File info = new File(file, "info.properties");

        Properties properties = new Properties();
        FileInputStream in = new FileInputStream(info);
        properties.load(in);
        String stringVersion = properties.getProperty("chemet.version");
        in.close();

        Version version = stringVersion == null ? IOConstants.CURRENT
                                                : new Version(stringVersion);

        // open data input stream
        DataInputStream annotationStream = new DataInputStream(new BufferedInputStream(new FileInputStream(annotations), 2048));
        DataInputStream observationStream = new DataInputStream(new BufferedInputStream(new FileInputStream(observations), 2048));
        DataInputStream entityStream = new DataInputStream(new BufferedInputStream(new FileInputStream(entities), 2048));

        EntityFactory factory = DefaultEntityFactory.getInstance();

        ObservationInput observationInput = new ObservationDataInputStream(observationStream, version);
        AnnotationInput annotationInput = new AnnotationDataInputStream(annotationStream, version);
        EntityInput entityInput = new EntityDataInputStream(version, entityStream, factory, annotationInput, observationInput);


        long start = System.currentTimeMillis();
        Reconstruction reconstruction = entityInput.read(null);
        long end = System.currentTimeMillis();
        LOGGER.info("Loaded project data in " + (end - start) + " (ms)");

        entityStream.close();
        annotationStream.close();
        observationStream.close();

        return reconstruction;

    }


}
