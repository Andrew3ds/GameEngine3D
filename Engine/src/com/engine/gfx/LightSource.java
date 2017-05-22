package com.engine.gfx;

/**
 * Created by Andrew on 5/20/2017.
 */
public class LightSource {
    public enum Type {
        Point, Directional, Spot;
    }

    private Type type;

    protected LightSource(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}
