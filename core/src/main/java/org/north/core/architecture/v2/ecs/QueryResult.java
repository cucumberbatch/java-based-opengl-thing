package org.north.core.architecture.v2.ecs;

/**
 * Represents a filtered view of entities matching specific component requirements.
 *
 * <p>A {@link QueryResult} provides efficient iteration over entities that have all
 * the components specified in {@link EntityManager#queryAllWith queryAllWith(...)}.
 * It enables cache-friendly access to components through type-safe
 * {@link ComponentAccessor}s without scanning the entire entity storage.</p>
 *
 * <p><b>Key characteristics:</b></p>
 * <ul>
 *   <li>Zero-copy iteration over matching archetype/chunks</li>
 *   <li>Cache-optimal column access via {@link ComponentAccessor}</li>
 *   <li>Scoped lifetime via {@code try-with-resources}</li>
 *   <li>Thread-safety controlled by {@link AccessType}</li>
 * </ul>
 *
 * <p><b>Lifecycle:</b> Always use within try-with-resources to ensure proper cleanup
 * of internal iterators and deferred operations. The {@link #close()} method applies
 * any scheduled modifications and releases chunk iteration state.</p>
 *
 * <p>Typical usage pattern:
 * <pre>{@code
 *     try (QueryResult<EntityId> result = em.queryAllWith(Position.class, Velocity.class)) {
 *         ComponentAccessor<EntityId, Position> positions  = result.accessor(Position.class);
 *         ComponentAccessor<EntityId, Velocity> velocities = result.accessor(Velocity.class);
 *
 *         for (EntityId entity : result) {
 *             Position pos = positions.get(entity);
 *             Velocity vel = velocities.get(entity);
 *             // System logic here...
 *         }
 *     }
 * }</pre></p>
 *
 * <p>Only entities guaranteed to have <em>all</em> queried components are returned
 * by iteration. Component accessors provide direct column access without validation.</p>
 *
 * @param <E> the entity type (typically {@code EntityId} or {@code long})
 * @see EntityManager#queryAllWith(Class[])
 * @see ComponentAccessor
 * @see AccessType
 */
public interface QueryResult<E> extends Iterable<E>, AutoCloseable {

    /**
     * Creates a {@link ComponentAccessor} for the specified component type with
     * default {@link AccessType#READ} access.
     *
     * <p>The component type <b>must</b> be one of the types specified in the
     * parent {@link EntityManager#queryAllWith queryAllWith(...)} call that created
     * this {@link QueryResult}.</p>
     *
     * @param componentType the component class to access
     * @return a type-safe accessor for the specified component
     * @throws IllegalArgumentException if the component type was not part of the query
     */
    <C> ComponentAccessor<E, C> accessor(Class<C> componentType);

    /**
     * Creates a {@link ComponentAccessor} for the specified component type with
     * explicit access control for concurrent execution.
     *
     * <p>Allows systems to declare their intent (read-only vs. read-write) for
     * dependency resolution and parallel scheduling. The component type <b>must</b>
     * be part of the original query specification.</p>
     *
     * @param componentType the component class to access
     * @param accessType the access semantics ({@link AccessType#READ} or {@link AccessType#WRITE})
     * @return a type-safe accessor with specified access control
     * @throws IllegalArgumentException if the component type was not part of the query
     * @see AccessType
     */
    <C> ComponentAccessor<E, C> accessor(Class<C> componentType, AccessType accessType);

    /**
     * Releases resources and applies any deferred modifications.
     *
     * <p>This method is called automatically when using try-with-resources. It:
     * <ul>
     *   <li>Applies all scheduled removals from {@link ComponentAccessor}s</li>
     *   <li>Releases internal chunk iteration state</li>
     *   <li>Notifies the ECS scheduler of completion (for dependency tracking)</li>
     * </ul></p>
     *
     * <p><b>Important:</b> Manual calls to {@code close()} are permitted but ensure
     * all {@link ComponentAccessor}s created from this result have also completed
     * their operations.</p>
     */
    @Override
    void close();
}
