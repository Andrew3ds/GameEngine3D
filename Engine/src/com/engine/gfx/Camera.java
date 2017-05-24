package com.engine.gfx;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Created by Andrew on 1/6/2017.
 */
public abstract class Camera {
    public float FOV;
    public float Far_Plane;
    public float Near_Plane;

    public abstract Vector3f getPosition();
    public abstract Vector3f getRotation();
    public abstract Vector3f getDirection();
    public abstract Matrix4f getWorldMatrix();
    public abstract Camera createProjection();
    public abstract Matrix4f getProjection();
}
