#version 330 core

layout(location = 0) out vec4 fragColor;

in vec2 tCoord_out;

uniform sampler2D sampler;
uniform vec3 color;
uniform vec3 lightColor;
vec4 textureColor;

void main() {
    textureColor = texture(sampler, tCoord_out);

	fragColor = textureColor * vec4(lightColor * color, 1);
}