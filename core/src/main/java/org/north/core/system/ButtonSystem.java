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

    public enum ButtonState {
        IDLE_BUTTON_STATE, HOVER_BUTTON_STATE,
        IDLE_TO_HOVER_BUTTON_STATE, HOVER_TO_IDLE_BUTTON_STATE;
    }

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
                button.uv
        );

        GL30.glClearColor(0f, 0f, 0f, 1f);
    }

    @Override
    public void update(Button button, float deltaTime) {
        float transitionTimeLimit = button.transitionTimeLimit;

        switch (button.buttonState) {
            case IDLE_BUTTON_STATE:
            case HOVER_BUTTON_STATE:
                break;
            case IDLE_TO_HOVER_BUTTON_STATE: {
                if (button.transitionTimeAccumulator > transitionTimeLimit) {
                    button.transitionTimeAccumulator = .0f;
                    button.buttonColor = button.buttonOnHoverColor;
                    button.buttonState = ButtonState.HOVER_BUTTON_STATE;
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
                    button.buttonState = ButtonState.IDLE_BUTTON_STATE;
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
    public void onCollisionStarted(Button button, Collision collision) {
        VisualCursor visualCursor = ((Entity) collision.getA()).get(VisualCursor.class);
        if (visualCursor != null && visualCursor.isIntersects && visualCursor.previouslySelectedButtonShape != button.buttonShape) return;
        button.buttonState = ButtonState.IDLE_TO_HOVER_BUTTON_STATE;
    }

    @Override
    public void onCollisionEnded(Button button, Collision collision)  {
        button.buttonState = ButtonState.HOVER_TO_IDLE_BUTTON_STATE;
    }

    @Override
    public void onCollisionContinued(Button button, Collision collision) {}

    @Override
    public void render(Button button, Graphics graphics) {
//        component.vertexBuffer.updateVertexBuffer(component.buttonShape.toVertices());
//        Shader.GUI.setUniform("u_color", component.buttonColor);
//        Renderer2D.draw(component.vertexBuffer, component.buttonTexture, Shader.GUI);
    }

}
