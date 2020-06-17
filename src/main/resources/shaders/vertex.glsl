#version 460

uniform mat4 view;
uniform mat4 proj;

in vec3  pos;
in vec3  norm;
in vec2  tex;
in float light;
in float aLight;

out vec3  passNorm;
out vec2  passTex;
out float passLight;
out float passALight;

void main() {
    gl_Position = proj * view * vec4(pos, 1);
    passNorm = norm;
    passTex = tex;
    passLight = light;
    passALight = aLight;
}
