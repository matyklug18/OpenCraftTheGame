package matyk.game.main;

import matyk.engine.client.Client;
import matyk.engine.data.Block;
import matyk.engine.data.Mesh;
import matyk.engine.data.World;
import matyk.engine.gen.WorldGen;
import matyk.engine.io.Input;
import matyk.engine.io.Window;
import matyk.engine.meshing.Mesher;
import matyk.engine.physics.AABB;
import matyk.engine.render.Renderer;
import matyk.engine.utils.FastNoise;
import matyk.game.main.blocks.BlockAir;
import matyk.game.main.blocks.BlockDirt;
import matyk.game.main.blocks.BlockGrass;
import matyk.game.main.blocks.BlockStone;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.Random;

import static matyk.engine.utils.Utils.forge;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwGetTime;


public class Main {

    public static float timeOfDay = 0;



    public static Block pBlock = new BlockDirt();

    public static Block getBlock() {
        return forge(pBlock.getClass());
    }

    private static final int[] inds = new int[] {
            2, 1, 0,
            2, 3, 1
    };

    private static final Vector2f[] texs = new Vector2f[] {
            new Vector2f(0,0),
            new Vector2f(0,1),
            new Vector2f(1,0),
            new Vector2f(1,1),
    };

    private static final Vector3f nfront = new Vector3f(0,0,1);

    private static final Vector3f[] vfront = new Vector3f[] {
            new Vector3f(-25, -25, 0.01f),
            new Vector3f(25, -25, 0.01f),
            new Vector3f(-25, 25, 0.01f),
            new Vector3f(25, 25, 0.01f),
    };

    private static float sdSphere( Vector3f p, float s )
    {
        return p.length() - s;
    }

