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

package uk.ac.ebi.mdk.io.domain;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.io.EntityInput;
import uk.ac.ebi.mdk.io.EntityReader;
import uk.ac.ebi.mdk.io.SequenceSerializer;
import uk.ac.ebi.mdk.domain.entity.Gene;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.ProteinProduct;

import java.io.DataInput;
import java.io.IOException;

/**
 * ProteinProductDataReader - 12.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@CompatibleSince("0.9")
public class ProteinProductDataReader
        implements EntityReader<ProteinProduct> {

    private static final Logger LOGGER = Logger.getLogger(ProteinProductDataReader.class);

    private DataInput in;
    private EntityFactory factory;
    private EntityInput ein;

    public ProteinProductDataReader(DataInput in, EntityFactory factory, EntityInput ein) {
        this.in = in;
        this.factory = factory;
        this.ein = ein;
    }

    public ProteinProduct readEntity(Reconstruction reconstruction) throws IOException, ClassNotFoundException {

        ProteinProduct p = factory.newInstance(ProteinProduct.class);

        int n = in.readInt();
        
        for(int i = 0 ; i < n; i++) {
            p.addSequence(SequenceSerializer.readProteinSequence(in));
        }

        int nGenes = in.readByte();
        for(int i = 0; i < nGenes; i++){
            reconstruction.associate(ein.read(Gene.class, reconstruction), p);
        }

        return p;

    }

}
