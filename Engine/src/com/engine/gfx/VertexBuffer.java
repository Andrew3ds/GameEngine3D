package com.engine.gfx;

import java.nio.ByteBuffer;

import static com.engine.core.Engine.gl;
import static com.engine.gfx.IGL.*;

/**
 * Created by Andrew on 12/28/2016.
 */
public class VertexBuffer implements GLObject {
    public enum Target {
        ArrayBuffer(GL_ARRAY_BUFFER), ElementArrayBuffer(GL_ELEMENT_ARRAY_BUFFER);

        final int handle;

        Target(int handle) {
            this.handle = handle;
        }
    }

    public enum Usage {
        StaticDraw(GL_STATIC_DRAW);

        final int handle;

        Usage(int handle) {
            this.handle = handle;
        }
    }

    private int handle = -1;
    private Target target = Target.ArrayBuffer;
    private Usage usage = Usage.StaticDraw;
    private int size;

    public VertexBuffer() {
        handle = gl.GenBuffers();
    }

    public Target getTarget() {
        return target;
    }

    public VertexBuffer setTarget(Target target) {
        this.target = target;

        return this;
    }

    public Usage getUsage() {
        return usage;
    }

    public VertexBuffer setUsage(Usage usage) {
        this.usage = usage;

        return this;
    }

    public VertexBuffer bufferData(ByteBuffer buffer) {
        bind();
        gl.BufferData(target.handle, (ByteBuffer)buffer.flip(), usage.handle);
        unbind();

        size = buffer.capacity() / 4;

        return this;
    }

    public VertexBuffer attribPointerFloat(int position, int size) {
        bind();
        gl.VertexAttribPointer(position, size, GL_FLOAT, false, 0, 0L);
        unbind();

        return this;
    }

    public VertexBuffer attribPointerInt(int position, int size) {
        bind();
        gl.VertexAttribPointer(position, size, GL_INT, false, 0, 0L);
        unbind();

        return this;
    }

    public int getSize() {
        return size;
    }

    @Override
    public void dispose() {
        gl.DeleteBuffers(getHandle());
        handle = -1;
        target = null;
        usage = null;
    }

    @Override
    public int getHandle() {
        return handle;
    }

    @Override
    public void bind() {
        gl.BindBuffer(target.handle, getHandle());
    }

    @Override
    public void unbind() {
        gl.BindBuffer(target.handle, 0);
    }
}
