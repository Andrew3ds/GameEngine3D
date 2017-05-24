package com.game;

import com.engine.core.Application;
import com.engine.core.Engine;
import com.engine.core.Timer;
import com.engine.gfx.*;
import com.engine.input.Key;
import com.engine.io.Asset;
import com.engine.io.Loader;
import org.joml.Vector3f;

import java.util.Random;

import static com.engine.core.Engine.*;

/**
 * Created by Andrew on 12/27/2016.
 */
public class Main implements Application {
    Texture texture;
    WorldObject wo;
    Random random = new Random();
    Player player;

    public static void main(String[] args) {
        init();
        startApp(new Main());
    }

    @Override
    public void Start() {
        player = new Player();

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
                new Shader(Shader.Type.Vertex, ShaderProgram.BuiltIn.defaultV),
                new Shader(Shader.Type.Fragment, ShaderProgram.BuiltIn.defaultF)
        );
        renderer.registerShader(defaultShader);

        texture = Loader.loadTexture(new Asset("texture/man.jpg"));

        Mesh mesh = MeshGen.pyramid(1, 1, 1).finish();

        display.getGlfwWindow().toggleCursor();
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
                getTransform().rotateY(1f);
            }

            @Override
            public void onRender() {
                texture.bind();
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

        renderer.getScene().addLight("point", new PointLight(new Vector3f(0, 0.5f, 1f), new Vector3f(0, 25F, 0), 1.0F, 0.007F, 0.0002F));
        renderer.getScene().addLight("point2", new PointLight(new Vector3f(1f, 0.5f, 0f), new Vector3f(0, 25F, 0), 1.0F, 0.007F, 0.0002F));
        renderer.getScene().addLight("spot", new SpotLight(new Vector3f(1), player.getCamera().getPosition(), player.getCamera().getDirection(), 12.5F, 17.5F));

        for(int i = 0; i < 5; i++) {
            renderer.getScene().addLight("point" + i, new PointLight(
                new Vector3f(random.nextFloat(), random.nextFloat(), random.nextFloat()),
                new Vector3f(random.nextFloat() * 15F * (random.nextBoolean()?-1F:1F), random.nextFloat() * 15F * (random.nextBoolean()?-1F:1F), random.nextFloat() * 15F * (random.nextBoolean()?-1F:1F)),
                1.0F, 0.007F, 0.0002F)
            );
        }
    }

    @Override
    public void Stop() {

    }

    @Override
    public void Input() {

    }

    float x, y, r = 5f, _t;

    @Override
    public void GameLoop() {
        _t += 5f;

        x = (float) Math.cos(Math.toRadians(_t)) * r;
        y = (float) Math.sin(Math.toRadians(_t)) * r;

        renderer.getScene().getSpotLights().get("spot").setPosition(player.getCamera().getPosition());
        renderer.getScene().getSpotLights().get("spot").setDirection(player.getCamera().getDirection());
        renderer.getScene().getPointLights().get("point2").setPosition(new Vector3f(x, 0, y));
    }

    @Override
    public void Render() {

    }

    @Override
    public void Resize(int w, int h) {

    }
}
