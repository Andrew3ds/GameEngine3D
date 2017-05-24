package com.engine.gfx;

import com.engine.core.Engine;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * Created by Andrew on 1/6/2017.
 */
public class FirstPersonCamera extends Camera {
    private Vector3f position;
    private Vector3f rotation;
    private Vector3f vec;
    private Vector3f direction;
    private Vector3f cameraTarget;
    private Matrix4f transform;
    private Matrix4f projection;
    private Quaternionf quaternionf;

    public FirstPersonCamera(Vector3f position, Vector3f rotation) {
        this.position = position;
        this.rotation = rotation;
        this.vec = new Vector3f();
        this.direction = new Vector3f();
        this.cameraTarget = new Vector3f().zero();
        this.quaternionf = new Quaternionf();

        transform = new Matrix4f();
    }

    public FirstPersonCamera() {
        this(new Vector3f(), new Vector3f());
    }

    private void update() {
        if(rotation.y > 180) {
            rotation.y = -180;
        }
        if(rotation.y < -180) {
            rotation.y = 180;
        }
        if(rotation.x > 90) {
            rotation.x = 90;
        }
        if(rotation.x < -90) {
            rotation.x = -90;
        }
    }

    @Override
    public Vector3f getPosition() {
        return position;
    }

    @Override
    public Vector3f getRotation() {
        return rotation;
    }

    @Override
    public Vector3f getDirection() {
        return direction;
    }

    @Override
    public Matrix4f getWorldMatrix() {
        update();

        quaternionf.identity();
        quaternionf.rotateXYZ((float)Math.toRadians(rotation.x),(float)Math.toRadians(rotation.y),(float)Math.toRadians(rotation.z));

        transform.identity();
        transform.rotate(quaternionf);
        transform.translate(vec.set(position).negate());

        direction = transform.positiveZ(direction).negate().normalize();

        return transform;
    }

    @Override
    public Camera createProjection() {
        if(projection == null) {
            projection = new Matrix4f();
        }

        projection.identity();

        FOV = 75F;
        Far_Plane = 1000F;
        Near_Plane = 0.1F;
        float aspect = (float)Engine.display.getWidth() / (float)Engine.display.getHeight();

        float y_scale = (float) (1F / (Math.tan(Math.toRadians(FOV / 2F))));
        float x_scale = y_scale / aspect;
        float frustum = Far_Plane - Near_Plane;

        projection.m00(x_scale);
        projection.m11(y_scale);
        projection.m22(-((Far_Plane + Near_Plane) / frustum));
        projection.m23(-1);
        projection.m32(-((2 * Near_Plane * Far_Plane) / frustum));
        projection.m33(0);

        return this;
    }

    @Override
    public Matrix4f getProjection() {
        return projection;
    }
}
