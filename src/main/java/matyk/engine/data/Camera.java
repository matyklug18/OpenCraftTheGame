package matyk.engine.data;

import org.joml.*;

public class Camera {

    public Vector3f pos = new Vector3f();
    public Vector3f rot = new Vector3f();

    public void setRot(Vector3f rot) {
        this.rot = rot;
    }

    public void setPos(Vector3f pos) {
        this.pos = pos;
    }

    /**
     * move the camera by the offset
     * @param offset the offset to the camera by
     */
    public void move(Vector3f offset) {
        this.pos.add(offset);
    }

    /**
     * look at a specific point
     * @param posToLook where to look
     */
    public void lookAt(Vector3f posToLook) {
        Matrix4f rotM = new Matrix4f().lookAt(pos, posToLook, new Vector3f(0,1,0));
        Quaternionf rotQ = new Quaternionf();
        rotM.getNormalizedRotation(rotQ);
        Vector3f rotV = new Vector3f();
        rotQ.getEulerAnglesXYZ(rotV);
        this.rot = rotV;
    }

}
