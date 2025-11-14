package org.north.core.architecture.v2.component;

import java.util.BitSet;
import java.util.Objects;

public class Archetype {
    private final BitSet mask;

    public Archetype(BitSet mask) {
        this.mask = mask;
    }

    public boolean isSame(Archetype archetype) {
        return mask.equals(archetype.mask);
    }

    public boolean isContains(Archetype archetype) {
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
}
