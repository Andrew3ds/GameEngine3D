package com.engine.input;

/**
 * Created by Andrew on 12/31/2016.
 */
public interface KeyEvent {
    void invoke(Key key, Key.EventType eventType);
}
