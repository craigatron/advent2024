package advent2024;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day1 {
    public static int part1() throws IOException {
        List<String> input = Utils.loadFile("day1.txt");

        List<Integer> left = new ArrayList<>();
        List<Integer> right = new ArrayList<>();

        for (String line : input) {
            var parts = line.split("\s+");
            left.add(Integer.parseInt(parts[0]));
            right.add(Integer.parseInt(parts[1]));
        }

        Collections.sort(left);
        Collections.sort(right);

        int total = 0;
        for (int i = 0; i < left.size(); i++) {
            total += Math.abs(left.get(i) - right.get(i));
        }
        return total;
    }

    public static int part2() throws IOException {
        List<String> input = Utils.loadFile("day1.txt");

        List<Integer> left = new ArrayList<>();
        List<Integer> right = new ArrayList<>();

        for (String line : input) {
            var parts = line.split("\s+");
            left.add(Integer.parseInt(parts[0]));
            right.add(Integer.parseInt(parts[1]));
        }

        Map<Integer, Long> rightCounts = right.stream().collect(Collectors.groupingBy(i -> i, Collectors.counting()));
        
        long total = 0;
        for (Integer i : left) {
            total += (i * rightCounts.getOrDefault(i, 0L));
        }

        return (int) total;
    }
}
