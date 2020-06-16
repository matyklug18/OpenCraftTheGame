package matyk.engine.io;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    public long winID;

    public int w, h;

    /**
     * init the window
     * @param wi width of the window
     * @param he height of the window
     * @return itself
     */
    public Window init(int wi, int he) {

        this.w = wi;
        this.h = he;

        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        winID = glfwCreateWindow(1000, 1000, "Hello World!", NULL, NULL);
        if (winID == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetKeyCallback(winID, Input.getKeyboardCallback());

        glfwSetMouseButtonCallback(winID, Input.getMouseButtonsCallback());

        glfwSetCursorPosCallback(winID, Input.getMouseMoveCallback());

        glfwSetWindowSizeCallback(winID, new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                w = width;
                h = height;
                glViewport(0, 0, width, height);
            }
        });

        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(winID, pWidth, pHeight);

            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(
                    winID,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }

        glfwMakeContextCurrent(winID);

        glfwSwapInterval(1);

        glfwShowWindow(winID);

        GL.createCapabilities();

        glEnable(GL_CULL_FACE);
        glCullFace(GL_FRONT);

        glEnable(GL_DEPTH_TEST);

        glClearColor(0.0f, 1.0f, 1.0f, 0.0f);

        return this;
    }

    /**
     * update the window. call this, then render, then call {@link #swapBuffers()}
     */
    public void update() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glfwPollEvents();
    }

    /**
     * swap the buffers
     */
    public void swapBuffers() {
        glfwSwapBuffers(winID);
    }
}
