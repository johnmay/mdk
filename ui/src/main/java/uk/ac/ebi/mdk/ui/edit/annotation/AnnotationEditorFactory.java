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

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.annotation.AuthorAnnotation;
import uk.ac.ebi.mdk.domain.annotation.GibbsEnergy;
import uk.ac.ebi.caf.component.factory.LabelFactory;
import uk.ac.ebi.mdk.domain.annotation.Flag;
import uk.ac.ebi.mdk.domain.identifier.ChEBIIdentifier;
import uk.ac.ebi.mdk.domain.identifier.KEGGCompoundIdentifier;
import uk.ac.ebi.mdk.domain.identifier.classification.ClassificationIdentifier;
import uk.ac.ebi.mdk.domain.identifier.classification.ECNumber;
import uk.ac.ebi.mdk.domain.annotation.Annotation;
import uk.ac.ebi.mdk.domain.annotation.crossreference.*;
import uk.ac.ebi.mdk.domain.annotation.primitive.StringAnnotation;
import uk.ac.ebi.mdk.domain.annotation.primitive.BooleanAnnotation;
import uk.ac.ebi.mdk.domain.annotation.primitive.DoubleAnnotation;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.event.MouseEvent;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.List;


/**
 * AnnotationEditorFactory 2012.02.14
 *
 * @author johnmay
 * @author $Author$ (this version)
 *         <p/>
 *         Class description
 * @version $Rev$ : Last Changed $Date$
 */
public class AnnotationEditorFactory {

    private static final Logger LOGGER = Logger.getLogger(AnnotationEditorFactory.class);

    private Map<Class, AbstractAnnotationEditor> editors = new HashMap<Class, AbstractAnnotationEditor>();

    private Map<Class, AbstractAnnotationEditor> cachedEditor = new HashMap<Class, AbstractAnnotationEditor>();


    public static AnnotationEditorFactory getInstance() {
        return AnnotationEditorFactoryHolder.INSTANCE;
    }

    private static class AnnotationEditorFactoryHolder {

        private static final AnnotationEditorFactory INSTANCE = new AnnotationEditorFactory();
    }


    private AnnotationEditorFactory() {
        editors.put(StringAnnotation.class, new StringAnnotationEditor());
        editors.put(CrossReference.class, new CrossReferenceEditor());
        editors.put(ChEBICrossReference.class, new CrossReferenceEditor(ChEBIIdentifier.class));
        editors.put(KEGGCrossReference.class, new CrossReferenceEditor(KEGGCompoundIdentifier.class));
        editors.put(Classification.class, new CrossReferenceEditor(ClassificationIdentifier.class));
        editors.put(EnzymeClassification.class, new CrossReferenceEditor(ECNumber.class));
        editors.put(AuthorAnnotation.class, new AuthorAnnotationEditor());
        editors.put(DoubleAnnotation.class, new DoubleAnnotationEditor());
        editors.put(BooleanAnnotation.class, new BooleanAnnotationEditor());
        editors.put(Flag.class, new FlagEditor());
        editors.put(GibbsEnergy.class, new GibbsEnergyEditor());
    }

    public AbstractAnnotationEditor newEditor(Class<? extends Annotation> c) {

        if (!cachedEditor.containsKey(c)) {
            cachedEditor.put(c, getExplicitEditor(c));
        }

        if (cachedEditor.get(c) == null) {
            throw new InvalidParameterException("No editor available");
        }

        return cachedEditor.get(c).newInstance();

    }


    public AbstractAnnotationEditor getExplicitEditor(Class<? extends Annotation> c) {

        if (editors.containsKey(c)) {
            return editors.get(c);
        }

        Queue<Class<?>> queue = new LinkedList<Class<?>>(); // the BFS' "to be visited" queue
        Set<Class<?>> visited = new HashSet<Class<?>>();    // the class objects we have visited

        queue.add(c);
        visited.add(c);

        while (!queue.isEmpty()) {
            Class<?> curClass = queue.remove();

            // get the super types to visit.
            List<Class<?>> supers = new LinkedList<Class<?>>();
            for (Class<?> itrfce : curClass.getInterfaces()) {
                supers.add(itrfce);
            }
            Class<?> superClass = curClass.getSuperclass(); // this would be null for interfaces.
            if (superClass != null) {
                supers.add(superClass);
            }

            for (Class<?> ifs : supers) {
                if (editors.containsKey(ifs)) {
                    return editors.get(ifs);
                }
                if (!visited.contains(ifs)) {
                    queue.add(ifs);
                    visited.add(ifs);
                }
            }

        }

        return new NonEditableAnnotationEditor();

    }

    public TableCellEditor getTableCellEditor() {
        if (tableeditor == null)
            tableeditor = new AnnotationTableCellEditor();
        return tableeditor;
    }

    private AnnotationTableCellEditor tableeditor;

    private class AnnotationTableCellEditor extends AbstractCellEditor implements TableCellEditor {

        private AbstractAnnotationEditor editor = new NonEditableAnnotationEditor();

        public AnnotationTableCellEditor() {
        }

        public JComponent getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

            if (value instanceof Annotation) {

                Annotation annotation = (Annotation) value;
                editor = getExplicitEditor(annotation.getClass());
                editor.setAnnotationClass(annotation.getClass());
                editor.setAnnotation(annotation);

                return editor.getComponent();
            }

            return new JLabel("-");


        }

        @Override
        public Object getCellEditorValue() {
            return editor != null ? editor.newAnnotation() : null;
        }

        @Override
        public boolean isCellEditable(EventObject anEvent) {
            if (anEvent instanceof MouseEvent) {
                return ((MouseEvent) anEvent).getClickCount() >= 2;
            }
            return false;
        }

    }
    


    private class NonEditableAnnotationEditor extends AbstractAnnotationEditor<Annotation> {


        private Annotation annotation;

        public NonEditableAnnotationEditor() {
        }

        @Override
        public JComponent getComponent() {
            return LabelFactory.newFormLabel("not currently supported");
        }

        @Override
        public void setAnnotation(Annotation annotation) {
            this.annotation = annotation;
        }

        // return an unedited annotation of that whichw as set
        @Override
        public Annotation newAnnotation() {
            return this.annotation;
        }

        @Override
        public AbstractAnnotationEditor newInstance() {
            return new NonEditableAnnotationEditor();
        }

    }

}
