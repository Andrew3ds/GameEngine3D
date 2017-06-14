package com.engine.gfx;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static com.engine.core.Engine.gl;
import static com.engine.gfx.IGL.*;

/**
 * Created by Andrew on 1/6/2017.
 */
public class Texture implements GLObject {
    public enum Target {
        Texture2D(GL_TEXTURE_2D), DepthTexture(GL_TEXTURE_2D);

        final int handle;

        Target(int handle) {
            this.handle = handle;
        }
    }

    public static List<Texture> textures = new ArrayList<>();
    public static Texture NULL;
    private int handle = -1;
    private int width;
    private int height;
    private Target target = Target.Texture2D;
    private ByteBuffer pixels;
    private TextureParameter parameter;

    public Texture(int width, int height, Target target, ByteBuffer pixels, TextureParameter parameter) {
        handle = gl.GenTextures();

        this.target = target;
        bind();
        switch(target) {
            case Texture2D: {
                gl.TexImage2D(target.handle, 0, GL_SRGB_ALPHA, this.width = width, this.height = height, 0, GL_RGBA, GL_UNSIGNED_BYTE, this.pixels = pixels);
            } break;
            case DepthTexture: {
                gl.TexImage2D(target.handle, 0, GL_DEPTH_COMPONENT32F, this.width = width, this.height = height, 0, GL_DEPTH_COMPONENT, GL_FLOAT, this.pixels = pixels);
            } break;
        }
        gl.GenerateMipmap(target.handle);
        parameter.apply(target);
        unbind();

        textures.add(this);
    }

    public Target getTarget() {
        return target;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Texture slot(int i) {
        gl.ActiveTexture(GL_TEXTURE0 + i);

        return this;
    }

    public static void clearTextures() {
        textures.forEach(Texture::dispose);
    }

    public static void initialize() {
        NULL = new Texture(0, 0, Texture.Target.Texture2D, null, TextureParameter.defaultInstance);
    }

    @Override
    public void dispose() {
        gl.DeleteTextures(getHandle());
        handle = -1;
    }

    @Override
    public int getHandle() {
        return handle;
    }

    @Override
    public void bind() {
        gl.BindTexture(target.handle, getHandle());
    }

    @Override
    public void unbind() {
        gl.BindTexture(target.handle, 0);
    }
}
