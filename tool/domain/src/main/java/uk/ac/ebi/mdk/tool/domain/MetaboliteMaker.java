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

package uk.ac.ebi.mdk.tool.domain;

import uk.ac.ebi.mdk.domain.annotation.SMILES;
import uk.ac.ebi.mdk.domain.entity.EntityFactory;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.identifier.basic.BasicChemicalIdentifier;

/**
 * A utility for creating metabolites from various inputs.
 *
 * @author John May
 */
public final class MetaboliteMaker {

    private final EntityFactory entities;

    public MetaboliteMaker(EntityFactory entities) {
        this.entities = entities;
    }

    /**
     * Create a metabolite from a SMILES string. The name of the metabolite
     * can be specified after the SMILES notation.
     * 
     * <blockquote><pre>
     *     MetaboliteMaker maker   = new MetaboliteMaker(entities);
     *     Metabolite      ethanol = maker.fromSmiles("CCO ethanol");
     * </pre></blockquote>
     * 
     * @param smi a smiles string (optionally with a name)
     * @return a metabolite instance
     */
    public Metabolite fromSmiles(String smi) {
        String[] parts = smi.split("[\t ]");
        Metabolite metabolite = entities.metabolite();
        metabolite.setIdentifier(BasicChemicalIdentifier.nextIdentifier());
        metabolite.setName(parts.length > 0 ? parts[1] : "unamed");
        metabolite.addAnnotation(new SMILES(parts[0]));
        return metabolite;
    }
}
