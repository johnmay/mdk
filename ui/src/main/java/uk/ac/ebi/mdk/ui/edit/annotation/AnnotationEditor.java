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

package uk.ac.ebi.mdk.ui.edit.annotation;

import uk.ac.ebi.mdk.domain.annotation.Annotation;

import javax.swing.*;


/**
 *          AnnotationEditor 2012.02.14 <br>
 *          Provides a description that other annotation editors
 *          need to build too
 * 
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 */
public interface AnnotationEditor<A extends Annotation> {

    /**
     * Set the annotation for this editor. This method
     * will setup the GUI editor to display information
     * of from an existing annotation that can be edited
     * 
     * @param annotation the annotation to set
     * 
     */
    public void setAnnotation(A annotation);


    /**
     * Access the annotation with appropriate values 
     * of the annotation set. Note invoking this method
     * provides a new instance of the annotation and not
     * and edited version of that passed in {@see setAnnotation}
     * 
     * @return configured annotation
     */
    public A newAnnotation();


    /**
     * Provides a new instance of the annotation editor.
     * This is primarily used in factories for creating
     * new instances
     * 
     * @return new instance of the this editor
     */
    public AnnotationEditor newInstance();


    /**
     * Access the JComponent for this editor. This will be displayed in the
     * JTable.
     * @return
     */
    public JComponent getComponent();

    
}
