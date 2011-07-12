///*
// *     This file is part of Metabolic Network Builder
// *
// *     Metabolic Network Builder is free software: you can redistribute it and/or modify
// *     it under the terms of the GNU Lesser General Public License as published by
// *     the Free Software Foundation, either version 3 of the License, or
// *     (at your option) any later version.
// *
// *     Foobar is distributed in the hope that it will be useful,
// *     but WITHOUT ANY WARRANTY; without even the implied warranty of
// *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// *     GNU General Public License for more details.
// *
// *     You should have received a copy of the GNU Lesser General Public License
// *     along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
// */
//package uk.ac.ebi.metabolomes.execs;
//
//import org.biojava3.alignment.NeedlemanWunsch;
//import org.biojava3.alignment.SimpleGapPenalty;
//import org.biojava3.alignment.SubstitutionMatrixHelper;
//import org.biojava3.core.sequence.ProteinSequence;
//import org.biojava3.core.sequence.compound.AminoAcidCompound;
//
///**
// * BioJaveGlobalAlignment.java
// * Class to test the BioJava global alignment algorithm
// *
// * @author johnmay
// * @date May 16, 2011
// */
//public class BioJaveGlobalAlignment {
//
//    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( BioJaveGlobalAlignment.class );
//
//    public static void main( String[] args ) {
//
//        ProteinSequence query = new ProteinSequence( "MTNYTVNTLELGEFITESGETIDHLRLRYEHVGLPGQPLVVVCHALTGNHLTYGTDAQPGWWREIIDGGYIPVHDYQFLTFNVIGSPFGSSSKLNDDNFPEHLTLRDIVRAIELGIQALEFKKINILIGGSLGGMQAMELLYNRQFEVEKAIILAATDKTSSYSRAFNEIARQAIHIGGKEGLSIARQLGFLTYRSSKSYDQRFTPDEVVSYQQHQGDKFKEYFDLNCYLTLLDVLDSHHLDRGRDDVDEVFQSLETKVLTMGFIDDLLYPDDQVRALGERFKYHRHFFVPDNVGHDGFLLNFNDWAPNLYHFLNLKQFRRK" );
//        ProteinSequence metx_geour = new ProteinSequence( "MTVGIVEEKSVTFDTELRLESGRILGPITLAYETYGELNAARSNAILVAHAWTGNAHLAGRYSENEQKPGWWNEIVGPGKLLDTDRYFIICANVIGSCFGSTGPASINPKTGKKYNLSFPVITVRDMVRAQQLLIDHLGIDRLFSVMGGSMGGMQALEWATQFPERIASAIVLATTPRPSAQAISLNAVARWAIFNDPTWKKGEYRKNPKDGLALARGIGHITFLSDESMTAKFGRRFSARDGQFDFFGRFEVERYLSYNGYNFVDRFDANSFLYLAKALDLYDVAWGYESLEEAFAQVAAPIQFFAFSSDWLYPPAQTEEMVTTLEKLGKPVEYHLITSAYGHDAFLLEHETFTPMVRAFLEKVKTAAK" );
//        short gapOpenPenalty = 10;
//        short gapExtensionPenalty = 0;
//
//        NeedlemanWunsch<ProteinSequence , AminoAcidCompound> nw = new NeedlemanWunsch<ProteinSequence , AminoAcidCompound>( query ,
//                                                                                                                            metx_geour ,
//                                                                                                                            new SimpleGapPenalty() ,
//                                                                                                                            SubstitutionMatrixHelper.getBlosum62() );
//        System.out.println( nw.getProfile() );
//        System.out.println( nw.getScore()   );
//
//    }
//}
