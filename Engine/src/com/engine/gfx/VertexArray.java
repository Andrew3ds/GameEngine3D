package com.engine.gfx;

import java.nio.ByteBuffer;

import static com.engine.core.Engine.gl;

/**
 * Created by Andrew on 12/30/2016.
 */
public class VertexArray implements GLObject {
    private int handle = -1;
    private VertexBuffer ibo;
    private VertexBuffer[] vbos;

    public VertexArray() {
        handle = gl.GenVertexArrays();
    }

    public VertexArray bufferData(Vertex[] vertices, Integer[] indices) {
        ibo = new VertexBuffer().setTarget(VertexBuffer.Target.ElementArrayBuffer).bufferData(Vertex.toByteBuffer(indices));

        ByteBuffer[] buffers = Vertex.toByteBuffer(vertices);

        bind();
        vbos = new VertexBuffer[] {
                new VertexBuffer().bufferData(buffers[0]).attribPointer(0, 3),
                new VertexBuffer().bufferData(buffers[1]).attribPointer(1, 2),
                new VertexBuffer().bufferData(buffers[2]).attribPointer(2, 3),
                new VertexBuffer().bufferData(buffers[3]).attribPointer(3, 3),
                new VertexBuffer().bufferData(buffers[4]).attribPointer(4, 3)
        };
        unbind();

        return this;
    }

    public int getSize() {
        return ibo.getSize();
    }

    @Override
    public void dispose() {
        gl.DeleteVertexArrays(getHandle());
        handle = -1;
        ibo.dispose();
        ibo = null;
        for(VertexBuffer vbo : vbos) {
            vbo.dispose();
        }
        vbos = null;
    }

    @Override
    public int getHandle() {
        return handle;
    }

    @Override
    public void bind() {
        gl.BindVertexArray(getHandle());
        if(ibo != null) { ibo.bind(); }
        if(vbos != null) {
            for (int i = 0; i < vbos.length; i++) {
                gl.EnableVertexAttribArray(i);
            }
        }
    }

    @Override
    public void unbind() {
        if(ibo != null) { ibo.unbind(); }
        if(vbos != null) {
            for (int i = 0; i < vbos.length; i++) {
                gl.DisableVertexAttribArray(i);
            }
        }
        gl.BindVertexArray(0);
    }
}
