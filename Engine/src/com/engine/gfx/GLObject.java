package com.engine.gfx;

import com.engine.core.Disposable;

/**
 * Created by Andrew on 12/28/2016.
 */
public interface GLObject extends Disposable {
    int getHandle();
    void bind();
    void unbind();
}
