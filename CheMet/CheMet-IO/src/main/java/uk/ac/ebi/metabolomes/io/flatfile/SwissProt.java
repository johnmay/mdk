/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.metabolomes.io.flatfile;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;
import org.biojava3.core.sequence.ProteinSequence;
import org.biojava3.core.sequence.compound.AminoAcidCompound;
import org.biojava3.core.sequence.compound.AminoAcidCompoundSet;
import org.biojava3.core.sequence.io.FastaReader;
import org.biojava3.core.sequence.io.FastaReaderHelper;
import org.biojava3.core.sequence.io.FileProxyProteinSequenceCreator;
import org.biojava3.core.sequence.io.GenericFastaHeaderParser;
import uk.ac.ebi.metabolomes.resource.UniProtDatabaseProperties;

/**
 * SwissProt.java
 * Uses BioJava3 to load the SwissProt sequences, specifically the sequences are mapped to their six-character alphanumeric identifier
 *
 * @author johnmay
 * @date Mar 21, 2011
 */
public class SwissProt {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( SwissProt.class );
    // Sequence file is loaded from the properties by default
    File swissProtSequencesFile = UniProtDatabaseProperties.getInstance().getSwissProtFastaFile();

    /**
     * Default constructor uses UniProtDatabaseProperties to find the SwissProt file
     */
    public SwissProt() {
    }

    /**
     * Loads the specified swissprot file (useful when dealing with a subset)
     * @param swissProtSequenceFile the file to load the sequences from
     */
    public SwissProt( File swissProtSequenceFile ) {
        this.swissProtSequencesFile = swissProtSequenceFile;
    }

    /**
     * Returns a hashmap of the String (UniProt) identifiers to the ProteinSequence (BioJava3)
     * object. This uses proxy loading as the SwissProt file is aprx >500,000 sequences. Proxy loading
     * the headers but stores the positions of the sequence in the stream and then re-reads them when loading sequence
     * @return
     */
    public LinkedHashMap<String , ProteinSequence> getSequenceHashMap() {
        LinkedHashMap<String , ProteinSequence> sequences = null;
        try {
            logger.info( "Starting proxy-loading of fasta sequences using proxy" );
            FastaReader<ProteinSequence , AminoAcidCompound> fastaProxyReader =
                                                             new FastaReader<ProteinSequence , AminoAcidCompound>( swissProtSequencesFile ,
                                                                                                                   new GenericFastaHeaderParser<ProteinSequence , AminoAcidCompound>() ,
                                                                                                                   new FileProxyProteinSequenceCreator( swissProtSequencesFile ,
                                                                                                                                                        AminoAcidCompoundSet.getAminoAcidCompoundSet() ) );
            sequences = fastaProxyReader.process();
            //sequences = FastaReaderHelper.readFastaProteinSequence( swissProtSequencesFile );
            logger.info( "Completed (proxy) loading of " + sequences.size() + " sequences" );
        } catch ( Exception ex ) {
            logger.error( "Could not load SwissProt sequences" , ex );
        }
        return sequences;
    }

    public static void main( String[] args ) {
        SwissProt sp = new SwissProt();
        LinkedHashMap<String , ProteinSequence> seq = sp.getSequenceHashMap();
        Set<Entry<String , ProteinSequence>> entries = seq.entrySet();
        int c = 0;
        logger.info( "Reading sequences" );
        for ( Entry<String , ProteinSequence> e : entries ) {
            if ( ++c > 10 ) {
                break;
            }
            System.out.println( e.getValue().getOriginalHeader() );
            System.out.println( e.getValue().getSequenceAsString() );
        }
        logger.info( "Finished" );
    }
}
