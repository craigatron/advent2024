package advent2024;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day12 {

    private record Point(int r, int c) {
    }

    private static Set<Point> findRegion(Point p, char[][] grid) {
        Set<Point> region = new HashSet<>();

        char ch = grid[p.r][p.c];

        Set<Point> seen = new HashSet<>();
        List<Point> queue = new ArrayList<>();
        queue.add(p);

        while (!queue.isEmpty()) {
            Point next = queue.removeFirst();
            seen.add(next);
            if (next.r < 0 || next.r >= grid.length || next.c < 0 || next.c >= grid.length
                    || grid[next.r][next.c] != ch || region.contains(next)) {
                continue;
            }

            region.add(next);

            for (Point adj : new Point[] {new Point(next.r + 1, next.c),
                    new Point(next.r - 1, next.c), new Point(next.r, next.c + 1),
                    new Point(next.r, next.c - 1)}) {
                if (!seen.contains(adj)) {
                    queue.add(adj);
                }
            }

        }

        return region;
    }

    private static int calculatePrice(Set<Point> region, char[][] grid) {
        int perimeter = 0;

        for (Point p : region) {
            char ch = grid[p.r][p.c];

            for (Point adj : new Point[] {new Point(p.r + 1, p.c), new Point(p.r - 1, p.c),
                    new Point(p.r, p.c + 1), new Point(p.r, p.c - 1)}) {
                if (adj.r < 0 || adj.r >= grid.length || adj.c < 0 || adj.c >= grid.length
                        || grid[adj.r][adj.c] != ch) {
                    perimeter += 1;
                }
            }
        }

        return perimeter * region.size();
    }

    public static long part1() throws IOException {
        List<String> rows = Utils.loadFile("day12.txt");

        char[][] grid = new char[rows.size()][rows.size()];
        for (int r = 0; r < rows.size(); r++) {
            grid[r] = rows.get(r).toCharArray();
        }

        Set<Point> allSeenPoints = new HashSet<>();

        int total = 0;
        for (int r = 0; r < rows.size(); r++) {
            for (int c = 0; c < rows.size(); c++) {
                Point p = new Point(r, c);
                if (!allSeenPoints.contains(p)) {
                    Set<Point> region = findRegion(p, grid);
                    allSeenPoints.addAll(region);
                    total += calculatePrice(region, grid);
                }
            }
        }

        return total;
    }

    private static int calculateAnnoyingPrice(Set<Point> region, char[][] grid) {
        int area = region.size();

        int minR =
                region.stream().mapToInt(p -> p.r).min().orElseThrow(IllegalArgumentException::new);
        int maxR =
                region.stream().mapToInt(p -> p.r).max().orElseThrow(IllegalArgumentException::new);
        int minC =
                region.stream().mapToInt(p -> p.c).min().orElseThrow(IllegalArgumentException::new);
        int maxC =
                region.stream().mapToInt(p -> p.c).max().orElseThrow(IllegalArgumentException::new);

        int edges = 0;

        // look up
        for (int r = minR; r <= maxR; r++) {
            boolean onEdge = false;
            for (int c = minC; c <= maxC; c++) {
                if (region.contains(new Point(r, c)) && !region.contains(new Point(r - 1, c))) {
                    if (!onEdge) {
                        onEdge = true;
                    }
                } else if (onEdge) {
                    onEdge = false;
                    edges += 1;
                }

            }
            if (onEdge) {
                edges += 1;
            }
        }
        // look right
        for (int c = minC; c <= maxC; c++) {
            boolean onEdge = false;
            for (int r = minR; r <= maxR; r++) {
                if (region.contains(new Point(r, c)) && !region.contains(new Point(r, c + 1))) {
                    if (!onEdge) {
                        onEdge = true;
                    }
                } else if (onEdge) {
                    onEdge = false;
                    edges += 1;
                }

            }
            if (onEdge) {
                edges += 1;
            }
        }
        // look down
        for (int r = minR; r <= maxR; r++) {
            boolean onEdge = false;
            for (int c = minC; c <= maxC; c++) {
                if (region.contains(new Point(r, c)) && !region.contains(new Point(r + 1, c))) {
                    if (!onEdge) {
                        onEdge = true;
                    }
                } else if (onEdge) {
                    onEdge = false;
                    edges += 1;
                }

            }
            if (onEdge) {
                edges += 1;
            }
        }
        // look left
        for (int c = minC; c <= maxC; c++) {
            boolean onEdge = false;
            for (int r = minR; r <= maxR; r++) {
                if (region.contains(new Point(r, c)) && !region.contains(new Point(r, c - 1))) {
                    if (!onEdge) {
                        onEdge = true;
                    }
                } else if (onEdge) {
                    onEdge = false;
                    edges += 1;
                }

            }
            if (onEdge) {
                edges += 1;
            }
        }

        return edges * area;
    }

    public static long part2() throws IOException {
        List<String> rows = Utils.loadFile("day12.txt");

        char[][] grid = new char[rows.size()][rows.size()];
        for (int r = 0; r < rows.size(); r++) {
            grid[r] = rows.get(r).toCharArray();
        }

        Set<Point> allSeenPoints = new HashSet<>();

        int total = 0;
        for (int r = 0; r < rows.size(); r++) {
            for (int c = 0; c < rows.size(); c++) {
                Point p = new Point(r, c);
                if (!allSeenPoints.contains(p)) {
                    Set<Point> region = findRegion(p, grid);
                    allSeenPoints.addAll(region);
                    total += calculateAnnoyingPrice(region, grid);
                }
            }
        }

        return total;
    }
}
