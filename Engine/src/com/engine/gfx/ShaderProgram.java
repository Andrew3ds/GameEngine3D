package com.engine.gfx;

import com.engine.misc.DialogWindow;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.engine.core.Engine.gl;
import static com.engine.gfx.IGL.GL_LINK_STATUS;
import static com.engine.gfx.IGL.GL_TRUE;

/**
 * Created by Andrew on 1/5/2017.
 */
public class ShaderProgram implements GLObject {
    public static final int TRANSFORM = 0;
    public static final int NORMAL = 1;
    public static final int WORLD = 2;
    public static final int PROJECTION = 3;

    private int handle = -1;
    private String name;
    private Map<Shader.Type, Shader> shaders;
    private Map<String, Uniform> uniforms;
    private boolean compiled;
    public Uniform[] matrix = {
            new Uniform<>("matrix.transform", new Matrix4f()),
            new Uniform<>("matrix.normal", new Matrix4f()),
            new Uniform<>("matrix.world", new Matrix4f()),
            new Uniform<>("matrix.projection", new Matrix4f())
    };

    public ShaderProgram(String name, Shader... shaders) {
        this.name = name;
        this.shaders = new HashMap<>();
        this.uniforms = new HashMap<>();

        for(Shader shader : shaders) {
            this.shaders.put(shader.getType(), shader);
        }

        handle = gl.CreateProgram();
    }

    public ShaderProgram compile() {
        if(compiled) {
            return this;
        }

        for(Shader shader : shaders.values()) {
            shader.compile();
            gl.AttachShader(getHandle(), shader.getHandle());
        }
        gl.LinkProgram(getHandle());
        if(gl.GetProgrami(getHandle(), GL_LINK_STATUS) != GL_TRUE) {
            try {
                throw new GLException(("Shader: ").concat(name).concat("\n").concat(gl.GetProgramInfoLog(getHandle())));
            } catch (GLException e) {
                DialogWindow.errorDialog(e);
            }
        }

        gl.ValidateProgram(getHandle());

        compiled = true;

        for(Uniform uniform : matrix) {
            addUniform(uniform);
        }

        return this;
    }

    public String getName() {
        return name;
    }

    public ShaderProgram addUniform(Uniform uniform) {
        uniforms.put(uniform.getName(), uniform.getLocation(this));

        return this;
    }

    public Uniform getUniform(String name) {
        return uniforms.get(name);
    }

    public ShaderProgram copyOf() {
        Shader[] shaders = new Shader[this.shaders.values().size()];
        int index = 0;
        for(Shader shader : this.shaders.values()) {
            shaders[index++] = shader;
        }

        return new ShaderProgram(name, shaders);
    }

    @Override
    public void dispose() {
        gl.DeleteProgram(getHandle());
        handle = -1;
        for(Shader shader : shaders.values()) {
            shader.dispose();
        }
        shaders = null;
        uniforms = null;
        name = null;
    }

    @Override
    public int getHandle() {
        return handle;
    }

    @Override
    public void bind() {
        gl.UseProgram(getHandle());
        for(Uniform uniform : uniforms.values()) {
            uniform.upload();
        }
    }

    @Override
    public void unbind() {
        gl.UseProgram(0);
    }
}
