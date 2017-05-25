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

out VS_OUT {
	vec3 normal;
	vec2 tCoord;
	vec3 fragPosition;
	vec4 fragPositionLightSpace;
} vs_out;

uniform mat4 lightSpaceMatrix;

void main() {
	vs_out.normal = mat3(matrix.normal) * normal;
	vs_out.tCoord = tCoord;
	vs_out.fragPosition = vec3(matrix.transform * vec4(position, 1));
	vs_out.fragPositionLightSpace = lightSpaceMatrix * vec4(vs_out.fragPosition, 1.0);

	mat4 Camera = matrix.projection * matrix.world * matrix.transform;
	gl_Position = Camera * vec4(position, 1);
}