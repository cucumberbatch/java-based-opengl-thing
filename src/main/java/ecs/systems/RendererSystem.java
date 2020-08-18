package ecs.systems;

import ecs.components.Renderer;
import ecs.gl.Window;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class RendererSystem extends AbstractSystem<Renderer> {

    @Override
    public void init() throws Exception {
        glShadeModel(GL_SMOOTH);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClearDepth(1.0f);
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
    }

    @Override
    public void update(float deltaTime) {
        if (Input.isPressed(GLFW_KEY_SPACE)) {
            java.lang.System.out.println("Space is pressed!");
        }
    }

    @Override
    public void render(Window window) {
        Renderer renderer = current_component;
        float rotateT = 0.0f;

        glClear(GL_COLOR_BUFFER_BIT);
        glClear(GL_DEPTH_BUFFER_BIT);
        glLoadIdentity();
        glTranslatef(0.0f, 0.0f, -5.0f);

        glRotatef(rotateT, 1.0f, 0.0f, 0.0f);
        glRotatef(rotateT, 0.0f, 1.0f, 0.0f);
        glRotatef(rotateT, 0.0f, 0.0f, 1.0f);
        glRotatef(rotateT, 0.0f, 1.0f, 0.0f);

        glBegin(GL_TRIANGLES);

        // Front
        glColor3f(0.0f, 1.0f, 1.0f);
        glVertex3f(0.0f, 1.0f, 0.0f);
        glColor3f(0.0f, 0.0f, 1.0f);
        glVertex3f(-1.0f, -1.0f, 1.0f);
        glColor3f(0.0f, 0.0f, 0.0f);
        glVertex3f(1.0f, -1.0f, 1.0f);

//        Right Side Facing Front
        glColor3f(0.0f, 1.0f, 1.0f);
        glVertex3f(0.0f, 1.0f, 0.0f);
        glColor3f(0.0f, 0.0f, 1.0f);
        glVertex3f(1.0f, -1.0f, 1.0f);
        glColor3f(0.0f, 0.0f, 0.0f);
        glVertex3f(0.0f, -1.0f, -1.0f);

//        Left Side Facing Front
        glColor3f(0.0f, 1.0f, 1.0f);
        glVertex3f(0.0f, 1.0f, 0.0f);
        glColor3f(0.0f, 0.0f, 1.0f);
        glVertex3f(0.0f, -1.0f, -1.0f);
        glColor3f(0.0f, 0.0f, 0.0f);
        glVertex3f(-1.0f, -1.0f, 1.0f);

//        Bottom
        glColor3f(0.0f, 0.0f, 0.0f);
        glVertex3f(-1.0f, -1.0f, 1.0f);
        glColor3f(0.1f, 0.1f, 0.1f);
        glVertex3f(1.0f, -1.0f, 1.0f);
        glColor3f(0.2f, 0.2f, 0.2f);
        glVertex3f(0.0f, -1.0f, -1.0f);

        glEnd();

        rotateT += 0.2f;
    }
}
