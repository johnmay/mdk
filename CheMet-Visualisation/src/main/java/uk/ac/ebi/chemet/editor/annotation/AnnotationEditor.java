package uk.ac.ebi.chemet.editor.annotation;

import uk.ac.ebi.interfaces.Annotation;

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
