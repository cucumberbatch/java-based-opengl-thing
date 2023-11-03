package ecs.systems.processes;

import ecs.graphics.Graphics;

public interface RenderProcess {

    /**
     * An interface method that renders component content
     *
     * @param graphics a graphics api for rendering objects
     * @author cucumberbatch
     */
    default void render(Graphics graphics) {}

}
