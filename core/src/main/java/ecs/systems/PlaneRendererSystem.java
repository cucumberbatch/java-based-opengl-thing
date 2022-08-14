package ecs.systems;

import ecs.components.ECSComponent;
import ecs.components.PlaneRenderer;
import ecs.components.Transform;
import ecs.entities.Entity;
import ecs.shapes.Rectangle;
import ecs.gl.Window;
import ecs.graphics.Renderer2D;
import ecs.graphics.Shader;
import ecs.graphics.Texture;
import ecs.graphics.VertexArray;
import ecs.utils.Logger;
import matrices.Matrix4f;
import vectors.Vector4f;
import vectors.Vector2f;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

import static ecs.utils.TerminalUtils.*;


// todo: cursor movement needs to be related on entity transform data, not local vectors
public class PlaneRendererSystem extends AbstractECSSystem<PlaneRenderer> {

    public static final int IDLE_CURSOR_STATE          = 0;
    public static final int HOVER_CURSOR_STATE         = 1;
    public static final int IDLE_TO_HOVER_CURSOR_STATE = 2;
    public static final int HOVER_TO_IDLE_CURSOR_STATE = 3;


    public int cursorState = 0;

    public float transitionTimeAccumulator = 0.0f;
    public float transitionTimeLimit       = 0.2f;

    public Vector4f buttonDefaultColor     = new Vector4f(0.75f, 0.75f, 0.75f, 1.0f);
    public Vector4f buttonOnHoverColor     = new Vector4f(0.87f, 0.87f, 1.00f, 1.0f);
    public Vector4f buttonColor            = new Vector4f(buttonDefaultColor);

