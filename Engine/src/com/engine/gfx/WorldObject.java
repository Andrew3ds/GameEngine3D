package com.engine.gfx;

import com.engine.core.Engine;
import com.engine.misc.DialogWindow;
import org.joml.Vector3f;

import java.util.Random;

import static com.engine.core.Engine.renderer;

/**
 * Created by Andrew on 1/8/2017.
 */
public abstract class WorldObject extends Node {
    private Mesh mesh;
    private ShaderProgram shaderProgram;

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
            this.shaderProgram.addUniform(new Uniform<>("lightColor", new Vector3f(1, 1, 1)));
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

        getShaderProgram().matrix[ShaderProgram.TRANSFORM].setData(getTransform().getMatrix());
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
