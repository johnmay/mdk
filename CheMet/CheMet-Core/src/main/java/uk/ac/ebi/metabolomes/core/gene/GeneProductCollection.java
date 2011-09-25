/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the Lesser GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package uk.ac.ebi.metabolomes.core.gene;

import java.io.Externalizable;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import org.jboss.serial.io.JBossObjectInputStream;
import org.jboss.serial.io.JBossObjectOutputStream;
import uk.ac.ebi.interfaces.Identifier;
import uk.ac.ebi.metabolomes.identifier.AbstractIdentifier;


/**
 * GeneProductCollection.java
 * A class to handle storage of collections of the different GeneProducts
 *
 * @author johnmay
 * @date Apr 28, 2011
 */
public class GeneProductCollection
  implements Externalizable {

    private transient static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.
      getLogger(GeneProductCollection.class);
    private ArrayList<GeneProduct> geneProducts;
    private ArrayList<GeneProteinProduct> proteinProducts;
    private HashMap<Identifier, GeneProteinProduct> proteinProductIdentifierMap;
    private HashMap<Identifier, GeneProduct> productIdentifierMap;


    /**
     * Constructor instantiates the underlying lists
     */
    public GeneProductCollection() {
        geneProducts = new ArrayList<GeneProduct>();
        proteinProducts = new ArrayList<GeneProteinProduct>();
        proteinProductIdentifierMap = new HashMap<Identifier, GeneProteinProduct>();
        productIdentifierMap = new HashMap<Identifier, GeneProduct>();
    }


    /**
     * Add the other collection of gene products to this one
     * @param productCollection another collection of gene products
     */
    public AbstractIdentifier[] addAll(GeneProductCollection productCollection) {
        return addAll(productCollection.getAllProducts());
    }


    /**
     * Add all the gene products to the collection
     * @param geneProducts
     */
    public AbstractIdentifier[] addAll(GeneProduct[] geneProducts) {

        ArrayList<Identifier> clashingIdentifiers = new ArrayList<Identifier>();

        for( GeneProduct geneProduct : geneProducts ) {
            if( addProduct(geneProduct) == Boolean.FALSE ) {
                clashingIdentifiers.add(geneProduct.getIdentifier());
            }
        }
        return clashingIdentifiers.toArray(new AbstractIdentifier[0]);
    }


    /**
     * Add all the protein products to the collection
     * @param geneProducts
     */
    public AbstractIdentifier[] addAll(GeneProteinProduct[] proteinProducts) {
        ArrayList<Identifier> clashingIdentifiers = new ArrayList<Identifier>();
        for( GeneProduct proteinProduct : proteinProducts ) {
            if( addProduct(proteinProduct) == Boolean.FALSE ) {
                clashingIdentifiers.add(proteinProduct.getIdentifier());
            }
        }
        return clashingIdentifiers.toArray(new AbstractIdentifier[0]);
    }


    /**
     * Add a product to the collection
     * @param product A product extending the abstract GeneProduct class
     * @return Whether the product was successfully added or not
     */
    public Boolean addProduct(GeneProduct product) {

        if( productIdentifierMap.containsKey(product.getIdentifier()) ) {
            return Boolean.FALSE;
        }

        geneProducts.add(product);
        productIdentifierMap.put(product.getIdentifier(),
                                 (GeneProteinProduct) product);

        if( product instanceof GeneProteinProduct ) {
            proteinProducts.add((GeneProteinProduct) product);
            // add to hash map to we can look it up quickly
            proteinProductIdentifierMap.put(product.getIdentifier(),
                                            (GeneProteinProduct) product);
        }

        return Boolean.TRUE;
    }


    /**
     * Accessor for the total number of products stored
     * @return The total number of all products
     */
    public int numberOfProducts() {
        return geneProducts.size();
    }


    /**
     * Accessor for the number of protein products stored
     * @return The number of protein products
     */
    public int numberOfProteinProducts() {
        return proteinProducts.size();
    }


    /**
     * Accessor for fixed size array of all gene products stored in collection
     * @return fixed size array of GeneProduct[] (i.e. GeneProteinProduct, GenetRNAProduct and GenerRNAProduct)
     */
    public GeneProduct[] getAllProducts() {
        return geneProducts.toArray(new GeneProduct[0]);
    }


    /**
     * Accessor for fixed size array of all protein products stored in collection
     * @return fixed size GeneProteinProduct[] array
     */
    public GeneProteinProduct[] getProteinProducts() {
        return geneProducts.toArray(new GeneProteinProduct[0]);
    }


    /**
     * Fetch a protein product from the collection when provided with the identifier
     * @return The gene protein product matching the identifier
     *         or a new protein product with that identifier
     */
    public GeneProteinProduct getProteinProduct(AbstractIdentifier identifier) {
        // the abstract identifier object has the hashmap and equals overiden
        // so should work fine in hashmap. This could go wrong if the identifier
        // is extensively extended and the default abstract identifier compare method
        // are no long
        if( proteinProductIdentifierMap.containsKey(identifier) ) {
            return proteinProductIdentifierMap.get(identifier);
        }
        GeneProteinProduct newProduct = new GeneProteinProduct(identifier);
        addProduct(newProduct);
        return newProduct;
    }


    public void write(File file) throws FileNotFoundException, IOException {
        write(file, this);
    }


    private void reloadProjectObservations() {
        for( GeneProduct geneProduct : geneProducts ) {
            geneProduct.getObservations().reassignParameterReferences();
        }
    }


    public static GeneProductCollection readCollection(File file) {
        ObjectInput ois = null;
        try {
            ois = new JBossObjectInputStream(new FileInputStream(file));
            GeneProductCollection collection = new GeneProductCollection();
            collection.readExternal(ois);
            collection.reloadProjectObservations();
            ois.close();
            return collection;
        } catch( IOException ex ) {
            logger.error(ex);
        } catch( Exception ex ) {
            logger.error(ex);
        }

        return new GeneProductCollection();
    }


    public static void write(File file, GeneProductCollection collection) throws
      FileNotFoundException, IOException {
        ObjectOutput oos = new JBossObjectOutputStream(new FileOutputStream(file));
        collection.writeExternal(oos);
        oos.close();
    }


    public int indexof(GeneProduct product) {
        return geneProducts.indexOf(product);
    }


    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(geneProducts.size());
        for( GeneProduct geneProduct : geneProducts ) {
            geneProduct.writeExternal(out);
        }
    }


    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

        int size = in.readInt();

        for( int i = 0 ; i < size ; i++ ) {

            // all protein products at time of writing
            GeneProteinProduct product = new GeneProteinProduct();
            product.readExternal(in);

            geneProducts.add(product);
            productIdentifierMap.put(product.getIdentifier(), product);
            proteinProducts.add(product);
            proteinProductIdentifierMap.put(product.getIdentifier(), product);

        }

    }


}

