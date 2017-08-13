package com.engine.gfx;

import com.engine.core.Disposable;
import org.joml.Vector3f;

/**
 * Created by Andrew on 5/24/2017.
 */
public class Material implements Disposable {
    private Texture diffuse;
    private Texture normalMap;
    private Vector3f color;

    public Material(Texture diffuse, Texture normalMap, Vector3f color) {
        this.diffuse = diffuse;
        this.normalMap = normalMap;
        this.color = color;
    }

    public Texture getDiffuse() {
        return diffuse;
    }

    public Texture getNormalMap() {
        return normalMap;
    }

    public Vector3f getColor() {
        return color;
    }

    @Override
    public void dispose() {
        diffuse.dispose();
        normalMap.dispose();
        color = null;
    }
}
