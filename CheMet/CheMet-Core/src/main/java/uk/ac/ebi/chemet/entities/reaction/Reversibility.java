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

    REVERSIBLE( "\u21CC" ),
    IRREVERSIBLE_LEFT_TO_RIGHT( "\u2192" ),
    IRREVERSIBLE_RIGHT_TO_LEFT( "\u2190" ),
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

