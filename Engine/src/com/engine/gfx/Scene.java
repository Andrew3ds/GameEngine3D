package com.engine.gfx;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * Created by Andrew on 1/8/2017.
 */
public class Scene {
    private Map<String, Node> nodes;
    private Map<String, PointLight> pointLights;
    private Map<String, DirectionalLight> dirLights;
    private Map<String, SpotLight> spotLights;

    public Scene(Map<String, Node> nodes, Map<String, LightSource> lights) {
        this.nodes = nodes;
        this.pointLights = new HashMap<>();
        this.dirLights = new HashMap<>();
        this.spotLights = new HashMap<>();

        lights.forEach((s, lightSource) -> {
            switch(lightSource.getType()) {
                case Point: {
                    pointLights.put(s, (PointLight)lightSource);
                } break;
                case Directional: {
                    dirLights.put(s, (DirectionalLight)lightSource);
                } break;
                case Spot: {
                    spotLights.put(s, (SpotLight)lightSource);
                } break;
            }
        });
    }

    public Scene() {
        this(new TreeMap<>(), new TreeMap<>());
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

    public void addLight(String name, LightSource lightSource) {
        switch(lightSource.getType()) {
            case Point: {
                pointLights.put(name, (PointLight)lightSource);
            } break;
            case Directional: {
                dirLights.put(name, (DirectionalLight)lightSource);
            } break;
            case Spot: {
                spotLights.put(name, (SpotLight)lightSource);
            } break;
        }
    }

    public Map<String, PointLight> getPointLights() {
        return pointLights;
    }

    public Map<String, DirectionalLight> getDirLights() {
        return dirLights;
    }

    public Map<String, SpotLight> getSpotLights() {
        return spotLights;
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
