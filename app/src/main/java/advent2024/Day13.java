package advent2024;

import java.io.IOException;
import java.util.List;

public class Day13 {

    private record Pair(long x, long y) {
    }

    private static Pair parseButton(String row) {
        String[] parts = row.split(", ");
        String[] xParts = parts[0].split("\\+");
        String[] yParts = parts[1].split("\\+");
        return new Pair(Long.parseLong(xParts[1]), Long.parseLong(yParts[1]));
    }

    private static Pair parseTotal(String row) {
        String[] parts = row.split("=");
        String[] xParts = parts[1].split(", ");
        return new Pair(Long.parseLong(xParts[0]), Long.parseLong(parts[2]));
    }

    public static long part1() throws IOException {
        List<String> rows = Utils.loadFile("day13.txt");

        long total = 0;
        int row = 0;
        while (row < rows.size()) {
            Pair buttonA = parseButton(rows.get(row));
            Pair buttonB = parseButton(rows.get(row + 1));
            Pair totals = parseTotal(rows.get(row + 2));

            long num = (buttonA.x * totals.y) - (buttonA.y * totals.x);
            long den = (buttonA.x * buttonB.y) - (buttonA.y * buttonB.x);

            if (num % den == 0) {
                long b = num / den;
                long a = (totals.x - (buttonB.x * b)) / buttonA.x;
                total += ((3 * a) + b);
            }

            row += 4;
        }

        return total;
    }

    public static long part2() throws IOException {
        List<String> rows = Utils.loadFile("day13.txt");

        long total = 0;
        int row = 0;
        int system = 0;
        while (row < rows.size()) {
            Pair buttonA = parseButton(rows.get(row));
            Pair buttonB = parseButton(rows.get(row + 1));
            Pair totals = parseTotal(rows.get(row + 2));
            Pair actualTotals = new Pair(totals.x + 10000000000000L, totals.y + 10000000000000L);

            long num = (buttonA.x * actualTotals.y) - (buttonA.y * actualTotals.x);
            long den = (buttonA.x * buttonB.y) - (buttonA.y * buttonB.x);

            if (num % den == 0) {
                long b = num / den;
                long aNum = actualTotals.x - (buttonB.x * b);
                if (aNum % buttonA.x == 0) {
                    long a = aNum / buttonA.x;
                    total += ((3 * a) + b);
                }
            }
            system += 1;

            row += 4;
        }

        return total;
    }
}
