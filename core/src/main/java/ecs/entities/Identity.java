package ecs.entities;


public class Identity<T> {
    public final long id;
    public final Object object;
    public final Class<T> identityClass;
    public String name;

    public Identity(long id, Object object, Class<T> identityClass) {
        this.id = id;
        this.object = object;
        this.identityClass = identityClass;
    }

    public Identity(long id, Object object, Class<T> identityClass, String name) {
        this.id = id;
        this.object = object;
        this.identityClass = identityClass;
    }

    public <E> E getObject() {
        return null;
    }
}
