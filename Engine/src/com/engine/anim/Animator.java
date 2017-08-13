package com.engine.anim;

import com.engine.core.Timer;
import org.joml.Matrix4f;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andrew on 8/9/2017.
 */
public class Animator {
    private static Matrix4f matrix = new Matrix4f();

    private final AnimatedModel target;

    private Animation currentAnimation;
    private float animationTime = 0;

    public Animator(AnimatedModel target) {
        this.target = target;
    }

    public void doAnimation(Animation animation) {
        this.animationTime = 0;
        this.currentAnimation = animation;
    }

    public void update() {
        if(currentAnimation == null) {
            return;
        }

        increaseAnimationTime();
        Map<String, Matrix4f> currentPose = calculateCurrentAnimationPose();
        applyPoseToJoints(currentPose, target.getRootJoint(), new Matrix4f());
    }

    private void increaseAnimationTime() {
        animationTime += Timer.getDelta();
        if(animationTime > currentAnimation.getLength()) {
            this.animationTime %= currentAnimation.getLength();
        }
    }

    private Map<String, Matrix4f> calculateCurrentAnimationPose() {
        KeyFrame[] frames = getPreviousAndNextFrames();
        float progression = calculateProgression(frames[0], frames[1]);

        return interpolatePoses(frames[0], frames[1], progression);
    }

    private void applyPoseToJoints(Map<String, Matrix4f> currentPose, Joint joint, Matrix4f parentTransform) {
        matrix.identity();

        Matrix4f currentLocalTransform = currentPose.get(joint.name);
        Matrix4f currentTransform = matrix.set(parentTransform).mul(currentLocalTransform);
        for(Joint child : joint.children) {
            applyPoseToJoints(currentPose, child, currentTransform);
        }
        currentTransform = matrix.set(currentTransform).mul(joint.getInverseBindTransform());
        joint.setAnimatedTransform(currentTransform);
    }

    private KeyFrame[] getPreviousAndNextFrames() {
        KeyFrame[] allFrames = currentAnimation.getKeyFrames();
        KeyFrame previousFrame = allFrames[0];
        KeyFrame nextFrame = allFrames[0];
        for(int i = 0; i < allFrames.length; i++) {
            nextFrame = allFrames[i];
            if(nextFrame.getTimeStamp() > animationTime) {
                break;
            }
            previousFrame = allFrames[i];
        }

        return new KeyFrame[] { previousFrame, nextFrame };
    }


    private float calculateProgression(KeyFrame previousFrame, KeyFrame nextFrame) {
        float totalTime = nextFrame.getTimeStamp() - previousFrame.getTimeStamp();
        float currentTime = animationTime - previousFrame.getTimeStamp();

        return currentTime / totalTime;
    }

    private Map<String, Matrix4f> interpolatePoses(KeyFrame previousFrame, KeyFrame nextFrame, float progression) {
        Map<String, Matrix4f> currentPose = new HashMap<>();
        for(String jointName : previousFrame.getJointKeyFrames().keySet()) {
            JointTransform previousTransform = previousFrame.getJointKeyFrames().get(jointName);
            JointTransform nextTransform = nextFrame.getJointKeyFrames().get(jointName);
            JointTransform currentTransform = JointTransform.interpolate(previousTransform, nextTransform, progression);
            currentPose.put(jointName, currentTransform.getLocalTransform());
        }

        return currentPose;
    }
}
