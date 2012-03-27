package uk.ac.ebi.interfaces.reaction;

/**
 * Enumeration defines the direction reactions can proceeds.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
public enum Direction {

    /**
     * Defines the reaction direction as 'forward' or
     * 'left to right'.
     */
    FORWARD("\u2192", "-->", Boolean.FALSE),

    /**
     * Defines the reaction direction as 'backward' or
     * 'right to left'.
     */
    BACKWARD("\u2190", "<--", Boolean.FALSE),


    /**
     * Defines the reaction as bi-directional/reversible
     */
    BIDIRECTIONAL("\u21CC", "<==>", Boolean.TRUE),


    /**
     * Defines unknown direction
     */
    UNKNOWN("\u21af", "<?>", Boolean.TRUE);

    private String  utf8;
    private String  ascii;
    private Boolean reversible;

    private Direction(String  utf8,
                      String  ascii,
                      Boolean reversible) {
        this.utf8       = utf8;
        this.ascii      = ascii;
        this.reversible = reversible;
    }

    /**
     * Access the ascii value for the direction arrow
     * @return ascii arrow representation
     */
    public String getAscii() {
        return ascii;
    }

    /**
     * Access the uff8 arrow for the direction, useful
     * when displaying on uis.
     * @return utf8 arrow the given direction
     */
    public String getSymbol() {
        return this.utf8;
    }

    /**
     * Access the reversibility of this direction
     * @return whether this direction is irreversible
     */
    public Boolean isReversible() {
        return reversible;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String toString() {
        return getSymbol();
    }

}
