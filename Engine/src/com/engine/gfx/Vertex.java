package com.engine.gfx;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;
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
    private Vector3i jointIDS;
    private Vector3f weights;

    public Vertex(Vector3f position, Vector2f texCoord, Vector3f normal) {
        this.position = position;
        this.texCoord = texCoord;
        this.normal = normal;

        this.jointIDS = new Vector3i();
        this.weights = new Vector3f();
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

    public static ByteBuffer[] toByteBuffer(Vertex[] vertices) {
        ByteBuffer[] buffers = {
                MemoryUtil.memAlloc(vertices.length * 3 * 4), //Position
                MemoryUtil.memAlloc(vertices.length * 2 * 4), //Texture coordinates
                MemoryUtil.memAlloc(vertices.length * 3 * 4), //Normals
                MemoryUtil.memAlloc(vertices.length * 3 * 4),  //JOINTS
                MemoryUtil.memAlloc(vertices.length * 3 * 4)  //WEIGHTS
        };

        for(Vertex vertex : vertices) {
            buffers[0].putFloat(vertex.position.x).putFloat(vertex.position.y).putFloat(vertex.position.z);
            buffers[1].putFloat(vertex.texCoord.x).putFloat(vertex.texCoord.y);
            buffers[2].putFloat(vertex.normal.x).putFloat(vertex.normal.y).putFloat(vertex.normal.z);
            buffers[3].putInt(vertex.jointIDS.x).putInt(vertex.jointIDS.y).putInt(vertex.jointIDS.z);
            buffers[4].putFloat(vertex.weights.x).putFloat(vertex.weights.y).putFloat(vertex.weights.z);
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
