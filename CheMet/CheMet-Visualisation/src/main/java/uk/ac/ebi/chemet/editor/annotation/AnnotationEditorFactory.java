/**
 * AnnotationEditorFactory.java
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
package uk.ac.ebi.chemet.editor.annotation;

import java.security.InvalidParameterException;
import java.util.*;
import org.apache.log4j.Logger;
import uk.ac.ebi.annotation.AuthorAnnotation;
import uk.ac.ebi.annotation.base.AbstractDoubleAnnotation;
import uk.ac.ebi.annotation.crossreference.CrossReference;
import uk.ac.ebi.interfaces.Annotation;
import uk.ac.ebi.interfaces.StringAnnotation;


/**
 *
 *          AnnotationEditorFactory 2012.02.14
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 *          Class description
 *
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
        editors.put(AuthorAnnotation.class, new AuthorAnnotationEditor());
        editors.put(AbstractDoubleAnnotation.class, new DoubleAnnotationEditor());
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

        return null;

    }
}
