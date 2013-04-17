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

package uk.ac.ebi.mdk.ui.render.table;

import java.awt.Component;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
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
public class ClassBasedTableCellDDR
        implements TableCellRenderer {

    /** Cached renderers, stored in a Class->Renderer map. */
    private Map<Class<?>, TableCellRenderer> renderersCache = new HashMap<Class<?>, TableCellRenderer>();
    /**
     * The Class->Renderer map the user specified. We need this to find the appropriate
     * renderer for classes we have'nt yet met.
     */
    private Map<Class<?>, TableCellRenderer> explicitRenderers = new HashMap<Class<?>, TableCellRenderer>();

    /**
     * Default constructor used {@see DefaultTableCellRenderer} as the default renderer
     * for object
     */
    public ClassBasedTableCellDDR() {
        this(new DefaultTableCellRenderer());
    }

    /**
     * Constructor specifies the default renderer for the Object class
     * @param renderer
     */
    public ClassBasedTableCellDDR(TableCellRenderer renderer) {
        setRenderer(Object.class, renderer);
    }

    /**
     * Sets a renderer for a specific java class (or interface).
     * @param aClass the class for which we set the renderer
     * @param aRenderer the renderer instance that will display the instances of the class.
     */
    public void setRenderer(Class<?> aClass,
                            TableCellRenderer aRenderer) {
        if (aRenderer != null) {
            renderersCache.put(aClass, aRenderer);
            explicitRenderers.put(aClass, aRenderer);
        } else {
            explicitRenderers.remove(aClass);
            renderersCache.clear();
        }
    }

    /**
     * Gets the proper renderer for the passed value.
     * @inheritDoc
     * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
     */
    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int row,
                                                   int column) {

        TableCellRenderer cr = null;
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
        
        Component component = cr.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        return component;
    }

    /**
     * Returns the "most appropriate" renderer for instances of <code>valClass</code>
     * We do a BFS run over the super-classes and interfaces of the object.
     * See article for full description.
     *
     * @param o the object whose super types we want to list
     * @return the super types, ordered by relevance.
     */
    protected TableCellRenderer getExplicitRenderer(Class<?> valClass) {
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
