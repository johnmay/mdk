/*
 * Copyright (C) 2012  John May and Pablo Moreno
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package uk.ac.ebi.mdk.domain.entity;

import uk.ac.ebi.mdk.domain.entity.collection.*;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.domain.matrix.StoichiometricMatrix;

import java.io.File;
import java.util.Collection;

/**
 * Reconstruction - 12.03.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public interface Reconstruction extends AnnotatedEntity {

    public static final String RECONSTRUCTION_FILE_EXTENSION = ".mr";

    public Genome getGenome();

    public void setGenome(Genome genome);

    public Collection<GeneProduct> getProducts();

    public Reactome getReactome();

    public Metabolome getMetabolome();

    public Proteome getProteome();

    public Identifier getTaxonomy();

    public File getContainer();

    public void setContainer(File f);

    public void setTaxonomy(Identifier identifier);

    public void addMetabolite(Metabolite metabolite);

    public void addProduct(GeneProduct product);

    public void addReaction(MetabolicReaction reaction);


    public boolean addSubset(EntityCollection subset);

    public boolean hasMatrix();

    public void setMatrix(StoichiometricMatrix matrix);

    public StoichiometricMatrix getMatrix();

    public Iterable<? extends EntityCollection> getSubsets();
}
