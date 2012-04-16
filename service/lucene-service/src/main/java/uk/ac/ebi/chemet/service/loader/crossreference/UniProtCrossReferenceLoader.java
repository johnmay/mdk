package uk.ac.ebi.chemet.service.loader.crossreference;

import org.apache.log4j.Logger;
import uk.ac.ebi.annotation.crossreference.CrossReference;
import uk.ac.ebi.chemet.io.parser.xml.uniprot.UniProtXMLReader;
import uk.ac.ebi.chemet.io.parser.xml.uniprot.marshal.UniProtCrossreferenceMarshal;
import uk.ac.ebi.chemet.io.parser.xml.uniprot.marshal.UniProtIdentifierMarhsal;
import uk.ac.ebi.chemet.service.index.crossreference.UniProtCrossReferenceIndex;
import uk.ac.ebi.chemet.service.loader.AbstractSingleIndexResourceLoader;
import uk.ac.ebi.chemet.service.loader.writer.DefaultCrossReferenceIndexWriter;
import uk.ac.ebi.interfaces.entities.EntityFactory;
import uk.ac.ebi.interfaces.entities.ProteinProduct;
import uk.ac.ebi.resource.IdentifierFactory;
import uk.ac.ebi.service.index.LuceneIndex;
import uk.ac.ebi.service.location.ResourceFileLocation;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

/**
 * Lucene index loader for UniProt cross-reference
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class UniProtCrossReferenceLoader extends AbstractSingleIndexResourceLoader {

    private static final Logger LOGGER = Logger.getLogger(UniProtCrossReferenceLoader.class);
    private EntityFactory     entityFactory;
    private IdentifierFactory identifierFactory;

    public UniProtCrossReferenceLoader(LuceneIndex index,
                                       EntityFactory entityFactory,
                                       IdentifierFactory identifierFactory) {
        super(index);

        // factories need for UniProt XML parser
        this.entityFactory = entityFactory;
        this.identifierFactory = identifierFactory;

        // could provide a default value but the file is > 4G so we should encourage
        // download first
        addRequiredResource("UniProt XML",
                            "XML file from uniprot (SwissProt or TrEMBL)",
                            ResourceFileLocation.class);

    }

    public UniProtCrossReferenceLoader(EntityFactory entityFactory,
                                       IdentifierFactory identifierFactory) {
        this(new UniProtCrossReferenceIndex(), entityFactory, identifierFactory);
    }

    @Override
    public void update() throws IOException {

        ResourceFileLocation             location = getLocation("UniProt XML");
        UniProtXMLReader                 reader = null;
        DefaultCrossReferenceIndexWriter writer = new DefaultCrossReferenceIndexWriter(getIndex());

        try {

            reader = new UniProtXMLReader(location.open(), entityFactory);

            reader.addMarshal(new UniProtIdentifierMarhsal());
            reader.addMarshal(new UniProtCrossreferenceMarshal(identifierFactory));

            while (reader.hasNext() && !isCancelled()){

                ProteinProduct product = reader.next();

                // XXX: we should really use getAnnotationsExtending here to catch
                //      cross-reference subclasses. However at the time of writing
                //      the uniprot xref marshal only uses the base CrossReference class
                //      and checking for subclasses would dramatically reduce the speed
                //      of the loader (unit test checks we catch identifiers that may
                //      be subclasses (i.e. ECNumber) - johnmay
                for(CrossReference xref : product.getAnnotations(CrossReference.class)){
                    writer.write(product.getAccession(), xref.getIdentifier());
                }

            }

        } catch (XMLStreamException ex) {
            System.err.println("Stream exception!");
        }

        writer.close();
        reader.close();
        location.close();

    }
}
