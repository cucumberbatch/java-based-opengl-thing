package ecs.utils;

import static org.lwjgl.opengl.GL20.*;

public class ShaderUtils {

    private ShaderUtils() {
    }

    public static int load(String vertexPath, String fragmentPath) {
        String vertex   = FileUtils.loadAsString(vertexPath);
        String fragment = FileUtils.loadAsString(fragmentPath);
        return create(vertex, fragment);
    }

    private static int create(String vertex, String fragment) {
        int program = glCreateProgram();
        int vertexID = glCreateShader(GL_VERTEX_SHADER);
        int fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(vertexID, vertex);
        glShaderSource(fragmentID, fragment);

        glCompileShader(vertexID);
        if (glGetShaderi(vertexID, GL_COMPILE_STATUS) == GL_FALSE) {
            System.out.println("Failed to compile vertex shader!");
            System.out.println(glGetShaderInfoLog(vertexID));
            return -1;
        }

        glCompileShader(fragmentID);
        if (glGetShaderi(fragmentID, GL_COMPILE_STATUS) == GL_FALSE) {
            System.out.println("Failed to compile fragment shader!");
            System.out.println(glGetShaderInfoLog(fragmentID));
            return -1;
        }

        glAttachShader(program, vertexID);
        glAttachShader(program, fragmentID);
        glLinkProgram(program);
        glValidateProgram(program);

        glDeleteShader(vertexID);
        glDeleteShader(fragmentID);

        return program;
    }
}
