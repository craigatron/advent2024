package advent2024;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day7 {

    private record SearchEntry(int index, long total) {
    }

    public static long part1() throws IOException {
        List<String> rows = Utils.loadFile("day7.txt");

        long count = 0;
        for (String r : rows) {
            String[] parts = r.split(": ");
            Long target = Long.parseLong(parts[0]);
            List<Long> numbers =
                    Arrays.stream(parts[1].split("\s+")).map(i -> Long.parseLong(i)).toList();

            List<SearchEntry> search = new ArrayList<>();
            search.add(new SearchEntry(1, numbers.getFirst()));

            boolean matchFound = false;
            while (!search.isEmpty()) {
                SearchEntry entry = search.removeFirst();

                if (entry.index == numbers.size() && entry.total == target) {
                    matchFound = true;
                    break;
                } else if (entry.index < numbers.size()) {
                    search.add(new SearchEntry(entry.index + 1,
                            entry.total + numbers.get(entry.index)));
                    search.add(new SearchEntry(entry.index + 1,
                            entry.total * numbers.get(entry.index)));
                }
            }
            if (matchFound) {
                count += target;
            }
        }

        return count;
    }

    public static long part2() throws IOException {
        List<String> rows = Utils.loadFile("day7.txt");

        long count = 0;
        for (String r : rows) {
            String[] parts = r.split(": ");
            Long target = Long.parseLong(parts[0]);
            List<Long> numbers =
                    Arrays.stream(parts[1].split("\s+")).map(i -> Long.parseLong(i)).toList();

            List<SearchEntry> search = new ArrayList<>();
            search.add(new SearchEntry(1, numbers.getFirst()));

            boolean matchFound = false;
            while (!search.isEmpty()) {
                SearchEntry entry = search.removeFirst();

                if (entry.index == numbers.size() && entry.total == target) {
                    matchFound = true;
                    break;
                } else if (entry.total <= target && entry.index < numbers.size()) {
                    search.add(new SearchEntry(entry.index + 1,
                            entry.total + numbers.get(entry.index)));
                    search.add(new SearchEntry(entry.index + 1,
                            entry.total * numbers.get(entry.index)));
                    search.add(new SearchEntry(entry.index + 1, Long.parseLong(
                            Long.toString(entry.total) + numbers.get(entry.index).toString())));
                }
            }
            if (matchFound) {
                count += target;
            }
        }

        return count;
    }
}
