#version 460

in vec3  passNorm;
in vec2  passTex;
in float passLight;
in float passALight;

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

    float timeOfDayF = ( (sin(timeOfDay) + 1) / 2 ); //day night
    float sky = ( passLight / 5. ); //sky
    float art = ( passALight / 15. ); //glow

    //from the sun
    float sun = ( timeOfDayF * sky );

    const float ambient = 0.2;

    float result = ( sun + art * (0.99999 - sun ) );
    result = (ambient + (result * 0.8)) * bright;

    outColor = vec4(texture(tex, passTex).rgb * result, 1.) ;

    //outColor = vec4(vec3(bright), 1);
    //outColor = vec4(( passLight / 15. + 0.2));
}
