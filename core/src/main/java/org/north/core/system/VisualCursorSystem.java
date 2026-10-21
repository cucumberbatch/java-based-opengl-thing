package org.north.core.system;

import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.north.core.architecture.ApplicationContext;
import org.north.core.architecture.ComponentHandler;
import org.north.core.architecture.Inject;
import org.north.core.exception.ComponentNotFoundException;
import org.north.core.graphics.Material;
import org.north.core.graphics.PredefinedMeshes;
import org.north.core.graphics.Window;
import org.north.core.graphics.shader.Shader;
import org.north.core.physics.shape.Rectangle;
import org.north.core.scene.Entity;
import org.north.core.scene.component.Button;
import org.north.core.scene.component.MeshCollider;
import org.north.core.scene.component.MeshRenderer;
import org.north.core.scene.component.Transform;
import org.north.core.scene.component.VisualCursor;
import org.north.core.system.process.InitProcess;
import org.north.core.system.process.UpdateProcess;

import java.util.Collection;

/**
 * Drives the physical visual cursor.
 *
 * <p>The cursor follows the pointer with a damped spring instead of being teleported. When the
 * pointer is over a {@link MeshCollider}, the cursor is magnetized to the collider rectangle and
 * grows/shrinks into its shape. Buttons are notified through their {@code hovered} flag.</p>
 */
