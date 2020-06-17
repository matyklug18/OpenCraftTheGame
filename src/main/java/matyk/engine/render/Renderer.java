package matyk.engine.render;

import matyk.engine.client.Client;
import matyk.engine.data.Mesh;
import matyk.engine.io.Window;
import matyk.engine.utils.MatrixUtils;
import matyk.engine.utils.TextureLoader;
import matyk.game.main.Main;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_ZERO;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_3D;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Renderer {

    private static Shader nShader = new Shader().init("vertex", "fragment");
    private static Shader gShader = new Shader().init("gVertex", "gFragment");

    private static int aTexID;
    private static int gTexID;

    /**
     * init the renderer
     */
    public static void init() {
        aTexID = TextureLoader.load("atlas.png").id;
        gTexID = TextureLoader.load("guiAtlas.png").id;
    }

    /**
     * render the supplied object
     * @param mesh the mesh of the object to render, must be initialized
     * @param win the window to render on
     */
    public static void render(Mesh mesh, Window win, boolean isGui) {
        glBindVertexArray(mesh.vaoID);

        Shader shader = isGui ? gShader : nShader;

        glActiveTexture(GL_TEXTURE0);
        if(isGui)
            glBindTexture(GL_TEXTURE_2D, gTexID);
        else
            glBindTexture(GL_TEXTURE_2D, aTexID);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, mesh.ibo);

        shader.use();
        if(!isGui) {
            shader.setUniform("view", MatrixUtils.viewMatrix(Client.player.cam.pos, Client.player.cam.rot));
            shader.setUniform("proj", MatrixUtils.projectionMatrix(70, ((float) win.w) / ((float) win.h), 0.1f, 1024));
        }
        else
            shader.setUniform("view", new Vector2f(win.w, win.h));

        shader.setUniform("timeOfDay", Main.timeOfDay);

        glDrawElements(GL_TRIANGLES, mesh.inds, GL_UNSIGNED_INT, 0);

        shader.unuse();

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        glBindTexture(GL_TEXTURE_2D, GL_ZERO);

        glBindVertexArray(0);
    }
}