    public Vector4f cursorColor            = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);

    public Vector2f cursorIdleTopLeft      = new Vector2f(-20f, -20f);
    public Vector2f cursorIdleBottomRight  = new Vector2f(20f, 20f);

    private Vector2f savedCursorPosition   = new Vector2f();
    private boolean  isCursorMoved         = false;
    private boolean  isIntersects          = false;

    // cursor velocity for smoother movement, I guess
    private Vector2f cursorVelocity        = new Vector2f(1, 1);

    private Vector2f restoringForce        = new Vector2f(1, 1);
    private Vector2f displacement          = new Vector2f(1, 1);
    private float    springFactor          = 12.0f;

    private float    mass                  = 0.01f;

    private Vector2f previousPhysicalPosition = new Vector2f(1, 1);

    private Rectangle imaginaryCursor      = new Rectangle(Input.getCursorPosition().add(cursorIdleTopLeft), Input.getCursorPosition().add(cursorIdleBottomRight));

    @Override
    public int getWorkflowMask() {
        return INIT_MASK | UPDATE_MASK | RENDER_MASK | COLLISION_HANDLING_MASK;
    }

    @Override
    public void init() throws Exception {

        PlaneRenderer renderer = getComponent();
        renderer.cursor = new Rectangle(new Vector2f(170, 170).add(cursorIdleTopLeft), new Vector2f(170, 170).add(cursorIdleBottomRight));
        renderer.button = new Rectangle(renderer.initialTopLeftVertex, renderer.initialBottomRightVertex);

        renderer.background = new VertexArray(renderer.vertices, renderer.indices, renderer.uv);
        renderer.texture    = new Texture("core/assets/textures/screen-frame-1024.png");

        setCursorPosition(imaginaryCursor, Input.getCursorPosition());

        MeshCollider mesh = (MeshCollider) getComponent(Type.MESH_COLLIDER);
        mesh.mesh = imaginaryCursor;
    }

    @Override
    public void update(float deltaTime) {

        float xFactor = 1f;
        float yFactor = 1f;
        float zFactor = 1f;

        Shader.BACKGROUND.setUniform1i("u_tex", Shader.BACKGROUND.getId());

        Shader.BACKGROUND.setUniformMat4f("u_model",
                // Matrix4f.multiply(
                        Matrix4f.translation(component.transform.position));
                        // Matrix4f.multiply(
                        //         Matrix4f.multiply(
                        //             Matrix4f.rotation(Vector3f.right(), currentComponent.transform.rotation.x),
                        //             Matrix4f.rotation(Vector3f.up(),    currentComponent.transform.rotation.y)),
                        //         Matrix4f.rotation(Vector3f.forward(), currentComponent.transform.rotation.z)
                        // )));

//        Shader.BACKGROUND.setUniformMat4f("u_view", Matrix4f.identity());

        Rectangle cursor = component.cursor;
        Rectangle button = component.button;

//        cursor.topLeft     = Input.getCursorPosition().add(cursorIdleTopLeft);
//        cursor.bottomRight = Input.getCursorPosition().add(cursorIdleBottomRight);


//        /*

        if (isIntersects) {
            switch (cursorState) {
                case IDLE_TO_HOVER_CURSOR_STATE:
                    if (transitionTimeAccumulator > transitionTimeLimit) {
                        transitionTimeAccumulator = 0.0f;
                        cursorState = HOVER_CURSOR_STATE;
                        Logger.debug("Cursor state change: HOVER_CURSOR_STATE");
                    } else {
                        transitionTimeAccumulator += deltaTime;
                        cursor.topLeft     = Vector2f.lerp(cursor.topLeft,     button.topLeft,     transitionTimeAccumulator / transitionTimeLimit);
                        cursor.bottomRight = Vector2f.lerp(cursor.bottomRight, button.bottomRight, transitionTimeAccumulator / transitionTimeLimit);
                        buttonColor        = Vector4f.lerp(buttonDefaultColor, buttonOnHoverColor, transitionTimeAccumulator / transitionTimeLimit);
                    }
                    break;

                case HOVER_TO_IDLE_CURSOR_STATE:
                    cursorState = IDLE_TO_HOVER_CURSOR_STATE;
                    Logger.debug("Cursor state change: IDLE_TO_HOVER_CURSOR_STATE");
                    break;

                case IDLE_CURSOR_STATE:
                    cursorState = IDLE_TO_HOVER_CURSOR_STATE;
                    Logger.debug("Cursor state change: IDLE_TO_HOVER_CURSOR_STATE");
                    break;

                case HOVER_CURSOR_STATE:
                    break;
            }
        } else {
            displacement  = getRectangleCenter(cursor).sub(Input.getCursorPosition());
            isCursorMoved = !displacement.equals(Vector2f.zero());

            Vector2f position = calculatePosition(
                    getRectangleCenter(cursor), previousPhysicalPosition,
                    displacement, springFactor, mass, deltaTime);

            if (cursorState != HOVER_TO_IDLE_CURSOR_STATE) {
                setCursorPosition(cursor, position);
            }

            switch (cursorState) {
                case HOVER_TO_IDLE_CURSOR_STATE:
                    if (transitionTimeAccumulator > transitionTimeLimit) {
                        transitionTimeAccumulator = 0.0f;
                        cursorState = IDLE_CURSOR_STATE;
                        Logger.debug("Cursor state change: IDLE_CURSOR_STATE");
                    } else {
                        transitionTimeAccumulator += deltaTime;
//                        setCursorPosition(cursor, position);
                        cursor.topLeft     = Vector2f.lerp(button.topLeft,     new Vector2f(position).add(cursorIdleTopLeft),     transitionTimeAccumulator / transitionTimeLimit);
                        cursor.bottomRight = Vector2f.lerp(button.bottomRight, new Vector2f(position).add(cursorIdleBottomRight), transitionTimeAccumulator / transitionTimeLimit);
                        buttonColor        = Vector4f.lerp(buttonOnHoverColor, buttonDefaultColor,                  transitionTimeAccumulator / transitionTimeLimit);
                    }
                    break;

                case HOVER_CURSOR_STATE:
                    cursorState = HOVER_TO_IDLE_CURSOR_STATE;
                    Logger.debug("Cursor state change: HOVER_TO_IDLE_CURSOR_STATE");
                    break;

                case IDLE_TO_HOVER_CURSOR_STATE:
                    cursorState = HOVER_TO_IDLE_CURSOR_STATE;
                    Logger.debug("Cursor state change: HOVER_TO_IDLE_CURSOR_STATE");
                    break;

                case IDLE_CURSOR_STATE:
                    break;
            }
        }

//        */

        displacement  = getRectangleCenter(imaginaryCursor).sub(Input.getCursorPosition());
        isCursorMoved = !displacement.equals(Vector2f.zero());

        cursorColor = new Vector4f(
                Input.getCursorPosition().normalized().x,
                Input.getCursorPosition().normalized().y,
                component.transform.position.x,
                1.0f);


        Transform transform = component.transform;
        transform.position.set(Input.getCursorPosition().x, Input.getCursorPosition().y, 0f);

        previousPhysicalPosition = getRectangleCenter(cursor);

        // cursor is tranform dependent now!
        setCursorPosition(imaginaryCursor, new Vector2f(transform.position.x, transform.position.y));

        if (isCursorMoved) {  }

    }

    @Override
    public void onCollisionStart(Collision collision) {
        component.button = ((MeshCollider) collision.A.getComponent(Type.MESH_COLLIDER)).mesh;
        isIntersects = true;
    }

    @Override
    public void onCollisionEnd(Collision collision)  {
        isIntersects = false;
    }

    private Vector2f calculatePosition(Vector2f center, Vector2f previousPhysicalPosition, Vector2f displacement, float springFactor, float mass, float deltaTime) {
        return center.mul(2f).sub(previousPhysicalPosition).sub(displacement.mul(springFactor * deltaTime * deltaTime / mass));
    }

    private void setCursorPosition(Rectangle rectangle, Vector2f position) {
        rectangle.topLeft.set(new Vector2f(position).add(cursorIdleTopLeft));
        rectangle.bottomRight.set(new Vector2f(position).add(cursorIdleBottomRight));
    }

    private Vector2f getRectangleCenter(Rectangle rectangle) {
        return new Vector2f(rectangle.topLeft).add(new Vector2f(rectangle.bottomRight).sub(rectangle.topLeft).div(2.0f));
    }

    @Override
    public void render(Window window) {
        // System.out.println(Shader.GUI.getUniform("pos_shift"));

        VertexArray cursorVertices = new VertexArray(
                component.cursor.toVertices(),
                component.indices,
                component.uv);

        VertexArray buttonVertices = new VertexArray(
                component.button.toVertices(),
                component.indices,
                component.uv);


        // glBegin(GL_LINES);
        // glVertex2f(Input.getCursorPosition().x, 0.0f);
        // glVertex2f(Input.getCursorPosition().x, Window.height);
        // glEnd();

        // currentComponent.transform.position = currentComponent.transform.position.normalized();        

        Shader.GUI.setUniform4f("u_color", buttonColor);

        Renderer2D.draw(buttonVertices, getComponent().texture, Shader.GUI);

        Shader.GUI.setUniform4f("u_color", cursorColor);

        // Renderer2D.draw(getCurrentComponent().background, getCurrentComponent().texture, Shader.BACKGROUND);
        Renderer2D.draw(cursorVertices, getComponent().texture, Shader.GUI);


    }

}
