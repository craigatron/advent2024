package advent2024;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

public class Day8 {

    private record Point(int row, int col) {
    }

    public static int part1() throws IOException {
        List<String> rows = Utils.loadFile("day8.txt");

        Map<Character, List<Point>> antennae = new HashMap<>();
        ListIterator<String> rowIterator = rows.listIterator();
        while (rowIterator.hasNext()) {
            int rowIndex = rowIterator.nextIndex();
            String row = rowIterator.next();
            for (int c = 0; c < row.length(); c++) {
                char ch = row.charAt(c);
                if (ch == '.') {
                    continue;
                }
                if (!antennae.containsKey(ch)) {
                    antennae.put(ch, new ArrayList<>());
                }
                antennae.get(ch).add(new Point(rowIndex, c));
            }
        }

        Set<Point> antinodes = new HashSet<>();
        for (Map.Entry<Character, List<Point>> entry : antennae.entrySet()) {
            List<Point> ants = entry.getValue();
            for (int i = 0; i < ants.size() - 1; i++) {
                Point p1 = ants.get(i);
                for (int j = i + 1; j < ants.size(); j++) {
                    Point p2 = ants.get(j);

                    int dr = p2.row - p1.row;
                    int dc = p2.col - p1.col;

                    Point[] nextPoints = new Point[] {new Point(p1.row - dr, p1.col - dc),
                            new Point(p2.row + dr, p2.col + dc)};

                    for (Point p : nextPoints) {
                        if (p.row >= 0 && p.row < rows.size() && p.col >= 0
                                && p.col < rows.size()) {
                            antinodes.add(p);
                        }
                    }
                }
            }
        }
        return antinodes.size();
    }

    public static int part2() throws IOException {
        List<String> rows = Utils.loadFile("day8.txt");

        Map<Character, List<Point>> antennae = new HashMap<>();
        ListIterator<String> rowIterator = rows.listIterator();
        while (rowIterator.hasNext()) {
            int rowIndex = rowIterator.nextIndex();
            String row = rowIterator.next();
            for (int c = 0; c < row.length(); c++) {
                char ch = row.charAt(c);
                if (ch == '.') {
                    continue;
                }
                if (!antennae.containsKey(ch)) {
                    antennae.put(ch, new ArrayList<>());
                }
                antennae.get(ch).add(new Point(rowIndex, c));
            }
        }

        Set<Point> antinodes = new HashSet<>();
        for (Map.Entry<Character, List<Point>> entry : antennae.entrySet()) {
            List<Point> ants = entry.getValue();
            for (int i = 0; i < ants.size() - 1; i++) {
                Point p1 = ants.get(i);
                antinodes.add(p1);
                for (int j = i + 1; j < ants.size(); j++) {
                    Point p2 = ants.get(j);

                    int dr = p2.row - p1.row;
                    int dc = p2.col - p1.col;

                    Point nextP = p2;
                    while (nextP.col >= 0 && nextP.col < rows.size() && nextP.row >= 0
                            && nextP.row < rows.size()) {
                        antinodes.add(nextP);
                        nextP = new Point(nextP.row + dr, nextP.col + dc);
                    }

                    nextP = p2;
                    while (nextP.col >= 0 && nextP.col < rows.size() && nextP.row >= 0
                            && nextP.row < rows.size()) {
                        antinodes.add(nextP);
                        nextP = new Point(nextP.row - dr, nextP.col - dc);
                    }
                }
            }
        }
        return antinodes.size();
    }
}
