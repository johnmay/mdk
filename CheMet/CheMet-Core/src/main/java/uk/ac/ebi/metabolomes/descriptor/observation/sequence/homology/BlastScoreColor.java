package uk.ac.ebi.metabolomes.descriptor.observation.sequence.homology;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.Color;

/**
 * BlastScoreColor.java
 *
 *
 * @author johnmay
 * @date May 5, 2011
 */
// use the new visualisation package
@Deprecated
public enum BlastScoreColor {

    VERY_LOW( 0 , 40 , new Color( Integer.parseInt( "111111" , 16 ) ) ),
    LOW( 40 , 50 , new Color( Integer.parseInt( "3311FF" , 16 ) ) ),
    MEDIUM( 50 , 80 , new Color( Integer.parseInt( "33FF11" , 16 ) ) ),
    HIGH( 80 , 200 , new Color( Integer.parseInt( "FF11FF" , 16 ) ) ),
    VERY_HIGH( 200 , Integer.MAX_VALUE , new Color( Integer.parseInt( "FF1133" , 16 ) ) ),
    UNKNOWN( -1, -1 , Color.WHITE );
    private Integer min;
    private Integer max;
    private Color color;

    private BlastScoreColor( Integer min , Integer max , Color color ) {
        this.min = min;
        this.max = max;
        this.color = color;
    }

    /**
     * Returns the required object given a bit score
     * @param bitScore
     * @return
     */
    public static BlastScoreColor getColorForScore( int bitScore ) {
        for ( BlastScoreColor blastScoreColor : values() ) {
            if ( blastScoreColor.isInRange( bitScore ) ) {
                return blastScoreColor;
            }
        }
        return UNKNOWN;
    }

    /**
     * Set the color of the
     * @param color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Returns the max value
     * @return
     */
    public Integer getMax() {
        return max;
    }

    /**
     * Returns the min value
     * @return
     */
    public Integer getMin() {
        return min;
    }

    /**
     * Tests whether the provided bit score is within range
     * of this objects values
     * @param bitScore
     * @return T/F whether the bitscore is in the range of the object min/max limits
     */
    public boolean isInRange( Integer bitScore ) {
        if ( min <= bitScore && max > bitScore ) {
            return true;
        }
        return false;
    }
}
