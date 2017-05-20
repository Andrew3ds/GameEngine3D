package com.engine.core;

/**
 * Created by Andrew on 1/6/2017.
 */
public class Timer {
    private static long lastFrame;
    private static long lastSecond;
    private static int frames;
    private static int fps;
    private static double delta;

    public static long getTime() {
        return System.nanoTime() / 1000000;
    }

    public static void update() {
        delta = ((double) System.nanoTime() - (double) lastFrame) / 1000000.0D;
        lastFrame = System.nanoTime();
        long current = getTime();
        if(current - lastSecond > 1000) {
            lastSecond = current;
            fps = frames;
            frames = 0;
            System.out.println(fps);
        } else {
            frames++;
        }
    }

    public static float getDelta() {
        return (float) delta;
    }

    public static int getFPS() {
        return fps;
    }
}
