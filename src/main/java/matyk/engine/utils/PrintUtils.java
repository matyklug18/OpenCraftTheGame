package matyk.engine.utils;

import matyk.engine.data.Block;
import org.joml.Vector3f;

public class PrintUtils {
    public static <T> T printAndReturn(T obj) {
        System.out.println(obj);
        return obj;
    }

    public static void printVec3f(Vector3f vec) {
        System.out.println(vec.x + ", " + vec.y + ", " + vec.z);
    }
}