    public static void main(String[] args) {
        Window win = new Window().init(100,100);

        Mesh crosshair = new Mesh().init(vfront, texs, new Vector3f[]{nfront}, inds, new int[0], new int[0]);

        Block[][][] blocks = new Block[World.size.x*16][World.size.y*16][World.size.z*16];

        for(int i = 0; i < World.size.x*16; i++) {
            for(int j = 0; j < World.size.y*16; j++) {
                for(int k = 0; k < World.size.z*16; k++) {
                    blocks[i][j][k] = WorldGen.getBlock(i,j,k);
                }
            }
        }

        for(int i = 0; i < World.size.x*16; i++) {
            for(int j = 0; j < World.size.y*16; j++) {
                for(int k = 0; k < World.size.z*16; k++) {
                    if(blocks[i][j][k] instanceof BlockDirt && blocks[i][j+1][k] instanceof BlockAir) {
                        blocks[i][j][k] = new BlockGrass();
                    }
                }
            }
        }

        for(int i = 0; i < World.size.x*16; i++) {
            for (int j = 0; j < World.size.y * 16; j++) {
                for (int k = 0; k < World.size.z * 16; k++) {
                    if (blocks[i][j][k] instanceof BlockDirt && blocks[i][j + 1][k] instanceof BlockDirt) {
                        blocks[i][j][k] = new BlockStone();
                    }
                }
            }
        }

        Random rand = new Random();

        Vector3f dir = new Vector3f(0,-1,0);

        for(int i = 0; i < 8; i ++) {
            Vector3f pos = new Vector3f(rand.nextInt(World.size.x*16), 0, rand.nextInt(World.size.z*16));

            for(int j = 0; j < World.size.y*16; j++) {
                if((int) pos.x < World.size.x*16 && (int) pos.y < World.size.y*16 && (int) pos.z < World.size.z*16 && (int) pos.x >= 0 && (int) pos.y >= 0 && (int) pos.z >= 0)
                    if(blocks[(int) pos.x][(int) pos.y][(int) pos.z] instanceof BlockAir)
                        break;
                    else
                        pos.y += 1;
                else
                    break;
            }

            for(int j = 0; j < 256; j ++) {
                dir.add(new Vector3f(rand.nextFloat()*2-1, rand.nextFloat()*2-1.1f, rand.nextFloat()*2-1));
                pos.add(dir);
                dir.normalize();

                if((int) pos.x < World.size.x*16 && (int) pos.y < World.size.y*16 && (int) pos.z < World.size.z*16 && (int) pos.x >= 0 && (int) pos.y >= 0 && (int) pos.z >= 0) {
                    for(int x = -4; x < 4; x++) {
                        for (int y = -4; y < 4; y++) {
                            for (int z = -4; z < 4; z++) {
                                if(sdSphere(new Vector3f(x,y,z), 4) < 0)
                                    if((int) pos.x + x < World.size.x*16 && (int) pos.x + x >= 0 && (int) pos.y + y < World.size.y*16 && (int) pos.y + y >= 0 && (int) pos.z + z < World.size.z*16 && (int) pos.z + z >= 0)
                                        blocks[(int) pos.x + x][(int) pos.y + y][(int) pos.z + z] = new BlockAir();
                            }
                        }
                    }
                }
                else
                    break;
            }

        }

        for (int i = 0; i < World.size.x * 16; i++) {
            for (int j = 0; j < World.size.y * 16; j++) {
                for (int k = 0; k < World.size.z * 16; k++) {
                    if(!(blocks[i][j][k].isTransparent()))
                        for (int y = j + 1; y < World.size.y * 16; y ++) {
                            if (y == World.size.y * 16 - 1) {
                                propagate(new Vector3i(i,j+1,k), blocks, 5);
                                break;
                            }
                            if(!(blocks[i][y][k].isTransparent()))
                                break;
                        }
                }
            }
        }

        for (int i = 0; i < World.size.x * 16; i++) {
            for (int j = 0; j < World.size.y * 16; j++) {
                for (int k = 0; k < World.size.z * 16; k++) {
                    for(int l = 0; l < 6; l++) {
                        blocks[i][j][k].aLightLevel[l] = blocks[i][j][k].getLightLevel();
                    }
                }
            }
        }

        World.mesh(blocks);

        for(int i = 0; i < World.size.x*16; i++) {
            for(int j = 0; j < World.size.y*16; j++) {
                for(int k = 0; k < World.size.z*16; k++) {
                    if
                    (
                            ((i < blocks.length - 1 && !blocks[i + 1][j][k].isFull()) || i == blocks.length - 1) && blocks[i][j][k].isFull()
                            || ((i > 0 && !blocks[i - 1][j][k].isFull()) || i == 0) && blocks[i][j][k].isFull()
                            || ((j < blocks[i].length - 1 && !blocks[i][j + 1][k].isFull()) || j == blocks[i].length - 1) && blocks[i][j][k].isFull()
                            || ((j > 0 && !blocks[i][j - 1][k].isFull()) || j == 0) && blocks[i][j][k].isFull()
                            || ((k < blocks[i][j].length - 1 && !blocks[i][j][k + 1].isFull()) || k == blocks[i][j].length - 1) && blocks[i][j][k].isFull()
                            || ((k > 0 && !blocks[i][j][k - 1].isFull()) || k == 0) && blocks[i][j][k].isFull()
                    )
                    {
                        Vector3f a = new Vector3f(i+1,j+1,k+1);
                        Vector3f b = new Vector3f(i,j,k);
                        World.chunks[i/16][j/16][k/16].aabbs[i%16][j%16][k%16] = new AABB(a, b);
                    }
                }
            }
        }


        Renderer.init();

        glfwSetInputMode(win.winID, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        float delta = 0;

        while(true) {

            float preT = (float) glfwGetTime();
            Vector2f pre = new Vector2f((float) Input.getMouseX(), (float) Input.getMouseY());

            Client.update(delta);
            win.update();
            for(int x = 0; x < World.size.x; x++) {
                for (int y = 0; y <  World.size.y; y++) {
                    for (int z = 0; z <  World.size.z; z++) {
                        if(World.chunks[x][y][z].contains)
                            Renderer.render(World.chunks[x][y][z].mesh, win, false);
                    }
                }
            }

            Renderer.render(crosshair, win, true);

            win.swapBuffers();

            Vector2f po = new Vector2f((float) Input.getMouseX(), (float) Input.getMouseY());

            Client.player.handleMouse(pre, po, 0.05f);
            delta = (float) (glfwGetTime() - preT);
        }
    }

    public static void propagate(Vector3i pos, Block[][][] blocks, int value) {
        if (value < 1)
            return;

        Block block = blocks[pos.x][pos.y][pos.z];

        Block right = null;
        if(pos.x < World.size.x * 16 - 1)
            right = blocks[pos.x + 1][pos.y][pos.z];

        Block left = null;
        if(pos.x > 0)
            left = blocks[pos.x - 1][pos.y][pos.z];

        Block top = null;
        if(pos.y < World.size.y * 16 - 1)
            top = blocks[pos.x][pos.y + 1][pos.z];

        Block bot = null;
        if(pos.y > 0)
            bot = blocks[pos.x][pos.y - 1][pos.z];

        Block front = null;
        if(pos.z < World.size.z * 16 - 1)
            front = blocks[pos.x][pos.y][pos.z + 1];

        Block back = null;
        if(pos.z > 0)
            back = blocks[pos.x][pos.y][pos.z - 1];

        if(block.isTransparent()) {

            for(int l = 0; l < 6; l++) {
                block.lightLevel[l] = value;
            }

            if(right != null && right.lightLevel[1] < value) {
                if (!right.isTransparent())
                    right.lightLevel[1] = value;
                else
                    propagate(new Vector3i(pos).add(new Vector3i(1, 0, 0)), blocks, value - 1);
            }

            if(left != null && left.lightLevel[0] < value) {
                if (!left.isTransparent())
                    left.lightLevel[0] = value;
                else
                    propagate(new Vector3i(pos).add(new Vector3i(-1, 0, 0)), blocks, value - 1);
            }

            if(top != null && top.lightLevel[3] < value) {
                if (!top.isTransparent())
                    top.lightLevel[3] = value;
                else
                    propagate(new Vector3i(pos).add(new Vector3i(0, 1, 0)), blocks, value - 1);
            }

            if(bot != null && bot.lightLevel[2] < value) {
                if (!bot.isTransparent())
                    bot.lightLevel[2] = value;
                else
                    propagate(new Vector3i(pos).add(new Vector3i(0, -1, 0)), blocks, value - 1);
            }

            if(front != null && front.lightLevel[5] < value) {
                if (!front.isTransparent())
                    front.lightLevel[5] = value;
                else
                    propagate(new Vector3i(pos).add(new Vector3i(0, 0, 1)), blocks, value - 1);
            }

            if(back != null && back.lightLevel[4] < value) {
                if (!back.isTransparent())
                    back.lightLevel[4] = value;
                else
                    propagate(new Vector3i(pos).add(new Vector3i(0, 0, -1)), blocks, value - 1);
            }
        }

        if(World.chunks[pos.x/16][pos.y/16][pos.z/16] != null)
            World.chunks[pos.x/16][pos.y/16][pos.z/16].dirty = true;
    }

    public static void aPropagate(Vector3i pos, Block[][][] blocks, int value) {
        if (value < 1)
            return;

        Block block = blocks[pos.x][pos.y][pos.z];

        Block right = null;
        if(pos.x < World.size.x * 16 - 1)
            right = blocks[pos.x + 1][pos.y][pos.z];

        Block left = null;
        if(pos.x > 0)
            left = blocks[pos.x - 1][pos.y][pos.z];

        Block top = null;
        if(pos.y < World.size.y * 16 - 1)
            top = blocks[pos.x][pos.y + 1][pos.z];

        Block bot = null;
        if(pos.y > 0)
            bot = blocks[pos.x][pos.y - 1][pos.z];

        Block front = null;
        if(pos.z < World.size.z * 16 - 1)
            front = blocks[pos.x][pos.y][pos.z + 1];

        Block back = null;
        if(pos.z > 0)
            back = blocks[pos.x][pos.y][pos.z - 1];

        if(block.isTransparent() || block.getLightLevel() > 0) {

            for(int l = 0; l < 6; l++) {
                block.aLightLevel[l] = value;
            }

            if(right != null && right.aLightLevel[1] < value) {
                if (!right.isTransparent())
                    right.aLightLevel[1] = value;
                else
                    aPropagate(new Vector3i(pos).add(new Vector3i(1, 0, 0)), blocks, value - 1);
            }

            if(left != null && left.aLightLevel[0] < value) {
                if (!left.isTransparent())
                    left.aLightLevel[0] = value;
                else
                    aPropagate(new Vector3i(pos).add(new Vector3i(-1, 0, 0)), blocks, value - 1);
            }

            if(top != null && top.aLightLevel[3] < value) {
                if (!top.isTransparent())
                    top.aLightLevel[3] = value;
                else
                    aPropagate(new Vector3i(pos).add(new Vector3i(0, 1, 0)), blocks, value - 1);
            }

            if(bot != null && bot.aLightLevel[2] < value) {
                if (!bot.isTransparent())
                    bot.aLightLevel[2] = value;
                else
                    aPropagate(new Vector3i(pos).add(new Vector3i(0, -1, 0)), blocks, value - 1);
            }

            if(front != null && front.aLightLevel[5] < value) {
                if (!front.isTransparent())
                    front.aLightLevel[5] = value;
                else
                    aPropagate(new Vector3i(pos).add(new Vector3i(0, 0, 1)), blocks, value - 1);
            }

            if(back != null && back.aLightLevel[4] < value) {
                if (!back.isTransparent())
                    back.aLightLevel[4] = value;
                else
                    aPropagate(new Vector3i(pos).add(new Vector3i(0, 0, -1)), blocks, value - 1);
            }
        }

        if(World.chunks[pos.x/16][pos.y/16][pos.z/16] != null)
            World.chunks[pos.x/16][pos.y/16][pos.z/16].dirty = true;
    }
}
