package org.north.core.systems;

import org.lwjgl.opengl.GL30;
import org.north.core.components.Button;
import org.north.core.components.VisualCursor;
import org.north.core.architecture.entity.Entity;
import org.north.core.context.ApplicationContext;
import org.north.core.graphics.Graphics;
import org.north.core.graphics.Texture;
import org.north.core.graphics.VertexArray;
import org.north.core.physics.collision.Collision;
import org.north.core.reflection.ComponentHandler;
import org.north.core.reflection.di.Inject;
import org.north.core.systems.processes.CollisionHandlingProcess;
import org.north.core.systems.processes.InitProcess;
import org.north.core.systems.processes.RenderProcess;
import org.north.core.systems.processes.UpdateProcess;
import org.joml.Vector4f;

import java.util.Random;

@ComponentHandler(Button.class)
public class ButtonSystem extends AbstractSystem<Button>
        implements InitProcess, UpdateProcess, CollisionHandlingProcess, RenderProcess {

    public static final int IDLE_BUTTON_STATE          = 0;
    public static final int HOVER_BUTTON_STATE         = 1;
    public static final int IDLE_TO_HOVER_BUTTON_STATE = 2;
    public static final int HOVER_TO_IDLE_BUTTON_STATE = 3;

    @Inject
    public ButtonSystem(ApplicationContext context) {
        super(context);
    }

    @Override
    public void init() throws RuntimeException {
        component.buttonTexture = new Texture("core/src/main/resources/assets/textures/screen-frame-1024.png");
        component.buttonOnHoverColor = new Vector4f(
                new Random().nextFloat(),
                new Random().nextFloat(),
                new Random().nextFloat(),
                1.0f
        );

        component.vertexBuffer = new VertexArray(
                component.buttonShape.toVertices(),
                component.indices,
                component.uv);

        GL30.glClearColor(0f, 0f, 0f, 1f);
    }

    @Override
    public void update(float deltaTime) {
        float transitionTimeLimit = component.transitionTimeLimit;

        switch (component.buttonState) {
            case IDLE_BUTTON_STATE:
                break;
            case HOVER_BUTTON_STATE:
                break;
            case IDLE_TO_HOVER_BUTTON_STATE: {
                if (component.transitionTimeAccumulator > transitionTimeLimit) {
                    component.transitionTimeAccumulator = .0f;
                    component.buttonColor = component.buttonOnHoverColor;
                    component.buttonState = HOVER_BUTTON_STATE;
                } else {
                    component.transitionTimeAccumulator += deltaTime;
                    float ratio = component.transitionTimeAccumulator / transitionTimeLimit;
                    component.buttonColor = new Vector4f(component.buttonDefaultColor).lerp(component.buttonOnHoverColor, ratio);
                }
                break;
            }
            case HOVER_TO_IDLE_BUTTON_STATE: {
                if (component.transitionTimeAccumulator > transitionTimeLimit) {
                    component.transitionTimeAccumulator = .0f;
                    component.buttonColor = component.buttonDefaultColor;
                    component.buttonState = IDLE_BUTTON_STATE;
                } else {
                    component.transitionTimeAccumulator += deltaTime;
                    float ratio = component.transitionTimeAccumulator / transitionTimeLimit;
                    component.buttonColor = new Vector4f(component.buttonOnHoverColor).lerp(component.buttonDefaultColor, ratio);
                }
                break;
            }
        }
    }

    @Override
    public void onCollisionStart(Collision collision) {
        VisualCursor visualCursor = ((Entity) collision.A).getComponent(VisualCursor.class);
        if (visualCursor != null && visualCursor.isIntersects && visualCursor.previouslySelectedButtonShape != component.buttonShape) return;
        component.buttonState = IDLE_TO_HOVER_BUTTON_STATE;
    }

    @Override
    public void onCollisionEnd(Collision collision)  {
        component.buttonState = HOVER_TO_IDLE_BUTTON_STATE;
    }

    @Override
    public void onCollision(Collision collision) {}

    @Override
    public void render(Graphics graphics) {
//        component.vertexBuffer.updateVertexBuffer(component.buttonShape.toVertices());
//        Shader.GUI.setUniform("u_color", component.buttonColor);
//        Renderer2D.draw(component.vertexBuffer, component.buttonTexture, Shader.GUI);
    }

}
