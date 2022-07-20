#version 330 core

layout (location = 0) out vec4 color;

uniform sampler2D u_tex;
uniform vec4 u_color;



in DATA {
    vec2 uv;
} fs_in;


void main() {
    color = u_color;
}
