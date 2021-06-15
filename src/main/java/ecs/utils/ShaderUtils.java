package ecs.utils;

import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glValidateProgram;

public class ShaderUtils {

    private ShaderUtils() {
    }

    public static int load(String vertexPath, String fragmentPath) {
        String vertex   = FileUtils.loadAsString(vertexPath);
        String fragment = FileUtils.loadAsString(fragmentPath);
        return create(vertex, fragment);
    }

    private static int create(String vertex, String fragment) {
        int program    = glCreateProgram();
        int vertexID   = glCreateShader(GL_VERTEX_SHADER);
        int fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(vertexID,   vertex);
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
