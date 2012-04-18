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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import javax.swing.JComponent;
import org.apache.log4j.Logger;

/**
 *          PanelPool - 2011.12.12 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public abstract class ComponentRenderingPool<C extends JComponent, O> {

    private static final Logger LOGGER = Logger.getLogger(ComponentRenderingPool.class);
    private long expirationTime;
    private BiMap<O, C> prerenderPool = HashBiMap.create();
    private Set<AgingComponent> locked = new HashSet();
    private PriorityQueue<AgingComponent> unlocked;
    private Map<C, AgingComponent> componentMap = new HashMap();

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

    public abstract C create();

    /**
     * Validates/sets up the new object
     * @param panel
     * @param object
     * @return
     */
    public abstract boolean setup(C component,
                                  O object);

    public abstract void expire(C component);

    private void remove(AgingComponent wrapper) {
        C component = wrapper.getComponent();
        if (locked.contains(wrapper)) {
            LOGGER.error("Hmm that's odd?");
        }
        prerenderPool.inverse().remove(component);
        componentMap.remove(component);
        expire(component);
        component = null;
        wrapper = null;
    }

    public synchronized C checkOut(O value) {

        LOGGER.debug("Atempting check out component for " + value);

        if (prerenderPool.containsKey(value)) {
            LOGGER.debug("...using prerendered pool");
            C panel = prerenderPool.get(value);
            unlocked.remove(componentMap.get(panel));
            if (!locked.contains(componentMap.get(panel))) {
                locked.add(componentMap.get(panel));
            }
            return panel;
        }

        long currentTime = System.currentTimeMillis();

        LOGGER.debug(getClass().getSimpleName() + " unlocked size: " + unlocked.size());
        LOGGER.debug(getClass().getSimpleName() + " locked size: " + locked.size());

        if (unlocked.size() > 0) {

            while (!unlocked.isEmpty()
                   && unlocked.peek().isExpired(currentTime)) {
                remove(unlocked.poll());
                LOGGER.debug("Removing expired item");
            }

            AgingComponent wrapper = unlocked.poll(); // oldest item
            if (wrapper != null) {
                C component = wrapper.getComponent();

                if (setup(component, value)) {
                    LOGGER.debug("...using unlocked pooled");
                    unlocked.remove(wrapper);
                    prerenderPool.inverse().remove(component);
                    prerenderPool.put(value, component);
                    locked.add(wrapper.resetTimestamp());
                    return component;
                } else {
                    LOGGER.fatal("Component failed setup. This is not recoverable");
                }
            }

        }

        LOGGER.debug("...creating new");
        C component = create();
        setup(component, value);
        AgingComponent wrapper = new AgingComponent(component);
        locked.add(wrapper);
        componentMap.put(component, wrapper);
        prerenderPool.put(value, component);
        return component;

    }

    public synchronized void checkIn(O value) {
        LOGGER.debug("Checking in " + value);
        if (prerenderPool.containsKey(value)) {
            checkIn(prerenderPool.get(value));
        } else {
            LOGGER.debug("Attepting check-in on non managed object");
        }
    }

    public synchronized void checkIn(C component) {
        AgingComponent wrapper = componentMap.get(component);
        locked.remove(wrapper);
        unlocked.add(wrapper.resetTimestamp());
    }

    private class AgingComponent
            implements Comparable<AgingComponent> {

        private final C component;
        private Long age;

        public AgingComponent(C component) {
            this.component = component;
            age = System.currentTimeMillis();
        }

        public AgingComponent resetTimestamp() {
            age = System.currentTimeMillis();
            return this;
        }

        public C getComponent() {
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