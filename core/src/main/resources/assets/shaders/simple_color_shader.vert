#version 330 core

layout (location = 0) in vec3 a_position;

uniform mat4 u_model;
uniform mat4 u_view;
uniform mat4 u_projection;

void main() {
    vec4 vertPosition = u_projection * u_view * u_model * vec4(a_position, 1);
    gl_Position = vertPosition;
}
