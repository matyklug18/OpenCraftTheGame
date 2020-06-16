#version 460

uniform vec2 view;

in vec3 pos;
in vec3 norm;
in vec2 tex;

out vec3 passNorm;
out vec2 passTex;

void main() {
    gl_Position = vec4(vec2(pos.x/view.x, pos.y/view.y), pos.z, 1);
    passNorm = norm;
    passTex = tex;
}
