#version 330 core

layout(location = 0) out vec4 fragColor;

in vec3 normal_out;
in vec2 tCoord_out;
in vec3 fragPosition;

uniform sampler2D sampler;
uniform vec3 color;
uniform vec3 lightColor;
uniform vec3 lightPosition;
uniform vec3 viewPosition;
vec4 textureColor;

struct PointLight {
    vec3 color;
    vec3 position;

    float constant;
    float linear;
    float quadratic;
};

uniform PointLight pLight;

vec3 calcPointLight(vec3 normal, PointLight light) {
    vec3 lightDir = normalize(light.position - fragPosition);
    float diffuseIntensity = max(dot(normal, lightDir), 0.0);
    vec3 diffuse = diffuseIntensity * light.color;

    float specularStrength = 0.5f;
    vec3 viewDir = normalize(viewPosition - fragPosition);
    vec3 reflectDir = reflect(-lightDir, normal);
    float specularIntensity = pow(max(dot(viewDir, reflectDir), 0.0), 32);
    vec3 specular = specularStrength * specularIntensity * light.color;

    float distance = length(light.position - fragPosition);
    float attenuation = 1.0f / (light.constant + light.linear * distance + light.quadratic * (distance * distance));

    diffuse *= attenuation;
    specular *= attenuation;

    return (diffuse + specular);
}

vec3 calcDirLight(vec3 normal, vec3 lightDirection) {
    vec3 lightDir = normalize(-lightDirection);

    float diffuseIntensity = max(dot(normal, lightDir), 0.0);
    vec3 diffuse = diffuseIntensity * lightColor;

    float specularStrength = 0.5f;
    vec3 viewDir = normalize(viewPosition - fragPosition);
    vec3 reflectDir = reflect(-lightDir, normal);
    float specularIntensity = pow(max(dot(viewDir, reflectDir), 0.0), 32);
    vec3 specular = specularStrength * specularIntensity * lightColor;

    return (diffuse + specular);
}

void main() {
    float ambientStrength = 0.1f;
    vec3 ambient = ambientStrength * lightColor;

    vec3 normal = normalize(normal_out);

    textureColor = texture(sampler, tCoord_out);

    vec3 lightResult = (ambient + calcPointLight(normal, pLight)) * color;
    //vec3 lightResult = (ambient + calcDirLight(normal, vec3( -0.2f, -1.0f, -0.3f))) * color;
	fragColor = textureColor * vec4(lightResult, 1);
}