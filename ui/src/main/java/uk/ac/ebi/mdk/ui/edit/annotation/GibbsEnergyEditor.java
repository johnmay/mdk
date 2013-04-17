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
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.annotation.GibbsEnergy;
import uk.ac.ebi.caf.component.factory.FieldFactory;

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

    public GibbsEnergyEditor() {
        JComponent component = getComponent();
        component.setBorder(Borders.EMPTY_BORDER);
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
