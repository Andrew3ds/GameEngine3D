package com.engine.gfx;

import com.engine.misc.DialogWindow;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.engine.core.Engine.gl;
import static com.engine.gfx.IGL.GL_LINK_STATUS;
import static com.engine.gfx.IGL.GL_TRUE;

/**
 * Created by Andrew on 1/5/2017.
 */
public class ShaderProgram implements GLObject {
    public static final int TRANSFORM = 0;
    public static final int NORMAL = 1;
    public static final int WORLD = 2;
    public static final int PROJECTION = 3;

    private int handle = -1;
    private String name;
    private Map<Shader.Type, Shader> shaders;
    private Map<String, Uniform> uniforms;
    private boolean compiled;
    public Uniform[] matrix = {
            new Uniform<>("matrix.transform", new Matrix4f()),
            new Uniform<>("matrix.normal", new Matrix4f()),
            new Uniform<>("matrix.world", new Matrix4f()),
            new Uniform<>("matrix.projection", new Matrix4f())
    };

    public ShaderProgram(String name, Shader... shaders) {
        this.name = name;
        this.shaders = new HashMap<>();
        this.uniforms = new HashMap<>();

        for(Shader shader : shaders) {
            this.shaders.put(shader.getType(), shader);
        }

        handle = gl.CreateProgram();
    }

    public ShaderProgram compile() {
        if(compiled) {
            return this;
        }

        for(Shader shader : shaders.values()) {
            shader.compile();
            gl.AttachShader(getHandle(), shader.getHandle());
        }
        gl.LinkProgram(getHandle());
        if(gl.GetProgrami(getHandle(), GL_LINK_STATUS) != GL_TRUE) {
            try {
                throw new GLException(("Shader: ").concat(name).concat("\n").concat(gl.GetProgramInfoLog(getHandle())));
            } catch (GLException e) {
                DialogWindow.errorDialog(e);
            }
        }

        gl.ValidateProgram(getHandle());

        compiled = true;

        for(Uniform uniform : matrix) {
            addUniform(uniform);
        }

        return this;
    }

    public String getName() {
        return name;
    }

    public ShaderProgram uniform(String name, Object data) {
        if(uniformExists(name)) {
            getUniform(name).setData(data);
        } else {
            addUniform(new Uniform<>(name, data));
        }

        return this;
    }

    public ShaderProgram addUniform(Uniform uniform) {
        uniforms.put(uniform.getName(), uniform.getLocation(this));

        return this;
    }

    public Uniform getUniform(String name) {
        return uniforms.get(name);
    }

    public boolean uniformExists(String name) {
        return getUniform(name) != null;
    }

    public ShaderProgram copyOf() {
        Shader[] shaders = new Shader[this.shaders.values().size()];
        int index = 0;
        for(Shader shader : this.shaders.values()) {
            shaders[index++] = shader;
        }

        return new ShaderProgram(name, shaders);
    }

    @Override
    public void dispose() {
        gl.DeleteProgram(getHandle());
        handle = -1;
        for(Shader shader : shaders.values()) {
            shader.dispose();
        }
        shaders = null;
        uniforms = null;
        name = null;
    }

    @Override
    public int getHandle() {
        return handle;
    }

    @Override
    public void bind() {
        gl.UseProgram(getHandle());
        for(Uniform uniform : uniforms.values()) {
            uniform.upload();
        }
    }

    @Override
    public void unbind() {
        gl.UseProgram(0);
    }

