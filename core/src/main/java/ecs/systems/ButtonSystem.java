package ecs.systems;

import ecs.architecture.ComponentManager;
import ecs.architecture.EntityManager;
import ecs.components.Button;
import ecs.components.VisualCursor;
import ecs.entities.Entity;
import ecs.graphics.*;
import ecs.reflection.ComponentHandler;
import ecs.systems.processes.CollisionHandlingProcess;
import ecs.systems.processes.InitProcess;
import ecs.systems.processes.RenderProcess;
import ecs.systems.processes.UpdateProcess;
import org.joml.Vector4f;

import java.util.Random;

@ComponentHandler(Button.class)
public class ButtonSystem extends AbstractSystem<Button>
        implements InitProcess, UpdateProcess, CollisionHandlingProcess, RenderProcess {

    public static final int IDLE_BUTTON_STATE          = 0;
    public static final int HOVER_BUTTON_STATE         = 1;
    public static final int IDLE_TO_HOVER_BUTTON_STATE = 2;
    public static final int HOVER_TO_IDLE_BUTTON_STATE = 3;


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
