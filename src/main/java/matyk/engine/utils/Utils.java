package matyk.engine.utils;

public class Utils {

    public static <T> T forge(final Class<T> type) {
        try {
            return type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
