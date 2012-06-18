package uk.ac.ebi.mdk.service.loader.crossreference;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.annotation.crossreference.CrossReference;
import uk.ac.ebi.mdk.io.xml.uniprot.UniProtXMLReader;
import uk.ac.ebi.mdk.io.xml.uniprot.marshal.UniProtCrossreferenceMarshal;
import uk.ac.ebi.mdk.io.xml.uniprot.marshal.UniProtIdentifierMarhsal;
import uk.ac.ebi.mdk.service.index.crossreference.UniProtCrossReferenceIndex;
import uk.ac.ebi.mdk.service.loader.AbstractSingleIndexResourceLoader;
import uk.ac.ebi.mdk.service.loader.writer.DefaultCrossReferenceIndexWriter;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.ProteinProduct;
import uk.ac.ebi.mdk.domain.DefaultIdentifierFactory;
import uk.ac.ebi.mdk.service.index.LuceneIndex;
import uk.ac.ebi.mdk.service.location.ResourceFileLocation;

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
    private DefaultIdentifierFactory identifierFactory;

    public UniProtCrossReferenceLoader(LuceneIndex index,
                                       EntityFactory entityFactory,
                                       DefaultIdentifierFactory identifierFactory) {
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
                                       DefaultIdentifierFactory identifierFactory) {
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
