package com.engine.input;

import com.engine.core.Engine;
import org.lwjgl.glfw.GLFWKeyCallback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;

/**
 * Created by Andrew on 1/1/2017.
 */
public class Input implements IInput {
    private List<Key> pressedKeys;
    private List<KeyEvent> keyEvents;

    @Override
    public void init() {
        pressedKeys = new ArrayList<>();
        keyEvents = new ArrayList<>();
        for(Key key : Key.values()) {
            Key.charMap.put(key.getValue(), key);
            Key.intMap.put(key.getHandle(), key);
        }
        for(Key.EventType eventType : Key.EventType.values()) {
            Key.EventType.intMap.put(eventType.handle, eventType);
        }

        Engine.display.setKeyCallback(new GLFWKeyCallback() {
            public void invoke(long window, int key, int scancode, int event, int mods) {
                Key keyEnum = Key.intMap.get(key);
                Key.EventType eventType = Key.EventType.intMap.get(event);

                for(KeyEvent keyEvent : keyEvents) {
                    keyEvent.invoke(keyEnum, eventType);
                }

                if (eventType == Key.EventType.Press) {
                    pressedKeys.add(keyEnum);
                }
                if (eventType == Key.EventType.Release) {
                    pressedKeys.remove(keyEnum);
                }
            }
        });
    }

    @Override
    public void pollInput() {
        glfwPollEvents();
        Engine.app.Input();
    }

    @Override
    public boolean isKeyDown(Key key) {
        return pressedKeys.contains(key);
    }

    @Override
    public void addKeyEvent(KeyEvent keyEvent) {
        keyEvents.add(keyEvent);
    }

    @Override
    public void removeKeyEvent(KeyEvent keyEvent) {
        keyEvents.remove(keyEvent);
    }

    @Override
    public void addMouseEvent(MouseEvent mouseEvent) {

    }

    @Override
    public void removeMouseEvent(MouseEvent mouseEvent) {

    }

    @Override
    public void centerMouse() {
        Engine.display.getGlfwWindow().centerMouse();
    }

    @Override
    public int getMouseX() {
        return Engine.display.getGlfwWindow().getMouseX();
    }

    @Override
    public int getMouseY() {
        return Engine.display.getGlfwWindow().getMouseY();
    }

    @Override
    public Collection<Key> getPressedKeys() {
        return pressedKeys;
    }
}
