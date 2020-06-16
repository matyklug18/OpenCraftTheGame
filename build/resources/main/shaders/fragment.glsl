#version 460

in vec3  passNorm;
in vec2  passTex;
in float passLight;

out vec4 outColor;

uniform sampler2D tex;
uniform float timeOfDay;

void main() {

    float bright = 1;

    if(abs(passNorm.x) > 0.01)
        bright = 0.7;
    if(abs(passNorm.z) > 0.01)
        bright = 0.5;
    if(passNorm.y > 0.01)
        bright = 0.9;
    if(passNorm.y < -0.01)
        bright = 0.3;

    outColor = vec4(texture(tex, passTex).rgb * bright * ( (sin(timeOfDay / 157.) + 1) / 2 / 1.5 + 0.5 ) * ( passLight / 15. + 0.2), 1.) ;
    //outColor = vec4(vec3(bright), 1);
    //outColor = vec4(( passLight / 15. + 0.2));
}