    public static class BuiltIn {
        public static String defaultV = "#version 330 core\n" +
                "\n" +
                "layout(location = 0) in vec3 position;\n" +
                "layout(location = 1) in vec2 tCoord;\n" +
                "layout(location = 2) in vec3 normal;\n" +
                "\n" +
                "uniform struct Matrix {\n" +
                "\tmat4 projection;\n" +
                "\tmat4 world;\n" +
                "\tmat4 normal;\n" +
                "\tmat4 transform;\n" +
                "} matrix;\n" +
                "\n" +
                "out vec3 normal_out;\n" +
                "out vec2 tCoord_out;\n" +
                "out vec3 fragPosition;\n" +
                "\n" +
                "void main() {\n" +
                "    normal_out = mat3(matrix.normal) * normal;\n" +
                "\ttCoord_out = tCoord;\n" +
                "\tfragPosition = vec3(matrix.transform * vec4(position, 1));\n" +
                "\n" +
                "\tmat4 Camera = matrix.projection * matrix.world * matrix.transform;\n" +
                "\tgl_Position = Camera * vec4(position, 1);\n" +
                "}";
        public static String defaultF = "#version 330 core\n" +
                "\n" +
                "layout(location = 0) out vec4 fragColor;\n" +
                "\n" +
                "in vec3 normal_out;\n" +
                "in vec2 tCoord_out;\n" +
                "in vec3 fragPosition;\n" +
                "\n" +
                "uniform sampler2D sampler;\n" +
                "uniform vec3 viewPosition;\n" +
                "uniform vec3 color;\n" +
                "uniform vec3 ambientColor;\n" +
                "uniform int numPointLights;\n" +
                "uniform int numDirLights;\n" +
                "uniform int numSpotLights;\n" +
                "\n" +
                "vec4 textureColor;\n" +
                "\n" +
                "struct PointLight {\n" +
                "    vec3 color;\n" +
                "    vec3 position;\n" +
                "\n" +
                "    float constant;\n" +
                "    float linear;\n" +
                "    float quadratic;\n" +
                "};\n" +
                "\n" +
                "#define NR_POINT_LIGHTS 6\n" +
                "uniform PointLight pointLights[NR_POINT_LIGHTS];\n" +
                "\n" +
                "struct DirLight {\n" +
                "    vec3 color;\n" +
                "    vec3 direction;\n" +
                "};\n" +
                "\n" +
                "#define NR_DIR_LIGHTS 4\n" +
                "uniform DirLight dirLights[NR_DIR_LIGHTS];\n" +
                "\n" +
                "struct SpotLight {\n" +
                "    vec3 color;\n" +
                "    vec3 position;\n" +
                "    vec3 direction;\n" +
                "    float cutoff;\n" +
                "    float outerCutoff;\n" +
                "};\n" +
                "\n" +
                "#define NR_SPOT_LIGHTS 4\n" +
                "uniform SpotLight spotLights[NR_SPOT_LIGHTS];\n" +
                "\n" +
                "vec3 calcDiffuse(vec3 normal, vec3 lightDir, vec3 lightColor) {\n" +
                "    float diffuseIntensity = max(dot(normal, lightDir), 0.0);\n" +
                "\n" +
                "    return diffuseIntensity * lightColor;\n" +
                "}\n" +
                "\n" +
                "vec3 calcSpecular(vec3 normal, float specularStrength, vec3 lightDir, vec3 lightColor) {\n" +
                "    vec3 viewDir = normalize(viewPosition - fragPosition);\n" +
                "    vec3 halfwayDir = normalize(lightDir + viewDir);\n" +
                "    float specularIntensity = pow(max(dot(normal, halfwayDir), 0.0), 32);\n" +
                "\n" +
                "    return specularStrength * specularIntensity * lightColor;\n" +
                "}\n" +
                "\n" +
                "vec3 calcPointLight(vec3 normal, PointLight light) {\n" +
                "    vec3 lightDir = normalize(light.position - fragPosition);\n" +
                "\n" +
                "    vec3 diffuse = calcDiffuse(normal, lightDir, light.color);\n" +
                "    vec3 specular = calcSpecular(normal, 0.5f, lightDir, light.color);\n" +
                "\n" +
                "    float distance = length(light.position - fragPosition);\n" +
                "    float attenuation = 1.0f / (light.constant + light.linear * distance + light.quadratic * (distance * distance));\n" +
                "\n" +
                "    diffuse *= attenuation;\n" +
                "    specular *= attenuation;\n" +
                "\n" +
                "    return (diffuse + specular);\n" +
                "}\n" +
                "\n" +
                "vec3 calcDirLight(vec3 normal, DirLight light) {\n" +
                "    vec3 lightDir = normalize(-light.direction);\n" +
                "\n" +
                "    vec3 diffuse = calcDiffuse(normal, lightDir, light.color);\n" +
                "    vec3 specular = calcSpecular(normal, 0.5f, lightDir, light.color);\n" +
                "\n" +
                "    return (diffuse + specular);\n" +
                "}\n" +
                "\n" +
                "vec3 calcSpotLight(vec3 normal, SpotLight light) {\n" +
                "    vec3 lightDir = normalize(light.position - fragPosition);\n" +
                "    float theta = dot(lightDir, normalize(-light.direction));\n" +
                "    float epsilon = light.cutoff - light.outerCutoff;\n" +
                "    float intensity = clamp((theta - light.outerCutoff) / epsilon, 0.0, 1.0);\n" +
                "\n" +
                "    if(theta > light.outerCutoff) {\n" +
                "        vec3 diffuse = calcDiffuse(normal, lightDir, light.color);\n" +
                "        vec3 specular = calcSpecular(normal, 0.5f, lightDir, light.color);\n" +
                "\n" +
                "        diffuse *= intensity;\n" +
                "        specular *= intensity;\n" +
                "\n" +
                "        return (diffuse + specular);\n" +
                "    } else {\n" +
                "        return vec3(0);\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "void main() {\n" +
                "    textureColor = texture(sampler, tCoord_out);\n" +
                "    textureColor = vec4(pow(textureColor.rgb, vec3(1.0/2.2)), textureColor.a);\n" +
                "\n" +
                "    float ambientStrength = 0.25f;\n" +
                "    vec3 ambient = ambientStrength * ambientColor;\n" +
                "\n" +
                "    vec3 normal = normalize(normal_out);\n" +
                "\n" +
                "    vec3 totalLighting = vec3(0);\n" +
                "\n" +
                "    for(int i = 0; i < numPointLights; i++) {\n" +
                "        totalLighting += calcPointLight(normal, pointLights[i]);\n" +
                "    }\n" +
                "    for(int i = 0; i < numDirLights; i++) {\n" +
                "        totalLighting += calcDirLight(normal, dirLights[i]);\n" +
                "    }\n" +
                "    for(int i = 0; i < numSpotLights; i++) {\n" +
                "        totalLighting += calcSpotLight(normal, spotLights[i]);\n" +
                "    }\n" +
                "\n" +
                "    vec3 lightResult = (ambient + totalLighting) * color;\n" +
                "\tfragColor = textureColor * vec4(lightResult, 1);\n" +
                "}";
    }
}
