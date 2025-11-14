package org.north.core.architecture.v2.component;

import java.util.Arrays;
import java.util.BitSet;
import java.util.IdentityHashMap;
import java.util.Map;

public class ComponentRegistry {
    private final Map<Class<?>, Integer> typeToIdMap;
    private final Class<?>[] types;

    private ComponentRegistry(Builder builder) {
        types       = builder.types;
        typeToIdMap = new IdentityHashMap<>();

        for (int index = 0; index < types.length; index++) {
            Class<?> type = types[index];
            if (typeToIdMap.put(type, index) != null) {
                throw new IllegalArgumentException(String.format("Component of type %s already exists in registry", type.getName()));
            }
        }
    }

    public static class Builder {
        private Class<?>[] types = new Class[0];

        public Builder add(Class<?> type) {
            int length = types.length;
            types = Arrays.copyOf(types, length + 1);
            types[length] = type;
            return this;
        }

        public ComponentRegistry build() {
            return new ComponentRegistry(this);
        }
    }

    public int getIdByComponentType(Class<?> type) {
        return typeToIdMap.get(type);
    }

    public Class<?> getComponentTypeById(int id) {
        return types[id];
    }

    public Archetype createArchetype(Class<?>[] types) {
        BitSet mask = new BitSet();
        for (Class<?> type : types) {
            int bit = typeToIdMap.get(type);
            mask.set(bit);
        }
        return new Archetype(mask);
    }

}
