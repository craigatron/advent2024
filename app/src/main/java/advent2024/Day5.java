package advent2024;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Day5 {

    private static class Pair {
        final int x;
        final int y;
        public Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static int part1() throws IOException {
        List<String> rows = Utils.loadFile("day5.txt");

        List<Pair> rules = new ArrayList<>();
        List<List<Integer>> pageList = new ArrayList<>();

        for (String r : rows) {
            if (r.contains("|")) {
                String[] parts = r.split("\\|");
                rules.add(new Pair(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
            }
            if (r.contains(",")) {
                String[] parts = r.split(",");
                List<Integer> pages = new ArrayList<>();
                for (String p : parts) {
                    pages.add(Integer.parseInt(p));
                }
                pageList.add(pages);
            }
        }

        int total = 0;
        for (List<Integer> pages : pageList) {
            boolean correct = true;
            for (Pair rule : rules) {
                if (!pages.contains(rule.x) || !pages.contains(rule.y)) {
                    continue;
                }
                if (pages.indexOf(rule.x) > pages.indexOf(rule.y)) {
                    correct = false;
                    break;
                }
            }

            if (correct) {
                total += pages.get(pages.size() / 2);
            }
        }

        return total;
    }

    public static int part2() throws IOException {
        List<String> rows = Utils.loadFile("day5.txt");

        List<Pair> rules = new ArrayList<>();
        List<List<Integer>> pageList = new ArrayList<>();

        for (String r : rows) {
            if (r.contains("|")) {
                String[] parts = r.split("\\|");
                rules.add(new Pair(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
            }
            if (r.contains(",")) {
                String[] parts = r.split(",");
                List<Integer> pages = new ArrayList<>();
                for (String p : parts) {
                    pages.add(Integer.parseInt(p));
                }
                pageList.add(pages);
            }
        }

        Comparator<Integer> comparator = new Comparator<Integer>() {
            public int compare(Integer a, Integer b) {
                for (Pair p : rules) {
                    if (p.x == a && p.y == b) {
                        return -1;
                    } else if (p.x == b && p.y == a) {
                        return 1;
                    }
                }
                throw new RuntimeException("no entry for values: " + a + " and " + b);
            }
        };

        int total = 0;

        for (List<Integer> pages : pageList) {
            List<Integer> copy = new ArrayList<>(pages);
            pages.sort(comparator);
            if (!copy.equals(pages)) {
                total += pages.get(pages.size() / 2);
            }
        }

        return total;
    }
}
