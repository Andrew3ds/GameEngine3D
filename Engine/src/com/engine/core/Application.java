package com.engine.core;

/**
 * Created by Andrew on 12/30/2016.
 */
public interface Application {
    void Start();
    void Stop();
    void Input();
    void GameLoop();
    void Render();
    void Resize(int w, int h);
}
