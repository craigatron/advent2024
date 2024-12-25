package advent2024;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Day25 {

    public static long part1() throws IOException {
        List<String> rows = Utils.loadFile("day25.txt");

        int r = 0;

        List<int[]> keys = new ArrayList<>();
        List<int[]> locks = new ArrayList<>();

        while (r < rows.size()) {
            List<String> schematic = rows.subList(r, r + 7);

            int[] heights = new int[5];
            for (int i = 0; i < 5; i++) {
                int iCopy = i;
                heights[i] = (int) schematic.stream().map(s -> s.charAt(iCopy))
                        .filter(c -> c == '#').count() - 1;
            }
            if (schematic.get(0).equals("#####")) {
                locks.add(heights);
            } else {
                keys.add(heights);
            }

            r += 8;
        }

        int total = 0;

        for (int[] k : keys) {
            for (int[] l : locks) {
                if (IntStream.range(0, 5).map(i -> k[i] + l[i]).allMatch(v -> v < 6)) {
                    total += 1;
                }
            }
        }

        return total;
    }

    public static long part2() throws IOException {
        List<String> rows = Utils.loadFile("day25.txt");

        return 0;
    }
}
