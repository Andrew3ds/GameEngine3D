package com.engine.gfx;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.par.ParShapes;
import org.lwjgl.util.par.ParShapesMesh;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Random;

import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Created by Andrew on 1/6/2017.
 */
public class MeshGen {
    private static int numCubes = 1;
    private static int numPyra = 1;
    private static int numIcos = 1;
    private static int numSpheres = 1;
    private static int numRocks = 1;
    private static Random random = new Random();

    public static Mesh cube(float width, float height, float depth) {
        ParShapesMesh cube = ParShapes.par_shapes_create_cube();
//        ParShapes.par_shapes_translate(cube, -width * 0.5F, -height * 0.5F, -depth * 0.5F);
        ParShapes.par_shapes_translate(cube,  -0.5F, -0.5F, -0.5F);

        return process("cube #".concat(String.valueOf(numCubes++)), cube, width, height, depth);
    }

    public static Mesh cubeTextured(float width, float height, float depth) {
        float x = width / 2F;
        float y = height / 2F;
        float z = height / 2F;

        Vertex[] vertices = {
                new Vertex(new Vector3f(-x,  y,  z), new Vector2f(0, 0)), //0
                new Vertex(new Vector3f(-x, -y,  z), new Vector2f(0, 1)), //1
                new Vertex(new Vector3f( x, -y,  z), new Vector2f(1, 1)), //2
                new Vertex(new Vector3f( x,  y,  z), new Vector2f(1, 0)), //3

                new Vertex(new Vector3f( x,  y,  z), new Vector2f(0, 0)), //4
                new Vertex(new Vector3f( x, -y,  z), new Vector2f(0, 1)), //5
                new Vertex(new Vector3f( x, -y, -z), new Vector2f(1, 1)), //6
                new Vertex(new Vector3f( x,  y, -z), new Vector2f(1, 0)), //7

                new Vertex(new Vector3f( x,  y, -z), new Vector2f(0, 0)), //8
                new Vertex(new Vector3f( x, -y, -z), new Vector2f(0, 1)), //9
                new Vertex(new Vector3f(-x, -y, -z), new Vector2f(1, 1)), //10
                new Vertex(new Vector3f(-x,  y, -z), new Vector2f(1, 0)), //11

                new Vertex(new Vector3f(-x,  y, -z), new Vector2f(0, 0)), //12
                new Vertex(new Vector3f(-x, -y, -z), new Vector2f(0, 1)), //13
                new Vertex(new Vector3f(-x, -y,  z), new Vector2f(1, 1)), //14
                new Vertex(new Vector3f(-x,  y,  z), new Vector2f(1, 0)), //15
        };
        Integer[] indices = {
                0, 1, 2,
                0, 2, 3,

                4, 5, 6,
                4, 6, 7,

                8, 9, 10,
                8, 10, 11,

                12, 13, 14,
                12, 14, 15
        };

        return new Mesh("cube #".concat(String.valueOf(numCubes++))).setVertices(vertices, indices);
    }

    public static Mesh pyramid(float width, float height, float depth) {
        float x = width / 2F;
        float y = height / 2F;
        float z = height / 2F;

        Vertex[] vertices = {
                new Vertex(new Vector3f( x, -y,  z), new Vector2f(0, 0)), //0
                new Vertex(new Vector3f(-x, -y,  z), new Vector2f(0, 0)), //1
                new Vertex(new Vector3f( x, -y, -z), new Vector2f(0, 0)), //2
                new Vertex(new Vector3f(-x, -y, -z), new Vector2f(0, 0)), //3

                new Vertex(new Vector3f(0,  y,  0), new Vector2f(0.5F, 0)), //4
                new Vertex(new Vector3f(-x, -y,  z), new Vector2f(0, 1)), //5
                new Vertex(new Vector3f( x, -y,  z), new Vector2f(1, 1)), //6

                new Vertex(new Vector3f( x, -y,  z), new Vector2f(0, 1)), //7
                new Vertex(new Vector3f( x, -y, -z), new Vector2f(1, 1)), //8

                new Vertex(new Vector3f( x, -y, -z), new Vector2f(0, 1)), //9
                new Vertex(new Vector3f(-x, -y, -z), new Vector2f(1, 1)), //10

                new Vertex(new Vector3f(-x, -y, -z), new Vector2f(0, 1)), //11
                new Vertex(new Vector3f(-x, -y,  z), new Vector2f(1, 1)), //12
        };
        Integer[] indices = {
                0, 1, 2,
                2, 1, 3,

                4, 5, 6,
                4, 7, 8,
                4, 9, 10,
                4, 11, 12
        };

        return new Mesh("pyramid #".concat(String.valueOf(numPyra++))).setVertices(vertices, indices).generateNormals();
    }

    public static Mesh icosahedron(float size) {
        ParShapesMesh ico = ParShapes.par_shapes_create_icosahedron();

        return process("icosahedron #1".concat(String.valueOf(numIcos++)), ico, size, size, size);
    }

    public static Mesh sphere(int slices, int stacks, float radius) {
        ParShapesMesh sphere = ParShapes.par_shapes_create_parametric_sphere(slices, stacks);

        return process("sphere #".concat(String.valueOf(numSpheres++)), sphere, radius, radius, radius);
    }

    public static Mesh rock(int subdivisions, float width, float height, float depth) {
        ParShapesMesh rock = ParShapes.par_shapes_create_rock(random.nextInt(500), Math.min(4, subdivisions));

        return process("rock #".concat(String.valueOf(numRocks++)), rock, width, height, depth);
    }

    private static Mesh process(String name, ParShapesMesh mesh, float width, float height, float depth) {
        ParShapes.par_shapes_scale(mesh, width, height, depth);
        ParShapes.par_shapes_compute_normals(mesh);

        return parse(name, mesh);
    }

    private static Mesh parse(String name, ParShapesMesh mesh) {
        int vertexCount = mesh.npoints();
        int triangleCount = mesh.ntriangles();

        FloatBuffer points = mesh.points(vertexCount * 3);
        FloatBuffer normals = mesh.normals(vertexCount * 3);
        FloatBuffer tCoords = null;
        ShortBuffer triangles = mesh.triangles(triangleCount * 3);

        boolean hasTexCoords = MemoryUtil.memGetAddress(mesh.address() + ParShapesMesh.TCOORDS) != NULL;
        if(hasTexCoords) {
            tCoords = mesh.tcoords(vertexCount * 2);
        } else {
            System.out.println("Generated mesh has no texture coordinates");
        }

        Vertex[] vertices = new Vertex[vertexCount];
        Integer[] indices = new Integer[triangleCount * 3];

        for(int i = 0; i < vertexCount; i++) {
            vertices[i] = new Vertex(
                    new Vector3f(
                            points.get(), points.get(), points.get()),
                            tCoords==null?new Vector2f():new Vector2f(tCoords.get(), tCoords.get()),
                            new Vector3f(normals.get(), normals.get(), normals.get()
                    )
            );
        }
        for(int i = 0; i < triangleCount; i++) {
            indices[i*3 + 0] = (int)triangles.get();
            indices[i*3 + 1] = (int)triangles.get();
            indices[i*3 + 2] = (int)triangles.get();
        }

        ParShapes.par_shapes_free_mesh(mesh);
        MemoryUtil.memFree(points);
        MemoryUtil.memFree(normals);
        MemoryUtil.memFree(triangles);

        return new Mesh(name).setVertices(vertices, indices).generateNormals();
    }
}
