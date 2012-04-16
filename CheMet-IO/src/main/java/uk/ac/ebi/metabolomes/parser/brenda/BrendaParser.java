/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.metabolomes.parser.brenda;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import uk.ac.ebi.metabolomes.bioObjects.BrendaEntryEnzyme;
import uk.ac.ebi.metabolomes.bioObjects.PolymericReaction;
import uk.ac.ebi.metabolomes.bioObjects.Reaction;
import uk.ac.ebi.metabolomes.bioObjects.TransportReaction;
import uk.ac.ebi.metabolomes.webservices.OntologyLookUpService;
import uk.ac.ebi.metabolomes.webservices.util.CandidateEntry;

/**
 *
 * @author pmoreno
 */
public class BrendaParser {

    private BufferedReader reader;
    private String specie;
    private BrendaEntryEnzyme enzyme;
    private SN_RNLineParser snrnlp;

    public BrendaParser(String path, String specie) throws IOException {
        this.reader = new BufferedReader(new FileReader(path));
        this.specie = specie;

        this.snrnlp = new SN_RNLineParser(reader);
        /*int lineNum=1244343;
        int current=1;
        while(current<lineNum) {
        this.reader.readLine();
        current++;
         *
         * There was a DOS carriage return ^M on line 1246543
        }*/
    }

    private void init() {
    }

