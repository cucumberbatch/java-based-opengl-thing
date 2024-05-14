package org.north.core.context;

import org.north.core.reflection.di.registerer.DependencyRegisterer;

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

    private <T> T getDependencyInternal(Class<T> dependencyClass) {
        try {
            return dependencyRegisterer.getDependency(dependencyClass);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

}
