package advent2024;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Day4 {

    private static class Delta {
        private final int dr;
        private final int dc;

        public Delta(int dr, int dc) {
            this.dr = dr;
            this.dc = dc;
        }
    }

    private static final List<Delta> DELTAS = Arrays.asList(
            new Delta(-1, -1),
            new Delta(-1, 0),
            new Delta(-1, 1),
            new Delta(0, -1),
            new Delta(0, 1),
            new Delta(1, -1),
            new Delta(1, 0),
            new Delta(1, 1));

    public static int part1() throws IOException {
        List<String> rows = Utils.loadFile("day4.txt");
        char[][] grid = new char[140][140];
        for (int y = 0; y < rows.size(); y++) {
            String row = rows.get(y);
            for (int x = 0; x < row.length(); x++) {
                grid[y][x] = row.charAt(x);
            }
        }

        int count = 0;
        for (int r = 0; r < 140; r++) {
            for (int c = 0; c < 140; c++) {
                if (grid[r][c] != 'X') {
                    continue;
                }

                System.out.println("X at (" + r + "," + c + ")");
                for (Delta d : DELTAS) {
                    for (int i = 1; i < 4; i++) {
                        int nr = r + (i * d.dr);
                        int nc = c + (i * d.dc);
                        if (nr < 0 || nc < 0 || nr >= 140 || nc >= 140 || grid[nr][nc] != "XMAS".charAt(i)) {
                            break;
                        }
                        System.out.println("(" + r + "," + c + "): " + i + " (" + nr + "," + nc + ")");
                        if (i == 3) {
                            count += 1;
                        }
                    }
                }
            }
        }

        return count;
    }

    public static int part2() throws IOException {
        List<String> rows = Utils.loadFile("day4.txt");
        char[][] grid = new char[140][140];
        for (int y = 0; y < rows.size(); y++) {
            String row = rows.get(y);
            for (int x = 0; x < row.length(); x++) {
                grid[y][x] = row.charAt(x);
            }
        }

        int count = 0;

        for (int r = 1; r < 139; r++) {
            for (int c = 1; c < 139; c++) {
                if (grid[r][c] != 'A') {
                    continue;
                }

                if ((grid[r - 1][c - 1] == 'M' && grid[r - 1][c + 1] == 'M' && grid[r + 1][c - 1] == 'S'
                        && grid[r + 1][c + 1] == 'S') || (grid[r - 1][c - 1] == 'S' && grid[r - 1][c + 1] == 'M' && grid[r + 1][c - 1] == 'S'
                        && grid[r + 1][c + 1] == 'M') || (grid[r - 1][c - 1] == 'S' && grid[r - 1][c + 1] == 'S' && grid[r + 1][c - 1] == 'M'
                        && grid[r + 1][c + 1] == 'M') || (grid[r - 1][c - 1] == 'M' && grid[r - 1][c + 1] == 'S' && grid[r + 1][c - 1] == 'M'
                        && grid[r + 1][c + 1] == 'S')) {
                    count += 1;
                }
            }
        }

        return count;
    }
}
