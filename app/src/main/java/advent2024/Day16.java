package advent2024;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day16 {

    private enum Direction {
        NORTH, EAST, SOUTH, WEST;
    }

    private record Point(int r, int c) {
    }

    private record State(Point p, Direction d) {
    }

    private record SearchState(State s, int score) {
    }

    private record Result1(Map<State, Integer> minScores, int minScore) {
    }

    public static int part1() throws IOException {
        return part1Internal().minScore;
    }

    private static Result1 part1Internal() throws IOException {
        List<String> rows = Utils.loadFile("testday16.txt");

        Point start = null;
        Point end = null;

        boolean[][] grid = new boolean[141][141];
        Map<State, Integer> minScores = new HashMap<>();

        for (int r = 0; r < rows.size(); r++) {
            char[] chars = rows.get(r).toCharArray();
            for (int c = 0; c < chars.length; c++) {
                char ch = chars[c];
                grid[r][c] = ch != '#';
                if (ch == 'S') {
                    start = new Point(r, c);
                } else if (ch == 'E') {
                    end = new Point(r, c);
                }
            }
        }

        List<SearchState> states = new ArrayList<>();
        states.add(new SearchState(new State(start, Direction.EAST), 0));

        int minScore = Integer.MAX_VALUE;
        while (!states.isEmpty()) {
            SearchState s = states.removeFirst();

            Integer minStateScore = minScores.get(s.s);
            if (minStateScore != null && minStateScore <= s.score) {
                continue;
            }
            minScores.put(s.s, s.score);
            if (s.s.p.equals(end)) {
                if (s.score < minScore) {
                    minScore = s.score;
                }
                continue;
            }

            // move
            Point nextP;
            if (s.s.d == Direction.NORTH) {
                nextP = new Point(s.s.p.r - 1, s.s.p.c);
            } else if (s.s.d == Direction.EAST) {
                nextP = new Point(s.s.p.r, s.s.p.c + 1);
            } else if (s.s.d == Direction.SOUTH) {
                nextP = new Point(s.s.p.r + 1, s.s.p.c);
            } else {
                nextP = new Point(s.s.p.r, s.s.p.c - 1);
            }

            State moveState = new State(nextP, s.s.d);
            if (grid[nextP.r][nextP.c]) {
                states.add(new SearchState(moveState, s.score + 1));
            }

            // turns
            List<Direction> nextDirs;
            if (s.s.d == Direction.NORTH || s.s.d == Direction.SOUTH) {
                nextDirs = Arrays.asList(Direction.EAST, Direction.WEST);
            } else {
                nextDirs = Arrays.asList(Direction.NORTH, Direction.SOUTH);
            }
            for (Direction d : nextDirs) {
                State newState = new State(s.s.p, d);
                states.add(new SearchState(newState, s.score + 1000));
            }
        }

        return new Result1(minScores, minScore);
    }

    private record SearchState2(State s, int score, Set<Point> visited) {
    }

    public static long part2() throws IOException {
        // oh no this is so dumb but I'm playing catchup so
        Result1 p1 = part1Internal();
        int lowestScore = p1.minScore;
        Map<State, Integer> minScores = p1.minScores;

        List<String> rows = Utils.loadFile("testday16.txt");

        Point start = null;
        Point end = null;

        boolean[][] grid = new boolean[141][141];

        for (int r = 0; r < rows.size(); r++) {
            char[] chars = rows.get(r).toCharArray();
            for (int c = 0; c < chars.length; c++) {
                char ch = chars[c];
                grid[r][c] = ch != '#';
                if (ch == 'S') {
                    start = new Point(r, c);
                } else if (ch == 'E') {
                    end = new Point(r, c);
                }
            }
        }

        Set<Point> shortestPathPoints = new HashSet<>();

        List<SearchState2> searchStates = new ArrayList<>();
        searchStates.add(new SearchState2(new State(start, Direction.EAST), 0,
                new HashSet<>(Collections.singleton(start))));
        Map<State, Integer> minStates = new HashMap<>();

        int iteration = 0;
        while (!searchStates.isEmpty()) {
            if (iteration++ % 50000 == 0) {
                System.out.println("iteration: " + (iteration - 1) + ": " + searchStates.size()
                        + ", top state: " + searchStates.get(searchStates.size() - 1).s
                        + ", score: " + searchStates.get(searchStates.size() - 1).score);
            }

            SearchState2 searchState = searchStates.removeLast();

            if (searchState.s.p.equals(end)) {
                System.out.println("found end");
                shortestPathPoints.addAll(searchState.visited);
                continue;
            }

            if (searchState.score > lowestScore) {
                continue;
            }

            Integer minStateScore = minStates.get(searchState.s);
            if (minStateScore != null && minStateScore < searchState.score) {
                continue;
            }
            minStates.put(searchState.s, searchState.score);

            // move straight
            Point nextP;
            if (searchState.s.d == Direction.NORTH) {
                nextP = new Point(searchState.s.p.r - 1, searchState.s.p.c);
            } else if (searchState.s.d == Direction.EAST) {
                nextP = new Point(searchState.s.p.r, searchState.s.p.c + 1);
            } else if (searchState.s.d == Direction.SOUTH) {
                nextP = new Point(searchState.s.p.r + 1, searchState.s.p.c);
            } else {
                nextP = new Point(searchState.s.p.r, searchState.s.p.c - 1);
            }
            if (grid[nextP.r][nextP.c] && !searchState.visited.contains(nextP)) {
                State moveState = new State(nextP, searchState.s.d);
                Set<Point> newVisited = new HashSet<>(searchState.visited);
                newVisited.add(nextP);
                searchStates.add(new SearchState2(moveState, searchState.score + 1, newVisited));
            }

            // turn and move
            List<Direction> nextDirs = new ArrayList<>();
            if (searchState.s.d == Direction.NORTH || searchState.s.d == Direction.SOUTH) {
                nextDirs.add(Direction.WEST);
                nextDirs.add(Direction.EAST);
            } else {
                nextDirs.add(Direction.NORTH);
                nextDirs.add(Direction.SOUTH);
            }
            for (Direction d : nextDirs) {
                Point turnPoint;
                if (d == Direction.NORTH) {
                    turnPoint = new Point(searchState.s.p.r - 1, searchState.s.p.c);
                } else if (d == Direction.EAST) {
                    turnPoint = new Point(searchState.s.p.r, searchState.s.p.c + 1);
                } else if (d == Direction.SOUTH) {
                    turnPoint = new Point(searchState.s.p.r + 1, searchState.s.p.c);
                } else {
                    turnPoint = new Point(searchState.s.p.r, searchState.s.p.c - 1);
                }
                if (grid[turnPoint.r][turnPoint.c] && !searchState.visited.contains(turnPoint)) {
                    State newState = new State(turnPoint, d);
                    Set<Point> newVisited = new HashSet<>(searchState.visited);
                    newVisited.add(nextP);
                    searchStates
                            .add(new SearchState2(newState, searchState.score + 1001, newVisited));
                }
            }
        }

        return shortestPathPoints.size() + 1;
    }
}
