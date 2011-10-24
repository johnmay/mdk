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
import java.io.Externalizable;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import uk.ac.ebi.core.ProteinProduct;
import uk.ac.ebi.core.RibosomalRNA;
import uk.ac.ebi.core.TransferRNA;
import uk.ac.ebi.interfaces.Annotation;
import uk.ac.ebi.interfaces.GeneProduct;
import uk.ac.ebi.interfaces.Genome;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.interfaces.Observation;

/**
 * @name    ProductCollection - 2011.10.07 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class ProductCollection implements Iterable<GeneProduct>, Collection<GeneProduct>, Externalizable {

    private static final Logger LOGGER = Logger.getLogger(ProductCollection.class);
    private List<GeneProduct> productList = new ArrayList();
    private ArrayListMultimap<String, GeneProduct> products = ArrayListMultimap.create();
    private ArrayListMultimap<String, GeneProduct> accessionMap = ArrayListMultimap.create();
    // could use identifier but accession should be unique

    public ProductCollection() {
    }

    /**
     * Add a single gene product
     * @param product
     * @return
     */
    public boolean add(GeneProduct product) {
       products.put(product.getBaseType(), product);
       accessionMap.put(product.getAccession(), product);
       return true;
    }

    /**
     * Adds a collection of gene products
     * @param products
     * @return
     */
    public boolean addAll(Collection<? extends GeneProduct> products) {
        boolean changed = false;
        for (GeneProduct gp : products) {
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
     *
     * Adds an observation to product(s) matching the specified accession. If 
     * there are multiple products with the same accession the observation is
     * added to all
     *
     * @param accession
     * @param observation
     * @return
     * 
     */
    public boolean addObservation(String accession, Observation observation) {

        if (accessionMap.containsKey(accession)) {

            Collection<GeneProduct> products = accessionMap.get(accession);
            for (GeneProduct product : products) {
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

            Collection<GeneProduct> products = accessionMap.get(accession);
            for (GeneProduct product : products) {
                // product.addObservation(observations);
            }
            return true; // atm: observations will be added whether unique or not
        }
        return false;
    }

    public List<GeneProduct> get(String accession){
        return accessionMap.get(accession);
    }

    /**
     * Returns an iterator for all products
     * @return
     */
    public Iterator<GeneProduct> iterator() {
        return products.values().iterator();
    }

    public int size() {
        return products.values().size();
    }

    public boolean isEmpty() {
        return products.isEmpty();
    }

    public boolean contains(Object o) {
        return products.values().contains(o);
    }

    public Object[] toArray() {
        return products.values().toArray();
    }

    public <T> T[] toArray(T[] a) {
        return products.values().toArray(a);
    }

    public List<GeneProduct> getAll(String basetype) {
        return products.get(basetype);
    }

    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void clear() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @inheritDoc
     */
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        throw new NotSerializableException();
    }

    /**
     * @inheritDoc
     */
    public void writeExternal(ObjectOutput out) throws IOException {
        throw new NotSerializableException();
    }

    public void writeExternal(ObjectOutput out, Genome genome) throws IOException {
        Set<String> types = products.keySet();

        out.writeInt(types.size()); // number of types

        for (String type : types) {
            Collection<GeneProduct> ps = products.get(type);

            out.writeUTF(type);
            out.writeInt(ps.size());

            for (GeneProduct product : ps) {
                product.writeExternal(out, genome);
            }

        }
    }

    public void readExternal(ObjectInput in, Genome genome) throws IOException, ClassNotFoundException {
        int nTypes = in.readInt();
        for (int i = 0; i < nTypes; i++) {

            String baseType = in.readUTF();

            int nProds = in.readInt();
            GeneProduct base = baseType.equals("Protein") ? new ProteinProduct()
                               : baseType.equals("rRNA") ? new RibosomalRNA()
                                 : baseType.equals("tRNA") ? new TransferRNA()
                                   : null;
            for (int j = 0; j < nProds; j++) {
                GeneProduct product = base.newInstance();
                product.readExternal(in, genome);
                productList.add(product);
                accessionMap.put(product.getAccession(), product);
                products.put(product.getBaseType(), product);
            }

        }
    }
}
