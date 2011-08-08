/**
 * GenericBiochemicalReaction.java
 *
 * 2011.08.08
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
package uk.ac.ebi.chemet.entities.reaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.log4j.Logger;
import uk.ac.ebi.metabolomes.core.gene.GeneProduct;

/**
 * @name    GenericBiochemicalReaction
 * @date    2011.08.08
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 * @brief   Class extends GenericReaction but adds the option to add a modifier (i.e. enzyme)
 *
 */
public class GenericBiochemicalReaction<M , S extends Comparable , C extends Comparable>
        extends GenericReaction<M , S , C> {

    private static final Logger LOGGER = Logger.getLogger( GenericBiochemicalReaction.class );
    private List<GeneProduct> modifiers;

    public GenericBiochemicalReaction( Comparator<M> moleculeComparator ) {
        this( moleculeComparator , new ArrayList<GeneProduct>() );
    }

    public GenericBiochemicalReaction( Comparator<M> moleculeComparator , List<GeneProduct> modifiers ) {
        super( moleculeComparator );
        this.modifiers = modifiers;
    }

    public List<GeneProduct> getModifiers() {
        return Collections.unmodifiableList( modifiers );
    }

    public void addModifier( GeneProduct modifier ) {
        this.modifiers.add( modifier );
    }
}
