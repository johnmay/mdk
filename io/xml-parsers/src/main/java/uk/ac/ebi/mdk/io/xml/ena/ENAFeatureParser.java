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
package uk.ac.ebi.mdk.io.xml.ena;

import org.apache.log4j.Logger;
import org.biojava3.core.sequence.ProteinSequence;
import org.biojava3.core.sequence.Strand;
import org.biojava3.core.sequence.template.AbstractSequence;
import org.codehaus.stax2.XMLStreamReader2;
import uk.ac.ebi.mdk.domain.DefaultIdentifierFactory;
import uk.ac.ebi.mdk.domain.annotation.Locus;
import uk.ac.ebi.mdk.domain.annotation.crossreference.CrossReference;
import uk.ac.ebi.mdk.domain.entity.AnnotatedEntity;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.Gene;
import uk.ac.ebi.mdk.domain.entity.ProteinProduct;
import uk.ac.ebi.mdk.domain.entity.RNAProduct;
import uk.ac.ebi.mdk.domain.entity.RibosomalRNA;
import uk.ac.ebi.mdk.domain.entity.TransferRNA;
import uk.ac.ebi.mdk.domain.identifier.DynamicIdentifier;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.identifier.IdentifierFactory;
import uk.ac.ebi.mdk.domain.identifier.IdentifierSet;
import uk.ac.ebi.mdk.domain.identifier.basic.BasicGeneIdentifier;
import uk.ac.ebi.mdk.domain.identifier.basic.BasicProteinIdentifier;
import uk.ac.ebi.mdk.domain.identifier.basic.BasicRNAIdentifier;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ENAFeatureParser - 2011.10.17 <br> Class description
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
public class ENAFeatureParser {

    private static final Logger LOGGER = Logger.getLogger(ENAFeatureParser.class);
    private Set<String> warnings = new HashSet<String>();
    private static DefaultIdentifierFactory IDENTIFIER_FACTORY = DefaultIdentifierFactory
            .getInstance();
    private int start;
    private int end;
    private boolean complement;
    private Type type;
    private IdentifierSet identifiers = new IdentifierSet();
    private AbstractSequence sequence;
    private Map<String, String> qualifiers = new HashMap<String, String>();
    private EntityFactory factory;

    public enum Type {

        GENE("gene"),
        CDS("CDS"),
        SOURCE("source"),
        TRNA("tRNA"),
        RRNA("rRNA"),
        UNKNOWN("");

        private final String label;

        private Type(String label) {
            this.label = label;
        }

        private static final Map<String, Type> map = new HashMap<String, Type>(12);

        static {
            for (Type type : values()) {
                map.put(type.label, type);
            }
        }

        /**
         * Access an enum of the given label - if no match is found UNKNOWN is
         * returned.
         *
         * @param label a label to match
         * @return a type matching the label or unknown
         */
        public static Type of(String label) {
            if (map.containsKey(label))
                return map.get(label);
            return UNKNOWN;
        }


    }

    ;
    private Pattern IDENTITY_MATCH = Pattern.compile("(\\d+)\\.\\.(\\d+)");
    private Pattern COMPLEMENT_MATCH = Pattern.compile("complement\\((\\d+)\\.\\.(\\d+)\\)");
    private Pattern COMPLEMENT_AND_JOIN_MATCH = Pattern.compile("complement\\(join\\((\\d+)\\.\\.(\\d+),(\\d+)\\.\\.(\\d+)\\)\\)");
    private Map<String, Resolver> resolverMap = new HashMap<String, Resolver>();

    {
        resolverMap.put("xref", new CrossReferenceResolver());
        resolverMap.put("qualifier", new QualifierResolver());
    }


