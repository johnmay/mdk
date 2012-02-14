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

import uk.ac.ebi.interfaces.io.marshal.EntityMarshaller;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.Version;
import uk.ac.ebi.core.MetabolicReactionImplementation;
import uk.ac.ebi.core.Reconstruction;
import uk.ac.ebi.interfaces.Gene;
import uk.ac.ebi.interfaces.Genome;
import uk.ac.ebi.interfaces.entities.Entity;
import uk.ac.ebi.interfaces.entities.EntityFactory;
import uk.ac.ebi.interfaces.entities.GeneProduct;
import uk.ac.ebi.interfaces.entities.Metabolite;
import uk.ac.ebi.interfaces.io.ReconstructionInputStream;
import uk.ac.ebi.resource.IdentifierFactory;
import uk.ac.ebi.resource.organism.Taxonomy;


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
public class DefaultReconstructionInputStream
        extends ObjectInputStream
        implements ReconstructionInputStream {

    private static final Logger LOGGER = Logger.getLogger(DefaultReconstructionInputStream.class);

    private MarshallFactoryImplementation marshalFactory;

    private int metaboliteCount = 0;

    private Map<Integer, Metabolite> metaboliteMap = new HashMap<Integer, Metabolite>();

    private EntityFactory factory;


    public DefaultReconstructionInputStream(InputStream out, EntityFactory factory) throws IOException {
        super(out);
        this.factory = factory;
    }


    public void read(Reconstruction reconstruction) throws IOException, ClassNotFoundException {

        // get the marshal factory for the correct version
        this.marshalFactory = new MarshallFactoryImplementation(new Version(readInt()), factory);

        // basic info
        reconstruction.setIdentifier(IdentifierFactory.getInstance().read(this));
        reconstruction.setName(readUTF());
        reconstruction.setAbbreviation(readUTF());

        // container
        reconstruction.setContainer(new File(readUTF()));

        // taxon
        Taxonomy taxon = new Taxonomy();
        taxon.readExternal(this);
        reconstruction.setTaxonomy(taxon);

        // genome
        reconstruction.getGenome().read(this);
        genome = reconstruction.getGenome();


        // gene products
        int nTypes = readInt();
        for (int i = 0; i < nTypes; i++) {

            Class<Entity> c = (Class<Entity>) readObject();
            int nProds = readInt();

            EntityMarshaller marshaller = marshalFactory.getMarhsaller(c);

            for (int j = 0; j < nProds; j++) {
                reconstruction.getProducts().add((GeneProduct) marshaller.read(this));
            }

        }

        // metabolites
        int nMetabolites = readInt();
        System.out.println(nMetabolites);
        for (int i = 0; i < nMetabolites; i++) {
            reconstruction.addMetabolite(getMetabolite(readInt()));
        }


        // reactions
        EntityMarshaller rxnMarshaller = marshalFactory.getReactionMarshaller();

        LOGGER.debug("Reaction Marshaller: " + rxnMarshaller.getVersion());

        int nReactions = readInt();
        System.out.println(nReactions);
        while (nReactions-- > 0) {
            reconstruction.addReaction((MetabolicReactionImplementation) rxnMarshaller.read(this));
        }


    }

    private Genome genome;


    public Gene getGene(int c, int g) {
        return genome.getGene(c, g);
    }


    public Metabolite getMetabolite(int index) throws IOException, ClassNotFoundException {

        // if metabolite has been read, or
        // read the next object       

        if (metaboliteMap.containsKey(index)) {
            return metaboliteMap.get(index);
        }

        Metabolite m = (Metabolite) marshalFactory.getMetaboliteMarshaller().read(this);

        metaboliteMap.put(index, m);

        return m;

    }
}