@ComponentHandler(VisualCursor.class)
public class VisualCursorSystem extends AbstractSystem<VisualCursor>
        implements InitProcess<VisualCursor>, UpdateProcess<VisualCursor> {

    private static final String SHADER_PATH = "assets/shaders/simple_color_shader";
    private static final float  MAX_DELTA_TIME = 1f / 30f;

    private final Input  input;
    private final Window window;

    private final Vector2f pointer        = new Vector2f();
    private final Vector2f desiredCenter  = new Vector2f();
    private final Vector2f desiredSize    = new Vector2f();
    private final Vector2f delta          = new Vector2f();
    private final Vector2f acceleration   = new Vector2f();
    private final Vector2f temporary      = new Vector2f();

    @Inject
    public VisualCursorSystem(ApplicationContext context) {
        super(VisualCursor.class, context);
        this.input  = context.getDependency(Input.class);
        this.window = context.getDependency(Window.class);
    }

    @Override
    public void init(VisualCursor cursor) {
        MeshRenderer renderer = cm.get(cursor.getEntity(), MeshRenderer.class);
        if (renderer == null) {
            throw new ComponentNotFoundException(MeshRenderer.class);
        }

        renderer.mesh = PredefinedMeshes.QUAD;
        renderer.screenSpace = true;
        renderer.renderOrder = 100;

        if (renderer.material == null) {
            Shader shader = assetService.get(Shader.class, SHADER_PATH);
            renderer.material = new Material(shader);
        }
        renderer.material.setVector("u_color", cursor.color);

        input.getCursorPosition(pointer);
        cursor.center.set(pointer);
        cursor.previousCenter.set(pointer);
        cursor.targetCenter.set(pointer);
        cursor.velocity.zero();
        cursor.displaySize.set(cursor.idleSize);
        cursor.targetSize.set(cursor.idleSize);

        updateShape(cursor);
        syncTransform(cursor);

        if (cursor.hideSystemCursor) {
            GLFW.glfwSetInputMode(
                    window.getWindow(),
                    GLFW.GLFW_CURSOR,
                    GLFW.GLFW_CURSOR_HIDDEN
            );
        }
    }

    @Override
    public void update(VisualCursor cursor, float deltaTime) {
        if (!cursor.visible) {
            cursor.displaySize.zero();
            updateShape(cursor);
            syncTransform(cursor);
            return;
        }

        hideOperatingSystemCursor(cursor);

        float dt = Math.min(Math.max(deltaTime, 0f), MAX_DELTA_TIME);
        if (dt == 0f) return;

        input.getCursorPosition(pointer);

        MeshCollider target = findHoveredTarget(cursor, pointer);
        cursor.previousHoveredEntity = cursor.hoveredEntity;

        if (target != null) {
            target.body.getCenter(desiredCenter);
            desiredSize.set(target.body.width(), target.body.height());
            cursor.targetShape.topLeft.set(target.body.topLeft);
            cursor.targetShape.bottomRight.set(target.body.bottomRight);
            setHoveredEntity(cursor, target.getEntity());
        } else {
            desiredCenter.set(pointer);
            desiredSize.set(cursor.idleSize);
            cursor.targetShape.topLeft.set(pointer);
            cursor.targetShape.bottomRight.set(pointer);
            setHoveredEntity(cursor, null);
        }

        cursor.targetCenter.set(desiredCenter);
        cursor.targetSize.set(desiredSize);

        updatePosition(cursor, dt);
        updateSize(cursor, dt);
        updateColor(cursor, dt);

        updateShape(cursor);
        syncTransform(cursor);
    }

    private MeshCollider findHoveredTarget(VisualCursor cursor, Vector2f pointer) {
        Collection<MeshCollider> colliders = cm.getAllByType(MeshCollider.class);
        if (colliders == null || colliders.isEmpty()) return null;

        Entity cursorEntity = cursor.getEntity();

        // Keep the current target while the pointer is inside it. This avoids target flickering
        // when several rectangles overlap or when the pointer is exactly on a border.
        if (cursor.hoveredEntity != null) {
            MeshCollider current = cm.get(cursor.hoveredEntity, MeshCollider.class);
            if (isUsable(current, cursorEntity) && current.body.isInside(pointer)) {
                return current;
            }
        }

        // A target is selected by the pointer only. The physical cursor rectangle is intentionally
        // not used here: if it were, a large magnetized cursor placed between two buttons could
        // overlap both of them, alternate between the two targets every frame and stay squeezed
        // in the gap while both buttons flicker.
        MeshCollider best = null;
        float bestScore = Float.MAX_VALUE;

        for (MeshCollider collider : colliders) {
            if (!isUsable(collider, cursorEntity)) continue;

            Rectangle body = collider.body;
            if (!body.isInside(pointer)) continue;

            // Prefer the smallest target when several colliders overlap.
            float score = body.width() * body.height();
            if (score < bestScore) {
                bestScore = score;
                best = collider;
            }
        }

        return best;
    }

    private boolean isUsable(MeshCollider collider, Entity cursorEntity) {
        return collider != null
                && collider.isActive()
                && collider.getEntity() != cursorEntity
                && collider.body != null
                && !collider.body.isEmpty();
    }

    private void setHoveredEntity(VisualCursor cursor, Entity entity) {
        if (cursor.hoveredEntity == entity) return;

        if (cursor.hoveredEntity != null) {
            Button previousButton = cm.get(cursor.hoveredEntity, Button.class);
            if (previousButton != null) {
                previousButton.hovered = false;
            }
        }

        cursor.hoveredEntity = entity;

        if (entity != null) {
            Button button = cm.get(entity, Button.class);
            if (button != null) {
                button.hovered = true;
            }
        }
    }

    private void updatePosition(VisualCursor cursor, float dt) {
        cursor.previousCenter.set(cursor.center);

        delta.set(cursor.center).sub(cursor.targetCenter);

        acceleration
                .set(delta)
                .mul(-cursor.stiffness)
                .sub(temporary.set(cursor.velocity).mul(cursor.damping));

        cursor.velocity.add(temporary.set(acceleration).mul(dt));
        cursor.center.add(temporary.set(cursor.velocity).mul(dt));

        if (delta.lengthSquared() < 0.0004f && cursor.velocity.lengthSquared() < 0.0004f) {
            cursor.center.set(cursor.targetCenter);
            cursor.velocity.zero();
        }
    }

    private void updateSize(VisualCursor cursor, float dt) {
        float ratio = smoothingFactor(cursor.magnetizationSpeed, dt);
        cursor.displaySize.lerp(cursor.targetSize, ratio);
    }

    private void updateColor(VisualCursor cursor, float dt) {
        Vector4f targetColor = cursor.hoveredEntity != null
                ? cursor.hoverColor
                : cursor.idleColor;
        cursor.color.lerp(targetColor, smoothingFactor(cursor.colorSpeed, dt));
    }

    private float smoothingFactor(float speed, float dt) {
        return Math.min(1f, Math.max(0f, 1f - (float) Math.exp(-speed * dt)));
    }

    private void updateShape(VisualCursor cursor) {
        cursor.shape.setFromCenter(cursor.center, cursor.displaySize);
    }

    private void syncTransform(VisualCursor cursor) {
        Transform transform = cm.get(cursor.getEntity(), Transform.class);
        if (transform == null) return;

        transform.moveTo(cursor.center.x, cursor.center.y, 0f);
        transform.rescaleTo(
                Math.max(cursor.displaySize.x, 0.0001f),
                Math.max(cursor.displaySize.y, 0.0001f),
                1f
        );
    }

    private void hideOperatingSystemCursor(VisualCursor cursor) {
        if (!cursor.hideSystemCursor) return;

        long handle = window.getWindow();
        int mode = GLFW.glfwGetInputMode(handle, GLFW.GLFW_CURSOR);
        if (mode == GLFW.GLFW_CURSOR_NORMAL) {
            GLFW.glfwSetInputMode(handle, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
        }
    }

}
