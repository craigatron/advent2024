package advent2024;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day18 {

    private record Point(int r, int c) {
    }

    private record State(Point p, int d) {
    }

    public static int part1() throws IOException {
        List<String> rows = Utils.loadFile("day18.txt");
        Set<Point> bytes = rows.subList(0, 1024).stream().map(s -> s.split(","))
                .map(p -> new Point(Integer.parseInt(p[0]), Integer.parseInt(p[1])))
                .collect(Collectors.toSet());

        Set<Point> visited = new HashSet<>();
        visited.add(new Point(0, 0));

        List<State> states = new ArrayList<>();
        states.add(new State(new Point(0, 0), 0));

        while (!states.isEmpty()) {
            State state = states.removeFirst();

            if (state.p.equals(new Point(70, 70))) {
                return state.d;
            }

            List<Point> nextSteps = Arrays.asList(new Point(state.p.r - 1, state.p.c),
                    new Point(state.p.r + 1, state.p.c), new Point(state.p.r, state.p.c - 1),
                    new Point(state.p.r, state.p.c + 1));

            for (Point p : nextSteps) {
                if (!visited.contains(p) && !bytes.contains(p) && p.r >= 0 && p.r <= 70 && p.c >= 0
                        && p.c <= 70) {
                    states.add(new State(p, state.d + 1));
                    visited.add(p);
                }
            }
        }

        return 0;
    }

    public static String part2() throws IOException {
        List<String> rows = Utils.loadFile("day18.txt");

        int byteIndex = 1024;

        while (true) {
            Set<Point> bytes = rows.subList(0, byteIndex).stream().map(s -> s.split(","))
                    .map(p -> new Point(Integer.parseInt(p[0]), Integer.parseInt(p[1])))
                    .collect(Collectors.toSet());

            Set<Point> visited = new HashSet<>();
            visited.add(new Point(0, 0));

            List<State> states = new ArrayList<>();
            states.add(new State(new Point(0, 0), 0));

            boolean found = false;
            while (!states.isEmpty()) {
                State state = states.removeFirst();

                if (state.p.equals(new Point(70, 70))) {
                    System.out.println("index: " + byteIndex + ": " + state.d);
                    found = true;
                    break;
                }

                List<Point> nextSteps = Arrays.asList(new Point(state.p.r - 1, state.p.c),
                        new Point(state.p.r + 1, state.p.c), new Point(state.p.r, state.p.c - 1),
                        new Point(state.p.r, state.p.c + 1));

                for (Point p : nextSteps) {
                    if (!visited.contains(p) && !bytes.contains(p) && p.r >= 0 && p.r <= 70
                            && p.c >= 0 && p.c <= 70) {
                        states.add(new State(p, state.d + 1));
                        visited.add(p);
                    }
                }
            }

            if (!found) {
                return rows.get(byteIndex - 1);
            }

            byteIndex += 1;
        }
    }
}
