#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 tCoord;
layout(location = 2) in vec3 normal;

uniform struct Matrix {
	mat4 projection;
	mat4 world;
	mat4 transform;
} matrix;

out vec2 tCoord_out;

void main() {
	tCoord_out = tCoord;
	mat4 Camera = matrix.projection * matrix.world * matrix.transform;
	gl_Position = Camera * vec4(position, 1);
}