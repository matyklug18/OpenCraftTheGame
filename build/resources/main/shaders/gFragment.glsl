#version 460

in vec3 passNorm;
in vec2 passTex;

out vec4 outColor;

uniform sampler2D tex;

void main() {

    outColor = texture(tex, passTex);

    if(outColor.a < 0.01)
        discard;
}
