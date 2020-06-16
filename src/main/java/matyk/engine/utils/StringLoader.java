package matyk.engine.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class StringLoader {

    /**
     * make a stream out of anything
     * @param path the path to the resource
     * @return the stream
     */
    public static InputStream loadResourceAsStream(String path) {
        return StringLoader.class.getClassLoader().getResourceAsStream(path);
    }

    /**
     * read any text file
     * @param path path to the text file
     * @return the text file as an string
     */
    public static String loadResourceAsString(String path) {
        return new BufferedReader(new InputStreamReader(loadResourceAsStream(path))).lines().collect(Collectors.joining("\n"));
    }
}