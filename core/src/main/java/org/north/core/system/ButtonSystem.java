package org.north.core.system;

import org.lwjgl.opengl.GL30;
import org.north.core.component.Button;
import org.north.core.component.VisualCursor;
import org.north.core.architecture.entity.Entity;
import org.north.core.context.ApplicationContext;
import org.north.core.graphics.Graphics;
import org.north.core.graphics.Texture;
import org.north.core.graphics.VertexArray;
import org.north.core.physics.collision.Collision;
import org.north.core.reflection.ComponentHandler;
import org.north.core.reflection.di.Inject;
import org.north.core.system.process.CollisionHandlingProcess;
import org.north.core.system.process.InitProcess;
import org.north.core.system.process.RenderProcess;
import org.north.core.system.process.UpdateProcess;
import org.joml.Vector4f;

import java.util.Random;

@ComponentHandler(Button.class)
public class ButtonSystem extends AbstractSystem<Button> implements InitProcess<Button>,
                                                                    UpdateProcess<Button>,
                                                                    CollisionHandlingProcess<Button>,
                                                                    RenderProcess<Button> {

    public static final int IDLE_BUTTON_STATE          = 0;
    public static final int HOVER_BUTTON_STATE         = 1;
    public static final int IDLE_TO_HOVER_BUTTON_STATE = 2;
    public static final int HOVER_TO_IDLE_BUTTON_STATE = 3;

    @Inject
    public ButtonSystem(ApplicationContext context) {
        super(context);
    }

    @Override
    public void init(Button button) throws RuntimeException {
        button.buttonTexture = new Texture("core/src/main/resources/assets/textures/screen-frame-1024.png");
        button.buttonOnHoverColor = new Vector4f(
                new Random().nextFloat(),
                new Random().nextFloat(),
                new Random().nextFloat(),
                1.0f
        );

        button.vertexBuffer = new VertexArray(
                button.buttonShape.toVertices(),
                button.indices,
                button.uv);

        GL30.glClearColor(0f, 0f, 0f, 1f);
    }

    @Override
    public void update(Button button, float deltaTime) {
        float transitionTimeLimit = button.transitionTimeLimit;

        switch (button.buttonState) {
            case IDLE_BUTTON_STATE:
                break;
            case HOVER_BUTTON_STATE:
                break;
            case IDLE_TO_HOVER_BUTTON_STATE: {
                if (button.transitionTimeAccumulator > transitionTimeLimit) {
                    button.transitionTimeAccumulator = .0f;
                    button.buttonColor = button.buttonOnHoverColor;
                    button.buttonState = HOVER_BUTTON_STATE;
                } else {
                    button.transitionTimeAccumulator += deltaTime;
                    float ratio = button.transitionTimeAccumulator / transitionTimeLimit;
                    button.buttonColor = new Vector4f(button.buttonDefaultColor).lerp(button.buttonOnHoverColor, ratio);
                }
                break;
            }
            case HOVER_TO_IDLE_BUTTON_STATE: {
                if (button.transitionTimeAccumulator > transitionTimeLimit) {
                    button.transitionTimeAccumulator = .0f;
                    button.buttonColor = button.buttonDefaultColor;
                    button.buttonState = IDLE_BUTTON_STATE;
                } else {
                    button.transitionTimeAccumulator += deltaTime;
                    float ratio = button.transitionTimeAccumulator / transitionTimeLimit;
                    button.buttonColor = new Vector4f(button.buttonOnHoverColor).lerp(button.buttonDefaultColor, ratio);
                }
                break;
            }
        }
    }

    @Override
    public void onCollisionStart(Button button, Collision collision) {
        VisualCursor visualCursor = ((Entity) collision.A).get(VisualCursor.class);
        if (visualCursor != null && visualCursor.isIntersects && visualCursor.previouslySelectedButtonShape != button.buttonShape) return;
        button.buttonState = IDLE_TO_HOVER_BUTTON_STATE;
    }

    @Override
    public void onCollisionEnd(Button button, Collision collision)  {
        button.buttonState = HOVER_TO_IDLE_BUTTON_STATE;
    }

    @Override
    public void onCollision(Button button, Collision collision) {}

    @Override
    public void render(Button button, Graphics graphics) {
//        component.vertexBuffer.updateVertexBuffer(component.buttonShape.toVertices());
//        Shader.GUI.setUniform("u_color", component.buttonColor);
//        Renderer2D.draw(component.vertexBuffer, component.buttonTexture, Shader.GUI);
    }

}
