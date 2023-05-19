package ecs.systems;

import ecs.components.Button;
import ecs.components.VisualCursor;
import ecs.entities.Entity;
import ecs.gl.Window;
import ecs.graphics.Renderer2D;
import ecs.graphics.Shader;
import ecs.graphics.Texture;
import ecs.graphics.VertexArray;
import ecs.shapes.Rectangle;
import ecs.utils.Logger;
import ecs.utils.Stopwatch;
import vectors.Vector4f;

import java.util.Objects;
import java.util.Random;

public class ButtonSystem extends AbstractSystem<Button> {

    public static final int IDLE_BUTTON_STATE          = 0;
    public static final int HOVER_BUTTON_STATE         = 1;
    public static final int IDLE_TO_HOVER_BUTTON_STATE = 2;
    public static final int HOVER_TO_IDLE_BUTTON_STATE = 3;


    @Override
    public int getWorkflowMask() {
        return INIT_MASK | UPDATE_MASK | RENDER_MASK | COLLISION_HANDLING_MASK;
    }

    @Override
    public void init() throws RuntimeException {
        component.buttonTexture = new Texture("core/assets/textures/screen-frame-1024.png");
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
                    component.buttonState = HOVER_BUTTON_STATE;
                } else {
                    component.transitionTimeAccumulator += deltaTime;
                    float ratio = component.transitionTimeAccumulator / transitionTimeLimit;
                    component.buttonColor = Vector4f.lerp(component.buttonDefaultColor, component.buttonOnHoverColor, ratio);
                }
                break;
            }
            case HOVER_TO_IDLE_BUTTON_STATE: {
                if (component.transitionTimeAccumulator > transitionTimeLimit) {
                    component.transitionTimeAccumulator = .0f;
                    component.buttonState = IDLE_BUTTON_STATE;
                } else {
                    component.transitionTimeAccumulator += deltaTime;
                    float ratio = component.transitionTimeAccumulator / transitionTimeLimit;
                    component.buttonColor = Vector4f.lerp(component.buttonOnHoverColor, component.buttonDefaultColor, ratio);
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
    public void render(Window window) {
        component.vertexBuffer.updateVertexBuffer(component.buttonShape.toVertices());
        Shader.GUI.setUniform4f("u_color", component.buttonColor);
        Renderer2D.draw(component.vertexBuffer, component.buttonTexture, Shader.GUI);
    }

}
