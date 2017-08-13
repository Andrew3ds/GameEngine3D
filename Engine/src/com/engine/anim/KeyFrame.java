package com.engine.anim;

import java.util.Map;

/**
 * Created by Andrew on 8/9/2017.
 */
public class KeyFrame {
    private final float timeStamp;
    private final Map<String, JointTransform> jointKeyFrames;

    public KeyFrame(float timeStamp, Map<String, JointTransform> jointKeyFrames) {
        this.timeStamp = timeStamp;
        this.jointKeyFrames = jointKeyFrames;
    }

    public float getTimeStamp() {
        return timeStamp;
    }

    public Map<String, JointTransform> getJointKeyFrames() {
        return jointKeyFrames;
    }
}
