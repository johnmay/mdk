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

package uk.ac.ebi.mdk.hsql.handler;

import com.google.common.base.Function;
import org.openscience.cdk.interfaces.IAtomContainer;
import uk.ac.ebi.mdk.domain.annotation.AtomContainerAnnotation;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.identifier.Identifier;
import uk.ac.ebi.mdk.service.query.structure.StructureService;

/**
 * Add a chemical structure to a metabolite using it's primary <i>id</i> to look
 * up the structure.
 *
 * @author John May
 */
class StructureAnnotator implements Function<Metabolite, Metabolite> {

    private final StructureService<Identifier> service;

    /**
     * Create a new annotator using the provided service.
     *
     * @param service structure service
     */
    StructureAnnotator(StructureService<Identifier> service) {
        this.service = service;
    }

    @Override public Metabolite apply(Metabolite m) {
        IAtomContainer ac = service.getStructure(m.getIdentifier());
        if (!ac.isEmpty()) {
            m.addAnnotation(new AtomContainerAnnotation(ac));
        }
        return m;
    }
}
