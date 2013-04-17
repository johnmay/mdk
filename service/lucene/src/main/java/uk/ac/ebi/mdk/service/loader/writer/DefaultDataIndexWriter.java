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

package uk.ac.ebi.mdk.service.loader.writer;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.document.NumericField;
import uk.ac.ebi.mdk.service.index.LuceneIndex;
import uk.ac.ebi.mdk.service.query.QueryService;
import uk.ac.ebi.mdk.service.query.data.MolecularChargeService;
import uk.ac.ebi.mdk.service.query.data.MolecularFormulaService;

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
public class DefaultDataIndexWriter extends AbstractIndexWriter{

    /**
     * Create the index writer for the specified index
     *
     * @param index
     *
     * @throws IOException
     */
    public DefaultDataIndexWriter(LuceneIndex index) throws IOException {
       super(index);
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

        doc.add(create(QueryService.IDENTIFIER, identifier.trim()));

        if(formula != null && !formula.isEmpty()) {
            doc.add(create(MolecularFormulaService.MOLECULAR_FORMULA, formula.trim()));
        }

        // molecular charge
        Double chargeValue = getChargeValue(charge);
        if (!chargeValue.equals(Double.NaN)) {
            doc.add(newChargeField(chargeValue));
        }

        add(doc);
    }

     /**
     * Parse the charge value for the given string, if there is no charge
     * pressent this will return Double.NAN
     * @param charge
     * @return
     */
    public static Double getChargeValue(String charge) {

        Double value = Double.NaN;

        if (charge == null || charge.isEmpty())
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


}
