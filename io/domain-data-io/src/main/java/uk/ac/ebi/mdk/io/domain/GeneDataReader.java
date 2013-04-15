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
import org.biojava3.core.sequence.Strand;
import uk.ac.ebi.caf.utility.version.annotation.CompatibleSince;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.io.EnumReader;
import uk.ac.ebi.mdk.io.EntityReader;
import uk.ac.ebi.mdk.domain.entity.Gene;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;

import java.io.DataInput;
import java.io.IOException;

/**
 * ProteinProductDataWriter - 12.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@CompatibleSince("0.9")
public class GeneDataReader
        implements EntityReader<Gene> {

    private static final Logger LOGGER = Logger.getLogger(GeneDataReader.class);

    private DataInput in;
    private EnumReader enumIn;
    private EntityFactory factory;

    public GeneDataReader(DataInput in, EntityFactory factory) {
        this.in = in;
        this.factory = factory;
        this.enumIn = new EnumReader(in);
    }

    public Gene readEntity(Reconstruction reconstruction) throws IOException, ClassNotFoundException {

        Gene gene = factory.newInstance(Gene.class);

        gene.setStart(in.readInt());
        gene.setEnd(in.readInt());
        gene.setStrand((Strand) enumIn.readEnum());

        return gene;

    }

}
