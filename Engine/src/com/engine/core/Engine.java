package com.engine.core;

import com.engine.gfx.*;
import com.engine.input.IInput;
import com.engine.input.Input;
import com.engine.misc.DialogWindow;

/**
 * Created by Andrew on 12/27/2016.
 */
public class Engine {
    private static Thread mainThread;
    public static Application app;
    public static IGL gl;
    public static Display display;
    public static IInput input;
    public static Renderer renderer;
    private static boolean running;

    public static void init() {
        DialogWindow.init();
    }

    public static void startApp(Application app) {
        Engine.app = app;

        mainThread = new Thread(Engine::run, "Main Thread");
        mainThread.start();
        running = true;
    }

    public static void GameLoop() {
        while(!display.isClosing()) {
            Timer.update();
            input.pollInput();
            renderer.getScene().input();
            renderer.getScene().update();
            app.GameLoop();
            renderer.renderScene();

            display.update();
        }

        shutDownInternal();
    }

    public static void shutDown() {
        display.close();
    }

    public static boolean isRunning() {
        return running;
    }

    private static void shutDownInternal() {
        app.Stop();
        renderer.cleanUp();
        display.destroy();
        running = false;
    }

    private static void initGL() {
        display = new Display().create();

        gl = new GL();
        input = new Input();
        input.init();
        renderer = new Renderer();
        renderer.enableDepthTest(true);
        renderer.enableCulling(true);
        renderer.enableDepthClamp(true);
        renderer.enableBlending(true);
    }

    private static void run() {
        initGL();
        app.Start();
        GameLoop();
    }
}
