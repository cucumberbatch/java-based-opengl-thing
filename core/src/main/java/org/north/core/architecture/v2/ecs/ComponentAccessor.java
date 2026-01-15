package org.north.core.architecture.v2.ecs;

/**
 * Provides typed access to components within a {@link QueryResult} scope.
 *
 * <p>A {@link ComponentAccessor} is created by {@link QueryResult#accessor(Class)} and
 * provides efficient, cache-friendly access to components of type {@code C} for all
 * entities {@code E} returned by the parent {@link QueryResult}. It encapsulates the
 * mapping from entity IDs to component storage locations (archetypes, chunks, slots).</p>
 *
 * <p><b>Key guarantees:</b></p>
 * <ul>
 *   <li>Zero-allocation access during iteration (direct column/slot access)</li>
 *   <li>Type-safe component retrieval matching the query specification</li>
 *   <li>Scoped lifetime tied to parent {@link QueryResult} (AutoCloseable)</li>
 * </ul>
 *
 * <p>Typical usage within a system:
 * <pre>{@code
 *     try (QueryResult<EntityId> q = em.queryAllWith(Position.class, Velocity.class)) {
 *         ComponentAccessor<EntityId, Position> positions  = q.accessor(Position.class);
 *         ComponentAccessor<EntityId, Velocity> velocities = q.accessor(Velocity.class);
 *
 *         for (EntityId entity : q) {
 *             Position pos = positions.get(entity);
 *             Velocity vel = velocities.get(entity);
 *             // Update logic...
 *         }
 *     }
 * }</pre></p>
 *
 * @param <E> the entity type (typically {@code EntityId} or {@code long})
 * @param <C> the component type accessed by this accessor
 * @see QueryResult#accessor(Class)
 */
public interface ComponentAccessor<E, C> {

    /**
     * Retrieves the component instance for the given entity.
     *
     * <p>This method provides direct, zero-allocation access to the component
     * stored in the appropriate archetype/chunk column for the specified entity.
     * The entity <b>must</b> be from the parent {@link QueryResult} iteration
     * to ensure valid mapping.</p>
     *
     * @param entity the entity from the parent {@link QueryResult}
     * @return the component instance for the given entity
     * @throws IllegalArgumentException if the entity is not part of the current query result
     */
    C get(E entity);

    /**
     * Returns the component class/type this accessor provides access to.
     *
     * <p>This is the exact {@link Class} passed to
     * {@link QueryResult#accessor(Class)} and guaranteed to match the
     * components accessible via {@link #get(Object)}.</p>
     *
     * @return the component type class
     */
    Class<C> getComponentType();

    /**
     * Returns the access type specified for this accessor.
     *
     * <p>Determines concurrent access semantics for multithreaded execution:
     * <ul>
     *   <li>{@link AccessType#READ}: Multiple readers can access concurrently</li>
     *   <li>{@link AccessType#WRITE}: Exclusive access, blocks other readers/writers</li>
     * </ul>
     * Used by the ECS scheduler for dependency resolution and parallel execution planning.</p>
     *
     * @return the access type ({@link AccessType#READ} or {@link AccessType#WRITE})
     * @see AccessType
     */
    AccessType getAccessType();
}
