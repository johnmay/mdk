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

package uk.ac.ebi.mdk.io.text.brenda;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.FileReader;

/**
 * The BRENDA parser was built for the text file that can be downloaded from BRENDA at
 * http://www.brenda-enzymes.info/brenda_download/
 * after registration.
 *
 * Once initialized with an inputstream or a path to a file, and a species (as it is written in the BRENDA file), enzyme
 * entries for the organism are obtained iteratively through the {@link #next()} method. From the
 * {@link BrendaEntryEnzyme} object, small molecules, reactions, enzyme cross references, and tissues/cell types
 * associations can be obtained (as free text).
 * The reactions are obtained from the NSP (Natural Substrate Product) type of reaction record.
 *
 * This parser's output can be used with the BRENDA Tissue Ontology (BTO) to resolve free text tissues/cell types into
 * an identifier in the BTO.
 *
 * @author pmoreno
 */
public class BrendaParser {

    private BufferedReader reader;
    private String specie;
    private BrendaEntryEnzyme enzyme;
    private SN_RNLineParser snrnlp;

    /**
     * Constructor which relies on an inputstream providing the BRENDA Download file. The provided species name should
     * be written as it is written in the "PR" lines of the BRENDA Download file.
     *
     * @param dataStream providing data from a BRENDA Download file.
     * @param species the organism name as written in the BRENDA Download file.
     */
    public BrendaParser(InputStream dataStream, String species) {
        this.reader = new BufferedReader(new InputStreamReader(dataStream));
        this.specie = species;

        this.snrnlp = new SN_RNLineParser(reader);
    }

    /**
     * Constructor which relies on an file path pointing to the BRENDA Download file. The provided species name should
     * be written as it is written in the "PR" lines of the BRENDA Download file.
     *
     * @param path to a BRENDA Download file.
     * @param species the organism name as written in the BRENDA Download file.
     * @throws IOException
     */
    public BrendaParser(String path, String species) throws IOException {
        this.reader = new BufferedReader(new FileReader(path));
        this.specie = species;

        this.snrnlp = new SN_RNLineParser(reader);
    }


    /**
     * Moves the parser one complete Enzyme record forward (the BRENDA Download file is the collection of all the enzyme
     * records in the database, which spans all the organisms at once), and retrieves the next enzyme record, from where
     * other types of data can be retrieved.
     *
     * @return a {@link BrendaEntryEnzyme} containing enzyme, small molecules, reactions, tissues, and other elements.
     * @throws IOException
     */
    public BrendaEntryEnzyme next() throws IOException {
        String line = this.reader.readLine();


        boolean foundSpecie = false;


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

    public void close() throws IOException {
        this.reader.close();
    }
}
