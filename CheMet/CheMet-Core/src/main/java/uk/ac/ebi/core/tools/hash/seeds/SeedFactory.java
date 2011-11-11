/**
 * SeedFactory.java
 *
 * 2011.11.11
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
package uk.ac.ebi.core.tools.hash.seeds;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *          SeedFactory - 2011.11.11 <br>
 *          Factory provides access to MolecularHashFactory seeding methods.
 *          Seeds should only be instantiable from the factory as providing two
 *          identical seeds to the {@see uk.ac.ebi.core.tools.hash.MolecularHashFactory}
 *          could be troublesome. Centralising the object creation through this
 *          factory ensure that the {@see AtomSeed} {@see java.util.Set} only
 *          contains unique methods
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class SeedFactory {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SeedFactory.class);
    private Map<Class<? extends AtomSeed>, AtomSeed> methods = new HashMap();

    private SeedFactory() {

        for (AtomSeed method : Arrays.asList(new AtomicNumberSeed(),
                                             new BondOrderSumSeed(),
                                             new ConnectedAtomSeed(),
                                             new StereoSeed(),
                                             new ChargeSeed())) {
            methods.put(method.getClass(), method);
        }
    }

    public static SeedFactory getInstance() {
        return SeedFactoryHolder.INSTANCE;
    }

    private static class SeedFactoryHolder {

        private static final SeedFactory INSTANCE = new SeedFactory();
    }


    /**
     *
     * Access the seed for the given class type.
     *
     * <pre>
     *      SeedFactory.getInstance().getSeed(ChrageSeed.class);
     * </pre>
     *
     * @param seedClass The class of seed method to get
     * @return The AtomSeed method for use in MolecularHashFactory
     *
     */
    public AtomSeed getSeed(Class<? extends AtomSeed> seedClass) {
        return methods.get(seedClass);
    }
}
