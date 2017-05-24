package com.engine.gfx;

import static com.engine.core.Engine.gl;
import static com.engine.gfx.IGL.*;

/**
 * Created by Andrew on 1/11/2017.
 */
public class Framebuffer implements GLObject {
    public enum Target {
        Default(GL_FRAMEBUFFER), Read(GL_READ_FRAMEBUFFER), Draw(GL_DRAW_FRAMEBUFFER);

        final int handle;

        Target(int handle) {
            this.handle = handle;
        }
    }

    public enum Attachment {
        Color0(GL_COLOR_ATTACHMENT0), Color1(GL_COLOR_ATTACHMENT1), Color2(GL_COLOR_ATTACHMENT2), Color3(GL_COLOR_ATTACHMENT3), Depth(GL_DEPTH_ATTACHMENT);

        final int handle;

        Attachment(int handle) {
            this.handle = handle;
        }
    }

    private int handle = -1;
    private Target target = Target.Default;
    private int width;
    private int height;

    public Framebuffer(int width, int height) {
        this.width = width;
        this.height = height;

        handle = gl.GenFramebuffers();
    }

    public Framebuffer setTarget(Target target) {
        this.target = target;

        return this;
    }

    public void applyRenderbuffer(Renderbuffer renderbuffer, Attachment attachment) {
        bind();
        gl.FramebufferRenderbuffer(target.handle, attachment.handle, GL_RENDERBUFFER, renderbuffer.getHandle());
        unbind();
    }

    public void texture2D(Texture texture, Attachment attachment) {
        bind();
        gl.FramebufferTexture2D(target.handle, attachment.handle, texture.getTarget().handle, texture.getHandle(), 0);
        unbind();
    }

    public boolean isComplete() {
        bind();
        boolean status = gl.CheckFramebufferStatus(target.handle) == GL_FRAMEBUFFER_COMPLETE;
        unbind();

        return status;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void blitTo(Framebuffer framebuffer) {
        setTarget(Target.Read);
        bind();
        framebuffer.setTarget(Target.Draw);
        framebuffer.bind();
        gl.BlitFramebuffer(0, 0, getWidth(), getHeight(), 0, 0, framebuffer.getWidth(), framebuffer.getHeight(), GL_COLOR_BUFFER_BIT, GL_NEAREST);
        framebuffer.unbind();
        framebuffer.setTarget(Target.Default);
        unbind();
        setTarget(Target.Default);
    }

    @Override
    public void dispose() {
        gl.DeleteFramebuffers(getHandle());
        handle = -1;
    }

    @Override
    public int getHandle() {
        return handle;
    }

    @Override
    public void bind() {
        gl.BindFramebuffer(target.handle, getHandle());
    }

    @Override
    public void unbind() {
        gl.BindFramebuffer(target.handle, 0);
    }
}
