package matyk.engine.utils;

public class Utils {
    public static <T> T copyAndReturn(T in, T out) {
        out = in;
        return in;
    }
}
