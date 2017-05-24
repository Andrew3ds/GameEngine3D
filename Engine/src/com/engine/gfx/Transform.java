package com.engine.gfx;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Created by Andrew on 1/6/2017.
 */
public class Transform {
    private Vector3f position;
    private Vector3f rotation;
    private Vector3f scale;
    private Matrix4f matrix;
    private Matrix4f normalMatrix;

    public Transform(Vector3f position, Vector3f rotation, Vector3f scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;

        matrix = new Matrix4f();
        normalMatrix = new Matrix4f();
    }

    public Transform() {
        this(new Vector3f(), new Vector3f(), new Vector3f(1));
    }

    public Vector3f getPosition() {
        return position;
    }

    public Transform setPosition(Vector3f position) {
        this.position = position;

        return this;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public Transform setRotation(Vector3f rotation) {
        this.rotation = rotation;

        return this;
    }

    public Vector3f getScale() {
        return scale;
    }

    public Transform setScale(Vector3f scale) {
        this.scale = scale;

        return this;
    }

    public Transform rotateX(float amount) {
        rotation.x += amount;

        return this;
    }

    public Transform rotateY(float amount) {
        rotation.y += amount;

        return this;
    }

    public Transform rotateZ(float amount) {
        rotation.z += amount;

        return this;
    }

    public Transform rotate(float x, float y, float z) {
        return rotateX(x).rotateY(y).rotateZ(z);
    }

    public Transform rotate(Vector3f amount) {
        return rotate(amount.x, amount.y, amount.z);
    }

    public Matrix4f getMatrix() {
        if(rotation.x > 360) {
            rotation.x = 0;
        }

        matrix.identity();

        matrix.translate(position);
        matrix.rotateXYZ((float)Math.toRadians(rotation.x), (float)Math.toRadians(rotation.y), (float)Math.toRadians(rotation.z));
        matrix.scale(scale);

        return matrix;
    }

    public Matrix4f getNormalMatrix() {
        normalMatrix.identity();

        normalMatrix.set(getMatrix());
        normalMatrix.invert().transpose();

        return normalMatrix;
    }
}
