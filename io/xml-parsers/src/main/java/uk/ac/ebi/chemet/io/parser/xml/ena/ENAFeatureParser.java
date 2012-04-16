/**
 * ENAFeatureParser.java
 *
 * 2011.10.17
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
package uk.ac.ebi.chemet.io.parser.xml.ena;

import org.apache.log4j.Logger;
import org.biojava3.core.sequence.ProteinSequence;
import org.biojava3.core.sequence.Strand;
import org.biojava3.core.sequence.template.AbstractSequence;
import org.codehaus.stax2.XMLStreamReader2;
import uk.ac.ebi.annotation.Locus;
import uk.ac.ebi.annotation.crossreference.CrossReference;
import uk.ac.ebi.chemet.resource.IdentifierSet;
import uk.ac.ebi.chemet.resource.base.DynamicIdentifier;
import uk.ac.ebi.chemet.resource.basic.BasicProteinIdentifier;
import uk.ac.ebi.chemet.resource.basic.BasicRNAIdentifier;
import uk.ac.ebi.interfaces.AnnotatedEntity;
import uk.ac.ebi.interfaces.Gene;
import uk.ac.ebi.interfaces.entities.*;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.resource.IdentifierFactory;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 *          ENAFeatureParser - 2011.10.17 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class ENAFeatureParser {

    private static final Logger LOGGER = Logger.getLogger(ENAFeatureParser.class);
    private static IdentifierFactory IDENTIFIER_FACTORY = IdentifierFactory.getInstance();
    private int start;
    private int end;
    private boolean complement;
    private Type type;
    private IdentifierSet identifiers = new IdentifierSet();
    private AbstractSequence sequence;
    private Map<String, String> qualifiers = new HashMap();
    private EntityFactory factory;

    public enum Type {

        gene, CDS, source, tRNA, rRNA
    };
    private Pattern complementMatch = Pattern.compile("complement\\(");
    private Map<String, Resolver> resolverMap = new HashMap();

    {
        resolverMap.put("xref", new CrossReferenceResolver());
        resolverMap.put("qualifier", new QualifierResolver());
    }

    /**
     * Creates a new feature form xmlr
     * @param xmlr
     */
    public ENAFeatureParser(EntityFactory factory, XMLStreamReader2 xmlr) throws XMLStreamException {

        this.factory = factory;

        // get attributes
        String attrName = "";
        for (int i = 0; i < xmlr.getAttributeCount(); i++) {
            attrName = xmlr.getAttributeLocalName(i);
            if (attrName.equals("name")) {
                type = Type.valueOf(xmlr.getAttributeValue(i));
            } else if (attrName.equals("location")) {
                String locationText = xmlr.getAttributeValue(i);

                // remove scaffold
                if (locationText.contains(":")) {
                    locationText = locationText.substring(locationText.indexOf(":") + 1);
                }

                complement = complementMatch.matcher(locationText).find();
                String location = complement ? locationText.substring(11, locationText.length() - 1) : locationText;
                String[] values = location.split("\\.\\.");
                start = Integer.parseInt(values[0]);
                end = Integer.parseInt(values[1]);
            }
        }

        int event;
        while (xmlr.hasNext()) {
            event = xmlr.next();
            switch (event) {
                case XMLEvent.END_ELEMENT:
                    if (xmlr.getLocalName().equals("feature")) {
                        return;
                    }
                    break;
                case XMLEvent.START_ELEMENT:
                    String localName = xmlr.getLocalName();
                    if (resolverMap.containsKey(localName)) {
                        resolverMap.get(localName).resolve(xmlr);
                    }
                    break;
            }
        }

    }

    public boolean isCDS() {
        return type == Type.CDS;
    }

    public boolean isGene() {
        return type == Type.gene;
    }

    public boolean isRNA() {
        return isRNA() || isTRNA();
    }

    public boolean isSource() {
        return type == Type.source;
    }

    public boolean isRRNA() {
        return type == Type.rRNA;
    }

    public boolean isTRNA() {
        return type == Type.tRNA;
    }

    public boolean isProduct() {
        return isCDS() || isRNA();
    }

    public IdentifierSet getCrossReferences() {
        return identifiers;
    }

    public Map<String, String> getQualifiers() {
        return qualifiers;
    }

    public String getLocusTag() {
        return qualifiers.containsKey("locus_tag") ? qualifiers.get("locus_tag") : "";
    }

    public String getOldLocusTag() {
        return qualifiers.containsKey("old_locus_tag") ? qualifiers.get("old_locus_tag") : "";
    }

    public int getTranslationTable() {
        return Integer.parseInt(qualifiers.containsKey("transl_table") ? qualifiers.get("transl_table") : "-1");
    }

    public String getTranslation() {
        return qualifiers.containsKey("translation") ? qualifiers.get("translation") : "";
    }

    public Integer getCodonStart() {
        return Integer.parseInt(qualifiers.containsKey("codon_start") ? qualifiers.get("codon_start") : "-1");
    }

    /**
     * Returns a Gene, ProteinProduct, TranscriptionRNA of RibsombalRNA
     * @return
     */
    public AnnotatedEntity getEntity() {

        if (isGene()) {
            return getGene();
        } else if (isCDS()) {
            return getCodingSequence();
        } else if (isRNA()) {
            return getRNA();
        }

        return null;
    }

    private ProteinProduct getCodingSequence() {

        ProteinProduct cds = factory.ofClass(ProteinProduct.class, getProteinIdentifier(), getLocusTag(), getProduct());

        for (Identifier identifier : identifiers.getIdentifiers()) {
            cds.addAnnotation(new CrossReference(identifier));
        }

        cds.addSequence(new ProteinSequence(getTranslation()));

        return cds;

    }

    private RNAProduct getRNA() {
        if (isRRNA()) {
            return factory.ofClass(RibosomalRNA.class, new BasicRNAIdentifier(), getLocusTag(), getProduct());
        } else if (isTRNA()) {
            return factory.ofClass(TransferRNA.class, new BasicRNAIdentifier(), getLocusTag(), getProduct());
        }
        return null;
    }

    private Gene getGene() {

        Gene gene = factory.ofClass(Gene.class, new BasicRNAIdentifier(),
                                           "",
                                           getLocusTag());
        gene.addAnnotation(new Locus(getLocusTag()));
        gene.setStart(start);
        gene.setStart(end);
        gene.setStrand(complement ? Strand.NEGATIVE : Strand.POSITIVE);

        // we presume there will always be a locus tag
        gene.addAnnotation(new Locus(getLocusTag()));
        if (getOldLocusTag().isEmpty() == false) {
            gene.addAnnotation(new Locus(getOldLocusTag()));
        }

        return gene;

    }

    /**
     * CDS only
     */
    public Identifier getProteinIdentifier() {
        return qualifiers.containsKey("protein_id") ? new DynamicIdentifier("ENA Protein ID", qualifiers.get("protein_id")) : new BasicProteinIdentifier();
    }

    public String getProduct() {
        return qualifiers.containsKey("product") ? qualifiers.get("product") : "";
    }

    /** RESOLVERS **/
    private interface Resolver {

        public void resolve(XMLStreamReader2 xmlr) throws XMLStreamException;
    }

    private class CrossReferenceResolver implements Resolver {

        public void resolve(XMLStreamReader2 xmlr) throws XMLStreamException {
            String db = xmlr.getAttributeValue(0);
            try {
                Identifier identifier = IDENTIFIER_FACTORY.ofSynonym(db);
                identifier.setAccession(xmlr.getAttributeValue(1));
                identifiers.add(identifier);
            } catch (InvalidParameterException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    private class QualifierResolver implements Resolver {

        public void resolve(XMLStreamReader2 xmlr) throws XMLStreamException {

            String name = xmlr.getAttributeValue(0);

            int event;
            while (xmlr.hasNext()) {
                event = xmlr.next();
                switch (event) {
                    case XMLEvent.END_ELEMENT:
                        if (xmlr.getLocalName().equals("qualifier")) {
                            return;
                        }
                        break;
                    case XMLEvent.START_ELEMENT:
                        if (xmlr.getLocalName().equals("value")) {
                            event = xmlr.next();
                            String value = "";
                            while (event != XMLEvent.END_ELEMENT) {
                                String text = xmlr.getText();
                                value += text.replaceAll("\n", "").trim();
                                event = xmlr.next();
                            }
                            qualifiers.put(name, value);
                        }
                        break;
                }

            }
        }
    }
}
