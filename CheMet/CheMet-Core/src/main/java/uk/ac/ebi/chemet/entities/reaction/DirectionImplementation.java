/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.chemet.entities.reaction;

import uk.ac.ebi.interfaces.reaction.Direction;


/**
 * Enumeration class for storing reaction reversibility
 * @author johnmay
 */
public enum DirectionImplementation implements Direction {

    @Deprecated
    REVERSIBLE("\u21CC", "<==>", (byte) 1, false),
    @Deprecated
    IRREVERSIBLE_LEFT_TO_RIGHT("\u2192", "-->", (byte) 2, false),
    @Deprecated
    IRREVERSIBLE_RIGHT_TO_LEFT("\u2190", "<--", (byte) 3, false),
    @Deprecated
    UNKNOWN("<?>", "<?>", (byte) 4, true),
    FORWARD("\u2192", "-->", (byte) 5, false),
    BACKWARD("\u2190", "<--", (byte) 6, false),
    BIDIRECTIONAL("\u21CC", "<==>", (byte) 7, true);

    private String symbol;

    private String ascii;

    private boolean reversible;

    private byte index;


    private DirectionImplementation(String symbol, String ascii, Byte index, boolean reversible) {
        this.symbol = symbol;
        this.ascii = ascii;
        this.index = index;
        this.reversible = reversible;
    }


    public String getAscii() {
        return ascii;
    }


    @Override
    public String toString() {
        return this.symbol;
    }


    public byte getIndex() {
        return index;
    }


    public static DirectionImplementation valueOf(byte index) {
        for (DirectionImplementation reversibility : values()) {
            if (reversibility.index == index) {
                return reversibility;
            }
        }
        throw new UnsupportedOperationException("No reversibility of specified index");
    }


    public String getSymbol() {
        return this.symbol;
    }


    public boolean isReversible() {
        return reversible;
    }
}
