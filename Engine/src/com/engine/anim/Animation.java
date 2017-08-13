package com.engine.anim;

/**
 * Created by Andrew on 8/9/2017.
 */
public class Animation {
    private final float length; // in seconds
    private final KeyFrame[] keyFrames;

    public Animation(float lengthInSeconds, KeyFrame[] keyFrames) {
        this.length = lengthInSeconds;
        this.keyFrames = keyFrames;
    }

    public float getLength() {
        return length;
    }

    public KeyFrame[] getKeyFrames() {
        return keyFrames;
    }
}
