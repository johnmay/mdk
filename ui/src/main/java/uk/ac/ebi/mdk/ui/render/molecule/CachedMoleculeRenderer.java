/**
 * CachedMoleculeRenderer.java
 *
 * 2012.01.27
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
package uk.ac.ebi.mdk.ui.render.molecule;

import com.google.common.base.Objects;
import org.apache.log4j.Logger;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * CachedMoleculeRenderer 2012.01.27
 *
 * @author johnmay
 * @author $Author$ (this version)
 *         <p/>
 *         Class description
 * @version $Rev$ : Last Changed $Date$
 */
public class CachedMoleculeRenderer extends MoleculeRenderer {

    private static final Logger LOGGER = Logger.getLogger(CachedMoleculeRenderer.class);

    private int cacheSize = 250;

    private Map<CachingKey, BufferedImage> cache = new LinkedHashMap<CachingKey, BufferedImage>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry<CachingKey, BufferedImage> eldest) {
            return size() > cacheSize;
        }
    };

    @Override
    public BufferedImage getImage(IAtomContainer molecule, Rectangle bounds, Color background) throws CDKException {

        CachingKey key = new CachingKey(molecule, background, bounds);

        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        BufferedImage img = super.getImage(molecule, bounds, background);
        cache.put(key, img);

        return img;

    }


    public static CachedMoleculeRenderer getInstance() {
        return CachedMoleculeRenderer.MoleculeRendererHolder.INSTANCE;
    }


    private static class MoleculeRendererHolder {

        private static final CachedMoleculeRenderer INSTANCE = new CachedMoleculeRenderer();
    }


    private class CachingKey {

        private IAtomContainer atomContainer;

        private Color background;

        private Rectangle dimensions;


        public CachingKey(IAtomContainer atomContainer, Color background, Rectangle dimensions) {
            this.atomContainer = atomContainer;
            this.background = background;
            this.dimensions = dimensions;
        }


        @Override
        public int hashCode() {
            return Objects.hashCode(atomContainer, background, dimensions);
        }


        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final CachingKey other = (CachingKey) obj;
            if (this.atomContainer != other.atomContainer && (this.atomContainer == null || !this.atomContainer.equals(other.atomContainer))) {
                return false;
            }
            if (this.background != other.background && (this.background == null || !this.background.equals(other.background))) {
                return false;
            }
            if (this.dimensions != other.dimensions && (this.dimensions == null || !this.dimensions.equals(other.dimensions))) {
                return false;
            }
            return true;
        }
    }
}
