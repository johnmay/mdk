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
package uk.ac.ebi.mdk.ui.component.table.accessor;

import com.google.common.base.Joiner;
import uk.ac.ebi.mdk.domain.annotation.Synonym;
import uk.ac.ebi.mdk.domain.entity.AnnotatedEntity;

/**
 *          SynonymAccessor - 2011.11.24 <br>
 *          Access the name of the entity
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class SynonymsAccessor
        implements EntityValueAccessor {

    public String getName() {
        return "Synonyms";
    }

    public Object getValue(AnnotatedEntity entity) {
        return Joiner.on(", ").join(entity.getAnnotationsExtending(Synonym.class));
    }

    public Class getColumnClass() {
        return String.class;
    }

    public Class getValueClass() {
        return String.class;
    }
}
