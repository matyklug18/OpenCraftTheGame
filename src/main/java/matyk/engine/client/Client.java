package matyk.engine.client;

import matyk.engine.data.Player;
import matyk.engine.data.World;
import matyk.engine.io.Input;
import matyk.game.main.Main;
import matyk.game.main.blocks.*;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Client {
    public static Player player = new Player();

    public static boolean edit = false;

    private static float lag;

    public static void update(float delta) {

        Main.timeOfDay += delta;

        lag += delta;

        float speed = 1;

        if(Input.isKeyDown(GLFW_KEY_LEFT_CONTROL))
            speed = 10;

        if(Input.isKeyDown(GLFW_KEY_W)) {
            player.moveIn(new Vector3f(0,0,-speed));
        }

        if(Input.isKeyDown(GLFW_KEY_S)) {
            player.moveIn(new Vector3f(0,0,speed));
        }

        if(Input.isKeyDown(GLFW_KEY_A)) {
            player.moveIn(new Vector3f(-speed,0,0));
        }

        if(Input.isKeyDown(GLFW_KEY_D)) {
            player.moveIn(new Vector3f(speed,0,0));
        }

        if(Input.isKeyDown(GLFW_KEY_SPACE)) {
            player.moveIn(new Vector3f(0,10f*speed, 0));
        } else
            player.moveIn(new Vector3f(0,-10f * (Input.isKeyDown(GLFW_KEY_LEFT_SHIFT) ? speed : 1), 0));

        if(Input.isButtonDown(GLFW_MOUSE_BUTTON_1) && lag >= 0.25f) {
            player.breakBlock();
            edit = true;
            lag = 0;
        }

        if(Input.isButtonDown(GLFW_MOUSE_BUTTON_2) &&  lag >= 0.25f) {
            player.placeBlock(Main.getBlock());
            edit = true;
            lag = 0;
        }

        if(Input.isKeyDown(GLFW_KEY_1)) {
            Main.pBlock = new BlockDirt();
        }

        if(Input.isKeyDown(GLFW_KEY_2)) {
            Main.pBlock = new BlockGrass();
        }

        if(Input.isKeyDown(GLFW_KEY_3)) {
            Main.pBlock = new BlockLever();
        }

        if(Input.isKeyDown(GLFW_KEY_4)) {
            Main.pBlock = new BlockStone();
        }

        if(Input.isKeyDown(GLFW_KEY_5)) {
            Main.pBlock = new BlockGlow();
        }
    }
}
