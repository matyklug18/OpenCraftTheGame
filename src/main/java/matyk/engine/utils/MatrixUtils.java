package matyk.engine.utils;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class MatrixUtils {

    /**
     * make a model matrix
     * @param translation the position
     * @param rotation the rotation
     * @param scale the scale
     * @return the matrix
     */
    public static Matrix4f transformationMatrix(Vector3f translation, Vector3f rotation, Vector3f scale) {
        rotation = rotation.mul((float) (Math.PI / 180.0f), new Vector3f());
        Matrix4f matrix = new Matrix4f();
        return matrix.translate(translation).rotateXYZ(rotation).scale(scale);
    }

    /**
     * make a projection matric
     * @param fov the fov
     * @param aspectRatio the aspect ratio (w/h)
     * @param zNear the near plane
     * @param zFar the far plane
     * @return the matrix
     */
    public static Matrix4f projectionMatrix(float fov, float aspectRatio, float zNear, float zFar) {
        return new Matrix4f().perspective((float) Math.toRadians(fov), aspectRatio, zNear, zFar);
    }

    /**
     * make a view matrix
     * @param position the position
     * @param rotation the rotation
     * @return the matrix
     */
    public static Matrix4f viewMatrix(Vector3f position, Vector3f rotation) {
        rotation = rotation.mul((float) (Math.PI / 180.0f), new Vector3f());
        return new Matrix4f().rotateXYZ(rotation).translate(position.negate(new Vector3f()));
    }

}