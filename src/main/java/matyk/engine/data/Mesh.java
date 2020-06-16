package matyk.engine.data;

import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Mesh {
    public int vaoID;

    public int vbo;
    public int tbo;
    public int nbo;
    public int lbo;

    public int ibo;

    public int inds;

    /**
     * create the mesh with the data
     * @param verts the verticies
     * @param texs the UVs
     * @param norms the normals
     * @param inds the indicies
     * @return the initialized mesh
     */
    public Mesh init(Vector3f[] verts, Vector2f[] texs, Vector3f[] norms, int[] inds, int[] lights, int[] aLights) {
        this.inds = inds.length;

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        float[] vBuf = new float[verts.length * 3];
        for(int i = 0; i < verts.length; i++) {
            vBuf[i*3] = verts[i].x;
            vBuf[i*3+1] = verts[i].y;
            vBuf[i*3+2] = verts[i].z;
        }

        float[] nBuf = new float[norms.length * 3];
        for(int i = 0; i < norms.length; i++) {
            nBuf[i*3] = norms[i].x;
            nBuf[i*3+1] = norms[i].y;
            nBuf[i*3+2] = norms[i].z;
        }

        float[] tBuf = new float[texs.length * 2];
        for(int i = 0; i < texs.length; i++) {
            tBuf[i*2] = texs[i].x;
            tBuf[i*2+1] = texs[i].y;
        }

        int[] iBuf = new int[inds.length];
        for(int i = 0; i < inds.length; i++) {
            iBuf[i] = inds[i];
        }

        float[] lBuf = new float[lights.length];
        for(int i = 0; i < lights.length; i++) {
            lBuf[i] = lights[i];
        }

        float[] aBuf = new float[aLights.length];
        for(int i = 0; i < aLights.length; i++) {
            aBuf[i] = aLights[i];
        }

        vbo = glGenBuffers();

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vBuf,  GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        nbo = glGenBuffers();

        glBindBuffer(GL_ARRAY_BUFFER, nbo);
        glBufferData(GL_ARRAY_BUFFER, nBuf,  GL_STATIC_DRAW);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        tbo = glGenBuffers();

        glBindBuffer(GL_ARRAY_BUFFER, tbo);
        glBufferData(GL_ARRAY_BUFFER, tBuf,  GL_STATIC_DRAW);
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        lbo = glGenBuffers();

        glBindBuffer(GL_ARRAY_BUFFER, lbo);
        glBufferData(GL_ARRAY_BUFFER, lBuf,  GL_STATIC_DRAW);
        glVertexAttribPointer(3, 1, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        ibo = glGenBuffers();

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, iBuf, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        return this;
    }
}
