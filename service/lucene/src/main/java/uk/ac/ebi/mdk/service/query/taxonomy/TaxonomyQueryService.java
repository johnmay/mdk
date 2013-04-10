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

package uk.ac.ebi.mdk.service.query.taxonomy;

import org.apache.log4j.Logger;
import org.apache.lucene.index.Term;
import uk.ac.ebi.mdk.service.index.other.TaxonomyIndex;
import uk.ac.ebi.mdk.service.query.AbstractLuceneService;
import uk.ac.ebi.mdk.domain.identifier.Taxonomy;
import uk.ac.ebi.mdk.service.query.name.NameService;

import java.util.Arrays;
import java.util.Collection;

/**
 * TaxonmyQueryService - 23.02.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class TaxonomyQueryService
        extends AbstractLuceneService<Taxonomy>
        implements NameService<Taxonomy> {

    private static final Logger LOGGER = Logger.getLogger(TaxonomyQueryService.class);

    public static final Term KINGDOM = new Term("Kingdom");
    public static final Term CODE = new Term("Code");

    public TaxonomyQueryService() {
        super(new TaxonomyIndex());
    }

    @Override
    public Taxonomy getIdentifier() {
        return new Taxonomy();
    }

    @Override
    public Collection<Taxonomy> searchName(String name, boolean approximate) {

//        Query query = approximate ? new WildcardQuery(NAME.createTerm(name + "*"))
//                : new TermQuery(NAME.createTerm(name));
        try{
            return setup(getIdentifiers(getParser(NAME).parse(name + "*")));
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return Arrays.asList();
    }

    private Collection<Taxonomy> setup(Collection<Taxonomy> identifiers) {
        for (Taxonomy id : identifiers) {
            id.setCode(getCode(id));
            id.setKingdom(Taxonomy.Kingdom.getKingdom(getKingdom(id)));
            id.setOfficialName(getNames(id).iterator().next());
        }
        return identifiers;
    }

    @Override
    public Collection<String> getNames(Taxonomy identifier) {
        return values(construct(identifier.getAccession(), IDENTIFIER), NAME);
    }

    public String getKingdom(Taxonomy identifier) {
        return firstValue(identifier, KINGDOM);
    }

    public String getCode(Taxonomy identifier) {
        return firstValue(identifier, CODE);
    }

    public Collection<Taxonomy> searchTaxonomyIdentifier(String identifier, boolean approximate) {
        Collection<Taxonomy> identifiers = getIdentifiers(construct(identifier, IDENTIFIER, approximate));
        return setup(identifiers);
    }

    public Collection<Taxonomy> searchCode(String code, boolean approximate) {
        Collection<Taxonomy> identifiers = getIdentifiers(construct(code, CODE, approximate));
        return setup(identifiers);
    }

    public static void main(String[] args) {
        TaxonomyQueryService service = new TaxonomyQueryService();
        service.setMaxResults(50);
        service.setMinSimilarity(0.5f);


        long start = System.currentTimeMillis();
        service.setMaxResults(5);
            service.searchCode("lacpl", false);

        long end = System.currentTimeMillis();
        System.out.println("Time: " + (end - start) + "ms");
    }


}
