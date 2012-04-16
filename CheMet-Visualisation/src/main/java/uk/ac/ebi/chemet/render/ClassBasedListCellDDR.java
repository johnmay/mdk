package uk.ac.ebi.chemet.render;

import java.awt.Component;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 *
 * Adapted from code: <br>
 * A class-based display delegation renderer. Uses a map that maps classes to renderers to decide to which
 * renderer to delegate the display request to.
 * In case of a class that is not in the map, the renderer performs a BFS up the class hierarchy to find a
 * class for which a renderer was specified.<br />
 * See article for full details.
 * <br/><a href="http://today.java.net/pub/a/today/2008/08/21/complex-table-cell-rendering.html">Article</a>
 * 
 * @author Michael Bar-Sinai mich.barsinai@gmail.com
 *
 *
 *
 */
public class ClassBasedListCellDDR
        implements ListCellRenderer {

    /** Cached renderers, stored in a Class->Renderer map. */
    private Map<Class<?>, ListCellRenderer> renderersCache = new HashMap<Class<?>, ListCellRenderer>();
    /**
     * The Class->Renderer map the user specified. We need this to find the appropriate
     * renderer for classes we have'nt yet met.
     */
    private Map<Class<?>, ListCellRenderer> explicitRenderers = new HashMap<Class<?>, ListCellRenderer>();

    /**
     * Default constructor used {@see DefaultTableCellRenderer} as the default renderer
     * for object
     */
    public ClassBasedListCellDDR() {
        this(new DefaultListCellRenderer());
    }

    /**
     * Constructor specifies the default renderer for the Object class
     * @param renderer
     */
    public ClassBasedListCellDDR(ListCellRenderer renderer) {
        setRenderer(Object.class, renderer);
    }

    /**
     * Sets a renderer for a specific java class (or interface).
     * @param aClass the class for which we set the renderer
     * @param aRenderer the renderer instance that will display the instances of the class.
     */
    public void setRenderer(Class<?> aClass,
                            ListCellRenderer aRenderer) {
        if (aRenderer != null) {
            renderersCache.put(aClass, aRenderer);
            explicitRenderers.put(aClass, aRenderer);
        } else {
            explicitRenderers.remove(aClass);
            renderersCache.clear();
        }
    }

    public Component getListCellRendererComponent(JList list,
                                                  Object value,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean hasFocus) {
        ListCellRenderer cr = null;
        if (value != null) {
            // Try to get the renderer from the cache
            Class<? extends Object> valueClass = value.getClass();
            cr = renderersCache.get(valueClass);
            if (cr == null) {
                // Cache miss. Find the the proper renderer for the class using BFS.
                cr = getExplicitRenderer(valueClass);
                renderersCache.put(valueClass, cr);
            }
        }

        if (cr == null) {
            cr = explicitRenderers.get(Object.class);
        }

        Component component = cr.getListCellRendererComponent(list, value, index, isSelected, hasFocus);

        return component;
    }

    public Map<Class<?>, ListCellRenderer> getExplicitRenderers() {
        return explicitRenderers;
    }

    /**
     * Returns the "most appropriate" renderer for instances of <code>valClass</code>
     * We do a BFS run over the super-classes and interfaces of the object.
     * See article for full description.
     *
     * @param o the object whose super types we want to list
     * @return the super types, ordered by relevance.
     */
    protected ListCellRenderer getExplicitRenderer(Class<?> valClass) {
        Queue<Class<?>> queue = new LinkedList<Class<?>>(); // the BFS' "to be visited" queue
        Set<Class<?>> visited = new HashSet<Class<?>>();    // the class objects we have visited

        queue.add(valClass);
        visited.add(valClass);

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
                if (explicitRenderers.containsKey(ifs)) {
                    return explicitRenderers.get(ifs);
                }
                if (!visited.contains(ifs)) {
                    queue.add(ifs);
                    visited.add(ifs);
                }
            }

        }

        return explicitRenderers.get(Object.class);
    }
}
