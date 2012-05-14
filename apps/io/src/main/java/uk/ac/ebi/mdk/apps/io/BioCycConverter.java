package uk.ac.ebi.mdk.apps.io;

import org.apache.log4j.Logger;

import java.io.File;

/**
 * Converts a BioCyc project to MDK domain objects
 *
 * @author John May
 */
public class BioCycConverter {

    private static final Logger LOGGER = Logger.getLogger(BioCycConverter.class);

    private File root;

    public BioCycConverter(File cycRoot) {
        this.root = cycRoot;
    }



}
