package com.engine.gfx;

import org.joml.Vector3f;

/**
 * Created by Andrew on 5/21/2017.
 */
public class SpotLight extends LightSource {
    private Vector3f color;
    private Vector3f position;
    private Vector3f direction;

    private float cutoff;
    private float outerCutoff;

    public SpotLight(Vector3f color, Vector3f position, Vector3f direction, float cutoff, float outerCutoff) {
        super(Type.Spot);

        this.color = color;
        this.position = position;
        this.direction = direction;
        this.cutoff = cutoff;
        this.outerCutoff = outerCutoff;
    }

    public Vector3f getColor() {
        return color;
    }

    public SpotLight setColor(Vector3f color) {
        this.color = color;
        return this;
    }

    public Vector3f getPosition() {
        return position;
    }

    public SpotLight setPosition(Vector3f position) {
        this.position = position;
        return this;
    }

    public Vector3f getDirection() {
        return direction;
    }

    public SpotLight setDirection(Vector3f direction) {
        this.direction = direction;
        return this;
    }

    public float getCutoff() {
        return cutoff;
    }

    public float getOuterCutoff() {
        return outerCutoff;
    }
}
