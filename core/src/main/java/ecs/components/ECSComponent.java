package ecs.components;

import ecs.entities.Entity;
import ecs.systems.ECSSystem;
import ecs.utils.Instantiatable;
import ecs.utils.Replicable;
import ecs.utils.Turntable;

import java.io.Externalizable;
import java.io.Serializable;
import java.util.UUID;

public interface ECSComponent
        extends Turntable, Instantiatable<ECSComponent>, Replicable<ECSComponent> {

    /* Get and set methods for Id of component */
    UUID getId();

    void setId(UUID Id);

    void reset();

    /* Get and set methods for the name */
    String getName();

    void setName(String name);


    /* Get and set methods for an entity */
    Entity getEntity();

    void setEntity(Entity entity);


    /* Get and set methods for the system */
    ECSSystem<? extends ECSComponent> getSystem();

    void setSystem(ECSSystem<? extends ECSComponent> system);


    /* Get and set methods for the transform component */
    Transform getTransform();

    void setTransform(Transform transform);


}
 