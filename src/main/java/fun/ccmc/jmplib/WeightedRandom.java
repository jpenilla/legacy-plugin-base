package fun.ccmc.jmplib;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Randomly select from object entries with weights
 *
 * @param <T> The type of object to randomly select
 */
public class WeightedRandom<T> {

    private class Entry {
        double accumulatedWeight;
        T object;
    }

    private List<Entry> entries = new ArrayList<>();
    private double accumulatedWeight;
    private Random rand = new Random();

    /**
     * Adds an entry taking the object and it's weight double
     *
     * @param object the object
     * @param weight the weight for selection
     */
    public void addEntry(T object, double weight) {
        accumulatedWeight += weight;
        Entry e = new Entry();
        e.object = object;
        e.accumulatedWeight = accumulatedWeight;
        entries.add(e);
    }

    /**
     * Draws from the added entries to the pool using weights
     *
     * @return The randomly selected object
     */
    public T getRandom() {
        double r = rand.nextDouble() * accumulatedWeight;

        for (Entry entry: entries) {
            if (entry.accumulatedWeight >= r) {
                return entry.object;
            }
        }
        return null;
    }
}