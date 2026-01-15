package org.north.core.architecture.v2.ecs;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface EntityManager<E> {

    E create();
    //E batchCreate(int count);

    Object[] createEntity(Class<?>... componentTypes);

    <C> C add(E entity, C component);
    <C> C add(E entity, Class<C> componentType);
    <C> C get(E entity, Class<C> componentType);
    <C> boolean has(E entity, Class<C> componentType);
    <C> boolean remove(E entity, Class<C> componentType);

    QueryResult<E> queryAllWith(Class<?>... componentTypes);

    <C>      void forAllWith(Class<C> componentType, Consumer<C> processor);
    <C1, C2> void forAllWith(Class<C1> firstComponentType, Class<C2> secondComponentType, BiConsumer<C1, C2> processor);
}
