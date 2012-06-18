/**
 * AnnotationChoiceEditor.java
 *
 * 2012.02.14
 *
 * This file is part of the CheMet library
 * 
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * CheMet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.mdk.ui.edit.annotation;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.*;
import uk.ac.ebi.mdk.domain.annotation.DefaultAnnotationFactory;
import uk.ac.ebi.caf.component.factory.ComboBoxFactory;
import uk.ac.ebi.mdk.domain.entity.AnnotatedEntity;
import uk.ac.ebi.mdk.domain.annotation.Annotation;


/**
 *
 *          AnnotationChoiceEditor 2012.02.14
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 *          Class description
 *
 */
public class AnnotationChoiceEditor extends JComponent {

    private static DefaultAnnotationFactory ANNOTATION_FACTORY = DefaultAnnotationFactory.getInstance();

    private static AnnotationEditorFactory EDITOR_FACTORY = AnnotationEditorFactory.getInstance();

    private JComboBox type = ComboBoxFactory.newComboBox(ANNOTATION_FACTORY.ofContext(AnnotatedEntity.class));

    private ItemListener listener;

    private AnnotationEditor editor;


    public AnnotationChoiceEditor(final Window window) {

        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        add(type);

        type.setRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                setText(((Annotation) value).getShortDescription());
                return this;
            }
        });


        listener = new ItemListener() {

            public void itemStateChanged(ItemEvent e) {

                if (getComponentCount() > 1) {
                    remove(1);
                }

                AbstractAnnotationEditor localEditor = EDITOR_FACTORY.newEditor((Class<Annotation>) type.getSelectedItem().getClass());
                localEditor.setAnnotationClass(type.getSelectedItem().getClass());
                editor = localEditor;
                add(localEditor.getComponent(), 1);
                window.pack();

            }
        };

        // when the user selects an item, show the coorect editor
        type.addItemListener(listener);

    }


    public void setContext(Class<? extends AnnotatedEntity> c) {
        DefaultComboBoxModel model = (DefaultComboBoxModel) type.getModel();
        type.removeItemListener(listener);
        model.removeAllElements();
        for (Annotation annotation : ANNOTATION_FACTORY.ofContext(c)) {
            model.addElement(annotation);
        }
        type.addItemListener(listener);
    }


    public AnnotationEditor getEditor() {
       return editor;
    }
}
