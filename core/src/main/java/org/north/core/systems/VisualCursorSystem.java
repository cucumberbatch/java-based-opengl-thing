package org.north.core.systems;

import org.north.core.components.MeshRenderer;
import org.north.core.components.VisualCursor;
import org.north.core.components.Transform;
import org.north.core.entities.Entity;
import org.north.core.graphics.*;
import org.north.core.reflection.ComponentHandler;
import org.north.core.shapes.Rectangle;
import org.north.core.systems.processes.CollisionHandlingProcess;
import org.north.core.systems.processes.InitProcess;
import org.north.core.systems.processes.RenderProcess;
import org.north.core.systems.processes.UpdateProcess;
import org.north.core.utils.Logger;
import org.joml.Vector4f;
import org.joml.Vector2f;

// todo: cursor movement needs to be related on entity transform data, not local vectors
@ComponentHandler(VisualCursor.class)
public class VisualCursorSystem extends AbstractSystem<VisualCursor>
        implements InitProcess, UpdateProcess, CollisionHandlingProcess, RenderProcess {

    public static final int IDLE_CURSOR_STATE          = 0;
    public static final int HOVER_CURSOR_STATE         = 1;
    public static final int IDLE_TO_HOVER_CURSOR_STATE = 2;
    public static final int HOVER_TO_IDLE_CURSOR_STATE = 3;


    public int cursorState = 0;

    public float transitionTimeLimit       = 0.2f;
    public float transitionTimeAccumulator = 0.0f;

    public Vector4f cursorColor            = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
    public Vector4f cursorDefaultColor     = new Vector4f(0.4f, 0.4f, 0.4f, 1.0f);
    public Vector4f cursorOnHoverColor     = new Vector4f(0.6f, 0.9f, 1.0f, 1.0f);

    public Vector2f cursorIdleTopLeft      = new Vector2f(-20f, -20f);
    public Vector2f cursorIdleBottomRight  = new Vector2f(+20f, +20f);

//    private Vector2f savedCursorPosition   = new Vector2f();

    private boolean  isCursorMoved         = false;

    // cursor velocity for smoother movement, I guess
//    private Vector2f cursorVelocity        = new Vector2f(1, 1);
//    private Vector2f restoringForce        = new Vector2f(1, 1);

    private Vector2f displacement          = new Vector2f(1, 1);
    private float    springFactor          = 12.0f;
    private float    mass                  = 0.01f;

    private Vector2f previousPhysicalPosition = new Vector2f(1, 1);

    private Rectangle imaginaryCursorShape = new Rectangle(Input.getCursorPosition().add(cursorIdleTopLeft), Input.getCursorPosition().add(cursorIdleBottomRight));

    private Entity selectedEntity = null;

    @Override
    public void init() throws RuntimeException {

        VisualCursor cursorComponent = this.getComponent();
        cursorComponent.cursor  = new Rectangle(new Vector2f(0, 0).add(cursorIdleTopLeft), new Vector2f(0, 0).add(cursorIdleBottomRight));
        cursorComponent.texture = new Texture("core/assets/textures/screen-frame-1024.png");

        setCursorPosition(imaginaryCursorShape, Input.getCursorPosition());

        MeshCollider mesh = component.entity.getComponent(MeshCollider.class);
        mesh.body = imaginaryCursorShape;

        component.vertexBuffer = new VertexArray(
                component.cursor.toVertices(),
                component.indices,
                component.uv);

        Rectangle visualCursorShape = new Rectangle(
                new Vector2f(cursorIdleTopLeft),
                new Vector2f(cursorIdleBottomRight)
        );

        MeshRenderer renderer = componentManager.getComponent(component.entity, MeshRenderer.class);
        renderer.texture = new Texture("core/assets/textures/screen-frame-1024.png");
        renderer.mesh = new Mesh(visualCursorShape.toVertices(), component.indices, component.uv);
//        renderer.shader = OldShader.SIMPLE_COLOR_SHADER;
    }

    @Override
    public void update(float deltaTime) {

        float xFactor = 1f;
        float yFactor = 1f;
        float zFactor = 1f;

//        Shader.BACKGROUND.setUniform("u_tex",      Shader.BACKGROUND.getId());
//        Shader.BACKGROUND.setUniform("u_model", Matrix4f.translation(component.transform.position));

        // instead of cursor rectangle we must move whole entity using transform
        Rectangle cursor = component.cursor;

        Rectangle button = component.previouslySelectedButtonShape;


        if (component.isIntersects) {
            switch (cursorState) {
                case IDLE_TO_HOVER_CURSOR_STATE:
                    if (transitionTimeAccumulator > transitionTimeLimit) {
                        transitionTimeAccumulator = 0.0f;
                        cursorState = HOVER_CURSOR_STATE;
                        Logger.debug("Cursor state change: HOVER_CURSOR_STATE");
                    } else {
                        transitionTimeAccumulator += deltaTime;
                        float ratio = transitionTimeAccumulator / transitionTimeLimit;
                        cursor.topLeft     = new Vector2f(cursor.topLeft).lerp(button.topLeft, ratio);
                        cursor.bottomRight = new Vector2f(cursor.bottomRight).lerp(button.bottomRight, ratio);
                        cursorColor        = new Vector4f(cursorDefaultColor).lerp(cursorOnHoverColor, ratio);
                    }
                    break;

                case HOVER_TO_IDLE_CURSOR_STATE:
                case IDLE_CURSOR_STATE:
                    cursorState = IDLE_TO_HOVER_CURSOR_STATE;
                    Logger.debug("Cursor state change: IDLE_TO_HOVER_CURSOR_STATE");
                    break;

                case HOVER_CURSOR_STATE:
//                    if (Input.isPressed(GLFW.GLFW_KEY_SPACE) && selectedEntity != null) {
//                        selectedEntity.parent.transform.moveTo(cursor.topLeft.x, cursor.topLeft.y, selectedEntity.parent.transform.position.z);
//                        selectedEntity.parent.transform.position.x = cursor.topLeft.x;
//                        selectedEntity.parent.transform.position.y = cursor.topLeft.y;
//                    }
                    break;
            }
        } else {
            displacement  = getRectangleCenter(cursor).sub(Input.getCursorPosition());
            isCursorMoved = !displacement.equals(new Vector2f().zero());

            Vector2f position = calculatePosition(
                    getRectangleCenter(cursor), previousPhysicalPosition,
                    displacement, springFactor, mass, deltaTime);

            if (cursorState != HOVER_CURSOR_STATE) {
                setCursorPosition(cursor, position);
            }

            switch (cursorState) {
                case HOVER_TO_IDLE_CURSOR_STATE:
                    if (transitionTimeAccumulator > transitionTimeLimit) {
                        transitionTimeAccumulator = 0.0f;
                        component.previouslySelectedButtonShape = null;
                        cursorState = IDLE_CURSOR_STATE;
                        Logger.debug("Cursor state change: IDLE_CURSOR_STATE");
                    } else {
                        transitionTimeAccumulator += deltaTime;
                        float ratio = transitionTimeAccumulator / transitionTimeLimit;
                        cursor.topLeft     = new Vector2f(button.topLeft).lerp(new Vector2f(position).add(cursorIdleTopLeft), ratio);
                        cursor.bottomRight = new Vector2f(button.bottomRight).lerp(new Vector2f(position).add(cursorIdleBottomRight), ratio);
                        cursorColor        = new Vector4f(cursorOnHoverColor).lerp(cursorDefaultColor, ratio);
                    }
                    break;

                case HOVER_CURSOR_STATE:
                case IDLE_TO_HOVER_CURSOR_STATE:
                    cursorState = HOVER_TO_IDLE_CURSOR_STATE;
                    Logger.debug("Cursor state change: HOVER_TO_IDLE_CURSOR_STATE");
                    break;

                case IDLE_CURSOR_STATE:
                    break;
            }
        }

        displacement  = getRectangleCenter(imaginaryCursorShape).sub(Input.getCursorPosition());
        isCursorMoved = !displacement.equals(new Vector2f().zero());

        Transform transform = component.getTransform();
        transform.moveTo(
                (Input.getCursorPosition().x - Window.width  / 2f) / Window.width,
                (Input.getCursorPosition().y - Window.height / 2f) / Window.height,
                0f
        );

        previousPhysicalPosition = getRectangleCenter(cursor);

        // cursor is transform dependent now!
        setCursorPosition(imaginaryCursorShape, new Vector2f(transform.position.x, transform.position.y));

        if (isCursorMoved) {  }

    }

    @Override
    public void onCollisionStart(Collision collision) {
        selectedEntity = (Entity) collision.A;
        component.previouslySelectedButtonShape = selectedEntity.getComponent(MeshCollider.class).body;
        component.isIntersects = true;
        cursorState = HOVER_TO_IDLE_CURSOR_STATE;
    }

    @Override
    public void onCollisionEnd(Collision collision)  {
        if (collision.A == selectedEntity) {
            selectedEntity = null;
            component.isIntersects = false;
        }
    }

    @Override
    public void onCollision(Collision collision) {}

    private Vector2f calculatePosition(Vector2f center, Vector2f previousPhysicalPosition, Vector2f displacement, float springFactor, float mass, float deltaTime) {
        return center.mul(2f).sub(previousPhysicalPosition).sub(displacement.mul(springFactor * deltaTime * deltaTime / mass));
    }

    private void setCursorPosition(Rectangle rectangle, Vector2f position) {
        rectangle.topLeft.set(position).add(cursorIdleTopLeft);
        rectangle.bottomRight.set(position).add(cursorIdleBottomRight);
    }

    private Vector2f getRectangleCenter(Rectangle rectangle) {
        return new Vector2f(rectangle.topLeft).add(new Vector2f(rectangle.bottomRight).sub(rectangle.topLeft).mul(1 / 2.0f));
    }

    @Override
    public void render(Graphics graphics) {
//        graphics.drawMesh(PredefinedMeshes.CUBE, Vector4f.one(), component.transform);
//        component.vertexBuffer.updateVertexBuffer(component.cursor.toVertices());
//        Shader.GUI.setUniform("u_color", cursorColor);
//        Renderer2D.draw(component.vertexBuffer, component.texture, Shader.GUI);
    }
}