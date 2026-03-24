package org.north.core.utils;

import org.lwjgl.opengl.GL20;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glValidateProgram;

public class ShaderUtils {

    //todo: remove
    private static final ResourceLoader resourceLoader = new LocalResourceLoader();

    private static final Map<Integer, Integer> shaderPrograms = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(ShaderUtils.class);


    private ShaderUtils() {
    }

    public static int load(String vertexPath, String fragmentPath) {
        Integer id;
        int hash = hashShaderPaths(vertexPath, fragmentPath);

        if ((id = shaderPrograms.get(hash)) != null) {
            return id;
        }

        String vertex = null;
        String fragment = null;
        try {
            vertex = resourceLoader.loadAsString(vertexPath);
            fragment = resourceLoader.loadAsString(fragmentPath);
        } catch (IOException e) {
            log.error("unable to load shader", e);
        }

        id = create(vertex, fragment);

        shaderPrograms.put(hash, id);
        return id;
    }

    private static int create(String vertex, String fragment) {
        int vertexShader = compileShader(GL_VERTEX_SHADER, vertex);
        int fragmentShader = compileShader(GL_FRAGMENT_SHADER, fragment);
        return compileShaderProgram(vertexShader, fragmentShader);
    }

    private static int compileShader(int type, String shaderCode) {
        int shader = glCreateShader(type);

        glShaderSource(shader, shaderCode);
        glCompileShader(shader);

        int compileStatus = glGetShaderi(shader, GL_COMPILE_STATUS);

        if (compileStatus == GL_FALSE) {
            log.warn("Failed to compile shader! Reason: {}", glGetShaderInfoLog(shader));
            return -1;
        }

        return shader;
    }

    private static int compileShaderProgram(int... shaders) {
        int program = glCreateProgram();

        Arrays.stream(shaders)
                .forEach(shader -> glAttachShader(program, shader));

        glLinkProgram(program);
        glValidateProgram(program);

        Arrays.stream(shaders)
                .forEach(GL20::glDeleteShader);

        return program;
    }

    private static int hashShaderPaths(String... paths) {
        return Objects.hash(paths);
    }

}
