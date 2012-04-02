package uk.ac.ebi.chemet.editor.annotation;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.apache.log4j.Logger;
import uk.ac.ebi.annotation.reaction.GibbsEnergy;
import uk.ac.ebi.caf.component.factory.FieldFactory;
import uk.ac.ebi.interfaces.Annotation;

import javax.swing.*;

/**
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class GibbsEnergyEditor extends AbstractAnnotationEditor<GibbsEnergy> {

    private static final Logger LOGGER = Logger.getLogger(GibbsEnergyEditor.class);

    private JTextField value = FieldFactory.newField(4);
    private JTextField error = FieldFactory.newField(4);
    ;

    public GibbsEnergyEditor() {
        JComponent component = getComponent();
        component.setLayout(new FormLayout("p, 2dlu, p", "p"));
        CellConstraints cc = new CellConstraints();
        component.add(value, cc.xy(1, 1));
        component.add(error, cc.xy(3, 1));
    }

    @Override
    public void setAnnotation(GibbsEnergy annotation) {
        super.setAnnotation(annotation);
        value.setText(annotation.getValue().toString());
        error.setText(annotation.getError().toString());
    }

    @Override
    public GibbsEnergy newAnnotation() {
        GibbsEnergy energy = super.newAnnotation();
        energy.setValue(Double.parseDouble(value.getText())); // note: both could through number format
        energy.setError(Double.parseDouble(error.getText())); // exception but this should be done via validation
        return energy;
    }

    @Override
    public AbstractAnnotationEditor newInstance() {
        return new GibbsEnergyEditor();
    }
}
