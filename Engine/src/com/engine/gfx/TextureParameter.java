package com.engine.gfx;

import com.engine.core.Engine;

import static com.engine.core.Engine.gl;
import static com.engine.gfx.IGL.*;

/**
 * Created by Andrew on 1/11/2017.
 */
public class TextureParameter {
    public enum Filter {
        Linear(GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR), Nearest(GL_NEAREST_MIPMAP_NEAREST, GL_NEAREST);

        final int min;
        final int mag;

        Filter(int min, int mag) {
            this.min = min;
            this.mag = mag;
        }
    }

    public enum Edge {
        Repeat(GL_REPEAT), Clamp(GL_CLAMP_TO_BORDER), MirroredRepeat(GL_MIRRORED_REPEAT);

        final int handle;

        Edge(int handle) {
            this.handle = handle;
        }
    }

    public static TextureParameter defaultInstance = new TextureParameter(Filter.Linear, Edge.Repeat, 16);

    private Filter filter;
    private Edge edge;
    private float anisoFilteringLevel;

    public TextureParameter(Filter filter, Edge edge, float anisoFilteringLevel) {
        this.filter = filter;
        this.edge = edge;
        this.anisoFilteringLevel = anisoFilteringLevel;
    }

    public void apply(Texture.Target target) {
        gl.TexParameteri(target.handle, GL_TEXTURE_MIN_FILTER, filter.min);
        gl.TexParameteri(target.handle, GL_TEXTURE_MAG_FILTER, filter.mag);
        gl.TexParameterf(target.handle, GL_TEXTURE_WRAP_S, edge.handle);
        gl.TexParameterf(target.handle, GL_TEXTURE_WRAP_T, edge.handle);
        gl.TexParameterfv(target.handle, GL_TEXTURE_BORDER_COLOR, new float[]{ 1.0F, 1.0F, 1.0F, 1.0F });

        if(gl.supportsAniso()) {
            float maxAniso = gl.GetFloat(GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT);
            gl.TexParameterf(target.handle, GL_TEXTURE_MAX_ANISOTROPY_EXT, Math.min(anisoFilteringLevel, maxAniso));
        }
    }
}
