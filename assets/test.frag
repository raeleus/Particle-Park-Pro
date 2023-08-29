#ifdef GL_ES
    #define PRECISION mediump
    precision PRECISION float;
    precision PRECISION int;
#else
    #define PRECISION
#endif

varying vec2 v_texCoords;
uniform sampler2D u_texture;


void main () {
    vec2 uv = v_texCoords;

    uv.y += (cos((uv.y + (1.0 * 0.04 * .5)) * 45.0) * 0.0019 * 10) + (cos((uv.y + (1.0 * 0.1 * .5)) * 10.0) * 0.002 * 10);

    uv.x += (sin((uv.y + (1.0 * 0.07 * .5)) * 15.0) * 0.0029 * 10) + (sin((uv.y + (1.0 * 0.1 * .5)) * 15.0) * 0.002 * 10);

    gl_FragColor = texture2D(u_texture, uv);
}
