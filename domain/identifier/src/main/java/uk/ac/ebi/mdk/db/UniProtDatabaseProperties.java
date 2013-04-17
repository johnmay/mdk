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

package uk.ac.ebi.mdk.db;

import java.io.File;

/**
 * UniProtDatabaseProperties.java
 * A singleton class for handling the Uniprot file locations
 * of TrEMBL and SwissProt flat-fastafiles
 *
 * @author johnmay
 * @date Mar 21, 2011
 */
public class UniProtDatabaseProperties {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( UniProtDatabaseProperties.class );
    private static final DatabaseProperties dbproperties = DatabaseProperties.getInstance();
    private static UniProtDatabaseProperties instance;
    // statically set keys for access to the properties file
    private static final String UNIPROT_ROOT_KEY = "uniprot.root";
    private static final String SWISSPROT_KEY = "swissprot.fasta";
    private static final String TREMBL_KEY = "trembl.fasta";
    // storate of the files
    private File swissProtFastaFile;
    private File trEMBLFastaFile;
    private File uniProtRoot;

    private UniProtDatabaseProperties() {

        // load the specific filenames
        uniProtRoot = dbproperties.getFile( UNIPROT_ROOT_KEY );
        swissProtFastaFile = dbproperties.getFile( SWISSPROT_KEY );
        trEMBLFastaFile = dbproperties.getFile( TREMBL_KEY );

        logger.debug( "loaded uniprot locations: '" + swissProtFastaFile + "', '" + trEMBLFastaFile + "' and '" + uniProtRoot + "'" );
    }

    public static UniProtDatabaseProperties getInstance() {
        if ( instance == null ) {
            instance = new UniProtDatabaseProperties();
        }
        return instance;
    }

    /**
     * Accessor the root directory of the uniprot directory
     * @return file
     */
    public File getUniProtRoot() {
        return uniProtRoot;
    }

    /**
     * Accessor for the path to the UniProtKB/SwissProt fasta file
     * @return file
     */
    public File getSwissProtFastaFile() {
        return swissProtFastaFile;
    }

    /**
     * Accessor for the path to the UniProtKB/TrEMBL fasta file
     * @return file
     */
    public File getTrEMBLFastaFile() {
        return trEMBLFastaFile;
    }
}
