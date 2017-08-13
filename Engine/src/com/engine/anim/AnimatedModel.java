package com.engine.anim;

import com.engine.gfx.Material;
import com.engine.gfx.Mesh;
import org.joml.Matrix4f;

/**
 * Created by Andrew on 8/9/2017.
 */
public class AnimatedModel {
    private final Mesh mesh;
    private final Material material;

    private final Joint rootJoint;
    private final int jointCount;

    private final Animator animator;

    public AnimatedModel(Mesh mesh, Material material, Joint rootJoint, int jointCount) {
        this.mesh = mesh;
        this.material = material;
        this.rootJoint = rootJoint;
        this.jointCount = jointCount;

        this.animator = new Animator(this);

        if(rootJoint != null) {
            rootJoint.calcInverseBindTransform(new Matrix4f());
        }
    }

    public Mesh getMesh() {
        return mesh;
    }

    public Material getMaterial() {
        return material;
    }

    public Joint getRootJoint() {
        return rootJoint;
    }

    public int getJointCount() {
        return jointCount;
    }

    public void delete() {
        mesh.dispose();
        material.dispose();
    }

    public void doAnimation(Animation animation) {
        animator.doAnimation(animation);
    }

    public void update() {
        animator.update();
    }

    public Matrix4f[] getJointTransforms() {
        Matrix4f[] jointMatrices = new Matrix4f[jointCount];
        addJointsToArray(rootJoint, jointMatrices);

        return jointMatrices;
    }

    private void addJointsToArray(Joint headJoint, Matrix4f[] jointMatrices) {
        jointMatrices[headJoint.index] = headJoint.getAnimatedTransform();
        for(Joint child : headJoint.children) {
            addJointsToArray(child, jointMatrices);
        }
    }
}
