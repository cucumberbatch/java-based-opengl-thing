#version 330 core

layout (location = 0) in vec4 position;
layout (location = 1) in vec2 uv;

uniform mat4 u_model;
uniform mat4 u_view;
uniform mat4 u_projection;

out DATA {
    vec2 uv;
} vs_out;

void main() {
    gl_Position = u_projection * u_view * u_model * position;
    vs_out.uv = uv;
}
