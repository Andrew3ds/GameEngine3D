package com.engine.gfx;

import com.engine.misc.DialogWindow;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.engine.core.Engine.renderer;

/**
 * Created by Andrew on 1/8/2017.
 */
public abstract class WorldObject extends Node {
    private Mesh mesh;
    private ShaderProgram shaderProgram;
    private Uniform<Vector3f> viewPosition;

    public WorldObject(String name, Mesh mesh, String shaderName) {
        super(name);

        this.mesh = mesh;
        this.shaderProgram = renderer.getShader(shaderName);
        if(this.shaderProgram == null) {
            DialogWindow.errorDialog(new IllegalArgumentException("Non-existent shader given for \"".concat(name).concat("\"")));
            renderer.getScene().removeNode(this);

            return;
        }

        Random random = new Random();

        if(renderer.getCamera() != null) {
            this.shaderProgram = this.shaderProgram.copyOf().compile();

            this.shaderProgram.matrix[ShaderProgram.PROJECTION].setData(renderer.getCamera().getProjection());

            this.shaderProgram.uniform("color", new Vector3f(1f));
//            this.shaderProgram.uniform("color", new Vector3f(random.nextFloat(), random.nextFloat(), random.nextFloat()));
            this.shaderProgram.uniform("ambientColor", new Vector3f(1F));
            this.shaderProgram.uniform("numPointLights", 0);

            this.shaderProgram.addUniform(viewPosition = new Uniform<>("viewPosition", renderer.getCamera().getPosition()));
        }
    }

    public Mesh getMesh() {
        return mesh;
    }

    public ShaderProgram getShaderProgram() {
        return shaderProgram;
    }

    @Override
    public void render() {
        super.render();

        Map<String, PointLight> pointLights = renderer.getScene().getPointLights();
        Map<String, DirectionalLight> dirLights = renderer.getScene().getDirLights();
        Map<String, SpotLight> spotLights = renderer.getScene().getSpotLights();

        int numPointLights = pointLights.size();
        int numDirLights = dirLights.size();
        int numSpotLights = spotLights.size();

        viewPosition.setData(renderer.getCamera().getPosition());
        getShaderProgram().uniform("numPointLights", numPointLights);
        getShaderProgram().uniform("numDirLights", numDirLights);
        getShaderProgram().uniform("numSpotLights", numSpotLights);
        getShaderProgram().matrix[ShaderProgram.TRANSFORM].setData(getTransform().getMatrix());
        getShaderProgram().matrix[ShaderProgram.NORMAL].setData(getTransform().getNormalMatrix());
        getShaderProgram().matrix[ShaderProgram.WORLD].setData(renderer.getCamera().getWorldMatrix());

        int i = 0;
        for(PointLight pointLight : pointLights.values()) {
            loadPointLight(pointLight, i++);
        }
        i = 0;
        for(DirectionalLight directionalLight : dirLights.values()) {
            loadDirLight(directionalLight, i++);
        }
        i = 0;
        for(SpotLight spotLight : spotLights.values()) {
            loadSpotLight(spotLight, i++);
        }

        getShaderProgram().bind();
        getMesh().render();
        getShaderProgram().unbind();
    }

    @Override
    public void onDestroy() {
        shaderProgram.dispose();
        shaderProgram = null;
    }

    private void loadPointLight(PointLight light, int id) {
        String pre = "pointLights[".concat(Integer.toString(id)).concat("].");

        getShaderProgram().uniform(pre.concat("color"), light.getColor());
        getShaderProgram().uniform(pre.concat("position"), light.getPosition());
        getShaderProgram().uniform(pre.concat("constant"), light.getConstant());
        getShaderProgram().uniform(pre.concat("linear"), light.getLinear());
        getShaderProgram().uniform(pre.concat("quadratic"), light.getQuadratic());
    }

    private void loadDirLight(DirectionalLight light, int id) {
        String pre = "dirLights[".concat(Integer.toString(id)).concat("].");

        getShaderProgram().uniform(pre.concat("color"), light.getColor());
        getShaderProgram().uniform(pre.concat("direction"), light.getDirection());
    }

    private void loadSpotLight(SpotLight light, int id) {
        String pre = "spotLights[".concat(Integer.toString(id)).concat("].");

        getShaderProgram().uniform(pre.concat("color"), light.getColor());
        getShaderProgram().uniform(pre.concat("position"), light.getPosition());
        getShaderProgram().uniform(pre.concat("direction"), light.getDirection());
        getShaderProgram().uniform(pre.concat("cutoff"), (float) Math.cos(Math.toRadians(light.getCutoff())));
        getShaderProgram().uniform(pre.concat("outerCutoff"), (float) Math.cos(Math.toRadians(light.getOuterCutoff())));
    }
}
