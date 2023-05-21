#version 330 core

out vec4 out_color;

in vec2 texture_coordinates;

uniform sampler2D u_texture;
uniform vec4      u_color;

void main() {
    out_color = texture(u_texture, texture_coordinates);
}
