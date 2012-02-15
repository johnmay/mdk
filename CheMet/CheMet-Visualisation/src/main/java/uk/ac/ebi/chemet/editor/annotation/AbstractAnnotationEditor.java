package uk.ac.ebi.chemet.editor.annotation;

import com.jgoodies.forms.factories.Borders;
import javax.swing.JComponent;
import uk.ac.ebi.annotation.util.AnnotationFactory;
import uk.ac.ebi.interfaces.Annotation;


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
        extends JComponent
        implements AnnotationEditor<A> {

    private A annotation;

    private Class<A> c;

    
    /**
     * Method must be called on instantiation if you
     * want you're editor to create new annotations
     * @param c 
     */
    public final void setClass(Class<A> c) {
        this.c = c;
    }


    public AbstractAnnotationEditor() {
        setBorder(Borders.DLU4_BORDER);
    }


    /**
     * @inheritDoc
     */
    public void setAnnotation(A annotation) {
        this.annotation = annotation;
    }


    /**
     * @inheritDoc
     */
    public A getAnnotation() {
        return annotation == null ? (A) AnnotationFactory.getInstance().ofClass(c) : annotation;
    }


    /**
     * @inheritDoc
     */
    public abstract AbstractAnnotationEditor newInstance();
}
