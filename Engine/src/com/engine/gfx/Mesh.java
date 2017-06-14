package com.engine.gfx;

import com.engine.core.Disposable;
import org.joml.Vector2f;
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
        Vector2f UVA = new Vector2f();
        Vector2f UVB = new Vector2f();
        Vector2f UVC = new Vector2f();

        Vector3f v1;
        Vector3f v2;

        Vector2f deltaUV1;
        Vector2f deltaUV2;

        for(int i = 0; i < indices.length; i+=3) {
            int i0 = indices[i + 0];
            int i1 = indices[i + 1];
            int i2 = indices[i + 2];

//            Normal generation
            A.set(vertices[i0].getPosition());
            B.set(vertices[i1].getPosition());
            C.set(vertices[i2].getPosition());

            v1 = B.sub(A);
            v2 = C.sub(A);

            Vector3f normal = v1.cross(v2).normalize();

            vertices[i0].getNormal().set(vertices[i0].getNormal().add(normal));
            vertices[i1].getNormal().set(vertices[i1].getNormal().add(normal));
            vertices[i2].getNormal().set(vertices[i2].getNormal().add(normal));

//            Tangent generation
            A.set(vertices[i0].getPosition());
            B.set(vertices[i1].getPosition());
            C.set(vertices[i2].getPosition());

            UVA.set(vertices[i0].getTexCoord());
            UVB.set(vertices[i1].getTexCoord());
            UVC.set(vertices[i2].getTexCoord());

            v1 = B.sub(A);
            v2 = C.sub(A);

            deltaUV1 = UVB.sub(UVA);
            deltaUV2 = UVC.sub(UVA);

            float f = 1.0f / (deltaUV1.x * deltaUV2.y - deltaUV2.x * deltaUV1.y);

            Vector3f tangent = new Vector3f();
            Vector3f biTangent = new Vector3f();

            tangent.x = f * (deltaUV2.y * v1.x - deltaUV1.y * v2.x);
            tangent.y = f * (deltaUV2.y * v1.y - deltaUV1.y * v2.y);
            tangent.z = f * (deltaUV2.y * v1.z - deltaUV1.y * v2.z);
            tangent.normalize();

            vertices[i0].getTangent().add(tangent);
            vertices[i1].getTangent().add(tangent);
            vertices[i2].getTangent().add(tangent);

            biTangent.x = f * (deltaUV2.x * v1.x + deltaUV1.x * v2.x);
            biTangent.y = f * (deltaUV2.x * v1.y + deltaUV1.x * v2.y);
            biTangent.z = f * (deltaUV2.x * v1.z + deltaUV1.x * v2.z);
            biTangent.normalize();

            vertices[i0].getBiTangent().add(biTangent);
            vertices[i1].getBiTangent().add(biTangent);
            vertices[i2].getBiTangent().add(biTangent);
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
