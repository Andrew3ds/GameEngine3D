package com.engine.gfx;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * Created by Andrew on 1/8/2017.
 */
public class Scene {
    private Map<String, Node> nodes;
    private List<PointLight> pointLights;
    private List<DirectionalLight> dirLights;
    private List<SpotLight> spotLights;

    public Scene(Map<String, Node> nodes, List<LightSource> lights) {
        this.nodes = nodes;
        this.pointLights = new ArrayList<>();
        this.dirLights = new ArrayList<>();
        this.spotLights = new ArrayList<>();

        lights.forEach(lightSource -> {
            switch(lightSource.getType()) {
                case Point: {
                    pointLights.add((PointLight)lightSource);
                } break;
                case Directional: {
                    dirLights.add((DirectionalLight)lightSource);
                } break;
                case Spot: {
                    spotLights.add((SpotLight)lightSource);
                } break;
            }
        });
    }

    public Scene() {
        this(new TreeMap<>(), new ArrayList<>());
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

    public void addLight(LightSource lightSource) {
        switch(lightSource.getType()) {
            case Point: {
                pointLights.add((PointLight)lightSource);
            } break;
            case Directional: {
                dirLights.add((DirectionalLight)lightSource);
            } break;
            case Spot: {
                spotLights.add((SpotLight)lightSource);
            } break;
        }
    }

    public List<PointLight> getPointLights() {
        return pointLights;
    }

    public List<DirectionalLight> getDirLights() {
        return dirLights;
    }

    public List<SpotLight> getSpotLights() {
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
