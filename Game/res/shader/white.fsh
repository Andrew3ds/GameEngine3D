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

void main() {
    float ambientStrength = 0.1f;
    vec3 ambient = ambientStrength * lightColor;

    vec3 normal = normalize(normal_out);
    vec3 lightDir = normalize(lightPosition - fragPosition);
    float diffuseIntensity = max(dot(normal, lightDir), 0.0);
    vec3 diffuse = diffuseIntensity * lightColor;

    float specularStrength = 5f;
    vec3 viewDir = normalize(viewPosition - fragPosition);
    vec3 reflectDir = reflect(-lightDir, normal);
    float specularIntensity = pow(max(dot(viewDir, reflectDir), 0.0), 32);
    vec3 specular = specularStrength * specularIntensity * lightColor;

    textureColor = texture(sampler, tCoord_out);

    vec3 lightResult = (ambient + diffuse + specular) * color;
	fragColor = textureColor * vec4(lightResult, 1);
}