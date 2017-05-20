package com.engine.gfx;

/**
 * Created by Andrew on 1/6/2017.
 */
public class GLException extends Exception {
    public GLException() {
    }

    public GLException(String message) {
        super(message);
    }

    public GLException(String message, Throwable cause) {
        super(message, cause);
    }

    public GLException(Throwable cause) {
        super(cause);
    }

    public GLException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
