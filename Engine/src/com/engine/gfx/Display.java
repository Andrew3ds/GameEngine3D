package com.engine.gfx;

import com.engine.core.Engine;
import org.lwjgl.GLWindow;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;

import static org.lwjgl.glfw.GLFW.GLFW_SAMPLES;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;

/**
 * Created by Andrew on 12/27/2016.
 */
public class Display {
    private GLWindow glfwWindow;

    public Display() {
        GLWindow.Init();
        GLWindow.hints();
        GLWindow.setResizable(true);
        GLWindow.setGLVersion(3, 3);
    }

    public Display create() {
        glfwWindow = new GLWindow(new Integer[]{1280, 720}, "Dungeon");
        glfwWindow.createContext();
        glfwWindow.show();

        glfwWindow.setResizeCallback(new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                Engine.renderer.resize(width, height);
                Engine.app.Resize(width, height);
            }
        });

        return this;
    }

    public void update() {
        glfwWindow.update();
    }

    public int getWidth() {
        return glfwWindow.getWidth();
    }

    public int getHeight() {
        return glfwWindow.getHeight();
    }

    public boolean isClosing() {
        return glfwWindow.isCloseRequested();
    }

    public GLWindow getGlfwWindow() {
        return glfwWindow;
    }

    public void setKeyCallback(GLFWKeyCallback callback) {
        glfwWindow.setKeyCallback(callback);
    }

    public void toggleFullscreen() {
        glfwWindow.toggleFullscreen();
    }

    public void close() {
        glfwWindow.close();
    }

    public void destroy() {
        close();
        glfwWindow.destroy();
        glfwTerminate();
    }
}
