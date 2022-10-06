package xyz.jpenilla.pluginbase.legacy;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@SuppressWarnings("unused")
@DefaultQualifier(NonNull.class)
public class WeightedRandom<E> {
    private final NavigableMap<Double, E> map = new TreeMap<>();
    private double total = 0;

    public WeightedRandom<E> add(final double weight, final E result) {
        if (weight <= 0) {
            return this;
        }
        this.total += weight;
        this.map.put(this.total, result);
        return this;
    }

    public E next(final Random random) {
        if (this.map.isEmpty()) {
            throw new IllegalStateException("No elements to select from");
        }
        return this.map.higherEntry(random.nextDouble() * this.total).getValue();
    }

    public E next() {
        return this.next(ThreadLocalRandom.current());
    }
}