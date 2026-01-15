package org.north.core.architecture.v2.ecs.impl;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Objects;

final class Archetype {
    private final Class<?>[] componentTypes;
    private final BitSet     mask;

    public Archetype(Class<?>[] componentTypes, BitSet mask) {
        this.componentTypes = componentTypes;
        this.mask           = mask;
    }

    public boolean isSame(Archetype archetype) {
        return mask.equals(archetype.mask);
    }

    public boolean contains(Archetype archetype) {
        BitSet checkingMask = (BitSet) mask.clone();
        checkingMask.and(archetype.mask);
        return checkingMask.equals(archetype.mask);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Archetype archetype = (Archetype) o;
        return Objects.equals(mask, archetype.mask);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mask);
    }

    @Override
    public String toString() {
        return "Archetype{" +
                "componentTypes=" + Arrays.toString(componentTypes) +
                ", mask=" + mask +
                '}';
    }
}
