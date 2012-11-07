package uk.ac.ebi.mdk.prototype.hash.util;

/**
 * Small class used when counting occurrences
 *
 * @author John May
 */
public class MutableInt {

    private int value;

    public MutableInt(int value) {
        this.value = value;
    }

    /**
     * Start at 1
     */
    public MutableInt() {
        this(1);
    }

    public int increment() {
        return ++value;
    }

    public int get() {
        return value;
    }

    public Integer getInteger() {
        return Integer.valueOf(value);
    }

    @Override
    public String toString() {
        return getInteger().toString();
    }
}
