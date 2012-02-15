package uk.ac.ebi.chemet.editor.annotation;

import com.jgoodies.forms.factories.Borders;
import javax.swing.JComponent;
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
        return annotation;
    }


    /**
     * @inheritDoc
     */
    public abstract AbstractAnnotationEditor newInstance();
}
