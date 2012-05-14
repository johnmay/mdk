package uk.ac.ebi.mdk.apps.io;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.Version;
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
