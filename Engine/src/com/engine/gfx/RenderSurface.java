package com.engine.gfx;

import com.engine.core.Disposable;
import com.engine.misc.DialogWindow;

/**
 * Created by Andrew on 1/11/2017.
 */
public class RenderSurface implements Disposable {
    private int width;
    private int height;
    private int samples;
    private Framebuffer multisampledFBO;
    private Framebuffer fbo;
    private Renderbuffer rboColor;
    private Renderbuffer rboDepth;
    private Texture texture;

    public RenderSurface(int width, int height, int samples) {
        this.width = width;
        this.height = height;
        this.samples = samples;

        create();
    }

    public void resize(int width, int height) {
        this.width = width;
        this.height = height;

        destroy();
        create();
    }

    private void create() {
        if(width == 0 || height == 0) {
            return;
        }

        multisampledFBO = new Framebuffer(width, height);
        rboColor = new Renderbuffer(width, height);
        rboDepth = new Renderbuffer(width, height);
        rboColor.multisampledStorage(samples, Renderbuffer.Format.RGBA8);
        rboDepth.multisampledStorage(samples, Renderbuffer.Format.Depth24Stencil8);
        multisampledFBO.applyRenderbuffer(rboColor, Framebuffer.Attachment.Color0);
        multisampledFBO.applyRenderbuffer(rboDepth, Framebuffer.Attachment.Depth);
        if(!multisampledFBO.complete()) {
            try {
                throw new GLException("Framebuffer incomplete");
            } catch (GLException e) {
                DialogWindow.errorDialog(e);
            }
        }

        fbo = new Framebuffer(width, height);
        texture = new Texture(width, height, null, TextureParameter.defaultInstance);
        fbo.texture2D(texture);
        if(!fbo.complete()) {
            try {
                throw new GLException("Framebuffer incomplete");
            } catch (GLException e) {
                DialogWindow.errorDialog(e);
            }
        }
    }

    public Texture getTexture() {
        return texture;
    }

    public void beginRender() {
        multisampledFBO.bind();
    }

    public void endRender() {
        multisampledFBO.unbind();
        multisampledFBO.blitTo(fbo);
    }

    private void destroy() {
        multisampledFBO.dispose();
        fbo.dispose();
        rboColor.dispose();
        rboDepth.dispose();
        texture.dispose();
    }

    @Override
    public void dispose() {
        destroy();
    }
}
