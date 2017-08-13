package com.engine.anim;

import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew on 8/9/2017.
 */
public class Joint {
    private static Matrix4f matrix = new Matrix4f();

    public final int index;
    public final String name;
    public final List<Joint> children;

    private Matrix4f animatedTransform;

    private final Matrix4f localBindTransform;
    private Matrix4f inverseBindTransform;

    public Joint(int index, String name, Matrix4f localBindTransform) {
        this.index = index;
        this.name = name;
        this.localBindTransform = localBindTransform;

        this.children = new ArrayList<>();
        this.animatedTransform = new Matrix4f();
        this.inverseBindTransform = new Matrix4f();
    }

    public void addChild(Joint child) {
        this.children.add(child);
    }

    public Matrix4f getAnimatedTransform() {
        return animatedTransform;
    }

    public Matrix4f getInverseBindTransform() {
        return inverseBindTransform;
    }

    public Joint setAnimatedTransform(Matrix4f animatedTransform) {
        this.animatedTransform = animatedTransform;

        return this;
    }

    protected void calcInverseBindTransform(Matrix4f parentBindTransform) {
        Matrix4f bindTransform = matrix.set(parentBindTransform).mul(localBindTransform);
        matrix.set(bindTransform).invert(inverseBindTransform);
        matrix.identity();
        for(Joint child : children) {
            child.calcInverseBindTransform(bindTransform);
        }
    }
}
