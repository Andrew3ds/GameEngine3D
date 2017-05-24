#version 330 core

layout(location = 0) out vec4 fragColor;

in vec3 normal_out;
in vec2 tCoord_out;
in vec3 fragPosition;

uniform sampler2D sampler;
uniform vec3 viewPosition;
uniform vec3 color;
uniform vec3 ambientColor;
uniform int numPointLights;
uniform int numDirLights;
uniform int numSpotLights;

vec4 textureColor;

struct PointLight {
    vec3 color;
    vec3 position;

    float constant;
    float linear;
    float quadratic;
};

#define NR_POINT_LIGHTS 4
uniform PointLight pointLights[NR_POINT_LIGHTS];

struct DirLight {
    vec3 color;
    vec3 direction;
};

#define NR_DIR_LIGHTS 4
uniform DirLight dirLights[NR_DIR_LIGHTS];

struct SpotLight {
    vec3 color;
    vec3 position;
    vec3 direction;
    float cutoff;
    float outerCutoff;
};

#define NR_SPOT_LIGHTS 4
uniform SpotLight spotLights[NR_SPOT_LIGHTS];

vec3 calcDiffuse(vec3 normal, vec3 lightDir, vec3 lightColor) {
    float diffuseIntensity = max(dot(normal, lightDir), 0.0);

    return diffuseIntensity * lightColor;
}

vec3 calcSpecular(vec3 normal, float specularStrength, vec3 lightDir, vec3 lightColor) {
    vec3 viewDir = normalize(viewPosition - fragPosition);
    vec3 halfwayDir = normalize(lightDir + viewDir);
    float specularIntensity = pow(max(dot(normal, halfwayDir), 0.0), 32);

    return specularStrength * specularIntensity * lightColor;
}

vec3 calcPointLight(vec3 normal, PointLight light) {
    vec3 lightDir = normalize(light.position - fragPosition);

    vec3 diffuse = calcDiffuse(normal, lightDir, light.color);
    vec3 specular = calcSpecular(normal, 0.5f, lightDir, light.color);

    float distance = length(light.position - fragPosition);
    float attenuation = 1.0f / (light.constant + light.linear * distance + light.quadratic * (distance * distance));

    diffuse *= attenuation;
    specular *= attenuation;

    return (diffuse + specular);
}

vec3 calcDirLight(vec3 normal, DirLight light) {
    vec3 lightDir = normalize(-light.direction);

    vec3 diffuse = calcDiffuse(normal, lightDir, light.color);
    vec3 specular = calcSpecular(normal, 0.5f, lightDir, light.color);

    return (diffuse + specular);
}

vec3 calcSpotLight(vec3 normal, SpotLight light) {
    vec3 lightDir = normalize(light.position - fragPosition);
    float theta = dot(lightDir, normalize(-light.direction));
    float epsilon = light.cutoff - light.outerCutoff;
    float intensity = clamp((theta - light.outerCutoff) / epsilon, 0.0, 1.0);

    if(theta > light.outerCutoff) {
        vec3 diffuse = calcDiffuse(normal, lightDir, light.color);
        vec3 specular = calcSpecular(normal, 0.5f, lightDir, light.color);

        diffuse *= intensity;
        specular *= intensity;

        return (diffuse + specular);
    } else {
        return vec3(0);
    }
}

void main() {
    textureColor = texture(sampler, tCoord_out);
    textureColor = vec4(pow(textureColor.rgb, vec3(1.0/2.2)), textureColor.a);

    float ambientStrength = 0.25f;
    vec3 ambient = ambientStrength * ambientColor;

    vec3 normal = normalize(normal_out);

    vec3 totalLighting = vec3(0);

    for(int i = 0; i < numPointLights; i++) {
        totalLighting += calcPointLight(normal, pointLights[i]);
    }
    for(int i = 0; i < numDirLights; i++) {
        totalLighting += calcDirLight(normal, dirLights[i]);
    }
    for(int i = 0; i < numSpotLights; i++) {
        totalLighting += calcSpotLight(normal, spotLights[i]);
    }

    vec3 lightResult = (ambient + totalLighting) * color;
	fragColor = textureColor * vec4(lightResult, 1);
}