    /**
     * Creates a new feature form xmlr
     *
     * @param xmlr
     */
    public ENAFeatureParser(EntityFactory factory, XMLStreamReader2 xmlr) throws XMLStreamException {

        this.factory = factory;

        // get attributes
        String attrName = "";
        for (int i = 0; i < xmlr.getAttributeCount(); i++) {
            attrName = xmlr.getAttributeLocalName(i);
            if (attrName.equals("name")) {
                type = Type.of(xmlr.getAttributeValue(i));
                if (type == Type.UNKNOWN)
                    warnings.add("unknown attribute type: " + xmlr.getAttributeValue(i));
            } else if (attrName.equals("location")) {
                String location = xmlr.getAttributeValue(i);

                // remove scaffold
                if (location.contains(":")) {
                    location = location.substring(location.indexOf(":") + 1);
                }

                Matcher matcher = IDENTITY_MATCH.matcher(location);
                if (matcher.matches()) {
                    start = Integer.parseInt(matcher.group(1));
                    end = Integer.parseInt(matcher.group(2));
                }

                matcher = COMPLEMENT_MATCH.matcher(location);
                if (matcher.matches()) {
                    complement = true;
                    start = Integer.parseInt(matcher.group(1));
                    end = Integer.parseInt(matcher.group(2));
                }

                matcher = COMPLEMENT_AND_JOIN_MATCH.matcher(location);
                if (matcher.matches()) {
                    warnings.add("complement(join(..not yet handled..))");
                }
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
        return type == Type.GENE;
    }

    public boolean isRNA() {
        return isRRNA() || isTRNA();
    }

    public boolean isSource() {
        return type == Type.SOURCE;
    }

    public boolean isRRNA() {
        return type == Type.RRNA;
    }

    public boolean isTRNA() {
        return type == Type.TRNA;
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
        return Integer.parseInt(qualifiers.containsKey("transl_table") ? qualifiers
                .get("transl_table") : "-1");
    }

    public String getTranslation() {
        return qualifiers.containsKey("translation") ? qualifiers.get("translation") : "";
    }

    public Integer getCodonStart() {
        return Integer.parseInt(qualifiers.containsKey("codon_start") ? qualifiers
                .get("codon_start") : "-1");
    }

    public int start(){
        return start;
    }

    public int end(){
        return end;
    }

    /**
     * Returns a Gene, ProteinProduct, TranscriptionRNA of RibsombalRNA
     *
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
            return factory.ofClass(RibosomalRNA.class, BasicRNAIdentifier.nextIdentifier(), getLocusTag(), getProduct());
        } else if (isTRNA()) {
            return factory.ofClass(TransferRNA.class, BasicRNAIdentifier.nextIdentifier(), getLocusTag(), getProduct());
        }
        return null;
    }

    private Gene getGene() {

        Gene gene = factory.ofClass(Gene.class,
                                    BasicGeneIdentifier.nextIdentifier(),
                                    "",
                                    getLocusTag());
        gene.addAnnotation(new Locus(getLocusTag()));
        gene.setStart(start);
        gene.setEnd(end);
        gene.setStrand(complement ? Strand.NEGATIVE : Strand.POSITIVE);

        // we presume there will always be a locus tag
        gene.addAnnotation(new Locus(getLocusTag()));
        if (!getOldLocusTag().isEmpty()) {
            gene.addAnnotation(new Locus(getOldLocusTag()));
        }

        return gene;

    }

    /**
     * CDS only
     */
    public Identifier getProteinIdentifier() {
        return qualifiers.containsKey("protein_id")
                ? new DynamicIdentifier("ENA Protein ID", qualifiers.get("protein_id")) : BasicProteinIdentifier.nextIdentifier();
    }

    public String getProduct() {
        return qualifiers.containsKey("product") ? qualifiers.get("product") : "";
    }

    /**
     * RESOLVERS *
     */
    private interface Resolver {

        public void resolve(XMLStreamReader2 xmlr) throws XMLStreamException;
    }

    private class CrossReferenceResolver implements Resolver {

        public void resolve(XMLStreamReader2 xmlr) throws XMLStreamException {
            String db = xmlr.getAttributeValue(0);
            Identifier identifier = IDENTIFIER_FACTORY.ofSynonym(db, xmlr.getAttributeValue(1));
            if (identifier != IdentifierFactory.EMPTY_IDENTIFIER)
                identifiers.add(identifier);
            else
                warnings.add("Could not resolve identifier for name: " + db);
        }
    }

    public Set<String> getWarnings() {
        return warnings;
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
