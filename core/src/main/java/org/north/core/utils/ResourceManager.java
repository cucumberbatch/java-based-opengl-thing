package org.north.core.utils;

public interface ResourceManager<R, ID> {

    R getResource(String path);

    ID getResourceId(String path);

}
