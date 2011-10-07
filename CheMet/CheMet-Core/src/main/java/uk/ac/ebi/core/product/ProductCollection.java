/**
 * ProductCollection.java
 *
 * 2011.10.07
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
package uk.ac.ebi.core.product;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.List;
import org.apache.log4j.Logger;
import uk.ac.ebi.core.AbstractGeneProduct;
import uk.ac.ebi.interfaces.Annotation;
import uk.ac.ebi.interfaces.Identifier;
import uk.ac.ebi.interfaces.Observation;

/**
 * @name    ProductCollection - 2011.10.07 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class ProductCollection {

    private static final Logger LOGGER = Logger.getLogger(ProductCollection.class);
    private Multimap<String, AbstractGeneProduct> products = ArrayListMultimap.create();
    private Multimap<String, AbstractGeneProduct> accessionMap = ArrayListMultimap.create();
    // could use identifier but accession should be unique

    public ProductCollection() {
    }

    /**
     * Add a single gene product
     * @param product
     * @return
     */
    public boolean add(AbstractGeneProduct product) {
        return products.put(product.getBaseType(), product);
    }

    /**
     * Adds a collection of gene products
     * @param products
     * @return
     */
    public boolean addAll(Collection<AbstractGeneProduct> products) {
        boolean changed = false;
        for (AbstractGeneProduct gp : products) {
            changed = add(gp) || changed;
        }
        return changed;
    }

    public boolean addAnnotation(Identifier id, Annotation annotation) {
        return addAnnotation(id.getAccession(), annotation);
    }

    public boolean addAnnotation(String accession, Annotation annotation) {
        throw new UnsupportedOperationException();
    }

    public boolean addAnnotations(Identifier id, Collection<Annotation> annotations) {
        return addAnnotations(id.getAccession(), annotations);
    }

    public boolean addAnnotations(String accession, Collection<Annotation> annotations) {
        throw new UnsupportedOperationException();
    }

    public boolean addObservation(Identifier id, Observation observation) {
        return addObservation(id.getAccession(), observation);
    }

    /**
     * Adds an observation to product(s) matching the specified accession. If there are multiple products with the
     * same accession the observation is added to all
     * @param accession
     * @param observation
     * @return
     */
    public boolean addObservation(String accession, Observation observation) {

        if (accessionMap.containsKey(accession)) {

            Collection<AbstractGeneProduct> products = accessionMap.get(accession);
            for (AbstractGeneProduct product : products) {
                // product.addObservation(observation);
            }
            return true; // atm: observations will be added whether unique or not
        }
        return false;
    }

    public boolean addObservations(Identifier id, Collection<Observation> observations) {
        return addObservations(id.getAccession(), observations);
    }

    /**
     * Adds an observation to product(s) matching the specified accession. If there are multiple products with the
     * same accession the observation is added to all
     * @param accession
     * @param observation
     * @return
     */
    public boolean addObservations(String accession, Collection<Observation> observations) {
        if (accessionMap.containsKey(accession)) {

            Collection<AbstractGeneProduct> products = accessionMap.get(accession);
            for (AbstractGeneProduct product : products) {
                // product.addObservation(observations);
            }
            return true; // atm: observations will be added whether unique or not
        }
        return false;
    }
}
