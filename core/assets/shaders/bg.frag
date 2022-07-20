#version 330 core

layout (location = 0) out vec4 color;

uniform sampler2D u_tex;

in DATA {
    vec2 uv;
} fs_in;


void main() {
    color = texture(u_tex, fs_in.uv);
}
