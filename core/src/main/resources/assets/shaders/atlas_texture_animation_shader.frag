#version 330 core

out vec4 color;

in vec2 texture_coordinates;

uniform sampler2D u_texture;
uniform vec4      u_color;
uniform int       u_sprite_index;
uniform int       u_sprite_width;
uniform int       u_sprite_height;

void main() {
    int sprite_i = u_sprite_index;
    int sprite_h = u_sprite_height;
    int sprite_w = u_sprite_width;

    float width = texture_coordinates.x / 6 - 2.0 / sprite_w * (sprite_i + 1);

    vec2 updated_coordinates = vec2(width, texture_coordinates.y);
    color = texture(u_texture, updated_coordinates) * u_color;
}
