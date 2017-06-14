#version 330 core
                
layout(location = 0) out vec4 fragColor;

in VS_OUT {
	vec3 normal;
	mat3 TBN;
	vec2 tCoord;
	float viewDist;
	vec3 viewPosition;
	vec3 fragPosition;
	vec4 fragPositionLightSpace;
} fs_in;

uniform struct Material {
	sampler2D diffuseMap;
	sampler2D normalMap;
	vec3 color;
} material;

uniform sampler2D shadowMap;

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

#define NR_POINT_LIGHTS 6
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

	return diffuseIntensity * lightColor * material.color;
}

vec3 calcSpecular(vec3 normal, float specularStrength, vec3 lightDir, vec3 lightColor) {
	vec3 viewDir = normalize(fs_in.viewPosition - fs_in.fragPosition);
	vec3 halfwayDir = normalize(lightDir + viewDir);
	float specularIntensity = pow(max(dot(normal, halfwayDir), 0.0), 32);

	return specularStrength * specularIntensity * lightColor;
}

vec3 calcPointLight(vec3 normal, PointLight light) {
	vec3 lightDir = normalize(light.position - fs_in.fragPosition);

	vec3 diffuse = calcDiffuse(normal, lightDir, light.color);
	vec3 specular = calcSpecular(normal, 0.5f, lightDir, light.color);

	float distance = length(light.position - fs_in.fragPosition);
	float attenuation = 1.0f / (light.constant + light.linear * distance + light.quadratic * (distance * distance));

	diffuse *= attenuation;
	specular *= attenuation;

	return (diffuse + specular);
}

vec3 calcDirLight(vec3 normal, DirLight light) {
	vec3 lightDir = normalize(light.direction);

	vec3 diffuse = calcDiffuse(normal, lightDir, light.color);
	vec3 specular = calcSpecular(normal, 0.5f, lightDir, light.color);

	return (diffuse + specular);
}

vec3 calcSpotLight(vec3 normal, SpotLight light) {
	vec3 lightDir = normalize(light.position - fs_in.fragPosition);
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

float calcShadow(vec4 fragPositionLightSpace) {
	vec3 projCoords = ((fragPositionLightSpace.xyz / fragPositionLightSpace.w) * 0.5) + 0.5;
	float currentDepth = projCoords.z;
	float closestDepth = texture(shadowMap, projCoords.xy).r;
	float bias = 0.005;
	float shadow = 0.0;
	vec2 texelSize = 1.0 / textureSize(shadowMap, 0);
	for(float x = -3.5; x <= 3.5; ++x) {
		for(float y = -3.5; y <= 3.5; ++y) {
			float pcfDepth = texture(shadowMap, projCoords.xy + vec2(x, y) * texelSize).r;
			shadow += (currentDepth - bias) > pcfDepth ? 1.0 : 0.0;
		}
	}
	shadow /= 64.0f;
	
	if(projCoords.z > 1.0) {
		return 0.0;
	}
	
	return shadow;
}

float calcFog(float distance, float maxDist, float density) {
    float fog = 1.0 / exp(pow((distance / maxDist) * density, 2));

    return clamp(fog, 0.0, 1.0);
}

void main() {
	textureColor = texture(material.diffuseMap, fs_in.tCoord);
	textureColor = vec4(pow(textureColor.rgb, vec3(1.0/2.2)), textureColor.a);

	float ambientStrength = 0.25f;
	vec3 ambient = ambientStrength * ambientColor * material.color;

    vec3 normal;
    vec3 normalMapTexel = texture(material.normalMap, fs_in.tCoord).rgb;
    if(normalMapTexel == vec3(0)) {
    	normal = normalize(fs_in.normal);
    } else {
        normal = normalize(fs_in.TBN * normalize((normalMapTexel * 2.0) - 1.0));
    }

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

    float shadow = calcShadow(fs_in.fragPositionLightSpace);
	shadow = min(shadow, 0.75);
	vec3 lightResult = (ambient + ((1.0 - shadow) * totalLighting));

    float fog = calcFog(fs_in.viewDist, 15.0f, 1.0);
    vec3 fogColor = vec3(0.5, 0.5, 0.52);

	fragColor = vec4(mix(fogColor, textureColor.rgb * lightResult, fog), textureColor.a);
	//fragColor = vec4((normal + 1.0) * 0.5, 1.0);
}