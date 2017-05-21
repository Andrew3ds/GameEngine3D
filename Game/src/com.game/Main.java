package com.game;

import com.engine.core.Application;
import com.engine.core.Engine;
import com.engine.core.Timer;
import com.engine.gfx.*;
import com.engine.input.Key;
import com.engine.io.Asset;
import com.engine.io.Loader;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.Random;

import static com.engine.core.Engine.*;

/**
 * Created by Andrew on 12/27/2016.
 */
public class Main implements Application {
    Texture t;
    WorldObject wo;
    Random random = new Random();

    public static void main(String[] args) {
        init();
        startApp(new Main());
    }

    @Override
    public void Start() {
        Player player = new Player();

        gl.ClearColor(0.1F, 0.1F, 0.1F, 1F);
        input.centerMouse();
        input.addKeyEvent((key, eventType) -> {
            if(key == Key.ESCAPE && eventType == Key.EventType.Press) {
                shutDown();
            }
            if(key == Key.ENTER && Engine.input.isKeyDown(Key.LEFT_ALT) && eventType == Key.EventType.Press) {
                display.toggleFullscreen();
            }
            if(key == Key.BACKSLASH && eventType == Key.EventType.Press) {
                display.getGlfwWindow().toggleCursor();
                input.centerMouse();
            }
        });

        renderer.addSceneElement(player);
        renderer.camera(player.getCamera());

        ShaderProgram defaultShader = new ShaderProgram("default",
                new Shader(Shader.Type.Vertex, Loader.getFileAsString(new Asset("shader/white.vsh"))),
                new Shader(Shader.Type.Fragment, Loader.getFileAsString(new Asset("shader/white.fsh")))
        );

        t = Loader.loadTexture(new Asset("texture/eye.png"));

//        Mesh mesh = MeshGen.rock(2, 1, 1, 1).merge(MeshGen.sphere(8, 8, 1)).finish();
//        Mesh mesh = MeshGen.icosahedron(2F).finish();
        Mesh mesh = MeshGen.pyramid(1, 1, 1).finish();

        display.getGlfwWindow().toggleCursor();
        renderer.registerShader(defaultShader);
        renderer.addSceneElement(wo = new WorldObject("cube", MeshGen.cubeTextured(1,1,1).finish(), "default") {
            @Override
            public void onCreate() {
                getTransform().getPosition().set(2, -5F);
            }

            @Override
            public void onInput() {

            }

            @Override
            public void onUpdate() {

            }

            @Override
            public void onRender() {
                t.bind();
            }
        });
        renderer.addSceneElement(new WorldObject("sphere", MeshGen.sphere(32,32,1).finish(), "default") {
            @Override
            public void onCreate() {
                getTransform().getPosition().set(3F, 0, -5F);
            }

            @Override
            public void onInput() {

            }

            @Override
            public void onUpdate() {

            }

            @Override
            public void onRender() {

            }
        });
        renderer.addSceneElement(new WorldObject("pyramid", mesh, "default") {
            @Override
            public void onCreate() {
                getTransform().getPosition().set(-3F, 0, -5F);
            }

            @Override
            public void onInput() {

            }

            @Override
            public void onUpdate() {
//                getTransform().getRotation().y += (0.3F/16F) * Timer.getDelta();
            }

            @Override
            public void onRender() {

            }
        });

        Mesh ico = MeshGen.cube(0.5F, 0.5F, 0.5F).finish();
        for(int i = 0; i < 500; i++) {
//            WorldObject wo = new WorldObject("rand" + i, MeshGen.pyramid(1,1,1).finish(),"default") {
//                @Override
//                public void onCreate() {
//
//                }
//
//                @Override
//                public void onInput() {
//
//                }
//
//                @Override
//                public void onUpdate() {
//
//                }
//
//                @Override
//                public void onRender() {
//
//                }
//            };
            WorldObject wo = new WorldObject("rand" + i, ico,"default") {
                @Override
                public void onCreate() {

                }

                @Override
                public void onInput() {

                }

                @Override
                public void onUpdate() {

                }

                @Override
                public void onRender() {

                }
            };
            wo.getTransform().getPosition().x = random.nextFloat() * 15F * (random.nextBoolean()?-1F:1F);
            wo.getTransform().getPosition().y = random.nextFloat() * 15F * (random.nextBoolean()?-1F:1F);
            wo.getTransform().getPosition().z = random.nextFloat() * 15F * (random.nextBoolean()?-1F:1F);

            wo.getTransform().getRotation().x = random.nextFloat() * 360F;
            wo.getTransform().getRotation().y = random.nextFloat() * 360F;
            wo.getTransform().getRotation().z = random.nextFloat() * 360F;
            renderer.addSceneElement(wo);
        }
    }

    @Override
    public void Stop() {

    }

    @Override
    public void Input() {

    }

    @Override
    public void GameLoop() {

    }

    @Override
    public void Render() {

    }

    @Override
    public void Resize(int w, int h) {

    }
}
