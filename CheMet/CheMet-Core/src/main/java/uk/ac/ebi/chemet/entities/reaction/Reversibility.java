/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.chemet.entities.reaction;


/**
 * Enumeration class for storing reaction reversibility
 * @author johnmay
 */
public enum Reversibility {

    REVERSIBLE( "<==>" ),
    IRREVERSIBLE_LEFT_TO_RIGHT( "-->" ),
    IRREVERSIBLE_RIGHT_TO_LEFT( "<--" ),
    UNKNOWN( "<?>" );
    private String symbol;


    private Reversibility( String symbol ) {
        this.symbol = symbol;
    }


    @Override
    public String toString() {
        return this.symbol;
    }


}

