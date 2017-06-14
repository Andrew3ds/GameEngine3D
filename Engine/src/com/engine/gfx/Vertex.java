package com.engine.gfx;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

/**
 * Created by Andrew on 12/30/2016.
 */
public class Vertex {
    private Vector3f position;
    private Vector2f texCoord;
    private Vector3f normal;
    private Vector3f tangent;
    private Vector3f biTangent;

    public Vertex(Vector3f position, Vector2f texCoord, Vector3f normal) {
        this.position = position;
        this.texCoord = texCoord;
        this.normal = normal;

        this.tangent = new Vector3f();
        this.biTangent = new Vector3f();
    }

    public Vertex(Vector3f position, Vector2f texCoord) {
        this(position, texCoord, new Vector3f());
    }

    public Vertex(Vector3f position) {
        this(position, new Vector2f());
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector2f getTexCoord() {
        return texCoord;
    }

    public Vector3f getNormal() {
        return normal;
    }

    public Vector3f getTangent() {
        return tangent;
    }

    public Vector3f getBiTangent() {
        return biTangent;
    }

    public static ByteBuffer[] toByteBuffer(Vertex[] vertices) {
        ByteBuffer[] buffers = {
                MemoryUtil.memAlloc(vertices.length * 3 * 4), //Position
                MemoryUtil.memAlloc(vertices.length * 2 * 4), //Texture coordinates
                MemoryUtil.memAlloc(vertices.length * 3 * 4), //Normals
                MemoryUtil.memAlloc(vertices.length * 3 * 4), //T
                MemoryUtil.memAlloc(vertices.length * 3 * 4)  //B
        };

        for(Vertex vertex : vertices) {
            buffers[0].putFloat(vertex.position.x).putFloat(vertex.position.y).putFloat(vertex.position.z);
            buffers[1].putFloat(vertex.texCoord.x).putFloat(vertex.texCoord.y);
            buffers[2].putFloat(vertex.normal.x).putFloat(vertex.normal.y).putFloat(vertex.normal.z);
            buffers[3].putFloat(vertex.tangent.x).putFloat(vertex.tangent.y).putFloat(vertex.tangent.z);
            buffers[4].putFloat(vertex.biTangent.x).putFloat(vertex.biTangent.y).putFloat(vertex.biTangent.z);
        }

        return buffers;
    }

    public static ByteBuffer toByteBuffer(Integer[] indices) {
        ByteBuffer buffer = MemoryUtil.memAlloc(indices.length * 4);
        for(Integer index : indices) {
            buffer.putInt(index);
        }

        return buffer;
    }
}
