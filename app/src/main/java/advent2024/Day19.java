package advent2024;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day19 {

    private static List<String> solvableTowels(List<String> baseTowels, List<String> towels) {
        List<String> solvableTowels = new ArrayList<>();

        rowLoop: for (String row : towels) {
            List<String> search = new ArrayList<>();
            Set<String> seen = new HashSet<>();
            search.add(row);

            while (!search.isEmpty()) {
                String next = search.removeLast();

                for (String towel : baseTowels) {
                    if (next.startsWith(towel)) {
                        String nextTowel = next.substring(towel.length());
                        if (nextTowel.isEmpty()) {
                            solvableTowels.add(row);
                            continue rowLoop;
                        }
                        if (seen.add(nextTowel)) {
                            search.add(nextTowel);
                        }
                    }
                }
            }
        }

        return solvableTowels;
    }

    public static long part1() throws IOException {
        List<String> rows = Utils.loadFile("day19.txt");
        List<String> towels = Arrays.asList(rows.get(0).split(", "));

        return solvableTowels(towels, rows.subList(2, rows.size())).size();
    }

    private static long countPossibles(String towel, List<String> baseTowels,
            Map<String, Long> cache) {
        if (towel.isEmpty()) {
            return 1;
        }
        if (cache.containsKey(towel)) {
            return cache.get(towel);
        }

        long total = 0;
        for (String bt : baseTowels) {
            if (towel.startsWith(bt)) {
                total += countPossibles(towel.substring(bt.length()), baseTowels, cache);
            }
        }
        cache.put(towel, total);
        return total;
    }

    public static long part2() throws IOException {
        List<String> rows = Utils.loadFile("day19.txt");
        List<String> towels = Arrays.asList(rows.get(0).split(", "));

        long total = 0;
        for (String row : solvableTowels(towels, rows.subList(2, rows.size()))) {
            System.out.println(row);
            total += countPossibles(row, towels, new HashMap<>());
        }

        return total;
    }
}
