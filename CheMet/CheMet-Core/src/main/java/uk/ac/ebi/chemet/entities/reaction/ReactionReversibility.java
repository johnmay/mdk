/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.chemet.entities.reaction;

/**
 * Enumeration class for storing reaction reversibility
 * @author johnmay
 */
public enum ReactionReversibility {

    REVERSIBLE( "<===>" ),
    IRREVERSIBLE( "<=!=>" ),
    UNKNOWN( "<=?=>" );


    private String symbol;

    private ReactionReversibility( String symbol ) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return this.symbol;
    }
}
