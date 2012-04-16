/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.chemet.io.file;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

/**
 *
 * @author pmoreno
 */
public class MolFileFilter implements FileFilter {

    public boolean accept( File file ) {
        return file.isFile() && file.getName().endsWith( ".mol" );
    }
}
