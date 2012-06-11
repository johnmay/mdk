package uk.ac.ebi.mdk.tool.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Class used for counting occurrences of an object type. Implementation
 * uses a HashMap and MutableInt's to increment the values when an object
 * occurrence is registered (see. {@see #register(O)})
 *
 * @param <O> object type
 *
 * @author John May
 */
public class OccurrenceCounter<O> {

    private Map<O, MutableInt> map = new HashMap<O, MutableInt>();

    /**
     * Registers the occurrence of an object. If this is the first occurrence
     * a new 'MutableInt is created'
     *
     * @param obj
     *
     * @return the current count
     */
    public int register(O obj) {
        MutableInt mutableInt = map.get(obj);
        if (mutableInt == null) {
            map.put(obj, new MutableInt());
            return 1;
        }
        return mutableInt.increment();
    }

    /**
     * Access the number of times each occurrence has been seen
     *
     * @param obj
     *
     * @return number of times the object has been seen. 0 if never seen.
     */
    public int getOccurrences(O obj) {
        MutableInt mutableInt = map.get(obj);
        return mutableInt == null ? 0 : mutableInt.get();
    }


    public void addAll(OccurrenceCounter<O> other) {
        for (Map.Entry<O, MutableInt> e : other.map.entrySet()) {
            O obj = e.getKey();
            map.put(obj, new MutableInt(this.getOccurrences(e.getKey()) +
                                                other.getOccurrences(e.getKey())));
        }
    }

    /**
     * Reset the counting map by clearing it's contents
     */
    public void clear() {
        map.clear();
    }

}
