package com.engine.gfx;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiConsumer;

/**
 * Created by Andrew on 1/8/2017.
 */
public class Scene {
    private Map<String, Node> nodes;

    public Scene(Map<String, Node> nodes) {
        this.nodes = nodes;
    }

    public Scene() {
        this(new TreeMap<>());
    }

    public void addNode(Node node) {
        nodes.put(node.getName(), node);
        node.onCreate();
    }

    public void removeNode(Node node) {
        removeNode(node.getName());
    }

    public void removeNode(String name) {
        nodes.remove(name);
    }

    public Node getNode(String name) {
        return nodes.get(name);
    }

    public Collection<Node> getNodes() {
        return nodes.values();
    }

    public void dispose() {
        nodes.values().forEach(Node::dispose);
    }

    public void input() {
        nodes.values().forEach(Node::input);
    }

    public void update() {
        nodes.values().forEach(Node::update);
    }

    public void render() {
        nodes.values().forEach(Node::render);
    }
}
