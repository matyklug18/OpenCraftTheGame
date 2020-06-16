package matyk.engine.render;

import matyk.engine.utils.StringLoader;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3i;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;

public class Shader {

    public int PID;

    /**
     * initialize the shader
     * @return the initialized shader
     */
    public Shader init(String v, String f) {
        PID = GL20.glCreateProgram();

        int VID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);

        GL20.glShaderSource(VID, StringLoader.loadResourceAsString("shaders/"+v+".glsl"));
        GL20.glCompileShader(VID);
        if (GL20.glGetShaderi(VID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Vertex Shader: " + GL20.glGetShaderInfoLog(VID));
        }

        int FID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);

        GL20.glShaderSource(FID, StringLoader.loadResourceAsString("shaders/"+f+".glsl"));
        GL20.glCompileShader(FID);
        if (GL20.glGetShaderi(FID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Fragment Shader: " + GL20.glGetShaderInfoLog(FID));
        }

        GL20.glAttachShader(PID, VID);
        GL20.glAttachShader(PID, FID);

        GL20.glLinkProgram(PID);
        if (GL20.glGetProgrami(PID, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
            System.err.println("Program Linking: " + GL20.glGetProgramInfoLog(PID));
        }

        GL20.glValidateProgram(PID);
        if (GL20.glGetProgrami(PID, GL20.GL_VALIDATE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Program Validation: " + GL20.glGetProgramInfoLog(PID));
        }

        return this;
    }

    /**
     * set a uniform
     * @param name the name
     * @param matrix the value of the uniform
     */

    public void setUniform(String name, Matrix4f matrix) {
        FloatBuffer matrixB = MemoryUtil.memAllocFloat(16);
        matrix.get(matrixB);
        glUniformMatrix4fv(glGetUniformLocation(PID, name), false, matrixB);
        MemoryUtil.memFree(matrixB);
    }

    /**
     * set a uniform
     * @param name the name
     * @param vector the value of the uniform
     */
    public void setUniform(String name, Vector2f vector) {
        glUniform2fv(glGetUniformLocation(PID, name), new float[] {vector.x, vector.y});
    }

    /**
     * set a uniform
     * @param name the name
     * @param value the value of the uniform
     */
    public void setUniform(String name, int value) {
        glUniform1iv(glGetUniformLocation(PID, name), new int[] {value});
    }

    /**
     * set a uniform
     * @param name the name
     * @param value the value of the uniform
     */
    public void setUniform(String name, float value) {
        glUniform1fv(glGetUniformLocation(PID, name), new float[] {value});
    }

    /**
     * set a uniform
     * @param name the name
     * @param vector the value of the uniform
     */
    public void setUniform(String name, Vector3i vector) {
        glUniform3iv(glGetUniformLocation(PID, name), new int[] {vector.x, vector.y, vector.z});
    }

    /**
     * use the shader
     */
    public void use() {
        glUseProgram(PID);
    }

    /**
     * stop using the shader
     */
    public void unuse() {
        glUseProgram(0);
    }
}
