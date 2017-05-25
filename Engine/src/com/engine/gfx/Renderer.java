package com.engine.gfx;

import com.engine.core.Engine;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

import static com.engine.core.Engine.display;
import static com.engine.core.Engine.gl;
import static com.engine.gfx.IGL.*;

/**
 * Created by Andrew on 1/4/2017.
 */
public class Renderer {
    public enum Mode {
        Default, Depth;
    }

    public enum Face {
        Front(GL_FRONT), Back(GL_BACK);

        int handle;

        Face(int handle) {
            this.handle = handle;
        }
    }

    private Scene scene;
    private Map<String, ShaderProgram> shaders;
    private Mode mode;
    private RenderSurface renderSurface;
    private DepthSurface depthSurface;
    private Camera camera;
    private Matrix4f lightView;
    private Matrix4f lightProjection;
    private Matrix4f lightSpaceMatrix;
    private VertexArray quad;
    private String textureVsh = "" +
            "#version 330 core\n" +
            "layout(location = 0) in vec3 position;\n" +
            "layout(location = 1) in vec2 tCoord;\n" +
            "out vec2 tCoord_out;\n" +
             "void main() {\n" +
            "   tCoord_out = tCoord;\n" +
            "   gl_Position = vec4(position, 1);\n" +
            "}";
    private String textureFsh = "" +
            "#version 330 core\n" +
            "in vec2 tCoord_out;\n" +
            "out vec4 color;\n" +
            "uniform sampler2D sampler;\n" +
            "void main() {\n" +
            "   color = texture(sampler, tCoord_out);\n" +
            "}";
    private String depthVsh = "" +
            "#version 330 core\n" +
            "\n" +
            "layout(location = 0) in vec3 position;\n" +
            "\n" +
            "uniform struct Matrix {\n" +
            "\tmat4 transform;\n" +
            "} matrix;\n" +
            "\n" +
            "uniform mat4 lightSpaceMatrix;\n" +
            "\n" +
            "void main() {\n" +
            "\tgl_Position = lightSpaceMatrix * matrix.transform * vec4(position, 1.0f);\n" +
            "}";
    private String depthFsh = "" +
            "#version 330 core \n" +
            "\n" +
            "void main() {}";

    public Renderer() {
        scene = new Scene();
        shaders = new HashMap<>();
        mode = Mode.Default;
        lightProjection = new Matrix4f().ortho(-10F, 10F, -10F, 10F, 1.0F, 500F);
        lightView = new Matrix4f().lookAt(new Vector3f(-2.0f, 4.0f, -1.0f), new Vector3f(0F, 0F, 0F), new Vector3f(0F, 1F, 0F));
        lightSpaceMatrix = new Matrix4f().set(lightProjection).mul(lightView);

        Vertex[] vertices = {
                new Vertex(new Vector3f(-1,  1, 0), new Vector2f(0, 1)),
                new Vertex(new Vector3f(-1, -1, 0), new Vector2f(0, 0)),
                new Vertex(new Vector3f( 1, -1, 0), new Vector2f(1, 0)),
                new Vertex(new Vector3f( 1,  1, 0), new Vector2f(1, 1)),
        };
        Integer[] indices = {
                0, 1, 2,
                0, 2, 3
        };
        quad = new VertexArray().bufferData(vertices, indices);
        registerShader(new ShaderProgram("texture", new Shader(Shader.Type.Vertex, textureVsh), new Shader(Shader.Type.Fragment, textureFsh)).compile());
        registerShader(new ShaderProgram("depth", new Shader(Shader.Type.Vertex, depthVsh), new Shader(Shader.Type.Fragment, depthFsh)));
        renderSurface = new RenderSurface(display.getWidth(), display.getHeight(), 0);
        depthSurface = new DepthSurface(1024, 1024);
    }

    public void clearScreen(boolean color, boolean depth, boolean stencil) {
        if(color) { gl.Clear(GL_COLOR_BUFFER_BIT); }
        if(depth) { gl.Clear(GL_DEPTH_BUFFER_BIT); }
        if(stencil) { gl.Clear(GL_STENCIL_BUFFER_BIT); }
    }

    public Scene getScene() {
        return scene;
    }

    public void renderScene() {
        clearScreen(true, true, false);

        renderToFramebuffer(depthSurface, Mode.Depth);
        renderToFramebuffer(renderSurface, Mode.Default);

        getShader("texture").bind();
        renderSurface.getTexture().bind();
//        depthSurface.getTexture().bind();
        quad.bind();
        gl.DrawElements(GL_TRIANGLES, quad.getSize(), GL_UNSIGNED_INT, 0L);
        quad.unbind();
        renderSurface.getTexture().unbind();
        getShader("texture").unbind();
    }

    public void camera(Camera camera) {
        this.camera = camera.createProjection();

        scene.getNodes().forEach(node ->  {
            if(node instanceof WorldObject) {
                WorldObject object = (WorldObject) node;

                object.getShaderProgram().matrix[ShaderProgram.PROJECTION].setData(camera.getProjection());
            }
        });
    }

    public void cullFace(Face face) {
        gl.CullFace(face.handle);
    }

    public void enableDepthTest(boolean value) {
        enableGL(GL_DEPTH_TEST, value);
    }

    public void enableCulling(boolean value) {
        enableGL(GL_CULL_FACE, value);
    }

    public void enableDepthClamp(boolean value) {
        enableGL(GL_DEPTH_CLAMP, value);
    }

    public void enableBlending(boolean value) {
        enableGL(GL_BLEND, value);
    }

    public void addSceneElement(Node node) {
        scene.addNode(node);
    }

    public void registerShader(ShaderProgram shader) {
        shaders.put(shader.getName(), shader.compile());
    }

    public void resize(int w, int h) {
        gl.Viewport(0, 0, w, h);
        renderSurface.resize(w, h);

        camera.createProjection();

        scene.getNodes().forEach(node ->  {
            if(node instanceof WorldObject) {
                WorldObject object = (WorldObject) node;

                object.getShaderProgram().matrix[ShaderProgram.PROJECTION].setData(camera.getProjection());
            }
        });
    }

    public ShaderProgram getShader(String name) {
        return shaders.get(name);
    }

    public Camera getCamera() {
        return camera;
    }

    public Mode getMode() {
        return mode;
    }

    public Matrix4f getLightSpaceMatrix() {
        return lightSpaceMatrix;
    }

    public Texture getShadowMap() {
        return depthSurface.getTexture();
    }

    public void cleanUp() {
        scene.getNodes().forEach(Node::dispose);
        Mesh.clearMeshes();
        Texture.clearTextures();
        shaders.forEach((s, shaderProgram) -> shaderProgram.dispose());
        renderSurface.dispose();
        depthSurface.dispose();
        quad.dispose();
    }

    private void renderToFramebuffer(IRenderTarget target, Mode mode) {
        this.mode = mode;
        target.beginRender();
        scene.render();
        Engine.app.Render();
        target.endRender();
    }

    private void enableGL(int target, boolean value) {
        if(value) {
            gl.Enable(target);
        } else {
            gl.Disable(target);
        }
    }
}
