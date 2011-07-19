/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the Lesser GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package uk.ac.ebi.metabolomes.core.reaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.openscience.cdk.Reaction;
import uk.ac.ebi.metabolomes.identifier.InChI;

/**
 * ProxyReaction.java
 * Proxy reaction stores the Product and compounds as InChIs
 *
 * @author johnmay
 * @date Apr 18, 2011
 */
class ProxyReaction
        extends Reaction
        implements Serializable {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( ProxyReaction.class );
    private static final long serialVersionUID = 1311431275054437049L;
    protected  ArrayList<InChI> inchiReactants = new ArrayList<InChI>();
    protected ArrayList<InChI> inchiProducts = new ArrayList<InChI>();
    // need to store the coefficients also as the CDK reaction ones are linked to the IMolecules (Ithink)
    protected ArrayList<Double> reactantCoefficients = new ArrayList<Double>();
    protected ArrayList<Double> productCoefficients = new ArrayList<Double>();

    public ProxyReaction() {
    }

    public ArrayList<InChI> getInchiProducts() {
        return new ArrayList<InChI>( Collections.unmodifiableList( inchiProducts ) );
    }

    public ArrayList<InChI> getInchiReactants() {
        return new ArrayList<InChI>( Collections.unmodifiableList( inchiReactants ) );
    }

    public ArrayList<Double> getInChIProductCoefficients() {
        return productCoefficients;
    }

    public ArrayList<Double> getInChIReactantCoefficients() {
        return reactantCoefficients;
    }

    public void addInChIProduct( InChI product , double coefficient ) {
        inchiProducts.add( product );
        productCoefficients.add( coefficient );
    }

    public void addInChIReactant( InChI reactant , double coefficient ) {
        inchiReactants.add( reactant );
        reactantCoefficients.add( coefficient );
    }
}
