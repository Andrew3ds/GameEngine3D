package com.engine.gfx;

import com.engine.core.Engine;
import com.engine.misc.DialogWindow;
import org.joml.Vector3f;

import java.util.Random;
import java.util.Vector;

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
            this.shaderProgram.addUniform(new Uniform<>("color", new Vector3f(random.nextFloat(), random.nextFloat(), random.nextFloat())));
            this.shaderProgram.addUniform(viewPosition = new Uniform<>("viewPosition", renderer.getCamera().getPosition()));
            this.shaderProgram.addUniform(new Uniform<>("ambientColor", new Vector3f(1F)));
            this.shaderProgram.addUniform(new Uniform<>("numPointLights", 1));
            this.shaderProgram.addUniform(new Uniform<>("pointLights[0].color", new Vector3f(1)));
            this.shaderProgram.addUniform(new Uniform<>("pointLights[0].position", new Vector3f(0, 25, 0)));
            this.shaderProgram.addUniform(new Uniform<>("pointLights[0].constant", 1.0f));
            this.shaderProgram.addUniform(new Uniform<>("pointLights[0].linear", 0.022f));
            this.shaderProgram.addUniform(new Uniform<>("pointLights[0].quadratic", 0.0019f));
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
        viewPosition.setData(renderer.getCamera().getPosition());
        getShaderProgram().matrix[ShaderProgram.TRANSFORM].setData(getTransform().getMatrix());
        getShaderProgram().matrix[ShaderProgram.NORMAL].setData(getTransform().getNormalMatrix());
        getShaderProgram().matrix[ShaderProgram.WORLD].setData(renderer.getCamera().getWorldMatrix());

        getShaderProgram().bind();
        getMesh().render();
        getShaderProgram().unbind();
    }

    @Override
    public void onDestroy() {
        shaderProgram.dispose();
        shaderProgram = null;
    }
}
