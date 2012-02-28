package uk.ac.ebi.chemet.service.loader.writer;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.util.Version;
import uk.ac.ebi.service.index.LuceneIndex;
import uk.ac.ebi.service.query.QueryService;
import uk.ac.ebi.service.query.data.MolecularChargeService;
import uk.ac.ebi.service.query.data.MolecularFormulaService;

import java.io.IOException;

/**
 * DefaultChemicalDataIndexWriter - 27.02.2012 <br/>
 * <p/>
 * Provides simplified writing of chemical data lucene documents,
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class DefaultDataIndexWriter {

    private static final Logger LOGGER = Logger.getLogger(DefaultDataIndexWriter.class);

    private LuceneIndex index;
    private IndexWriter writer;

    /**
     * Create the index writer for the specified index
     *
     * @param index
     *
     * @throws IOException
     */
    public DefaultDataIndexWriter(LuceneIndex index) throws IOException {
        this.index = index;
        this.writer = new IndexWriter(index.getDirectory(),
                                      new IndexWriterConfig(Version.LUCENE_34, index.getAnalyzer()));
    }

    /**
     * Write the identifier, charge and formula string's to the index. Note each field
     * is trimmed before storage
     *
     * @param identifier
     * @param charge
     * @param formula
     *
     * @throws IOException
     */
    public void write(String identifier, String charge, String formula) throws IOException {
        Document doc = new Document();

        doc.add(new Field(QueryService.IDENTIFIER.field(), identifier.trim(), Field.Store.YES, Field.Index.ANALYZED));

        if(formula != null) {
            doc.add(new Field(MolecularFormulaService.MOLECULAR_FORMULA.field(), formula.trim(), Field.Store.YES, Field.Index.ANALYZED));
        }

        // molecular charge
        Double chargeValue = getChargeValue(charge);
        if (chargeValue != Double.NaN)
            doc.add(newChargeField(chargeValue));

        writer.addDocument(doc);
    }

    public DefaultDataIndexWriter() {
        super();    //To change body of overridden methods use File | Settings | File Templates.
    }

    /**
     * Parse the charge value for the given string, if there is no charge
     * pressent this will return Double.NAN
     * @param charge
     * @return
     */
    public static Double getChargeValue(String charge) {

        Double value = Double.NaN;

        if (charge.isEmpty())
            return value;

        try {
            value = Double.parseDouble(charge);
        } catch (NumberFormatException ex) {
            // we're returning NAN for errors
        }

        return value;

    }

    /**
     * Create a new charge field
     * @param value
     * @return
     */
    public Fieldable newChargeField(Double value) {
        NumericField charge = new NumericField(MolecularChargeService.MOLECULAR_CHARGE.field(), Field.Store.YES, false);
        charge.setDoubleValue(value);
        return charge;
    }

    public void close() throws IOException {
        writer.close();
    }


}
