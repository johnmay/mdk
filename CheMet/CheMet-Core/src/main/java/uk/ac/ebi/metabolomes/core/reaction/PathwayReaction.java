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

import uk.ac.ebi.metabolomes.core.reaction.matrix.InChIStoichiometricMatrix;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import uk.ac.ebi.metabolomes.identifier.ECNumber;

/**
 * PathwayReaction.java
 *
 *
 * @author johnmay
 * @date Apr 19, 2011
 */
public class PathwayReaction {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( PathwayReaction.class );
    private static LinkedHashMap<ECNumber , Integer> visitedECs = new LinkedHashMap<ECNumber , Integer>();
    private static InChIStoichiometricMatrix s;
    private ECNumber enzymeNumber;
    private PathwayReaction[] next;
    private PathwayReaction[] previous;

    public PathwayReaction( ECNumber ec ) {
        this.enzymeNumber = ec;
        visitedECs.put( ec , visitedECs.containsKey( ec ) ? visitedECs.get( ec ) + 1 : 0 );
    }

    public static void setStoichiometricMatrix( InChIStoichiometricMatrix s ) {
        PathwayReaction.s = s;
    }

    public static InChIStoichiometricMatrix getStoichiometricMatrix() {
        return s;
    }

    /**
     * Trace the pathway of the reaction using the set stoichiometric matrix
     * this adds values to the next and previous arrays
     */
    public void tracePathways() {
//        Object[] metaboliteBalances = s.getValuesForReaction( enzymeNumber );
//
//        for ( int i = 0; i < metaboliteBalances.length; i++ ) {
//            Double balance = ( Double ) metaboliteBalances[i];
//            if ( balance != null ) {
//                if ( balance > 0 ) {
//                    System.out.println( s.getMolecule( i ).getName() );
//
//                    // producing this metabolite so find reactions that consume it
//                    ArrayList<ECNumber> consuming = s.getReactionsConsumingMolecule( s.getMolecule( i ) );
//                    next = new PathwayReaction[ consuming.size() ];
//                    for ( ECNumber upstreamreaction : consuming ) {
//                        System.out.println( "+" + upstreamreaction + " : " + s.getMolecule( i ).getName() );
//                    }
//                }
//                if ( balance < 0 ) {
//                    System.out.println( s.getMolecule( i ).getName() );
//
//                    // consuming this metabolite so find reactions that produce it
//                    ArrayList<ECNumber> producing = s.getReactionsProducingMolecule( s.getMolecule( i ) );
//                    previous = new PathwayReaction[ producing.size() ];
//                    for ( ECNumber downstreamreaction : producing ) {
//                        System.out.println( "-" + downstreamreaction + " : " + s.getMolecule( i ).getName() );
//                    }
//
//                }
//            }
//        }
    }
}
