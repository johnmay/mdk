/**
 * SDF2MolFiles.java
 *
 * 2011.10.09
 *
 * This file is part of the CheMet library
 * 
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * CheMet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.metabolomes.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * @name    SDF2MolFiles
 * @date    2011.10.09
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   This class takes an SDF file in the form of a Stream and breaks it down into separate mol files. It doesn't
 *          attempt any fancy intepretation, just treats the file as a text file, looking for certain patterns. No CDK
 *          reading is done. Mol files are written as they are seen in the SDF.
 */
public class SDF2MolFiles {

    private static final Logger LOGGER = Logger.getLogger(SDF2MolFiles.class);
    private String destinationDir;
    private InputStream incomingSDFStream;
    private String database;
    private String previousLineToID;
    private String toReplaceInID;
    
    private boolean writeFile;
    
    private FieldExtractor fieldExtractor;

    /**
     * 
     * 
     * @param incomingSDF The stream from where the SDF is going to be read.
     * @param database  The database to which the elements in the SDF belong to. This can be left null.
     * @param destinationDir  The destination directory where the mol files should be left.
     * @param previousLineToID  The line previous to the ID in the mol file annotations. This is required to find the id.
     */
    public SDF2MolFiles(InputStream incomingSDF, String database, String destinationDir, String previousLineToID) {
        this.incomingSDFStream = incomingSDF;
        if(destinationDir==null) {
            writeFile = false;
        } else {
            this.destinationDir = destinationDir;
            writeFile = true;
        }
        this.database = database != null ? database : "";
        this.previousLineToID = previousLineToID;
    }
    /**
     * This method writes the incoming SDF stream into separate MDL Mol files at the desired destination, and using
     * the database to which the ids belong to construct the path. For an individual identifer, the path can be reconstructed
     * using 
     * 
     * @param identifiers
     * @return the number of written files.
     * @throws IOException 
     */
    public Integer splitSDFIntoMolFilesForIDs(List<String> identifiers) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(this.incomingSDFStream));
        String line;
        StringBuffer buffer = new StringBuffer();
        String currentID = null;
        boolean nextLineIsID = false;
        boolean onlySelectedIDs = false;
        int filesWritten = 0;
        if(identifiers!=null && identifiers.size() > 0) {
            onlySelectedIDs=true;
            if(fieldExtractor!=null)
                fieldExtractor.setSelectedIdentifiers(identifiers);
        }
        while ((line = reader.readLine()) != null) {
            if (line.contains("$$$$") && currentID != null) {
                if(fieldExtractor!=null) {
                    fieldExtractor.shiftRecord();
                    if(fieldExtractor.proposeIdentifier())
                        currentID=fieldExtractor.getProposedIdentifier();
                }
                if(onlySelectedIDs && identifiers.contains(currentID)) {
                    writeBufferToFile(currentID, buffer);
                } else if(!onlySelectedIDs){
                    writeBufferToFile(currentID, buffer);
                }
                buffer = new StringBuffer();
                currentID = null;
                filesWritten++;
            } else {
                buffer.append(line).append("\n");
                if(fieldExtractor!=null)
                    fieldExtractor.feedLine(line);
                if (nextLineIsID) {
                    currentID = line.trim();
                    if(toReplaceInID!=null)
                        currentID = currentID.replaceAll(toReplaceInID, "");
                    nextLineIsID = false;
                }
                if (line.contains(previousLineToID)) {
                    nextLineIsID = true;
                }
            }
        }
        reader.close();

        return filesWritten;
    }

    private void writeBufferToFile(String currentID, StringBuffer buffer) throws IOException {
        if(writeFile) {
            FileWriter writer = new FileWriter(new File(buildPathFor(currentID)));
            writer.write(buffer.toString());
            writer.close();
        }
    }
    
    /**
     * Produced the path where one should find the mol file generated for this identifier.
     * 
     * @param identifer
     * @return mol file path for idenfier. 
     */
    public String getPathFor(String identifer) {
        return buildPathFor(identifer);
    }
    
    private String buildPathFor(String identifier) {
        return destinationDir + File.separator + database + "_" + identifier + ".mol";
    }
    
    public void setReplacementStringForID(String toReplaceInID) {
        this.toReplaceInID = toReplaceInID;
    }

    /**
     * @return the fieldExtractor
     */
    public FieldExtractor getFieldExtractor() {
        return fieldExtractor;
    }

    /**
     * Objects implementing the FieldExtractor interface can be associated to this class to obtain data from the SDF
     * that won't be extracted by this class. Different field extractors can be written for different purposes (extract
     * certain fields or do whatever processing is needed) or for different flavours of SDF comments (ChEBI, PubChem).
     * 
     * @param fieldExtractor the fieldExtractor to set
     */
    public void setFieldExtractor(FieldExtractor fieldExtractor) {
        this.fieldExtractor = fieldExtractor;
    }

    /**
     * @param writeFile the writeFile to set
     */
    public void setWriteFile(boolean writeFile) {
        this.writeFile = writeFile;
    }

    
}
