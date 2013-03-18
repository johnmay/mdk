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

import uk.ac.ebi.caf.component.factory.ComboBoxFactory;
import uk.ac.ebi.mdk.domain.annotation.Annotation;
import uk.ac.ebi.mdk.domain.annotation.DefaultAnnotationFactory;
import uk.ac.ebi.mdk.domain.entity.AnnotatedEntity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * AnnotationChoiceEditor 2012.02.14
 *
 * @author johnmay
 * @author $Author$ (this version)
 *
 *         Class description
 * @version $Rev$ : Last Changed $Date$
 */
public class AnnotationChoiceEditor extends JComponent {

    private static DefaultAnnotationFactory ANNOTATION_FACTORY = DefaultAnnotationFactory.getInstance();

    private static AnnotationEditorFactory EDITOR_FACTORY = AnnotationEditorFactory.getInstance();

    private JComboBox type = ComboBoxFactory.newComboBox(ANNOTATION_FACTORY.ofContext(AnnotatedEntity.class));

    private ItemListener listener;

    private AnnotationEditor editor;

    // context
    private final Class<? extends AnnotatedEntity> c;

    public AnnotationChoiceEditor(final Window window, final Class<? extends AnnotatedEntity> c) {

        this.c = c;
        setContext(c);
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
        List<Annotation> annotations = ANNOTATION_FACTORY.ofContext(c);
        Collections.sort(annotations, new Comparator<Annotation>() {
            @Override public int compare(Annotation o1, Annotation o2) {
                return o1.getBrief().compareTo(o2.getBrief());
            }
        });
        for (Annotation annotation : annotations) {
            model.addElement(annotation);
        }
        type.setSelectedIndex(0);
        // ensure default editor
        @SuppressWarnings("unchecked")
        Class<Annotation> defaultClass = (Class<Annotation>) type.getItemAt(0).getClass();
        AbstractAnnotationEditor editor = EDITOR_FACTORY.newEditor(defaultClass);
        editor.setAnnotationClass(defaultClass);
        this.editor = editor;
        type.addItemListener(listener);

        // force update
        Object obj = model.getElementAt(0);
        if (obj != null)
            model.setSelectedItem(obj);

    }


    public AnnotationEditor getEditor() {
        return editor;
    }
}
