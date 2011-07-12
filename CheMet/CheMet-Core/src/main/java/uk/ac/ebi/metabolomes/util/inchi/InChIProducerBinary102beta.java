/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.metabolomes.util.inchi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.MDLV2000Writer;

/**
 *
 * @author pmoreno
 */
public class InChIProducerBinary102beta extends InChIProducer {

    private final String path = System.getProperty("user.home") + System.getProperty("file.separator") + ".uk.ac.ebi.metabolomes" + System.getProperty("file.separator") + "bin/";
    private final String tmp = System.getProperty("user.home") + System.getProperty("file.separator") + ".uk.ac.ebi.metabolomes" + System.getProperty("file.separator") + "tmp/";
    private final String shellMol2InChi = "mol2InChI.sh";
    private Logger logger = Logger.getLogger(InChIProducerBinary102beta.class.getName());

    public boolean checkBinaries() {
        File shellScript = new File(path+shellMol2InChi);
        return shellScript.canRead();
    }

    @Override
    public InChIResult calculateInChI(IAtomContainer mol) {
        if (!checkMoleculeForInChI(mol)) {
            logger.warn("Molecule does not pass check for inchi");
            return null;
        }
        File tmpDir = new File(tmp + File.separator + "dir" + Math.round(1000 * Math.random()));
        if (!tmpDir.mkdir()) {
            logger.error("Cannot create temporary directory");
            return null;
        }
        String tmpFileName = tmpDir.getAbsolutePath() + File.separator + "fileInChiFromCDKMol";
        String outputFileName = tmpDir.getAbsolutePath() + File.separator + "output";
        String logFileName = tmpDir.getAbsolutePath() + File.separator + "logFile";
        MDLV2000Writer w;
        try {
            w = new MDLV2000Writer(new FileWriter(new File(tmpFileName)));
            w.writeMolecule(mol);
            w.close();
        } catch (Exception ex) {
            logger.error("Could not write mol file for inchi calculations.", ex);
            return null;
        }
        System.out.println(path + shellMol2InChi + " " + tmpFileName + " " + outputFileName + " " + logFileName);
        if (this.runProcess(path + shellMol2InChi + " " + tmpFileName + " " + outputFileName + " " + logFileName)) {
            String line = null;
            BufferedReader r;
            try {
                r = new BufferedReader(new FileReader(outputFileName));
                line = r.readLine();
                line += "\t"+r.readLine();
                r.close();
            } catch (IOException ex) {
                logger.error("Problems reading binary result file", ex);
                return null;
            }
            String[] tokens = line.split("\t");
            if (tokens.length < 4) {
                return null;
            }

            InChIResult res = new InChIResult();
            res.setInchi(tokens[1]);
            res.setAuxInfo(tokens[2]);
            res.setInchiKey(tokens[3]);
            deleteTmpDirAndFiles(tmpDir);
            return res;
        } else {
            return null;
        }

        
    }

    private boolean runProcess(String command) {
        Runtime rt = Runtime.getRuntime();
        Process p = null;
        try {
            p = rt.exec(command);
        } catch (IOException ex) {
            logger.error("Could not execute process running inchi binary.", ex);
            return false;
        }
        try {
            p.waitFor();
        } catch (InterruptedException ex) {
            logger.error("Process running inchi binary failed:", ex);
            return false;
        }
        return true;
    }

    private boolean deleteTmpDirAndFiles(File tmpDir) {
        File[] files = tmpDir.listFiles();
        boolean ans = true;
        for (File file : files) {
            ans = ans && file.delete();
        }
        return ans && tmpDir.delete();
    }
}
