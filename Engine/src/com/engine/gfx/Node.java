package com.engine.gfx;

import com.engine.core.Disposable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew on 1/8/2017.
 */
public abstract class Node implements Disposable {
    private String name;
    private List<Node> children;
    private Transform transform;

    public Node(String name) {
        this.name = name;
        this.transform = new Transform();
        this.children = new ArrayList<>();
    }

    public Node addChild(Node child) {
        children.add(child);

        return this;
    }

    public Node removeChild(String name) {
        Node toBeRemoved = null;

        for(Node child : children) {
            if(child.getName().equals(name)) {
                toBeRemoved = child;
            }
        }

        if(toBeRemoved != null) {
            children.remove(toBeRemoved);
        }

        return this;
    }

    public List<Node> getChildren() {
        return children;
    }

    public Transform getTransform() {
        return transform;
    }

    public String getName() {
        return name;
    }

    public void update() {
        this.onUpdate();
        children.forEach(Node::update);
    }

    public void input() {
        this.onInput();
        children.forEach(Node::input);
    }

    public void render() {
        this.onRender();
        children.forEach(Node::render);
    }

    public abstract void onCreate();
    public abstract void onInput();
    public abstract void onUpdate();
    public abstract void onRender();
    public abstract void onDestroy();

    @Override
    public void dispose() {
        this.onDestroy();
        children.forEach(Node::dispose);
    }
}
