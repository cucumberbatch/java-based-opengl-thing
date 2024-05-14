package org.north.core;

import org.north.core.component.InitEntities;
import org.north.core.architecture.entity.Entity;
import org.north.core.scene.Scene;

public class MainThread {
    public static void main(String[] args) throws Exception {
        new Engine().run();
    }
}