package ecs.entities;

import ecs.Engine;
import ecs.Scene;
import ecs.components.ECSComponent;
import ecs.components.Transform;
import ecs.exception.ComponentNotFoundException;
import ecs.physics.Collidable;
import ecs.systems.ECSSystem;
import ecs.utils.*;

import java.util.*;

/**
 * Entity is an object that contains a bunch of components
 * that describes its nature
 *
 * @author cucumberbatch
 */
public class Entity implements IComponentManager, Turntable, Instantiatable<Entity>, Replicable<Entity>, Collidable {

    public static final String NULL_ENTITY_NAME = "null_entity";
    public static final Entity NULL_ENTITY = null;

//    public Identity identity;
    public Layer    layer = Layer.DEFAULT;
    public String   tag;
    public UUID     id;   //deprecated: goes into identity
    public String   name; //deprecated: goes into identity

    private Engine engine;

    // Collection of daughter entities
    public List<Transform> daughters = new ArrayList<>();

    // Map of pairs of component types and components
    public Map<ECSSystem.Type, ECSComponent> componentMap = new HashMap<>();

    // Activity state of the entity
    public boolean activity;

    // Transform component of that entity
    public Transform transform;

    // Transform component of the parent entity
    public Transform parent;


    public Entity(String name, Engine engine, Scene scene) {
        this.name = name;
        this.engine = engine;
        this.engine.setScene(scene);
        this.id = UUID.randomUUID();

        /** a very very very very poor code, the component instantiation/finding mechanism
         * is not adapted to move like this
         *
         */

//        addComponent(ECSSystem.Type.TRANSFORM);
//        transform = getComponent(ECSSystem.Type.TRANSFORM);

        /* Such a weird stuff right here...
        actually, its a necessary reference manipulation which will help you to get
        the access to a transform component from any component or even entity itself */

//        transform.setTransform(transform);
    }

    /* Get the root entity in this branch */
    public Entity root() {
        Entity root = this;
        while (root.parent != null) {
            root = root.parent.getEntity();
        }
        return root;
    }

    /* Attach this entity to another entity */
    public void attachTo(Entity entity) {
        if (parent != null && !parent.getEntity().daughters.isEmpty()) {
            parent.getEntity().daughters.remove(this.transform);
        }
        parent = transform.parent = entity.transform;
        entity.daughters.add(transform);
    }

    public void detachDaughters() {
        for (Transform daughter : daughters) {
            daughter.parent = this.parent;
        }

        daughters.clear();
    }

    public void detachComponents() {
    }

    /* Compare tag of this and other entity by entity */
    public int compareTag(Entity entity) {
        return compareTag(entity.tag);
    }

    /* Compare tag of this and other entity by string */
    public int compareTag(String tag) {
        return this.tag.compareTo(tag);
    }

    // ---------------------  Component access interface  ------------------------------------------------------

    @Override
    public void addComponent(ECSSystem.Type type) throws IllegalArgumentException, ClassCastException {
        if (!componentMap.containsKey(type)) {
            lateInitComponent(type);
        }
    }

    public void anotherInitComponentMethod(ECSSystem.Type type) {
        ECSComponent component = initializeComponent(type);
        componentMap.put(type, component);
        engine.addComponentToSystem(type, component);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends ECSComponent> E getComponent(ECSSystem.Type type)
            throws IllegalArgumentException, ClassCastException, ComponentNotFoundException {
        E component = (E) componentMap.get(type);
        if (component == null) {
            throw new ComponentNotFoundException();
        }
        return component;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends ECSComponent> E removeComponent(ECSSystem.Type type) throws IllegalArgumentException, ClassCastException {
        return (E) engine.removeComponentFromSystem(type, componentMap.remove(type));
    }

    public <E extends ECSComponent> E initializeComponent(ECSSystem.Type type) {
        E component = engine.instantiateNewComponent(type);
        component.setName(component.getClass().getSimpleName());
        component.setTransform(transform);
        component.setEntity(this);
        return component;
    }

    // todo: it needs to add a new components into another component list,
    //  which would fill the list of components to update in the end of the update process
    public void lateInitComponent(ECSSystem.Type type) {
        engine.addComponent(this, type);
    }

    // ---------------------  Activity control interface  ------------------------------------------------------

    @Override
    public boolean isActive() {
        return activity;
    }

    @Override
    public void setActivity(boolean activity) {
        this.activity = activity;
    }

    @Override
    public void switchActivity() {
        activity = !activity;
    }

    // TODO: 07.06.2021 implementation isn't done yet
    //  note: 23.12.2022 maybe, its not needed... idk
    @Override
    public Entity getInstance() {
        Entity clone = engine.generateNewEntity();
        Set<ECSComponent> components = new HashSet<>(componentMap.values());
        clone.activity = activity;
        return null;
    }

    // TODO: 07.06.2021 implementation isn't done yet
    //  note: 23.12.2022 maybe, its not needed... idk
    @Override
    public Entity getReplica() {
        throw new UnsupportedOperationException();
    }

    public void reset() {
        detachDaughters();
        componentMap.clear();
    }

    public Engine getEngine() {
        return engine;
    }

    public String showHierarchy(int level) {
        String componentNames    = "";
        String levelIndent       = "";
        String accumulator       = "";
        String toStringComponent = "";

        for (int i = 0; i < level; i++) {
            levelIndent = levelIndent + TerminalUtils.indent4;
        }

        for (Iterator<ECSComponent> iterator = componentMap.values().iterator(); iterator.hasNext(); ) {
            ECSComponent component  = iterator.next();
            componentNames          = componentNames + component.getClass().getName();
            toStringComponent       = component.toString();

            if (toStringComponent != null && !toStringComponent.equals("")) {
                componentNames = componentNames + component.toString().replace("\n", "\n" + levelIndent + "    <state> ");
            }

            if (iterator.hasNext()) {
                componentNames = componentNames + "\n" + levelIndent + " > ";
            }
        }

        accumulator =
                accumulator +
                        levelIndent + TerminalUtils.formatOutputEntity(this, 0) + "\n" +
                        levelIndent + " > " + componentNames + "\n";

        for (Transform e : daughters) {
            accumulator = accumulator + e.entity.showHierarchy(level + 1);
        }

        return accumulator;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class Builder {

    }
}
