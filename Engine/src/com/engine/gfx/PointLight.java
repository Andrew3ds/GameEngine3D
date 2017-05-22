package com.engine.gfx;

import org.joml.Vector3f;

/**
 * Created by Andrew on 5/21/2017.
 */
public class PointLight extends LightSource {
    private Vector3f color;
    private Vector3f position;

    private float constant;
    private float linear;
    private float quadratic;

    public PointLight(Vector3f color, Vector3f position, float constant, float linear, float quadratic) {
        super(Type.Point);

        this.color = color;
        this.position = position;
        this.constant = constant;
        this.linear = linear;
        this.quadratic = quadratic;
    }

    public Vector3f getColor() {
        return color;
    }

    public PointLight setColor(Vector3f color) {
        this.color = color;

        return this;
    }

    public Vector3f getPosition() {
        return position;
    }

    public PointLight setPosition(Vector3f position) {
        this.position = position;

        return this;
    }

    public float getConstant() {
        return constant;
    }

    public float getLinear() {
        return linear;
    }

    public float getQuadratic() {
        return quadratic;
    }
}
