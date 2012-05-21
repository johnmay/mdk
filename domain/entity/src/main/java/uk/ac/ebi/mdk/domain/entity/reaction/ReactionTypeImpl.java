/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.mdk.domain.entity.reaction;


/**
 * Enumeration class for storing reaction reversibility
 * @author johnmay
 */
public enum ReactionTypeImpl
        implements ReactionType {

    ENZYMATIC("Enzymatic Reaction", (byte) 5),
    SPONTANEOUS("Spontaneous Reaction", (byte) 5),
    GENERIC("Generic reaction", (byte) 5),
    TRANSPORT("Transport reaction", (byte) 6),
    EXCHANGE("Exchange reaction", (byte) 7),
    UNKNOWN("Unknown reaction", (byte) 8);

    private String description;

    private byte index;


    private ReactionTypeImpl(String description, byte index) {
        this.description = description;
        this.index = index;
    }


    public byte getIndex() {
        return index;
    }


    public static ReactionTypeImpl valueOf(byte index) {
        for (ReactionTypeImpl reversibility : values()) {
            if (reversibility.index == index) {
                return reversibility;
            }
        }
        throw new UnsupportedOperationException("No type of specified index");
    }


    public String getDescription() {
        return description;
    }
}
