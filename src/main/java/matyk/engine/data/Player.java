package matyk.engine.data;

import matyk.engine.io.Input;
import matyk.engine.physics.AABB;
import matyk.engine.utils.MatrixUtils;
import matyk.game.main.Main;
import matyk.game.main.blocks.BlockAir;
import matyk.game.main.blocks.BlockDirt;
import org.apache.commons.io.output.WriterOutputStream;
import org.joml.*;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.stb.STBIZlibCompress;

import javax.print.DocFlavor;
import java.lang.Math;

public class Player {
    public Camera cam = new Camera();

    private Vector3f pos = new Vector3f(32,128,32);
    private Vector3f rot = new Vector3f();

    /**
     * this method moves the player forward.
     * @param oDir the vector to multiply the direction with
     */
    public void moveIn(Vector3f oDir) {
        Vector3f result = new Vector3f();

        Vector3f rotV = rot.mul((float) (Math.PI / 180.0f), new Vector3f());

        result.x = (float) ((oDir.x * Math.cos(rotV.y)) - (oDir.z * Math.sin(rotV.y)));
        result.z = (float) ((oDir.x * Math.sin(rotV.y)) + (oDir.z * Math.cos(rotV.y)));

        result.y = oDir.y / 10f;

        Vector3f posClone = new Vector3f(pos);

        setPos(new Vector3f(getPos()).add(result.div(10f)));

        int xO = 0;
        int xN = 0;
        int yO = 0;
        int yN = 0;
        int zO = 0;
        int zN = 0;

        if(((int) pos.x)/16 >= World.size.x) {
            xO = -1;
        }

        if(((int) pos.x)/16 <= 1) {
            xN = 1;
        }

        if(((int) pos.y)/16 >= World.size.y) {
            yO = -1;
        }

        if(((int) pos.y)/16 <= 1) {
            yN = 1;
        }

        if(((int) pos.z)/16 >= World.size.z) {
            zO = -1;
        }

        if(((int) pos.z)/16 <= 1) {
            zN = 1;
        }

        boolean collides = false;
        for(int i = (((int)pos.x) / 16 - 1) + xN; i <= (((int)pos.x) / 16) + xO; i++) {
            for (int j = (((int)pos.y) / 16 - 1) + yN; j <= (((int)pos.y) / 16) + yO; j++) {
                for (int k = (((int)pos.z) / 16 - 1) + zN; k <= (((int)pos.z) / 16) + zO; k++) {
                    for(int x = 0; x < 16; x++) {
                        for (int y = 0; y < 16; y++) {
                            for (int z = 0; z < 16; z++) {
                                if (World.chunks[i][j][k].aabbs[x][y][z] != null && World.chunks[i][j][k].aabbs[x][y][z].collides(getCollider()))
                                    collides = true;
                            }
                        }
                    }
                }
            }
        }
        if(collides)
            setPos(posClone);
    }

    public AABB getCollider() {
        return new AABB(new Vector3f(pos.x + 0.005f, pos.y + 0.005f, pos.z + 0.005f), new Vector3f(pos.x - 0.005f, pos.y - 0.005f, pos.z - 0.005f));
    }

    /**
     * handle mouse input
     * @param pre previous mouse pos
     * @param now current mouse pos
     * @param sens the sensitivity
     */
    public void handleMouse(Vector2f pre, Vector2f now, float sens) {
        Vector2f dif = pre.sub(now).mul(sens);
        if(new Vector3f(getRot()).add(-dif.y, -dif.x, 0).x >= 90)
            dif.y = 0;
        if(new Vector3f(getRot()).add(-dif.y, -dif.x, 0).x <= -90)
            dif.y = 0;
        setRot(new Vector3f(getRot()).add(-dif.y, -dif.x, 0));
    }

    public Vector3f getPos() {
        return pos;
    }

    public void setPos(Vector3f pos) {
        this.pos = pos;
        this.cam.pos = new Vector3f(pos).add(0,1,0);
    }

    public Vector3f getRot() {
        return rot;
    }

    public void setRot(Vector3f rot) {
        this.rot = rot;
        this.cam.rot = rot;
    }

    public void breakBlock() {
        Vector3f oDir = new Vector3f(0,0,-1);

        Vector3f rotV = new Vector3f(rot.x, rot.y, rot.z).mul((float) (Math.PI / 180.0f));

        Matrix3f rotM = new Matrix3f();

        rotM.rotateXYZ(rotV);

        rotM.invert();

        Vector3f result = oDir.mul(rotM);

        Vector3f currPos = new Vector3f(pos).add(new Vector3f(0,1,0));

        for(int i = 0; i < 600; i ++) {
            if(World.getBlockAt((int) currPos.x, (int) currPos.y, (int) currPos.z).isFull()) {
                World.setBlockAt((int) currPos.x, (int) currPos.y, (int) currPos.z, new BlockAir());
                break;
            }
            else
                currPos.add(new Vector3f(result).div(100));
        }
    }

    public void placeBlock(Block block) {
        Vector3f oDir = new Vector3f(0,0,-1);

        Vector3f rotV = new Vector3f(rot.x, rot.y, rot.z).mul((float) (Math.PI / 180.0f));

        Matrix3f rotM = new Matrix3f();

        rotM.rotateXYZ(rotV);

        rotM.invert();

        Vector3f result = oDir.mul(rotM);

        Vector3f currPos = new Vector3f(pos).add(new Vector3f(0,1,0));

        result.normalize();

        for(int i = 0; i < 600; i ++) {
            if(World.getBlockAt((int) currPos.x, (int) currPos.y, (int) currPos.z).isFull()) {
                if(World.getBlockAt((int) currPos.x, (int) currPos.y, (int) currPos.z).hasAction()) {
                    World.getBlockAt((int) currPos.x, (int) currPos.y, (int) currPos.z).onActivated(new Vector3i((int) currPos.x, (int) currPos.y, (int) currPos.z));
                }
                else {
                    Vector3f off = new Vector3f(currPos).sub(new Vector3f((int) currPos.x + 0.5f, (int) currPos.y + 0.5f, (int) currPos.z + 0.5f));
                    Vector3i offB = new Vector3i();
                    float[] offA = {
                            off.x,
                            off.y,
                            off.z
                    };
                    if (Math.abs(offA[0]) > Math.abs(offA[1]) && Math.abs(offA[0]) > Math.abs(offA[2])) {
                        offB.x = (int) Math.signum(offA[0]);
                    }
                    if (Math.abs(offA[1]) > Math.abs(offA[2]) && Math.abs(offA[1]) > Math.abs(offA[0])) {
                        offB.y = (int) Math.signum(offA[1]);
                    }
                    if (Math.abs(offA[2]) > Math.abs(offA[1]) && Math.abs(offA[2]) > Math.abs(offA[0])) {
                        offB.z = (int) Math.signum(offA[2]);
                    }

                    World.setBlockAt((int) currPos.x + offB.x, (int) currPos.y + offB.y, (int) currPos.z + offB.z, block);
                }
                break;
            }
            else
                currPos.add(new Vector3f(result).div(100));
        }
    }
}
