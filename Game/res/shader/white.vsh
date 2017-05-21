#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 tCoord;
layout(location = 2) in vec3 normal;

uniform struct Matrix {
	mat4 projection;
	mat4 world;
	mat4 normal;
	mat4 transform;
} matrix;

out vec3 normal_out;
out vec2 tCoord_out;
out vec3 fragPosition;

void main() {
    normal_out = mat3(matrix.normal) * normal;
	tCoord_out = tCoord;
	fragPosition = vec3(matrix.transform * vec4(position, 1));

	mat4 Camera = matrix.projection * matrix.world * matrix.transform;
	gl_Position = Camera * vec4(position, 1);
}