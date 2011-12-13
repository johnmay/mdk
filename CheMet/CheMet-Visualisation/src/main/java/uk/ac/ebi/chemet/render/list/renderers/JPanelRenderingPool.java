/**
 * PanelPool.java
 *
 * 2011.12.12
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
package uk.ac.ebi.chemet.render.list.renderers;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import org.apache.log4j.Logger;

/**
 *          PanelPool - 2011.12.12 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public abstract class JPanelRenderingPool<O>
        implements PoolBasedListRenderer<O> {

    private static final Logger LOGGER = Logger.getLogger(JPanelRenderingPool.class);
    private long expirationTime;
    private BiMap<O, JComponent> pool = HashBiMap.create();
    private Map<JComponent, Long> locked, unlocked;

    public JPanelRenderingPool() {
        expirationTime = 15000;
        locked = new HashMap();
        unlocked = new HashMap();
    }

    public abstract JPanel create(O object);

    /**
     * Validates/sets up the new object
     * @param panel
     * @param object
     * @return
     */
    public abstract boolean validate(JComponent component,
                                     O object);

    public abstract void expire(JComponent component);

    public synchronized JComponent checkOut(O obj) {

        if (pool.containsKey(obj)) {
            JComponent panel = pool.get(obj);
            locked.put(panel, expirationTime);
            return panel;
        }

        long now = System.currentTimeMillis();
        JComponent component;
        if (unlocked.size() > 0) {
            Iterator<JComponent> it = unlocked.keySet().iterator();
            while (it.hasNext()) {
                component = it.next();

                // remove object if has existed for longer then the expired time
                if ((now - ((Long) unlocked.get(component)).longValue())
                    > expirationTime) {
                    // object has expired
                    unlocked.remove(component);
                    pool.inverse().remove(component); // remove from pool
                    expire(component);
                    component = null;
                } else {
                    if (component != null && validate(component, obj)) {
                        unlocked.remove(component);
                        locked.put(component, new Long(now));
                        pool.inverse().remove(component); // remove old association
                        pool.put(obj, component);
                        return (component);
                    } else {
                        // object setup validation
                        unlocked.remove(component);
                        expire(component);
                        component = null;
                    }
                }
            }
        }
        // no objects available, create a new one
        component = create(obj);
        validate(component, obj);
        pool.put(obj, component);
        locked.put(component, new Long(now));
        return (component);
    }

    public synchronized void checkIn(O obj) {
        if (pool.containsKey(obj)) {
            checkIn(pool.get(obj));
        }
    }

    public synchronized void checkIn(JComponent component) {
        locked.remove(component);
        unlocked.put(component, new Long(System.currentTimeMillis()));
    }

    public Component getListCellRendererComponent(JList list,
                                                  Object value,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus) {
        JComponent component = checkOut((O) value);
        component.setBackground(isSelected ? Color.BLACK : Color.WHITE);
        component.setForeground(isSelected ? Color.WHITE : Color.BLACK);
        return component;
    }
}
