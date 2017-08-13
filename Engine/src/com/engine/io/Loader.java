package com.engine.io;

import com.engine.gfx.*;
import com.engine.misc.DialogWindow;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryUtil;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew on 1/6/2017.
 */
public class Loader {
    public static String getFileAsString(Asset location) {
        StringBuilder sb = new StringBuilder();
        try {
            FileReader fr = new FileReader(location.toString());
            BufferedReader reader = new BufferedReader(fr);
            String line;
            while((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }

            reader.close();
            fr.close();
        } catch (IOException e) {
            DialogWindow.errorDialog(e);
        }

        return sb.toString();
    }

    public static Texture loadTexture(Asset location) {
        ByteBuffer buffer;
        try {
            InputStream input = new FileInputStream(location.toString());
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] chunk = new byte[4096];
            int n;
            while((n = input.read(chunk)) > 0) {
                output.write(chunk, 0, n);
            }
            buffer = MemoryUtil.memAlloc(output.size());
            buffer.put(output.toByteArray());
            buffer.flip();
        } catch (IOException e) {
            DialogWindow.errorDialog(e);

            return new Texture(0, 0, Texture.Target.Texture2D, null, TextureParameter.defaultInstance);
        }

        IntBuffer w = MemoryUtil.memAllocInt(1);
        IntBuffer h = MemoryUtil.memAllocInt(1);
        IntBuffer comp = MemoryUtil.memAllocInt(1);

        ByteBuffer pixels = STBImage.stbi_load_from_memory(buffer, w, h, comp, 4);
        if(pixels == null) {
            try {
                throw new IOException(STBImage.stbi_failure_reason());
            } catch (IOException e) {
                DialogWindow.errorDialog(e);

                return new Texture(0, 0, Texture.Target.Texture2D, null, TextureParameter.defaultInstance);
            }
        }

        MemoryUtil.memFree(buffer);

        return new Texture(w.get(), h.get(), Texture.Target.Texture2D, pixels, TextureParameter.defaultInstance);
    }

    public static Mesh loadMesh(Asset location) {
        Mesh m = new Mesh(location.asFile().getName());
        List<Vector3f> positions = new ArrayList<>();
        List<Vector2f> tCoords = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Integer> indexList = new ArrayList<>();

        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(location.asFile()));

            int size = dis.readInt();
            for(int i = 0; i < size; i+=3) {
                Vector3f vertex = new Vector3f();
                vertex.x = dis.readFloat();
                vertex.y = dis.readFloat();
                vertex.z = dis.readFloat();

                positions.add(vertex);
            }
            size = dis.readInt();
            for(int i = 0; i < size; i+=2) {
                Vector2f tCoord = new Vector2f();
                tCoord.x = dis.readFloat();
                tCoord.y = dis.readFloat();

                tCoords.add(tCoord);
            }
            size = dis.readInt();
            for(int i = 0; i < size; i+=3) {
                Vector3f normal = new Vector3f();
                normal.x = dis.readFloat();
                normal.y = dis.readFloat();
                normal.z = dis.readFloat();

                normals.add(normal);
            }
            size = dis.readInt();
            for(int i = 0; i < size; i++) {
                indexList.add(dis.readInt());
            }

            dis.close();
        } catch (IOException e) {
            DialogWindow.errorDialog(e);

            return null;
        }

        Vertex[] vertices = new Vertex[positions.size()];
        for(int i = 0; i < positions.size(); i++) {
            vertices[i] = new Vertex(
                    positions.get(i),
                    tCoords.get(i),
                    normals.get(i)
            );
        }
        Integer[] indices = new Integer[indexList.size()];
        indexList.toArray(indices);

        m.setVertices(vertices, indices).generateNormals().finish();

        return m;
    }
}
