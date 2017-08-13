package com.engine.anim;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * Created by Andrew on 8/9/2017.
 */
public class JointTransform {
    private static Matrix4f matrix = new Matrix4f();

    private final Vector3f position;
    private final Quaternionf rotation;

    private Matrix4f localTransform;

    public JointTransform(Vector3f position, Quaternionf rotation) {
        this.position = position;
        this.rotation = rotation;

        localTransform = new Matrix4f();
    }

    public Matrix4f getLocalTransform() {
        matrix.identity();
        localTransform.identity();
        localTransform.translate(position);
        localTransform.mul(rotation.get(matrix));

        return localTransform;
    }

    protected static JointTransform interpolate(JointTransform frameA, JointTransform frameB, float progression) {
        Vector3f pos = interpolate(frameA.position, frameB.position, progression);
        Quaternionf rot = new Quaternionf().set(frameA.rotation).slerp(frameB.rotation, progression);

        return new JointTransform(pos, rot);
    }

    private static Vector3f interpolate(Vector3f start, Vector3f end, float progression) {
        float x = start.x + (end.x - start.x) * progression;
        float y = start.y + (end.y - start.y) * progression;
        float z = start.z + (end.z - start.z) * progression;

        return new Vector3f(x, y, z);
    }
}
