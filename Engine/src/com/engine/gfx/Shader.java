package com.engine.gfx;

import static com.engine.core.Engine.gl;
import static com.engine.gfx.IGL.GL_FRAGMENT_SHADER;
import static com.engine.gfx.IGL.GL_GEOMETRY_SHADER;
import static com.engine.gfx.IGL.GL_VERTEX_SHADER;

/**
 * Created by Andrew on 1/5/2017.
 */
public class Shader implements GLObject {
    public enum Type {
        Vertex(GL_VERTEX_SHADER), Fragment(GL_FRAGMENT_SHADER), Geometry(GL_GEOMETRY_SHADER);

        final int handle;

        Type(int handle) {
            this.handle = handle;
        }
    }

    private int handle = -1;
    private Type type;
    private String source;
    private boolean compiled;

    public Shader(Type type, String source) {
        handle = gl.CreateShader((this.type = type).handle);

        this.source = source;
    }

    public Shader compile() {
        if(compiled) {
            return this;
        }

        gl.ShaderSource(getHandle(), source);
        gl.CompileShader(getHandle());

        compiled = true;

        return this;
    }

    public Shader copyOf() {
        return new Shader(type, source);
    }

    public Type getType() {
        return type;
    }

    @Override
    public void dispose() {
        gl.DeleteShader(getHandle());
        handle = -1;
        type = null;
    }

    @Override
    public int getHandle() {
        return handle;
    }

    @Override
    public void bind() {
    }

    @Override
    public void unbind() {
    }
}
