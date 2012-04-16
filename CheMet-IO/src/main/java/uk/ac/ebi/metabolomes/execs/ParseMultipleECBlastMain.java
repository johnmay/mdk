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
//
//package uk.ac.ebi.metabolomes.execs;
//
//import au.com.bytecode.opencsv.CSVWriter;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.FilenameFilter;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Set;
//import org.apache.commons.lang.StringUtils;
//import uk.ac.ebi.metabolomes.core.gene.GeneProductCollection;
//import uk.ac.ebi.metabolomes.core.gene.GeneProteinProduct;
//import uk.ac.ebi.metabolomes.identifier.AbstractIdentifier;
//import uk.ac.ebi.resource.classification.ECNumber;
//import uk.ac.ebi.resource.protein.UniProtIdentifier;
//import uk.ac.ebi.metabolomes.io.flatfile.IntEnzXML;
//import uk.ac.ebi.metabolomes.io.homology.BlastXML;
//import uk.ac.ebi.metabolomes.descriptor.observation.JobParameters;
//import uk.ac.ebi.metabolomes.descriptor.observation.sequence.homology.BlastHit;
//
//
///**
// *
// * @author johnmay
// */
//public class ParseMultipleECBlastMain {
//
//    private static final org.apache.log4j.Logger logger =
//                                                 org.apache.log4j.Logger.getLogger(
//      ParseMultipleECBlastMain.class);
//
//
//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String[] args) throws IOException {
//
//        IntEnzXML iex = IntEnzXML.getLoadedInstance();
//        List upids = iex.getUniprotIdentifiers(new ECNumber("4.2.1.9"));
//        for( Object object : upids ) {
//            System.out.println(object);
//        }
//
//        System.exit(0);
//
//        double evalue = 1E-30D;
//        double positiveThreshold = 0.0D;
//
//        String eString = String.format("%.2e", evalue);
//
//        // output files
//        File analysisRoot =
//             new File("/Users/johnmay/EBI/Project/runspace/EcoCycECAnalysis/analysis/" + eString);
//        if( analysisRoot.exists() ) {
//            analysisRoot.delete();
//        }
//        analysisRoot.mkdir();
//        FileWriter hitFreqWriter = new FileWriter(new File(analysisRoot, "../selfhitfreq.tsv"));
//
//        // make sub directories
//        for( String subsubDir : new String[]{ "0.0", "1.0+", "0.0+", "0.1+", "0.2+", "0.3+",
//                                              "0.4+", "0.5+", "0.6+", "0.7+", "0.8+", "0.9+" } ) {
//            File subFile = new File(analysisRoot, subsubDir);
//            subFile.mkdir();
//        }
//
//
//        // TODO code application logic here
//        File dataRoot = new File("/Users/johnmay/EBI/Project/runspace/EcoCycECAnalysis/split");
//        File[] xmlFiles = dataRoot.listFiles(new FilenameFilter() {
//
//            @Override
//            public boolean accept(File dir, String name) {
//                return name.endsWith("xml");
//            }
//
//
//        });
//
//        GeneProductCollection productCollection = new GeneProductCollection();
//
//        for( File file : xmlFiles ) {
//            logger.info("loading xml file: " + file);
//            BlastXML xml = new BlastXML(file);
//            xml.loadProteinHomologyObservations(productCollection, evalue,
//                                                new JobParameters(file.toString()));
//        }
//
//        logger.info("loaded " + productCollection.numberOfProteinProducts() + " products");
//
//
//
//        GeneProteinProduct[] products = productCollection.getProteinProducts();
//
//        String[] headers = new String[]{
//            "Index",
//            "SwissProtID",
//            "EC Number(s)",
//            "Multiple EC",
//            "Query Length",
//            "Hit Length",
//            "Expected Value",
//            "Bit Score",
//            "Query Start",
//            "Query End",
//            "Hit Start",
//            "Hit End",
//            "Alignment Length",
//            "Positive",
//            "Identity"
//        };
//
//
//        int i = 0;
//        for( GeneProteinProduct geneProteinProduct : products ) {
//            HashMap<ECNumber, Double> resultCount;
//
//            resultCount = new HashMap<ECNumber, Double>();
//            //System.out.println( geneProteinProduct.getIdentifier().toString() );
//
//            List<String[]> rows = new ArrayList<String[]>();
//
//            List<BlastHit> hits = geneProteinProduct.getObservations().getBlastHits();
//            int hitIndex = 1;
//            for( BlastHit blastHit : hits ) {
//                List<AbstractIdentifier> ids = blastHit.getIdentifiers();
//                UniProtIdentifier upid = (UniProtIdentifier) ids.get(1);
//                List<ECNumber> ecs = iex.getECNumbers(upid);
//                Integer[] queryRange = blastHit.getQueryRange();
//                Integer[] hitRange = blastHit.getHitRange();
//
//                if( (int) blastHit.getHitLength() != (int) blastHit.getPositive() ) {
//
//                    for( ECNumber ec : ecs ) {
//                        if( ((double) blastHit.getPositive() /
//                             (double) blastHit.getHitLength() > positiveThreshold) ) {
//
//
//                            if( resultCount.containsKey(ec) ) {
//                                resultCount.put(ec, resultCount.get(ec) + 1);
//                            } else {
//                                resultCount.put(ec, 1D);
//                            }
//
//                            rows.add(new String[]{
//                                  Integer.toString(hitIndex++),
//                                  upid.toString(),
//                                  ec.toString(),
//                                  ecs.size() > 1 ? "1" : "0",
//                                  Integer.toString(geneProteinProduct.getSequence().length()),
//                                  blastHit.getHitLength().toString(),
//                                  Double.toString(blastHit.getBestExpectedValue()),
//                                  Double.toString(blastHit.getBestBitScore()),
//                                  Integer.toString(hitRange[0]),
//                                  Integer.toString(hitRange[1]),
//                                  Integer.toString(queryRange[0]),
//                                  Integer.toString(queryRange[1]),
//                                  Integer.toString(blastHit.getAlignmentLength()),
//                                  Integer.toString(blastHit.getPositive()),
//                                  Integer.toString(blastHit.getIdentity())
//                              });
//                        }
//
//                    }
//                } else {
//                    //logger.info( "removed self hit" );
//                }
//            }
//
//            // parse the EC From the header
//            String idString = geneProteinProduct.getIdentifier().toString();
//            String ecString = idString.substring(idString.indexOf("ec|") + 3);
//            ECNumber assignedECNumber = new ECNumber(ecString);
//            File hitFile = null;
//            if( resultCount.containsKey(assignedECNumber) ) {
//                Double frequency = resultCount.get(assignedECNumber) / (double) rows.size();
//                String folder = String.format("%.1f+", frequency);
//                hitFile = new File(new File(analysisRoot, folder), assignedECNumber.toString() +
//                                                                   ".hits");
//            } else {
//                hitFile = new File(new File(analysisRoot, "0.0"), assignedECNumber.toString() +
//                                                                  ".hits");
//            }
//            CSVWriter csv = new CSVWriter(new FileWriter(hitFile), '\t');
//            csv.writeNext(headers);
//            csv.writeAll(rows);
//            csv.close();
//
//            String flag = "RED";
//            List<ECNumber> ecs = null;
//
//            // decission stuff
//            if( resultCount.size() == 1 ) {
//                ecs = new ArrayList<ECNumber>(resultCount.keySet());
//                if( rows.size() > 5 ) {
//                    flag = "GREEN";
//                } else {
//                    flag = "ORANGE";
//                }
//            } else if( resultCount.size() > 1 ) {
//                if( rows.size() > 5 ) {
//                    ecs = new ArrayList<ECNumber>(resultCount.keySet());
//                    flag = "ORANGE";
//                } else {
//                    flag = "RED";
//                    ecs = new ArrayList<ECNumber>(resultCount.keySet());
//                }
//            }
//            System.out.println(flag.toString() + "\t" + StringUtils.join(ecs, ','));
//
//
//        }
//        hitFreqWriter.close();
//    }
//
//
//    private static Set converge(List<Double> bitScores,
//                                List classes) {
//
//        HashSet unique = new HashSet(classes);
//
//        if( unique.size() == 1 ) {
//            return unique;
//        } else if( bitScores.size() <= 5 ) {
//            return unique;
//        }
//
//        Double avgScore = mean(bitScores);
//        List<Double> newBitScore = new ArrayList<Double>();
//        List newClasses = new ArrayList();
//
//        for( int i = 0 ; i < bitScores.size() ; i++ ) {
//            Double adj = (bitScores.get(i) - avgScore);
//
//            if( adj > 0 ) {
//                newBitScore.add(bitScores.get(i));
//                newClasses.add(classes.get(i));
//            }
//        }
//
//
//        return converge(newBitScore, newClasses);
//    }
//
//
//    private static Double mean(List<Double> values) {
//        Double total = 0d;
//        if( values == null || values.isEmpty() ) {
//            return total;
//        }
//        for( Iterator<Double> it = values.iterator() ; it.hasNext() ; ) {
//            Double value = it.next();
//            total += value;
//        }
//        return total / values.size();
//    }
//
//
//}
//
