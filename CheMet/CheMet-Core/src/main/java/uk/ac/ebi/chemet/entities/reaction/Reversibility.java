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

    REVERSIBLE("\u21CC", "<==>", (byte) 1),
    IRREVERSIBLE_LEFT_TO_RIGHT("\u2192", "-->", (byte) 2),
    IRREVERSIBLE_RIGHT_TO_LEFT("\u2190", "<--", (byte) 3),
    UNKNOWN("<?>", "<?>", (byte) 4);

    private String symbol;

    private String ascii;

    private byte index;


    private Reversibility(String symbol, String ascii, Byte index) {
        this.symbol = symbol;
        this.ascii = ascii;
        this.index = index;
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


    public static Reversibility valueOf(byte index) {
        for (Reversibility reversibility : values()) {
            if (reversibility.index == index) {
                return reversibility;
            }
        }
        throw new UnsupportedOperationException("No reversibility of specified index");
    }
}
