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

import com.jgoodies.forms.factories.Borders;

import javax.swing.*;

import uk.ac.ebi.mdk.domain.annotation.DefaultAnnotationFactory;
import uk.ac.ebi.caf.component.factory.PanelFactory;
import uk.ac.ebi.mdk.domain.annotation.Annotation;


/**
 *          AbstractAnnotationEditor 2012.02.14 <br>
 *          Provides a foundation that other annotation editors
 *          can build upon
 * 
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 */
public abstract class AbstractAnnotationEditor<A extends Annotation>
        implements AnnotationEditor<A> {

    private A annotation;

    private Class<A> c;
    
    private JComponent component;

    /**
     * Method must be called on instantiation if you
     * want you're editor to create new annotations
     * @param c 
     */
    public final void setAnnotationClass(Class<A> c) {
        this.c = c;
    }
    
    public final Class<A> getAnnotationClass(){
        return this.c;
    }


    public AbstractAnnotationEditor() {
    }


    /**
     * @inheritDoc
     */
    public void setAnnotation(A annotation) {
        this.annotation = annotation;
        setAnnotationClass((Class<A>) annotation.getClass());
    }


    /**
     * @inheritDoc
     */
    public A newAnnotation() {
        return DefaultAnnotationFactory.getInstance().ofClass(c);
    }


    /**
     * @inheritDoc
     */
    public abstract AbstractAnnotationEditor newInstance();

    @Override
    public JComponent getComponent() {
        if(component == null) {
            component = PanelFactory.createInfoPanel();
            component.setBorder(Borders.EMPTY_BORDER);
        }
        return component;
    }
}
