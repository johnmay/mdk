/**
 * ReconstructionOutputStream.java
 *
 * 2012.01.31
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
package uk.ac.ebi.io.core;

import com.google.common.collect.ListMultimap;
import uk.ac.ebi.interfaces.io.marshal.EntityMarshaller;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.Version;
import uk.ac.ebi.interfaces.entities.Metabolite;
import uk.ac.ebi.core.Reconstruction;
import uk.ac.ebi.core.metabolite.Metabolome;
import uk.ac.ebi.core.product.ProductCollection;
import uk.ac.ebi.core.reaction.ReactionList;
import uk.ac.ebi.interfaces.Gene;
import uk.ac.ebi.interfaces.Genome;
import uk.ac.ebi.interfaces.entities.Entity;
import uk.ac.ebi.interfaces.entities.EntityFactory;
import uk.ac.ebi.interfaces.entities.GeneProduct;
import uk.ac.ebi.interfaces.io.ReconstructionOutputStream;
import uk.ac.ebi.resource.IdentifierFactory;


/**
 *
 *          ReconstructionOutputStream 2012.01.31
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 *          Class description
 *
 */
public class DefaultReconstructionOutputStream extends ObjectOutputStream implements ReconstructionOutputStream {

    private static final Logger LOGGER = Logger.getLogger(DefaultReconstructionOutputStream.class);

    private MarshallFactoryImplementation marshalFactory;

    private int metaboliteCount = 0;

    private Map<Metabolite, Integer> metaboliteMap = new HashMap<Metabolite, Integer>();


    public DefaultReconstructionOutputStream(OutputStream out, Version version, EntityFactory factory) throws IOException {
        super(out);
        this.marshalFactory = new MarshallFactoryImplementation(version, factory);
    }


    public void write(Reconstruction reconstruction) throws IOException {


        // version format
        writeInt(marshalFactory.getVersion().getIndex());

        // basic info (could delegate to EntityMarshaller)
        IdentifierFactory.getInstance().write(this, reconstruction.getIdentifier());
        writeUTF(reconstruction.getName());
        writeUTF(reconstruction.getAbbreviation());

        // container
        writeUTF(reconstruction.getContainer().getPath());

        // write the taxonomy identifier
        reconstruction.getTaxonomy().writeExternal(this);

        // genome (to migrate)
        reconstruction.getGenome().write(this);
        genome = reconstruction.getGenome();

        // Write Gene Products
        ProductCollection products = reconstruction.getProducts();
        ListMultimap<Class<? extends Entity>, GeneProduct> map = products.getMap();

        writeInt(map.keySet().size());

        for (Class<? extends Entity> c : map.keySet()) {

            List<GeneProduct> gps = map.get(c);

            writeObject(c);
            writeInt(gps.size());

            EntityMarshaller marhsaller = marshalFactory.getMarhsaller(c);

            if (marhsaller == null) {
                System.out.println("no marshaller for " + c);
            }

            for (GeneProduct gp : gps) {
                marhsaller.write(this, gp);
            }

        }


        // metabolites
        EntityMarshaller metaboliteMarshaller = marshalFactory.getMetaboliteMarshaller();
        LOGGER.debug("Metabolite marshaller: " + metaboliteMarshaller.getVersion());

        Metabolome mc = reconstruction.getMetabolome();
        System.out.println(mc.size());
        writeInt(mc.size());
        for (int i = 0; i < mc.size(); i++) {
            writeIndex(mc.get(i));
        }

        // reactions     
        EntityMarshaller rxnMarshaller = marshalFactory.getReactionMarshaller();

        LOGGER.debug("Reaction marshaller: " + rxnMarshaller.getVersion());

        ReactionList rl = reconstruction.getReactions();
        System.out.println(rl.size());
        writeInt(rl.size());
        for (int i = 0; i < rl.size(); i++) {
            rxnMarshaller.write(this, rl.get(i));
        }


    }

    private Genome genome;


    public int getChromosomeIndex(Gene gene) {
        return genome.getIndex(gene)[0];
    }


    public int getGeneIndex(Gene gene) {
        return genome.getIndex(gene)[1];
    }


    public void writeIndex(Metabolite metabolite) throws IOException {

        // if the metabolite has been writen return the index, if not
        // write the molecule and increment the index


        if (metaboliteMap.containsKey(metabolite)) {
            this.writeInt(metaboliteMap.get(metabolite));
            return;
        }


        this.writeInt(metaboliteCount);
        marshalFactory.getMetaboliteMarshaller().write(this, metabolite);

        metaboliteMap.put(metabolite, metaboliteCount);

        metaboliteCount++;



    }
}
