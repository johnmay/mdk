/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.metabolomes.execs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.MDLV2000Writer;
import org.openscience.cdk.io.MDLV2000Writer;
import org.openscience.cdk.io.SDFWriter;
import org.openscience.cdk.io.iterator.IteratingMDLReader;
import org.openscience.cdk.io.iterator.IteratingMDLReader;

/**
 *
 * @author pmoreno
 */
public class SDFToMolFiles {

    private String pathToSDF;
    private String outputDir;
    private IteratingMDLReader reader;

    public void execute() throws IOException {
        File sdfFile = new File(pathToSDF);
        reader = new IteratingMDLReader(
                new FileInputStream(sdfFile), DefaultChemObjectBuilder.getInstance());
        MDLV2000Writer writer;


        int counter=0;
        while (reader.hasNext()) {
            IMolecule molecule = (IMolecule) reader.next();
            counter++;
            writer = new MDLV2000Writer(new FileWriter(outputDir+"/"+(String)molecule.getProperty("ID")+".mol"));
            try {
                writer.write(molecule);
              //  System.out.println("Past writing "+counter);
            } catch(CDKException e) {

            }
            writer.close();
        }
        reader.close();
    }

    /**
     * @return the pathToSDF
     */
    public String getPathToSDF() {
        return pathToSDF;
    }

    /**
     * @param pathToSDF the pathToSDF to set
     */
    public void setPathToSDF(String pathToSDF) {
        this.pathToSDF = pathToSDF;
    }

    /**
     * @return the outputDir
     */
    public String getOutputDir() {
        return outputDir;
    }

    /**
     * @param outputDir the outputDir to set
     */
    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public static void main(String[] args) throws IOException {
        SDFToMolFiles sDFToMolFiles = new SDFToMolFiles();
        sDFToMolFiles.setPathToSDF("/Users/pmoreno/Documents/March2009/HumanMetabolome_hmdb/2.5/mcard_sdf_all_wIds.txt");
        sDFToMolFiles.setOutputDir("/Users/pmoreno/DataSources/HMDB/mol/");

        sDFToMolFiles.execute();
    }


}
