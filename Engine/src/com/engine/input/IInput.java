package com.engine.input;

import java.util.Collection;
import java.util.List;

/**
 * Created by Andrew on 12/31/2016.
 */
public interface IInput {
    void init();
    void pollInput();
    boolean isKeyDown(Key key);
    void addKeyEvent(KeyEvent keyEvent);
    void removeKeyEvent(KeyEvent keyEvent);
    void addMouseEvent(MouseEvent mouseEvent);
    void removeMouseEvent(MouseEvent mouseEvent);
    void centerMouse();
    int getMouseX();
    int getMouseY();

    Collection<Key> getPressedKeys();
}
