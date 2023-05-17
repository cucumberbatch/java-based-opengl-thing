package ecs.systems;

import ecs.components.Button;
import ecs.gl.Window;
import ecs.graphics.Renderer2D;
import ecs.graphics.Shader;
import ecs.graphics.Texture;
import ecs.graphics.VertexArray;
import ecs.shapes.Rectangle;
import vectors.Vector4f;

import java.util.Random;

public class ButtonSystem extends AbstractSystem<Button> {

    @Override
    public int getWorkflowMask() {
        return INIT_MASK | RENDER_MASK;
    }

    @Override
    public void init() throws RuntimeException {
//        MeshCollider meshComponent = componentManager.getComponent(component.entity, MeshCollider.class);
//        meshComponent.mesh      = new Rectangle(component.topLeft, component.bottomRight);
//        component.buttonShape   = new Rectangle(component.topLeft, component.bottomRight);
        component.buttonTexture = new Texture("core/assets/textures/screen-frame-1024.png");
        component.buttonOnHoverColor = new Vector4f(
                new Random().nextFloat(),
                new Random().nextFloat(),
                new Random().nextFloat(),
                1.0f
        );
    }

    @Override
    public void render(Window window) {
        VertexArray buttonVertices = new VertexArray(
                component.buttonShape.toVertices(),
                component.indices,
                component.uv);

        Shader.GUI.setUniform4f("u_color", component.buttonColor);

        Renderer2D.draw(buttonVertices, component.buttonTexture, Shader.GUI);
    }

    public static void lightUpButtonHighlight(Button button, float ratio) {
        button.buttonColor = Vector4f.lerp(button.buttonDefaultColor, button.buttonOnHoverColor, ratio);
    }

    public static void lightDownButtonHighlight(Button button, float ratio) {
        button.buttonColor = Vector4f.lerp(button.buttonOnHoverColor, button.buttonDefaultColor, ratio);
    }
}
