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

import org.apache.commons.lang.mutable.MutableByte;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.service.index.LuceneIndex;
import uk.ac.ebi.mdk.service.query.QueryService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static uk.ac.ebi.mdk.service.query.CrossReferenceService.CLASS_ID;
import static uk.ac.ebi.mdk.service.query.CrossReferenceService.CLASS_NAME;
import static uk.ac.ebi.mdk.service.query.CrossReferenceService.DATABASE_ACCESSION;
import static uk.ac.ebi.mdk.service.query.CrossReferenceService.DATABASE_IDENTIFIER_INDEX;

/**
 * Unified writing of cross-references to a lucene-index
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class DefaultCrossReferenceIndexWriter extends AbstractIndexWriter {

    private MutableByte      ticker = new MutableByte(-128);
    private Map<Class, Byte> map    = new HashMap<Class, Byte>();


    public DefaultCrossReferenceIndexWriter(LuceneIndex index) throws IOException {
        super(index);
    }


    public void write(String accession, Identifier xref) throws IOException {

        if (xref == null)
            return;

        Document document = new Document();

        document.add(create(QueryService.IDENTIFIER, accession));
        document.add(create(DATABASE_IDENTIFIER_INDEX,
                            getIndex(xref).toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        document.add(create(DATABASE_ACCESSION,
                            xref.getAccession()));

        add(document);

    }


    public Byte getIndex(Identifier identifier) throws IOException {

        Class c = identifier.getClass();

        if (map.containsKey(c)) {
            return map.get(c);
        }

        return newIdentifierDocument(c);

    }


    public Byte newIdentifierDocument(Class c) throws IOException {

        Byte id = ticker.byteValue();
        map.put(c, id);
        ticker.increment();

        Document document = new Document();

        document.add(create(CLASS_NAME,
                            c.getName(),
                            Field.Store.YES,
                            Field.Index.NOT_ANALYZED));
        document.add(create(CLASS_ID,
                            id.toString(),
                            Field.Store.YES,
                            Field.Index.NOT_ANALYZED));

        add(document);

        return id;
    }


}
