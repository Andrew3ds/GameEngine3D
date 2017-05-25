package com.engine.gfx;

import org.joml.Vector3f;

/**
 * Created by Andrew on 5/24/2017.
 */
public class Material {
    private Texture texture;
    private Vector3f color;

    public Material(Texture texture, Vector3f color) {
        this.texture = texture;
        this.color = color;
    }

    public Texture getTexture() {
        return texture;
    }

    public Vector3f getColor() {
        return color;
    }
}
