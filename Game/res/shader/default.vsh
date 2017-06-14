#version 330 core
  
layout(location = 0) in vec3 position;
layout(location = 1) in vec2 tCoord;
layout(location = 2) in vec3 normal;
layout(location = 3) in vec3 tangent;
layout(location = 4) in vec3 biTangent;

uniform struct Matrix {
	mat4 projection;
	mat4 world;
	mat4 normal;
	mat4 transform;
} matrix;

out VS_OUT {
	vec3 normal;
	mat3 TBN;
	vec2 tCoord;
	float viewDist;
	vec3 viewPosition;
	vec3 fragPosition;
	vec4 fragPositionLightSpace;
} vs_out;

uniform mat4 lightSpaceMatrix;
uniform vec3 viewPosition;

void main() {

    vec3 T = normalize(vec3(matrix.transform * vec4(tangent, 0.0)));
	vec3 B = normalize(vec3(matrix.transform * vec4(biTangent, 0.0)));
	vec3 N = normalize(vec3(matrix.transform * vec4(normal, 0.0)));
    vs_out.TBN = mat3(T, B, N);

	vs_out.normal = mat3(matrix.normal) * normal;
	vs_out.tCoord = tCoord;
	vs_out.fragPosition = vec3(matrix.transform * vec4(position, 1.0));
	vs_out.fragPositionLightSpace = lightSpaceMatrix * vec4(vs_out.fragPosition, 1.0);
	vs_out.viewDist = length(viewPosition - vs_out.fragPosition);
	vs_out.viewPosition = viewPosition;

	mat4 Camera = matrix.projection * matrix.world * matrix.transform;
	gl_Position = Camera * vec4(position, 1.0);
}