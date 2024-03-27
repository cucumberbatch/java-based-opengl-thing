package org.north.core.context;

import org.north.core.architecture.entity.Entity;
import org.north.core.architecture.tree.Tree;
import org.north.core.architecture.tree.EntityTree;
import org.north.core.architecture.entity.ComponentManager;
import org.north.core.config.EngineConfig;
import org.north.core.managment.SystemManager;
import org.north.core.reflection.di.registerer.DependencyRegisterer;
import org.north.core.utils.ResourceManager;

public class ApplicationContext {
    private final DependencyRegisterer dependencyRegisterer;

    public ApplicationContext() {
        this.dependencyRegisterer = new DependencyRegisterer();
        this.dependencyRegisterer.registerDependency(ApplicationContext.class, this);
    }

    public <T> T getDependency(Class<T> dependencyClass) {
        return getDependencyInternal(dependencyClass);
    }

    public <T> T addDependency(Class<T> dependencyClass) throws ReflectiveOperationException {
        return dependencyRegisterer.registerDependency(dependencyClass);
    }

    public <T> T addDependency(Class<T> dependencyClass, T dependency) throws ReflectiveOperationException {
        return dependencyRegisterer.registerDependency(dependencyClass, dependency);
    }

    public Object[] addDependencies(Class<?>[] classArray) throws ReflectiveOperationException {
        return dependencyRegisterer.registerDependencies(classArray);
    }

    public Tree<Entity> getEntityTree() {
        return getDependencyInternal(EntityTree.class);
    }

    public ComponentManager getComponentManager() {
        return getDependencyInternal(ComponentManager.class);
    }

    public SystemManager getSystemManager() {
        return getDependencyInternal(SystemManager.class);
    }

    public ResourceManager getResourceManager() {
        return getDependencyInternal(ResourceManager.class);
    }

    public EngineConfig getEngineConfig() {
        return getDependencyInternal(EngineConfig.class);
    }

    public DependencyRegisterer getDependencyRegisterer() {
        return dependencyRegisterer;
    }


    private <T> T getDependencyInternal(Class<T> dependencyClass) {
        try {
            return dependencyRegisterer.getDependency(dependencyClass);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

}
