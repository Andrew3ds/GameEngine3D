package com.engine.gfx;

import static com.engine.core.Engine.gl;
import static com.engine.gfx.IGL.GL_DEPTH24_STENCIL8;
import static com.engine.gfx.IGL.GL_RENDERBUFFER;
import static com.engine.gfx.IGL.GL_RGBA8;

/**
 * Created by Andrew on 1/11/2017.
 */
public class Renderbuffer implements GLObject {
    public enum Format {
        RGBA8(GL_RGBA8), Depth24Stencil8(GL_DEPTH24_STENCIL8);

        final int handle;

        Format(int handle) {
            this.handle = handle;
        }
    }

    private int handle = -1;
    private int width;
    private int height;

    public Renderbuffer(int width, int height) {
        this.width = width;
        this.height = height;

        handle = gl.GenRenderbuffers();
    }

    public void multisampledStorage(int samples, Format format) {
        bind();
        gl.RenderbufferStorageMultisample(GL_RENDERBUFFER, samples, format.handle, width, height);
        unbind();
    }

    @Override
    public void dispose() {
        gl.DeleteRenderbuffers(getHandle());
        handle = -1;
        width = -1;
        height = -1;
    }

    @Override
    public int getHandle() {
        return handle;
    }

    @Override
    public void bind() {
        gl.BindRenderbuffer(GL_RENDERBUFFER, getHandle());
    }

    @Override
    public void unbind() {
        gl.BindRenderbuffer(GL_RENDERBUFFER, 0);
    }
}
