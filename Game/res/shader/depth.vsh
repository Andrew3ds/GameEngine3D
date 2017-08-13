#version 330 core

layout(location = 0) in vec3 position;

uniform struct Matrix {
	mat4 transform;
} matrix;

uniform mat4 lightSpaceMatrix;

void main() {
	gl_Position = lightSpaceMatrix * matrix.transform * vec4(position, 1.0f);
}