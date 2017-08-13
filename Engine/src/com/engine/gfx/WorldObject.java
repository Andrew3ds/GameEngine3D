package com.engine.gfx;

import com.engine.anim.AnimatedModel;
import com.engine.misc.DialogWindow;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.Map;

import static com.engine.core.Engine.renderer;

/**
 * Created by Andrew on 1/8/2017.
 */
public abstract class WorldObject extends Node {
    private AnimatedModel model;
    private ShaderProgram shaderProgram;
    private ShaderProgram depthShader;
    private Uniform<Vector3f> viewPosition;
    private Matrix4f matrix;

    public WorldObject(String name, AnimatedModel model, String shaderName) {
        super(name);

        this.model = model;
        this.shaderProgram = renderer.getShader(shaderName);
        if(this.shaderProgram == null) {
            DialogWindow.errorDialog(new IllegalArgumentException("Non-existent shader given for \"".concat(name).concat("\"")));
            renderer.getScene().removeNode(this);

            return;
        }

        matrix = new Matrix4f();
        if(renderer.getCamera() != null) {
            this.shaderProgram = this.shaderProgram.copyOf().compile();
            this.depthShader = renderer.getShader("depth").copyOf().compile();

            this.shaderProgram.matrix[ShaderProgram.PROJECTION].setData(renderer.getCamera().getProjection());

            this.getShaderProgram().uniform("skinned", false);
            this.getShaderProgram().uniform("ambientColor", new Vector3f(1F));
            this.getShaderProgram().uniform("material.color", model.getMaterial().getColor());

            this.getShaderProgram().uniform("material.diffuseMap", 0);
            this.getShaderProgram().uniform("material.normalMap", 1);
            this.getShaderProgram().uniform("shadowMap", 2);
            this.getShaderProgram().uniform("useFog", true);
            for(int i = 0; i < model.getJointCount(); i++) {
                this.getShaderProgram().uniform("jointTransforms[" + i + "]", matrix);
            }

            this.getShaderProgram().addUniform(viewPosition = new Uniform<>("viewPosition", renderer.getCamera().getPosition()));
        }
    }

    public AnimatedModel getModel() {
        return model;
    }

    public ShaderProgram getShaderProgram() {
        return shaderProgram;
    }

    @Override
    public void render() {
        super.render();

        switch(renderer.getMode()) {
            case Default: {
                renderDefault();
            } break;
            case Depth: {
                renderDepth();
            } break;
        }
    }

    @Override
    public void onDestroy() {
        depthShader.dispose();
        shaderProgram.dispose();
        shaderProgram = null;
    }

    private void renderDefault() {
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
        getShaderProgram().uniform("lightSpaceMatrix", renderer.getLightSpaceMatrix());

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

        model.getMaterial().getDiffuse().slot(0).bind();
        model.getMaterial().getNormalMap().slot(1).bind();
        renderer.getShadowMap().slot(2).bind();

        getShaderProgram().bind();
        getModel().getMesh().render();
        getShaderProgram().unbind();

        renderer.getShadowMap().slot(2).unbind();
        model.getMaterial().getNormalMap().slot(1).unbind();
        model.getMaterial().getDiffuse().slot(0).unbind();
    }

    private void renderDepth() {
        depthShader.matrix[ShaderProgram.TRANSFORM].setData(getTransform().getMatrix());
        depthShader.uniform("lightSpaceMatrix", renderer.getLightSpaceMatrix());

        renderer.cullFace(Renderer.Face.Front);
        depthShader.bind();
        getModel().getMesh().render();
        depthShader.unbind();
        renderer.cullFace(Renderer.Face.Back);
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
