package uk.ac.ebi.mdk.apps.io;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.preference.type.IntegerPreference;
import uk.ac.ebi.caf.utility.version.Version;
import uk.ac.ebi.mdk.domain.DomainPreferences;
import uk.ac.ebi.mdk.domain.entity.DefaultEntityFactory;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.io.*;

import java.io.*;
import java.util.Properties;

/**
 * @author John May
 */
public class ReconstructionIOHelper {

    private static final Logger LOGGER = Logger.getLogger(ReconstructionIOHelper.class);

    public static void write(Reconstruction reconstruction, File file) throws IOException {

        reconstruction.setContainer(file);
        file.mkdir();

        IntegerPreference bufferPref = DomainPreferences.getInstance().getPreference("BUFFER_SIZE");

        reconstruction.getContainer().mkdirs();

        File entities = new File(reconstruction.getContainer(), "entities");
        File annotations = new File(reconstruction.getContainer(), "entity-annotations");
        File observations = new File(reconstruction.getContainer(), "entity-observations");
        File info = new File(reconstruction.getContainer(), "info.properties");

        Version version = IOConstants.VERSION;

        Properties properties = new Properties();

        if (info.exists()) {
            FileInputStream propInput = new FileInputStream(info);
            properties.load(propInput);
            propInput.close();
            String value = properties.getProperty("chemet.version");
            version = value == null ? version : new Version(value);
        }

        properties.put("chemet.version", version.toString());
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

    public static Reconstruction read(File file) throws IOException, ClassNotFoundException {

        File entities = new File(file, "entities");
        File annotations = new File(file, "entity-annotations");
        File observations = new File(file, "entity-observations");
        File info = new File(file, "info.properties");

        Properties properties = new Properties();
        FileInputStream in = new FileInputStream(info);
        properties.load(in);
        String stringVersion = properties.getProperty("chemet.version");
        in.close();

        Version version = stringVersion == null ? IOConstants.VERSION : new Version(stringVersion);

        // open data input stream
        DataInputStream annotationStream = new DataInputStream(new BufferedInputStream(new FileInputStream(annotations), 2048));
        DataInputStream observationStream = new DataInputStream(new BufferedInputStream(new FileInputStream(observations), 2048));
        DataInputStream entityStream = new DataInputStream(new BufferedInputStream(new FileInputStream(entities), 2048));

        EntityFactory factory = DefaultEntityFactory.getInstance();

        ObservationInput observationInput = new ObservationDataInputStream(observationStream, version);
        AnnotationInput annotationInput = new AnnotationDataInputStream(annotationStream, version);
        EntityInput entityInput = new EntityDataInputStream(version, entityStream, factory, annotationInput, observationInput);


        long start = System.currentTimeMillis();
        Reconstruction reconstruction = entityInput.read();
        long end = System.currentTimeMillis();
        LOGGER.info("Loaded project data in " + (end - start) + " (ms)");

        entityStream.close();
        annotationStream.close();
        observationStream.close();

        return reconstruction;

    }


}
