package com.engine.gfx;

import com.engine.core.Disposable;
import com.engine.misc.DialogWindow;

import static com.engine.core.Engine.gl;
import static com.engine.core.Engine.renderer;
import static org.lwjgl.opengl.GL11.GL_NONE;
import static org.lwjgl.opengl.GL11.glDrawBuffer;
import static org.lwjgl.opengl.GL11.glReadBuffer;

/**
 * Created by Andrew on 5/22/2017.
 */
public class DepthSurface implements Disposable {
    private int width;
    private int height;
    private Framebuffer depthComponent;
    private Texture texture;

    public DepthSurface(int width, int height) {
        this.width = width;
        this.height = height;

        create();
    }

    private void create() {
        if(width == 0 || height == 0) {
            return;
        }

        depthComponent = new Framebuffer(width, height);
        texture = new Texture(width, height, Texture.Target.DepthTexture, null,
                new TextureParameter(TextureParameter.Filter.Nearest, TextureParameter.Edge.Repeat, 0));
        depthComponent.texture2D(texture, Framebuffer.Attachment.Depth);
        depthComponent.bind();
        glDrawBuffer(GL_NONE);
        glReadBuffer(GL_NONE);
        depthComponent.unbind();

        if(!depthComponent.isComplete()) {
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
        gl.Viewport(0, 0, width, height);
        depthComponent.bind();
        renderer.clearScreen(false, true, false);
    }

    public void endRender() {
        depthComponent.unbind();
    }

    @Override
    public void dispose() {
        depthComponent.dispose();
        depthComponent = null;

        texture.dispose();
        texture = null;
    }
}
