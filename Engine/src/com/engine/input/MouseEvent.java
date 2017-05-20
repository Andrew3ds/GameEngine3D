package com.engine.input;

/**
 * Created by Andrew on 1/7/2017.
 */
public interface MouseEvent {
    void invoke(int x, int y, int button, Key.EventType event);
}
