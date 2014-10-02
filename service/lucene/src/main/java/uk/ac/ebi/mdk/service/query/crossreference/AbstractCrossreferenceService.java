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

package uk.ac.ebi.mdk.service.query.crossreference;

import org.apache.log4j.Logger;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import uk.ac.ebi.mdk.service.query.AbstractLuceneService;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.DefaultIdentifierFactory;
import uk.ac.ebi.mdk.service.index.LuceneIndex;
import uk.ac.ebi.mdk.service.query.CrossReferenceService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public abstract class AbstractCrossreferenceService<I extends Identifier>
        extends AbstractLuceneService<I>
        implements CrossReferenceService<I> {

    private static final Logger LOGGER = Logger.getLogger(AbstractCrossreferenceService.class);

    private Map<String, Class> indexMap = new HashMap<String, Class>();


    public AbstractCrossreferenceService(LuceneIndex index) {
        super(index);
    }

    /**
     * @inheritDoc
     */
    @Override
    @SuppressWarnings("unchecked")
    public Collection<? extends Identifier> getCrossReferences(I identifier) {

        Query query = construct(identifier.getAccession(), IDENTIFIER);

        Collection<Identifier> crossreferences = new ArrayList<Identifier>();

        TopScoreDocCollector collector = TopScoreDocCollector.create(5, true);
        try {

            getSearcher().search(query, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;

            for (ScoreDoc document : hits) {
                Class c = getIdentifierClass(value(document, DATABASE_IDENTIFIER_INDEX.field()));
                String accession = value(document, DATABASE_ACCESSION.field());
                crossreferences.add(DefaultIdentifierFactory.getInstance().ofClass(c, accession));
            }
        } catch (IOException ex) {
            LOGGER.error("IO Exception occurred on service: " + ex.getMessage());
        }

        return crossreferences;
    }


    /**
     * @inheritDoc
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T extends Identifier> Collection<T> getCrossReferences(I identifier, Class<T> filter) {

        // method could be improved by searching for the specified identifier index but this current
        // implementation will preserve identifier inheritance
        Collection<? extends Identifier> crossreferences = getCrossReferences(identifier);
        Collection<T> subset = new ArrayList<T>();

        for (Identifier crossreference : crossreferences) {
            if (filter.isInstance(crossreference)) {
                subset.add((T) crossreference);
            }
        }

        return subset;

    }

    /**
     * @inheritDoc
     */
    @Override
    public Collection<I> searchCrossReferences(Identifier crossreference) {
        return getIdentifiers(construct(crossreference.getAccession(), DATABASE_ACCESSION));
    }


    public Class getIdentifierClass(String id) {

        // to save space we store the class name's in the index with a 'class-index' link

        if (indexMap.containsKey(id)) {
            return indexMap.get(id);
        }

        Query q = new TermQuery(new Term(CLASS_ID.field(), id));
        String name = firstValue(q, CLASS_NAME);
        try {
            Class c = Class.forName(name);
            indexMap.put(id, c);
            return c;
        } catch (ClassNotFoundException ex) {
            LOGGER.error("Could not find class for name " + name);
        }

        return null;

    }

}
