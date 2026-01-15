package org.north.core.architecture.v2.ecs.impl;

import java.util.*;
import java.util.stream.IntStream;

final class ComponentRegistry {
    private final Map<Class<?>, Integer> typeToIdMap;
    private final Class<?>[]             types;

    ComponentRegistry(Collection<Class<?>> componentTypes) {
        types       = componentTypes.toArray(Class[]::new);
        typeToIdMap = new IdentityHashMap<>();
        IntStream.range(0, types.length).forEach(index -> typeToIdMap.put(types[index], index));
    }

    private ComponentRegistry(Builder builder) {
        types       = builder.types.toArray(Class[]::new);
        typeToIdMap = new IdentityHashMap<>();

        for (int index = 0; index < types.length; index++) {
            Class<?> type = types[index];
            if (typeToIdMap.put(type, index) != null) {
                throw new IllegalArgumentException(String.format("Component of type %s already exists in registry", type.getName()));
            }
        }
    }

    static class Builder {
        private final Set<Class<?>> types = new HashSet<>();

        public Builder add(Class<?> type) {
            types.add(type);
            return this;
        }

        public ComponentRegistry build() {
            return new ComponentRegistry(this);
        }
    }

    public int getComponentTypeId(Class<?> type) {
        return typeToIdMap.get(type);
    }

    public Class<?> getComponentType(int typeId) {
        return types[typeId];
    }

    public BitSet generateBitmaskByTypes(Class<?>[] types) {
        BitSet mask = new BitSet();
        for (Class<?> type : types) {
            int bit = typeToIdMap.get(type);
            mask.set(bit);
        }
        return mask;
    }

    public Archetype createArchetype(Class<?>[] types) {
        return new Archetype(types, generateBitmaskByTypes(types));
    }

}
