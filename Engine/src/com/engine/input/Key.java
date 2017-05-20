package com.engine.input;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by Andrew on 12/31/2016.
 */
public enum Key {
    ESCAPE(GLFW_KEY_ESCAPE, (char)0), F1(GLFW_KEY_F1, (char)0), F2(GLFW_KEY_F2, (char)0), F3(GLFW_KEY_F3, (char)0),
    F4(GLFW_KEY_F4, (char)0), F5(GLFW_KEY_F5, (char)0), F6(GLFW_KEY_F6, (char)0), F7(GLFW_KEY_F7, (char)0),
    F8(GLFW_KEY_F8, (char)0), F9(GLFW_KEY_F9, (char)0), F10(GLFW_KEY_F10, (char)0), F11(GLFW_KEY_F11, (char)0),
    F12(GLFW_KEY_F12, (char)0), PRNTSCRN(GLFW_KEY_PRINT_SCREEN, (char)0), PAUSE(GLFW_KEY_PAUSE, (char)0),
    TILDE(GLFW_KEY_GRAVE_ACCENT, '`'), MINUS(GLFW_KEY_MINUS, '-'), EQUALS(GLFW_KEY_EQUAL, '='),
    BACKSPACE(GLFW_KEY_BACKSPACE, (char)0), INSERT(GLFW_KEY_INSERT, (char)0), HOME(GLFW_KEY_HOME, (char)0),
    PAGE_UP(GLFW_KEY_PAGE_UP, (char)0), TAB(GLFW_KEY_TAB, '\t'), LEFT_BRACKET(GLFW_KEY_LEFT_BRACKET, '['),
    RIGHT_BRACKET(GLFW_KEY_RIGHT_BRACKET, ']'), BACKSLASH(GLFW_KEY_BACKSLASH, '\\'), DELETE(GLFW_KEY_DELETE, (char)0),
    END(GLFW_KEY_END, (char)0), PAGE_DOWN(GLFW_KEY_PAGE_DOWN, (char)0), SEMICOLON(GLFW_KEY_SEMICOLON, ';'),
    APOSTROPHE(GLFW_KEY_APOSTROPHE, '\''), ENTER(GLFW_KEY_ENTER, '\n'), LEFT_SHIFT(GLFW_KEY_LEFT_SHIFT, (char)0),
    COMMA(GLFW_KEY_COMMA, ','), PERIOD(GLFW_KEY_PERIOD, '.'), SLASH(GLFW_KEY_SLASH, '/'),
    RIGHT_SHIFT(GLFW_KEY_RIGHT_SHIFT, (char)0), LEFT_CTRL(GLFW_KEY_LEFT_CONTROL, (char)0),
    LEFT_ALT(GLFW_KEY_LEFT_ALT, (char)0), SPACE(GLFW_KEY_SPACE, ' '), RIGHT_ALT(GLFW_KEY_RIGHT_ALT, (char)0),
    RIGHT_CTRL(GLFW_KEY_RIGHT_CONTROL, (char)0), UP(GLFW_KEY_UP, (char)0), DOWN(GLFW_KEY_DOWN, (char)0),
    LEFT(GLFW_KEY_LEFT, (char)0), RIGHT(GLFW_KEY_RIGHT, (char)0),
    VK_0(GLFW_KEY_0, '0'), VK_1(GLFW_KEY_1, '1'), VK_2(GLFW_KEY_2, '2'), VK_3(GLFW_KEY_3, '3'), VK_4(GLFW_KEY_4, '4'),
    VK_5(GLFW_KEY_5, '5'), VK_6(GLFW_KEY_6, '6'), VK_7(GLFW_KEY_7, '7'), VK_8(GLFW_KEY_8, '8'), VK_9(GLFW_KEY_9, '9'),
    A(GLFW_KEY_A, 'A'), B(GLFW_KEY_B, 'B'), C(GLFW_KEY_C, 'C'), D(GLFW_KEY_D, 'D'), E(GLFW_KEY_E, 'E'), F(GLFW_KEY_F, 'F'),
    G(GLFW_KEY_G, 'G'), H(GLFW_KEY_H, 'H'), I(GLFW_KEY_I, 'I'), J(GLFW_KEY_J, 'J'), K(GLFW_KEY_K, 'K'), L(GLFW_KEY_L, 'L'),
    M(GLFW_KEY_M, 'M'), N(GLFW_KEY_N, 'N'), O(GLFW_KEY_O, 'O'), P(GLFW_KEY_P, 'P'), Q(GLFW_KEY_Q, 'Q'), R(GLFW_KEY_R, 'R'),
    S(GLFW_KEY_S, 'S'), T(GLFW_KEY_T, 'T'), U(GLFW_KEY_Y, 'U'), V(GLFW_KEY_V, 'V'), W(GLFW_KEY_W, 'W'), X(GLFW_KEY_X, 'X'),
    Y(GLFW_KEY_Y, 'Y'), Z(GLFW_KEY_Z, 'Z');

    final int handle;
    final char value;

    public static Map<Integer, Key> intMap = new HashMap<>();
    public static Map<Character, Key> charMap = new HashMap<>();

    Key(int handle, char value) {
        this.handle = handle;
        this.value = value;
    }

    public int getHandle() {
        return handle;
    }

    public char getValue() {
        return value;
    }

    public enum EventType {
        Press(GLFW_PRESS), Release(GLFW_RELEASE), Repeat(GLFW_REPEAT);

        final int handle;

        public static Map<Integer, EventType> intMap = new HashMap<>();

        EventType(int handle) {
            this.handle = handle;
        }

    }
}
