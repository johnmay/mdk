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

/**
 * ENAXMLReader.java
 *
 * 2011.10.16
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
package uk.ac.ebi.mdk.io.xml.ena;

import com.google.common.base.Joiner;
import org.apache.log4j.Logger;
import org.biojava3.core.sequence.DNASequence;
import org.biojava3.core.sequence.Strand;
import org.biojava3.core.sequence.template.Sequence;
import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLStreamReader2;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.Gene;
import uk.ac.ebi.mdk.domain.entity.GeneProduct;
import uk.ac.ebi.mdk.domain.entity.RNAProduct;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * ENAXMLReader - 2011.10.16 <br> A class to read ENA XML files into CheMet core
 * objects
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
public class ENAXMLReader {

    private static final Logger LOGGER = Logger.getLogger(ENAXMLReader.class);
    private List<String> genomeSections = new ArrayList<String>();
    private List<Gene> genes = new ArrayList<Gene>(200);
    private Map<String, Gene> geneMap = new HashMap<String, Gene>(); // mapped by locus
    private List<GeneProduct> products = new ArrayList();
    private Set<String> warnings = new HashSet<String>();
    private final Reconstruction reconstruction;

    public ENAXMLReader(EntityFactory factory, InputStream in) throws
                                                               XMLStreamException {

        // used to store associations
        this.reconstruction = factory.newReconstruction();

        XMLInputFactory2 xmlif = (XMLInputFactory2) XMLInputFactory2
                .newInstance();
        xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.TRUE);
        xmlif.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
        xmlif.configureForSpeed();

        XMLStreamReader2 xmlr = (XMLStreamReader2) xmlif
                .createXMLStreamReader(in);

        int event;

        Pattern featurePattern = Pattern.compile("feature");
        Pattern sequenceMatcher = Pattern.compile("sequence");

        while (xmlr.hasNext()) {
            event = xmlr.next();
            switch (event) {
                case XMLEvent.START_DOCUMENT:
                    break;
                case XMLEvent.START_ELEMENT:

                    if (featurePattern.matcher(xmlr.getLocalName()).matches()) {

                        ENAFeatureParser feature = new ENAFeatureParser(factory, xmlr);

                        if (feature.isGene()) {
                            Gene gene = (Gene) feature.getEntity();
                            geneMap.put(feature.getLocusTag(), gene);
                            genes.add(gene);
                            reconstruction.register(gene);
                        } else if (feature.isProduct()) {
                            GeneProduct product = (GeneProduct) feature
                                    .getEntity();
                            Gene gene = geneMap.get(feature.getLocusTag());
                            if (gene != null) {
                                reconstruction.associate(gene, product);
                                gene.setName(feature.geneName());
                            } else {
                                LOGGER.error("No gene found for '" + feature
                                        .getProteinIdentifier() + "' locus tag: '" + feature
                                        .getLocusTag() + "' location " + feature
                                        .start() + "..." + feature.end());
                            }
                            products.add(product);
                            reconstruction.register(product);
                        }

                        warnings.addAll(feature.getWarnings());

                    } else if (sequenceMatcher.matcher(xmlr.getLocalName())
                                              .matches()) {
                        LOGGER.info("parsing genome sequence..");
                        event = xmlr.next();
                        while (event != XMLEvent.END_ELEMENT) {
                            genomeSections.add(xmlr.getText()
                                                   .replaceAll("\n", "")
                                                   .trim());
                            event = xmlr.next();
                        }
                    } else {
                        LOGGER.info("unhandled element: " + xmlr.getName());
                    }


                    break;
            }
        }


        // resolve the RNA product sequences without sequences
        DNASequence genome = new DNASequence(getGenomeString());
        for (GeneProduct product : products) {
            if (product instanceof RNAProduct) {
                Collection<Gene> pGenes = reconstruction.genesOf(product);
                for (Gene gene : pGenes) {

                    Sequence seq = genome.getSubSequence(gene.getStart(), gene
                            .getEnd());

                    seq = gene.getStrand() == Strand.NEGATIVE ? seq.getInverse()
                                                              : seq;

                    if (seq.getLength() > 0) {
                        DNASequence dna = new DNASequence(seq.getSequenceAsString());
                        product.addSequence(dna.getRNASequence());
                    }
                }
            }
        }


    }

    public Set<String> getWarnings() {
        return warnings;
    }

    public List<Map.Entry<Gene, GeneProduct>> associations() {
        return reconstruction.geneAssociations();
    }

    public List<Gene> getGeneMap() {
        return new ArrayList<Gene>(genes);
    }

    public List<GeneProduct> getProducts() {
        return products;
    }

    public String getGenomeString() {
        return Joiner.on("").join(genomeSections);
    }

}
