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

import uk.ac.ebi.mdk.domain.entity.AnnotatedEntity;
import uk.ac.ebi.mdk.domain.annotation.Annotation;

/**
 *          NameAccessor - 2011.11.24 <br>
 *          Access the name of the entity
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class AnnotationAccess
        implements EntityValueAccessor {


    private Annotation annotation;
    
    public AnnotationAccess(Annotation annotation) {
        this.annotation = annotation;
    }  
    
    
    public String getName() {
        return annotation.getShortDescription();
    }

    public Object getValue(AnnotatedEntity entity) {
        return entity.getAnnotationsExtending(annotation);
    }

    public Class getColumnClass() {
        return Annotation.class;
    }

    public Class getValueClass() {
        return Annotation.class;
    }
}