    public static void main(String[] args) throws IOException {
        BrendaParser parser = new BrendaParser("/Users/pmoreno/DataSources/brenda/brenda_dl_0210.txt", "Homo sapiens");

        //BrendaMWTContainer bmwtc = new BrendaMWTContainer("/Users/pmoreno/Documents/June2011/brendaParsing/brenda.mwt");
        //BrendaMWTContainer bmwtc = new BrendaMWTContainer("/Users/pmoreno/Documents/June2011/brendaParsing/BrendaTissueOBO_id_name.txt");
        BrendaMWTContainer bmwtc = new BrendaMWTContainer(BrendaParser.class.getResourceAsStream("data/curated_tissue2ontology_dict.txt"));
        bmwtc.load();

        BrendaName2MolContainer bnmc = new BrendaName2MolContainer("/Users/pmoreno/DataSources/brenda/brendaMols/name2BrendaMolID.txt");
                //new BrendaName2MolContainer("/Users/pmoreno/Documents/June2011/brendaParsing/name2BrendaMolID.txt");
        bnmc.load();

        OntologyLookUpService lookUpService = new OntologyLookUpService();

        PrintWriter writerMetabsRE = new PrintWriter("/tmp/metabsRE.txt");
        PrintWriter writerMetabsNSP = new PrintWriter("/tmp/metabsNSP.txt");
        PrintWriter writerTissues = new PrintWriter("/tmp/tissues.txt");

        BrendaEntryEnzyme enzyme = parser.next();
        int conventionalRxns = 0;
        int nspRxns = 0;
        int spRxns = 0;
        int ecNumbers = 0;
        int tissueCount = 0;
        int locationsCount = 0;
        Set<String> tissues = new TreeSet<String>();
        Set<String> locations = new HashSet<String>();
        Set<String> metabolitesRE = new TreeSet<String>();
        Set<String> metabolitesNSP = new HashSet<String>();
        while (enzyme != null) {
            //System.out.println("EC:" + enzyme.getEcNumber());
            ecNumbers++;
            if (enzyme.getSystematicName() != null) {
            //    System.out.println("SN:" + enzyme.getSystematicName());
            }
            if (enzyme.getRecommendedName() != null) {
            //    System.out.println("RN:" + enzyme.getRecommendedName());
            }
            //System.out.println("Proteins with ext db ref:" + enzyme.getProteinLinks().size());
            //System.out.println("RE Rxns:" + enzyme.getReactions().size());
            conventionalRxns += enzyme.getReactions().size();
            for (Reaction reaction : enzyme.getReactions()) {
                if(PolymericReaction.isPolymeric(reaction))
                    System.out.println("\t" + reaction.toString());
            }
            //System.out.println("NSP Rxns:" + enzyme.getNaturalSubsProdRxns().size());
            nspRxns += enzyme.getNaturalSubsProdRxns().size();
            for (Reaction reaction : enzyme.getNaturalSubsProdRxns()) {
                if(PolymericReaction.isPolymeric(reaction))
                    System.out.println("\t" + reaction.toString());
            }
            //System.out.println("SP Rxns:" + enzyme.getSubstrateProductRxns().size());
            spRxns += enzyme.getSubstrateProductRxns().size();
            for (Reaction reaction : enzyme.getSubstrateProductRxns()) {
                if(PolymericReaction.isPolymeric(reaction))
                    System.out.println("\t" + reaction.toString());
            }
            //System.out.println("Citations:" + enzyme.getCitations().size());
            //System.out.println("Tissues:" + enzyme.getTissues().size());
            tissueCount += enzyme.getTissues().size();
            tissues.addAll(enzyme.getTissues());
            for (String tissue : enzyme.getTissues()) {
              //  System.out.println("\t" + tissue);
            }
            //System.out.println("Locations:" + enzyme.getLocations().size());
            locationsCount += enzyme.getLocations().size();
            locations.addAll(enzyme.getLocations());
            for (String location : enzyme.getLocations()) {
              //  System.out.println("\t" + location);
            }
            metabolitesNSP.addAll(enzyme.getMetabolitesFromNSPRxns());
            metabolitesRE.addAll(enzyme.getMetabolitesFromRERxns());
            System.out.println("");
            enzyme = parser.next();
        }

        System.out.println("Not found tissues:");

        BufferedWriter approved = new BufferedWriter(new FileWriter("/Users/pmoreno/Documents/June2011/brendaParsing/Approved_Mult_OLS_id_name.xls"));
        BufferedWriter toApprove = new BufferedWriter(new FileWriter("/Users/pmoreno/Documents/June2011/brendaParsing/ToApprove_Mult_OLS_id_name.xls"));
        int foundInMwt = 0;
        int foundInMwtOnt = 0;
        int foundInEFO = 0;
        int foundInOtherOntology = 0;
        for (String tissue : tissues) {
            if (bmwtc.getIDsForName(tissue) != null) {
                for (String candTissue : bmwtc.getIDsForName(tissue)) {
                    approved.write(bmwtc.getIDsForName(tissue)+"\t"+tissue+"\t***\t***\tSourceOntology\n");
                }
                foundInMwt++;
                if(foundInMwt % 50 == 0)
                    System.out.println("Approved:"+foundInMwt);
            } else {
                //System.out.println("\t|" + tissue + "|");
                try {
                    List<CandidateEntry> olsRes = lookUpService.getRankedCandidates(tissue, "BTO", 5);
                    if (olsRes != null && olsRes.size() > 0) {
                        foundInMwtOnt++;
                        for (CandidateEntry ce : olsRes) {
                            if(ce.getDistance()==0) {
                                approved.write(ce.getId()+"\t"+tissue+"\t***\t0\tOLS_dist\n");
                                //System.out.println("\t\t***\tFound 0 distance hit");
                                break;
                            }
                            toApprove.write(ce.getId()+"\t"+tissue+"\t"+ce.getDesc()+"\t"+ce.getDistance()+"\tOLS_dist\n");
                        }
                    } else {
                        olsRes = lookUpService.getRankedCandidates(tissue, "EFO", 5);
                        if (olsRes != null && olsRes.size() > 0) {
                            foundInEFO++;
                            for (CandidateEntry ce : olsRes) {
                                if(ce.getDistance()==0) {
                                    approved.write(ce.getId()+"\t"+tissue+"\t***\t0\tOLS_dist\n");
                                    System.out.println("\t\t***\tFound 0 distance hit");
                                    break;
                                }
                                toApprove.write(ce.getId()+"\t"+tissue+"\t"+ce.getDesc()+"\t"+ce.getDistance()+"\tOLS_dist\n");
                            }
                        } else {
                            olsRes = lookUpService.getRankedCandidates(tissue, 10);
                            if (olsRes != null && olsRes.size() > 0) {
                                foundInOtherOntology++;
                                for (CandidateEntry ce : olsRes) {
                                    if(ce.getDistance()==0) {
                                        approved.write(ce.getId()+"\t"+tissue+"\t"+"\t***\t0\tOLS_dist\n");
                                        System.out.println("\t\t***\tFound 0 distance hit");
                                        break;
                                }
                                    toApprove.write(ce.getId()+"\t"+tissue+"\t"+ce.getDesc()+"\t"+ce.getDistance()+"\tOLS_dist\n");
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
        }

        int foundInMolNamesNSP = 0;


        for (String metabNSP : metabolitesNSP) {
            String protOrNot="SM";
            if(ProteinSmallMoleculeDecider.isProteinName(metabNSP)) {
                protOrNot="PT";
            }
            if (bnmc.getMolID(metabNSP) != null) {
                foundInMolNamesNSP++;
                writerMetabsNSP.println(metabNSP+"\t"+bnmc.getMolID(metabNSP)+"\t"+protOrNot);
            }
            else
                writerMetabsNSP.println(metabNSP+"\t***"+"\t"+protOrNot);
        }

        int foundInMolNames = 0;
        int metabsFoundInMolNames = 0;
        System.out.println("Metabs RE not found:");


        for (String metabRE : metabolitesRE) {
            String protOrNot="SM";
            if(ProteinSmallMoleculeDecider.isProteinName(metabRE)) {
                protOrNot="PT";
            }
            if (bnmc.getMolID(metabRE) != null) {
                if(protOrNot.equals("SM"))
                    metabsFoundInMolNames++;
                foundInMolNames++;
                writerMetabsRE.println(metabRE+"\t"+bnmc.getMolID(metabRE)+"\t"+protOrNot);
            } else {
                if(protOrNot.equals("SM"))
                    System.out.println("\t" + metabRE.split(" ").length + "\t|" + metabRE + "|");
                writerMetabsRE.println(metabRE+"\t***"+"\t"+protOrNot);
            }
        }

        System.out.println("EC numbers:" + ecNumbers);
        System.out.println("Conventional rxn:" + conventionalRxns);
        System.out.println("NSP Rxns:" + nspRxns);
        System.out.println("SP Rxns:" + spRxns);
        System.out.println("RE Participants:" + metabolitesRE.size());
        System.out.println("RE Participants With Struct:" + foundInMolNames);
        System.out.println("RE Metabolites With Struct:" + metabsFoundInMolNames);
        System.out.println("NSP metabolites:" + metabolitesNSP.size());
        System.out.println("NSP With Struct:" + foundInMolNamesNSP);
        System.out.println("Tissues:" + tissueCount);
        System.out.println("Diff Tissues:" + tissues.size());
        System.out.println("Diff Tissues found Brenda Tissue Ontology:" + foundInMwt);
        System.out.println("Diff Tissues found Brenda Tissue Ontology:" + foundInMwtOnt);
        System.out.println("Diff Tissues found EFO:" + foundInEFO);
        System.out.println("Diff Tissues found in other ontology:" + foundInOtherOntology);
        System.out.println("Locations:" + locationsCount);
        System.out.println("Diff Locations:" + locations.size());

        approved.close();
        toApprove.close();

        writerMetabsNSP.close();
        writerMetabsRE.close();
    }

    public BrendaEntryEnzyme next() throws IOException {
        String line = this.reader.readLine();


        boolean foundSpecie = false;
        //List<String> numIdentifiersOfProts = new ArrayList<String>();


        while (line != null) {

            if (line.startsWith("ID\t")) {
                this.enzyme = new BrendaEntryEnzyme();


                this.enzyme.addEcNumber(line.split("\t")[1]);
                line = this.reader.readLine();


            } else if (line.startsWith("PR\t") && line.contains(this.specie)) {
                foundSpecie = true;
                PRLineParser prlp = new PRLineParser(reader, specie);
                line = prlp.parse(line);
                this.enzyme.addProtDummyID(prlp.getDummyProteinEntryIdentifier());

                this.enzyme.addCitationList(prlp.getCitations());
                this.enzyme.addCitationKeysForProtein(prlp.getDummyProteinEntryIdentifier(),prlp.getCitations());
                
                //numIdentifiersOfProts.add(prlp.getDummyProteinEntryIdentifier());

                // This is probably protein specific
                for (String extID : prlp.getId2DbRef().keySet()) {
                    this.enzyme.addProteinCrossRef(prlp.getDummyProteinEntryIdentifier(),extID, prlp.getId2DbRef().get(extID));
                }
            } else if (line.startsWith("RE\t")) {
                RELineParser reactionParser = new RELineParser(reader);
                line = reactionParser.parse(line);

                Reaction rxn = null;
                if(TransportReaction.isATransportReaction(reactionParser.getRxn())) {
                    rxn = new TransportReaction(reactionParser.getRxn());
                } else if(PolymericReaction.isPolymeric(reactionParser.getRxn())) {
                    rxn = new PolymericReaction(reactionParser.getRxn());
                } else
                    rxn = reactionParser.getRxn();
                this.enzyme.addReaction(rxn);


                while (line.startsWith("\t")) {
                    line = this.reader.readLine();


                }
            } else if (line.startsWith("ST\t")) {
                STLineParser sourceTissueParser = new STLineParser(reader, this.enzyme.getProtDummyIDs());
                line = sourceTissueParser.parse(line);

                for (String dummyId : sourceTissueParser.getFoundDummyIdentifersOfProteins()) {
                    this.enzyme.addSourceTissue(sourceTissueParser.getTissue(),dummyId);
                }
                


            } else if (line.startsWith("LO\t")) {
                LOLineParser lolp = new LOLineParser(reader, this.enzyme.getProtDummyIDs());
                line = lolp.parse(line);

                for (String dummyId : lolp.getFoundDummyIdentifersOfProteins()) {
                    this.enzyme.addLocalization(lolp.getLocation(),dummyId);
                }
                


            } else if (line.startsWith("NSP\t")) {
                NSPLineParser nsplp = new NSPLineParser(reader, this.enzyme.getProtDummyIDs());
                line = nsplp.parseSelect(line);

                Reaction rxn = null;
                if(TransportReaction.isATransportReaction(nsplp.getRxn())) {
                    rxn = new TransportReaction(nsplp.getRxn());
                } else if(PolymericReaction.isPolymeric(nsplp.getRxn())) {
                    rxn = new PolymericReaction(nsplp.getRxn());
                } else
                    rxn = nsplp.getRxn();

                if (nsplp.getRxn().getMetabolites().size() > 0) {
                    //this.enzyme.addNatSubsProdRxn(nsplp.getRxn());
                    for (String dummyProtId : nsplp.getFoundDummyIdentifersOfProteins()) {
                        this.enzyme.addNatSubsProdRxn(rxn,dummyProtId);
                    }

                }
                while (line.startsWith("\t")) {
                    line = this.reader.readLine();


                }
            } else if (line.startsWith("SP\t")) {
                SPLineParser splp = new SPLineParser(reader, this.enzyme.getProtDummyIDs());
                line = splp.parseSelect(line);


                if (splp.getRxn().getMetabolites().size() > 0) {
                    this.enzyme.addSubstrateProductRxn(splp.getRxn());
                }
                while (line.startsWith("\t")) {
                    line = this.reader.readLine();


                }
            } else if (line.startsWith("RN\t") || line.startsWith("SN\t")) {
                line = this.snrnlp.parse(line);
                // To avoid the problem with the RENATURED RD


                if (this.snrnlp.getRecommendedName() != null && this.enzyme.getRecommendedName() == null) {
                    this.enzyme.setRecommendedName(this.snrnlp.getRecommendedName());


                } else if (this.snrnlp.getSystematicName() != null) {
                    this.enzyme.setSystematicName(this.snrnlp.getSystematicName());


                }
                this.snrnlp.reset();


            } else if (line.startsWith("RF\t")) {
                RFLineParser rflp = new RFLineParser(reader);
                String key = rflp.getCitationNumber(line);


                if (this.enzyme.citationKeyExists(key)) {
                    line = rflp.parse(line);
                    Integer pubmedId;


                    try {
                        pubmedId = Integer.parseInt(rflp.getPubmedId());


                        this.enzyme.setCitationPubMedId(key, pubmedId);


                    } catch (NumberFormatException e) {
                    }
                    this.enzyme.setCitationTitle(key, rflp.getTitle());


                } else {
                    line = reader.readLine();


                }
            } else if (line.startsWith("///")) {
                if (foundSpecie) {
                    foundSpecie = false;


                    return this.enzyme;


                } else {
                    this.enzyme = new BrendaEntryEnzyme();
                    line = this.reader.readLine();


                }
            } else {
                line = this.reader.readLine();


            }
        }

        return null;

    }
}
