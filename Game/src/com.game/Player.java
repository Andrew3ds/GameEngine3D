package com.game;

import com.engine.core.Engine;
import com.engine.core.Timer;
import com.engine.gfx.Camera;
import com.engine.gfx.FirstPersonCamera;
import com.engine.gfx.Node;
import com.engine.gfx.WorldObject;
import com.engine.input.Key;
import org.joml.Vector3f;

import static com.engine.core.Engine.display;
import static com.engine.core.Engine.input;

/**
 * Created by Andrew on 5/12/2017.
 */
public class Player extends Node {
    private FirstPersonCamera camera;
    private Vector3f velocity = new Vector3f();
    private float dxV = 0.0F;
    private float dyV = 0.0F;

    public Player() {
        super("player");
    }

    @Override
    public void onCreate() {
        camera = new FirstPersonCamera();
    }

    @Override
    public void onInput() {
        if(!display.getGlfwWindow().isMouseHidden()) {
            return;
        }

        int x = Engine.input.getMouseX();
        int y = Engine.input.getMouseY();

        float dx = ((float) x - (Engine.display.getWidth() / 2)) * 0.25F;
        float dy = ((float) y - (Engine.display.getHeight() / 2)) * 0.25F;

        dxV += (dx - dxV) * ((0.35F / 16F) * Timer.getDelta());
        dyV += (dy - dyV) * ((0.35F / 16F) * Timer.getDelta());

        camera.getRotation().add(dyV, dxV, 0F);

        float speed = 0.008F;

        float xSpeed = 0;
        float zSpeed = 0;
        float ySpeed = 0;
        float friction = 0.0125F;
        boolean forward = input.isKeyDown(Key.W);
        boolean backward = input.isKeyDown(Key.S);
        boolean left = input.isKeyDown(Key.A);
        boolean right = input.isKeyDown(Key.D);
        if(forward && !backward) {
            ySpeed += (float)Math.sin(Math.toRadians(-camera.getRotation().x));
            xSpeed += (float)Math.sin(Math.toRadians(camera.getRotation().y)) * (1 - Math.abs(ySpeed)) * speed * Timer.getDelta();
            zSpeed -= (float)Math.cos(Math.toRadians(camera.getRotation().y)) * (1 - Math.abs(ySpeed)) * speed * Timer.getDelta();
        }
        if(backward && !forward) {
            ySpeed += -(float)Math.sin(Math.toRadians(-camera.getRotation().x));
            xSpeed -= (float)Math.sin(Math.toRadians(camera.getRotation().y)) * (1 - Math.abs(ySpeed)) * speed * Timer.getDelta();
            zSpeed += (float)Math.cos(Math.toRadians(camera.getRotation().y)) * (1 - Math.abs(ySpeed)) * speed * Timer.getDelta();
        }
        if(left && !right) {
            xSpeed -= (float)Math.sin(Math.toRadians(camera.getRotation().y + 90)) * speed * 0.5F * Timer.getDelta();
            zSpeed += (float)Math.cos(Math.toRadians(camera.getRotation().y + 90)) * speed * 0.5F * Timer.getDelta();
        }
        if(right && !left) {
            xSpeed -= (float)Math.sin(Math.toRadians(camera.getRotation().y - 90)) * speed * 0.5F * Timer.getDelta();
            zSpeed += (float)Math.cos(Math.toRadians(camera.getRotation().y - 90)) * speed * 0.5F * Timer.getDelta();
        }
        ySpeed = ySpeed * speed * Timer.getDelta();
        velocity.x += (xSpeed - velocity.x) * (friction * Timer.getDelta());
        velocity.z += (zSpeed - velocity.z) * (friction * Timer.getDelta());
        velocity.y += (ySpeed - velocity.y) * (friction * Timer.getDelta());

        camera.getPosition().add(velocity);

        input.centerMouse();
    }

    @Override
    public void onUpdate() {}

    @Override
    public void onRender() {}

    @Override
    public void onDestroy() {}

    public Camera getCamera() {
        return camera;
    }
}
