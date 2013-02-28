/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.mdk.io.text.brenda;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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
