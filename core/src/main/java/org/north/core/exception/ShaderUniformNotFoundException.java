package org.north.core.exception;

public class ShaderUniformNotFoundException extends RuntimeException {
    private final String uniformName;
    private final String shaderName;

    public ShaderUniformNotFoundException(String uniformName) {
        super(String.format("Couldn't find uniform variable '%s'!", uniformName));
        this.uniformName = uniformName;
        this.shaderName = null;
    }

    public ShaderUniformNotFoundException(String uniformName, String shaderName) {
        super(String.format("Couldn't find uniform variable '%s'!", uniformName));
        this.uniformName = uniformName;
        this.shaderName = shaderName;
    }

    public String getUniformName() {
        return uniformName;
    }

    public String getShaderName() {
        return shaderName;
    }
}
