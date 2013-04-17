/*
 * Copyright (C) 2013 Pablo Moreno <pablacious at users.sf.net>
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
package uk.ac.ebi.mdk.tool.inchi;

import com.google.common.io.Files;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import net.sf.jniinchi.INCHI_OPTION;
import org.apache.log4j.Logger;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.MDLV2000Writer;
import uk.ac.ebi.mdk.tool.inchi.InChIMoleculeChecker.InChIMoleculeCheckerResult;

/**
 *
 * @author pmoreno
 */
public class InChIProducerBinary102beta extends AbstractInChIProducer implements InChIProducer {

    private final String path = System.getProperty("user.home") + System.getProperty("file.separator") + ".uk.ac.ebi.metabolomes" + System.getProperty("file.separator") + "bin/";
    private final String shellMol2InChi = "mol2InChI_1.03.sh";
    private String inchiOptions;
    private Logger logger = Logger.getLogger(InChIProducerBinary102beta.class.getName());

    public boolean checkBinaries() {
        File shellScript = new File(path+shellMol2InChi);
        return shellScript.canRead();
    }

    @Override
    public InChIResult calculateInChI(IAtomContainer mol) {
        if(inchiOptions==null)
            inchiOptions="";
        
        InChIMoleculeCheckerResult resCheck = InChIMoleculeChecker.getInstance().checkMolecule(mol);
        if(!resCheck.isInChIFit()) {
            if(resCheck.isGenericAtom())
                logger.trace("Skipping generic molecule");
            else
                logger.trace("Molecule has null bonds, atoms or is emtpy");
            return null;
        }
        File tmpDir = Files.createTempDir();
        if (!tmpDir.mkdir()) {
            logger.error("Cannot create temporary directory");
            return null;
        }
        String tmpMolFile = tmpDir.getAbsolutePath() + File.separator + "fileInChiFromCDKMol";        
        MDLV2000Writer w;
        try {
            w = new MDLV2000Writer(new FileWriter(new File(tmpMolFile)));
            w.writeMolecule(mol);
            w.close();
        } catch (Exception ex) {
            logger.error("Could not write mol file for inchi calculations.", ex);
            return null;
        }
        return getInChIForMolFile(tmpDir, tmpMolFile);


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

    @Override
    public void setInChIOptions(List<INCHI_OPTION> inchiConfigOptions) {
        this.inchiOptions = "";
        for (INCHI_OPTION inchi_option : inchiConfigOptions) {
            if(inchi_option.equals(INCHI_OPTION.SNon)) {
                this.inchiOptions += "-SNon ";
                continue;
            } else if(inchi_option.equals(INCHI_OPTION.DoNotAddH)) {
                this.inchiOptions += "-DoNotAddH ";
                continue;
            } else if(inchi_option.equals(INCHI_OPTION.AuxNone)) {
                this.inchiOptions += "-AuxNone ";
                continue;
            } else if(inchi_option.equals(INCHI_OPTION.RecMet)) {
                this.inchiOptions += "-RecMet ";
                continue;
            } else if(inchi_option.equals(INCHI_OPTION.NoADP)) {
                //don't know how to handle this
                continue;
            } else if(inchi_option.equals(INCHI_OPTION.FixedH)) {
                this.inchiOptions += "-FixedH ";
                continue;
            }

        }
    }

    @Override
    public InChIResult calculateInChI(String mdlMol) {
        File tmpDir = Files.createTempDir();
        if (!tmpDir.mkdir()) {
            logger.error("Cannot create temporary directory");
            return null;
        }
        String tmpMolFile = tmpDir.getAbsolutePath() + File.separator + "fileInChiFromCDKMol";   
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(tmpMolFile));
            writer.write(mdlMol);
            writer.close();
        } catch(IOException e) {
            logger.error("Could not write MDL mol to file ", e);
        } 
        
        return getInChIForMolFile(tmpDir, tmpMolFile);
    }

    private InChIResult getInChIForMolFile(File tmpDir, String tmpFileName) {
        String outputFileName = tmpDir.getAbsolutePath() + File.separator + "output";
        String logFileName = tmpDir.getAbsolutePath() + File.separator + "logFile";
        //System.out.println(path + shellMol2InChi + " " + tmpFileName + " " + outputFileName + " " + logFileName + " " + inchiOptions);        
        if (this.runProcess(path + shellMol2InChi + " " + tmpFileName + " " + outputFileName + " " + logFileName +" "+inchiOptions)) {
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
}
