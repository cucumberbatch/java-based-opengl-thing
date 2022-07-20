#version 330 core

layout (location = 0) in vec4 position;
layout (location = 1) in vec2 uv;

uniform vec2 pos_shift;


out DATA {
    vec2 uv;
} vs_out;

void main() {
    gl_Position = position;// * pos_shift;
    vs_out.uv = uv;
}
