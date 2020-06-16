package matyk.engine.io;


import matyk.engine.client.Client;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class Input {
    private static boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST];
    private static boolean[] buttons = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
    private static double mouseX, mouseY;

    private static GLFWKeyCallback keyboard;
    private static GLFWCursorPosCallback mouseMove;
    private static GLFWMouseButtonCallback mouseButtons;

    static {
        keyboard = new GLFWKeyCallback() {
            public void invoke(long window, int key, int scancode, int action, int mods) {
                keys[key] = (action != GLFW.GLFW_RELEASE);
            }
        };

        mouseMove = new GLFWCursorPosCallback() {
            public void invoke(long window, double xpos, double ypos) {
                mouseX = xpos;
                mouseY = ypos;
            }
        };

        mouseButtons = new GLFWMouseButtonCallback() {
            public void invoke(long window, int button, int action, int mods) {
                buttons[button] = (action != GLFW.GLFW_RELEASE);
            }
        };
    }

    /**
     * check if the key is pressed
     * @param key the key to be checked
     * @return if the key is pressed or not
     */
    public static boolean isKeyDown(int key) {
        return keys[key];
    }

    /**
     * check if the button is pressed
     * @param button the button to be checked
     * @return if the button is pressed or not
     */
    public static boolean isButtonDown(int button) {
        return buttons[button];
    }

    public void destroy() {
        keyboard.free();
        mouseMove.free();
        mouseButtons.free();
    }

    /**
     * get the mouse X
     * @return the mouse X
     */
    public static double getMouseX() {
        return mouseX;
    }

    /**
     * get the mouse Y
     * @return the mouse Y
     */
    public static double getMouseY() {
        return mouseY;
    }

    public static GLFWKeyCallback getKeyboardCallback() {
        return keyboard;
    }

    public static GLFWCursorPosCallback getMouseMoveCallback() {
        return mouseMove;
    }

    public static GLFWMouseButtonCallback getMouseButtonsCallback() {
        return mouseButtons;
    }
}