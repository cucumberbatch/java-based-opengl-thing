#version 330 core

layout (location = 0) in vec3 a_position;
layout (location = 1) in vec2 a_texture_coordinates;

out vec2 texture_coordinates;

uniform vec3 u_position;

void main() {
    gl_Position = vec4(u_position + a_position, 1);
    texture_coordinates = a_texture_coordinates;
}
