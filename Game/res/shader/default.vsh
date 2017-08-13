#version 330 core

#define MAX_JOINTS 50
#define MAX_WEIGHTS 3

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 tCoord;
layout(location = 2) in vec3 normal;
layout(location = 3) in ivec3 jointIDS;
layout(location = 4) in vec3 weights;

uniform struct Matrix {
	mat4 projection;
	mat4 world;
	mat4 normal;
	mat4 transform;
} matrix;

out VS_OUT {
	vec3 normal;
	vec2 tCoord;
	float viewDist;
	vec3 viewPosition;
	vec3 fragPosition;
	vec4 fragPositionLightSpace;
} vs_out;

uniform bool skinned;
uniform mat4 lightSpaceMatrix;
uniform vec3 viewPosition;
uniform mat4 jointTransforms[MAX_JOINTS];

void main() {
	vec4 totalLocalPos = vec4(0);
	vec4 totalNormal = vec4(0);

    if(skinned) {
        for(int i = 0; i < MAX_WEIGHTS; i++) {
            mat4 jointTransform = jointTransforms[jointIDS[i]];
            vec4 posePosition = jointTransform * vec4(position, 1.0);
            totalLocalPos += posePosition * weights[i];

            vec4 worldNormal = jointTransform * vec4(normal, 0.0);
            totalNormal += worldNormal * weights[i];
        }
    } else {
        totalLocalPos = vec4(position, 1.0);
    	totalNormal = vec4(normal, 0.0);
    }

	vs_out.normal = mat3(matrix.normal) * totalNormal.xyz;
    vs_out.tCoord = tCoord;
    vs_out.fragPosition = vec3(matrix.transform * totalLocalPos);
    vs_out.fragPositionLightSpace = lightSpaceMatrix * vec4(vs_out.fragPosition, 1.0);
    vs_out.viewDist = length(viewPosition - vs_out.fragPosition);
    vs_out.viewPosition = viewPosition;

	mat4 Camera = matrix.projection * matrix.world * matrix.transform;
	gl_Position = Camera * totalLocalPos;
}