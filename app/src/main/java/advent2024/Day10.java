package advent2024;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day10 {

    private record Point(int r, int c) {
    }

    private static Point[] DELTAS =
            new Point[] {new Point(0, 1), new Point(0, -1), new Point(1, 0), new Point(-1, 0)};

    public static long part1() throws IOException {
        List<String> rows = Utils.loadFile("day10.txt");

        int[][] grid = new int[41][41];
        List<Point> zeros = new ArrayList<>();
        for (int r = 0; r < rows.size(); r++) {
            String row = rows.get(r);

            for (int c = 0; c < row.length(); c++) {
                int val = Integer.parseInt(Character.toString(row.charAt(c)));
                grid[r][c] = val;
                if (val == 0) {
                    zeros.add(new Point(r, c));
                }
            }
        }

        int total = 0;
        for (Point startPos : zeros) {
            List<Point> searchStates = new ArrayList<>();
            Set<Point> nines = new HashSet<>();
            searchStates.add(startPos);

            while (!searchStates.isEmpty()) {
                Point p = searchStates.removeFirst();
                int elevation = grid[p.r][p.c];

                if (elevation == 9) {
                    nines.add(p);
                    continue;
                }

                for (Point d : DELTAS) {
                    int newR = p.r + d.r;
                    int newC = p.c + d.c;
                    if (newR < 0 || newR >= 41 || newC < 0 || newC >= 41
                            || grid[newR][newC] != elevation + 1) {
                        continue;
                    }
                    searchStates.add(new Point(newR, newC));
                }

            }
            total += nines.size();
        }

        return total;
    }

    public static long part2() throws IOException {
        List<String> rows = Utils.loadFile("day10.txt");

        int[][] grid = new int[41][41];
        List<Point> zeros = new ArrayList<>();
        for (int r = 0; r < rows.size(); r++) {
            String row = rows.get(r);

            for (int c = 0; c < row.length(); c++) {
                int val = Integer.parseInt(Character.toString(row.charAt(c)));
                grid[r][c] = val;
                if (val == 0) {
                    zeros.add(new Point(r, c));
                }
            }
        }

        int total = 0;
        for (Point startPos : zeros) {
            List<Point> searchStates = new ArrayList<>();
            searchStates.add(startPos);
            int rating = 0;

            while (!searchStates.isEmpty()) {
                Point p = searchStates.removeFirst();
                int elevation = grid[p.r][p.c];

                if (elevation == 9) {
                    rating += 1;
                    continue;
                }

                for (Point d : DELTAS) {
                    int newR = p.r + d.r;
                    int newC = p.c + d.c;
                    if (newR < 0 || newR >= 41 || newC < 0 || newC >= 41
                            || grid[newR][newC] != elevation + 1) {
                        continue;
                    }
                    searchStates.add(new Point(newR, newC));
                }

            }
            total += rating;
        }

        return total;
    }
}
