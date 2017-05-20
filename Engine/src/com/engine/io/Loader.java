package com.engine.io;

import com.engine.gfx.Texture;
import com.engine.gfx.TextureParameter;
import com.engine.misc.DialogWindow;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryUtil;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

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

            return new Texture(0, 0, null, TextureParameter.defaultInstance);
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

                return new Texture(0, 0, null, TextureParameter.defaultInstance);
            }
        }

        MemoryUtil.memFree(buffer);

        return new Texture(w.get(), h.get(), pixels, TextureParameter.defaultInstance);
    }
}
