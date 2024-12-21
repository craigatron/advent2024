package advent2024;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day20 {

    private record Point(int r, int c) {
    }

    private record State(Point p, int d) {
    }

    private static int minDistance(boolean[][] grid, Point start, Point end, int maxDistance) {
        Set<Point> visited = new HashSet<>();
        List<State> queue = new ArrayList<>();
        State initial = new State(start, 0);
        visited.add(start);
        queue.add(initial);

        int minDistance = Integer.MAX_VALUE;
        while (!queue.isEmpty()) {
            State s = queue.removeFirst();

            if (s.d > maxDistance) {
                break;
            }

            if (s.p.equals(end)) {
                minDistance = s.d;
                break;
            }

            for (Point nextP : new Point[] {new Point(s.p.r - 1, s.p.c),
                    new Point(s.p.r + 1, s.p.c), new Point(s.p.r, s.p.c - 1),
                    new Point(s.p.r, s.p.c + 1)}) {
                if (grid[nextP.r][nextP.c] && !visited.contains(nextP)) {
                    visited.add(nextP);
                    queue.add(new State(nextP, s.d + 1));
                }
            }
        }
        return minDistance;
    }

    public static long part1() throws IOException {
        List<String> rows = Utils.loadFile("day20.txt");

        int grid[][] = new int[rows.size()][rows.size()];

        Point start = null;
        Point end = null;

        for (int r = 0; r < rows.size(); r++) {
            for (int c = 0; c < rows.size(); c++) {
                char ch = rows.get(r).charAt(c);
                if (ch == '#') {
                    grid[r][c] = -1;
                } else {
                    if (ch == 'S') {
                        start = new Point(r, c);
                        grid[r][c] = 0;
                    } else if (ch == 'E') {
                        end = new Point(r, c);
                        grid[r][c] = Integer.MAX_VALUE;
                    }
                    grid[r][c] = Integer.MAX_VALUE;
                }
            }
        }

        int[][] gridCopy = Arrays.stream(grid).map(int[]::clone).toArray(int[][]::new);
        bfs(gridCopy, start, end);

        int minDistance = gridCopy[end.r][end.c];

        int total = 0;
        for (int r = 1; r < rows.size() - 1; r++) {
            for (int c = 1; c < rows.size() - 1; c++) {
                if (grid[r][c] != -1) {
                    continue;
                }

                gridCopy = Arrays.stream(grid).map(int[]::clone).toArray(int[][]::new);
                gridCopy[r][c] = Integer.MAX_VALUE;

                bfs(gridCopy, start, end);

                if (gridCopy[end.r][end.c] <= minDistance - 100) {
                    total += 1;
                }
            }
        }

        return total;
    }

    private static void bfs(int grid[][], Point start, Point end) {
        grid[start.r][start.c] = 0;

        List<Point> queue = new ArrayList<>();
        queue.add(start);

        while (!queue.isEmpty()) {
            Point p = queue.removeFirst();

            if (p.equals(end)) {
                return;
            }

            for (Point nextP : new Point[] {new Point(p.r - 1, p.c), new Point(p.r + 1, p.c),
                    new Point(p.r, p.c - 1), new Point(p.r, p.c + 1)}) {
                if (grid[nextP.r][nextP.c] == Integer.MAX_VALUE) {
                    grid[nextP.r][nextP.c] = grid[p.r][p.c] + 1;
                    queue.add(nextP);
                }
            }
        }
    }

    public static long part2() throws IOException {
        List<String> rows = Utils.loadFile("day20.txt");

        int startGrid[][] = new int[rows.size()][rows.size()];
        int endGrid[][] = new int[rows.size()][rows.size()];

        Point start = null;
        Point end = null;

        for (int r = 0; r < rows.size(); r++) {
            for (int c = 0; c < rows.size(); c++) {
                char ch = rows.get(r).charAt(c);
                if (ch == '#') {
                    startGrid[r][c] = -1;
                } else {
                    if (ch == 'S') {
                        start = new Point(r, c);
                    } else if (ch == 'E') {
                        end = new Point(r, c);
                    }
                    startGrid[r][c] = Integer.MAX_VALUE;
                }
                endGrid[r][c] = startGrid[r][c];
            }
        }

        bfs(startGrid, start, null);
        bfs(endGrid, end, null);

        int minDistance = startGrid[end.r][end.c];

        int total = 0;
        for (int r = 1; r < rows.size() - 1; r++) {
            for (int c = 1; c < rows.size() - 1; c++) {
                if (startGrid[r][c] == -1) {
                    continue;
                }

                for (int dr = -20; dr <= 20; dr++) {
                    int maxDc = 20 - Math.abs(dr);
                    int minDc = -maxDc;
                    for (int dc = minDc; dc <= maxDc; dc++) {
                        if (dr == 0 && dc == 0) {
                            continue;
                        }
                        int newR = r + dr;
                        int newC = c + dc;
                        if (newR < 0 || newR >= rows.size() || newC < 0 || newC >= rows.size()
                                || startGrid[newR][newC] == -1) {
                            // invalid jump end, skip
                            continue;
                        }
                        if (startGrid[r][c] + endGrid[newR][newC] + Math.abs(dr)
                                + Math.abs(dc) <= minDistance - 100) {
                            total += 1;
                        }
                    }
                }
            }
        }


        return total;
    }
}
