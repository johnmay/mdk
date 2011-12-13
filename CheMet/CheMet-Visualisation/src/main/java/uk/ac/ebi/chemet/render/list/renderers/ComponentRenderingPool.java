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

import com.google.common.base.Objects;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
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
public abstract class ComponentRenderingPool<O>
        implements PoolBasedListRenderer<O> {

    private static final Logger LOGGER = Logger.getLogger(ComponentRenderingPool.class);
    private long expirationTime;
    private BiMap<O, JComponent> prerenderPool = HashBiMap.create();
    private Set<AgingComponent> lockedSet = new HashSet();
    private PriorityQueue<AgingComponent> unlocked;
    private Map<JComponent, AgingComponent> componentMap = new HashMap();

    public ComponentRenderingPool() {
        expirationTime = 10000; // ten second expiry
        unlocked = new PriorityQueue<AgingComponent>(200);
    }

    /**
     * Populates the unlocked queue with specified
     * number of items
     * @param n
     */
    private void populate(int n) {
        for (int i = 0; i < n; i++) {
            unlocked.add(new AgingComponent(create()));
        }
    }

    public abstract JComponent create();

    /**
     * Validates/sets up the new object
     * @param panel
     * @param object
     * @return
     */
    public abstract boolean setup(JComponent component,
                                  O object);

    public abstract void expire(JComponent component);

    private void remove(AgingComponent wrapper) {
        JComponent component = wrapper.getComponent();
        unlocked.remove(wrapper);
        prerenderPool.inverse().remove(component);
        componentMap.remove(component);
        expire(component);
        component = null;
        wrapper = null;
    }

    public synchronized JComponent checkOut(O value) {

        if (prerenderPool.containsKey(value)) {
            JComponent panel = prerenderPool.get(value);
            unlocked.remove(componentMap.get(panel));
            lockedSet.add(componentMap.get(panel).resetTimestamp());
            return panel;
        }

        long currentTime = System.currentTimeMillis();

        if (unlocked.size() > 0) {

            while (!unlocked.isEmpty()
                   && unlocked.peek().isExpired(currentTime)) {
                remove(unlocked.poll());
            }

            AgingComponent wrapper = unlocked.poll(); // oldest item
            if (wrapper != null) {
                JComponent component = wrapper.getComponent();

                if (setup(component, value)) {
                    unlocked.remove(wrapper);
                    prerenderPool.inverse().remove(component);
                    prerenderPool.put(value, component);
                    lockedSet.add(wrapper.resetTimestamp());
                    return component;
                } else {
                    LOGGER.fatal("Component failed setup. This is not recoverable");
                }
            }


        }

        JComponent component = create();
        setup(component, value);
        AgingComponent wrapper = new AgingComponent(component);
        lockedSet.add(wrapper);
        componentMap.put(component, wrapper);
        prerenderPool.put(value, component);
        return component;

    }

    public synchronized void checkIn(O value) {
        if (prerenderPool.containsKey(value)) {
            checkIn(prerenderPool.get(value));
        }
    }

    public synchronized void checkIn(JComponent component) {
        AgingComponent wrapper = componentMap.get(component);
        lockedSet.remove(wrapper);
        unlocked.add(wrapper.resetTimestamp());
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

    private class AgingComponent
            implements Comparable<AgingComponent> {

        private final JComponent component;
        private Long age;

        public AgingComponent(JComponent component) {
            this.component = component;
            age = System.currentTimeMillis();
        }

        public AgingComponent resetTimestamp() {
            age = System.currentTimeMillis();
            return this;
        }

        public JComponent getComponent() {
            return component;
        }

        public boolean isExpired(Long now) {
            return (now - age) > expirationTime;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(component, age);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final AgingComponent other = (AgingComponent) obj;
            if (this.component != other.component && (this.component == null || !this.component.equals(other.component))) {
                return false;
            }
            if (this.age != other.age && (this.age == null || !this.age.equals(other.age))) {
                return false;
            }
            return true;
        }

        public int compareTo(AgingComponent other) {
            return this.age.compareTo(other.age);
        }
    }
}