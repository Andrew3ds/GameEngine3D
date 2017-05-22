package com.engine.gfx;

import org.joml.Vector3f;

/**
 * Created by Andrew on 5/21/2017.
 */
public class DirectionalLight extends LightSource {
    private Vector3f color;
    private Vector3f direction;

    public DirectionalLight(Vector3f color, Vector3f direction) {
        super(Type.Directional);

        this.color = color;
        this.direction = direction;
    }

    public Vector3f getColor() {
        return color;
    }

    public DirectionalLight setColor(Vector3f color) {
        this.color = color;
        return this;
    }

    public Vector3f getDirection() {
        return direction;
    }

    public DirectionalLight setDirection(Vector3f direction) {
        this.direction = direction;
        return this;
    }
}
