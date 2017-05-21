package com.engine.gfx;

import com.engine.core.Disposable;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.engine.core.Engine.gl;
import static com.engine.gfx.IGL.GL_TRIANGLES;
import static com.engine.gfx.IGL.GL_UNSIGNED_INT;

/**
 * Created by Andrew on 1/6/2017.
 */
public class Mesh implements Disposable {
    public static List<Mesh> meshes = new ArrayList<>();
    private VertexArray vertexArray;
    private Vertex[] vertices;
    private Integer[] indices;
    private String name;
    private boolean finished;

    public Mesh(String name) {
        this.name = name;
    }

    public Integer[] getIndices() {
        return indices;
    }

    public Vertex[] getVertices() {
        return vertices;
    }

    public Mesh setVertices(Vertex[] vertices, Integer[] indices) {
        this.vertices = vertices;
        this.indices = indices;

        return this;
    }

    public Mesh finish() {
        if(finished) {
            return this;
        }

        vertexArray = new VertexArray().bufferData(vertices, indices);
        finished = true;
        meshes.add(this);

        return this;
    }

    public VertexArray getVertexArray() {
        return vertexArray;
    }

    public String getName() {
        return name;
    }

    public Mesh merge(Mesh other) {
        List<Vertex> vertexList = new ArrayList<>();
        List<Integer> integerList = new ArrayList<>();

        Collections.addAll(vertexList, this.vertices);
        Collections.addAll(vertexList, other.getVertices());
        Collections.addAll(integerList, this.indices);
        int offset = getVertices().length;
        for(Integer integer : other.getIndices()) {
            integerList.add(integer + offset);
        }

        Vertex[] newSet = new Vertex[vertexList.size()];
        vertexList.toArray(newSet);
        Integer[] newIndices = new Integer[integerList.size()];
        integerList.toArray(newIndices);

        return setVertices(newSet, newIndices);
    }

    public Mesh generateNormals() {
        Vector3f A = new Vector3f();
        Vector3f B = new Vector3f();
        Vector3f C = new Vector3f();

        Vector3f v1;
        Vector3f v2;

        for(int i = 0; i < indices.length; i+=3) {
            int i0 = indices[i + 0];
            int i1 = indices[i + 1];
            int i2 = indices[i + 2];

            A.set(vertices[i0].getPosition());
            B.set(vertices[i1].getPosition());
            C.set(vertices[i2].getPosition());

            v1 = B.sub(A);
            v2 = C.sub(A);

            Vector3f normal = v1.cross(v2).normalize();

            vertices[i0].getNormal().set(vertices[i0].getNormal().add(normal));
            vertices[i1].getNormal().set(vertices[i1].getNormal().add(normal));
            vertices[i2].getNormal().set(vertices[i2].getNormal().add(normal));
        }

        return this;
    }

    public Mesh unweld() {
        if(finished) {
            return this;
        }

        Vertex[] vertices = new Vertex[this.indices.length];
        Integer[] indices = new Integer[this.indices.length];
        int pos = 0;
        for(Integer index : this.indices) {
            indices[pos] = pos;
            vertices[pos++] = this.vertices[index];
        }

        setVertices(vertices, indices);

        return this;
    }

    public void render() {
        vertexArray.bind();
        gl.DrawElements(GL_TRIANGLES, vertexArray.getSize(), GL_UNSIGNED_INT, 0L);
        vertexArray.unbind();
    }

    public static void clearMeshes() {
        meshes.forEach(Mesh::dispose);
    }

    @Override
    public void dispose() {
        vertexArray.dispose();
        vertexArray = null;
        vertices = null;
        indices = null;
        name = null;
    }
}
