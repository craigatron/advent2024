package advent2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public final class Utils {
    private static final String BASE_DIR = "data/";

    private Utils() {}

    public static List<String> loadFile(String file) throws IOException {
        return Files.readAllLines(Paths.get(BASE_DIR + file));
    }
}
