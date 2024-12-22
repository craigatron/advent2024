package advent2024;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Day21 {

    private static enum Direction {
        UP("^"), DOWN("v"), LEFT("<"), RIGHT(">"), PRESS("A");

        private final String s;

        private Direction(String s) {
            this.s = s;
        }

        public String toString() {
            return s;
        }
    }

    private record Point(int r, int c) {
    }

    private record DirPair(Direction start, Direction end) {
    }

    private static final Map<Character, Point> numPad =
            Map.ofEntries(Map.entry('7', new Point(0, 0)), Map.entry('8', new Point(0, 1)),
                    Map.entry('9', new Point(0, 2)), Map.entry('4', new Point(1, 0)),
                    Map.entry('5', new Point(1, 1)), Map.entry('6', new Point(1, 2)),
                    Map.entry('1', new Point(2, 0)), Map.entry('2', new Point(2, 1)),
                    Map.entry('3', new Point(2, 2)), Map.entry('0', new Point(3, 1)),
                    Map.entry('A', new Point(3, 2)));

    private static final Map<Direction, Point> dPad = Map.of(Direction.UP, new Point(0, 1),
            Direction.PRESS, new Point(0, 2), Direction.LEFT, new Point(1, 0), Direction.DOWN,
            new Point(1, 1), Direction.RIGHT, new Point(1, 2));

    private static Map<DirPair, List<List<Direction>>> buildDPadMap() {
        Map<DirPair, List<List<Direction>>> dPadMap = new HashMap<>();

        for (Direction start : Direction.values()) {
            for (Direction end : Direction.values()) {
                DirPair dp = new DirPair(start, end);
                dPadMap.put(dp, shortestPaths(start, end));
            }
        }

        return dPadMap;
    }

    private static List<List<Direction>> shortestPaths(Direction from, Direction to) {
        if (from == to) {
            return Collections.singletonList(Collections.singletonList(Direction.PRESS));
        }

        Point start = dPad.get(from);
        Point end = dPad.get(to);

        List<Direction> vert = Collections.nCopies(Math.abs(start.r - end.r),
                start.r > end.r ? Direction.UP : Direction.DOWN);
        List<Direction> horiz = Collections.nCopies(Math.abs(start.c - end.c),
                start.c > end.c ? Direction.LEFT : Direction.RIGHT);

        if (from == Direction.LEFT || start.r == end.r) {
            List<Direction> directions = new ArrayList<>(horiz);
            directions.addAll(vert);
            directions.add(Direction.PRESS);
            return Collections.singletonList(directions);
        }
        if (to == Direction.LEFT || start.c == end.c) {
            List<Direction> directions = new ArrayList<>(vert);
            directions.addAll(horiz);
            directions.add(Direction.PRESS);
            return Collections.singletonList(directions);
        }

        List<List<Direction>> directions = new ArrayList<>();
        List<Direction> l1 = new ArrayList<>(vert);
        l1.addAll(horiz);
        l1.add(Direction.PRESS);
        directions.add(l1);

        List<Direction> l2 = new ArrayList<>(horiz);
        l2.addAll(vert);
        l2.add(Direction.PRESS);
        directions.add(l2);

        return directions;
    }

    private static List<List<Direction>> shortestPaths(char from, char to) {
        Point start = numPad.get(from);
        Point end = numPad.get(to);

        List<Direction> vert = Collections.nCopies(Math.abs(start.r - end.r),
                start.r > end.r ? Direction.UP : Direction.DOWN);
        List<Direction> horiz = Collections.nCopies(Math.abs(start.c - end.c),
                start.c > end.c ? Direction.LEFT : Direction.RIGHT);

        if (horiz.isEmpty()) {
            List<Direction> directions = new ArrayList<>(vert);
            directions.add(Direction.PRESS);
            return Collections.singletonList(directions);
        }
        if (vert.isEmpty()) {
            List<Direction> directions = new ArrayList<>(horiz);
            directions.add(Direction.PRESS);
            return Collections.singletonList(directions);
        }

        if (start.r == 3 && end.c == 0) {
            List<Direction> directions = new ArrayList<>(vert);
            directions.addAll(horiz);
            directions.add(Direction.PRESS);
            return Collections.singletonList(directions);
        }
        if (start.c == 0 && end.r == 3) {
            List<Direction> directions = new ArrayList<>(horiz);
            directions.addAll(vert);
            directions.add(Direction.PRESS);
            return Collections.singletonList(directions);
        }

        List<List<Direction>> directions = new ArrayList<>();
        List<Direction> l1 = new ArrayList<>(vert);
        l1.addAll(horiz);
        l1.add(Direction.PRESS);
        directions.add(l1);

        List<Direction> l2 = new ArrayList<>(horiz);
        l2.addAll(vert);
        l2.add(Direction.PRESS);
        directions.add(l2);

        return directions;
    }

    public static long part1() throws IOException {
        List<String> rows = Utils.loadFile("day21.txt");

        long total = 0;
        for (String code : rows) {
            List<List<Direction>> numPadPaths = new ArrayList<>(shortestPaths('A', code.charAt(0)));
            for (int i = 0; i < code.length() - 1; i++) {
                List<List<Direction>> newFullPaths = new ArrayList<>();
                List<List<Direction>> newShortestPaths =
                        shortestPaths(code.charAt(i), code.charAt(i + 1));
                for (List<Direction> path : numPadPaths) {
                    for (List<Direction> shortestPath : newShortestPaths) {
                        newFullPaths
                                .add(Stream.concat(path.stream(), shortestPath.stream()).toList());
                    }
                }
                numPadPaths = newFullPaths;
            }

            Map<DirPair, List<List<Direction>>> dPadMap = buildDPadMap();
            List<List<Direction>> allDPadPaths = findPaths(numPadPaths, dPadMap);

            allDPadPaths = findPaths(allDPadPaths, dPadMap);
            total += (allDPadPaths.stream().map(p -> p.size()).min(Integer::compareTo).get()
                    * Integer.parseInt(code.substring(0, 3)));
        }

        return total;
    }

    private static List<List<Direction>> findPaths(List<List<Direction>> paths,
            Map<DirPair, List<List<Direction>>> dPadMap) {
        List<List<Direction>> allDPadPaths = new ArrayList<>();
        for (List<Direction> numPadPath : paths) {
            List<List<Direction>> dPadPaths =
                    dPadMap.get(new DirPair(Direction.PRESS, numPadPath.get(0)));
            for (int i = 0; i < numPadPath.size() - 1; i++) {
                List<List<Direction>> newFullPaths = new ArrayList<>();
                List<List<Direction>> newShortestPaths =
                        dPadMap.get(new DirPair(numPadPath.get(i), numPadPath.get(i + 1)));
                for (List<Direction> path : dPadPaths) {
                    for (List<Direction> shortestPath : newShortestPaths) {
                        newFullPaths
                                .add(Stream.concat(path.stream(), shortestPath.stream()).toList());
                    }
                }
                dPadPaths = newFullPaths;
            }
            allDPadPaths.addAll(dPadPaths);
        }
        int minSize = allDPadPaths.stream().map(p -> p.size()).min(Integer::compareTo).get();
        return allDPadPaths.stream().filter(p -> p.size() == minSize).toList();
    }

    private record CacheKey(List<Direction> ld, int numPads) {
    }

    private static long countShortestPathLength(List<Direction> directions, int numPads,
            Map<DirPair, List<List<Direction>>> dPadMap, Map<CacheKey, Long> cache) {
        if (numPads == 0) {
            return directions.size();
        }
        CacheKey key = new CacheKey(directions, numPads);
        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        List<List<Direction>> chunks = new ArrayList<>();
        List<Direction> chunk = new ArrayList<>();
        for (Direction d : directions) {
            chunk.add(d);
            if (d == Direction.PRESS) {
                chunks.add(chunk);
                chunk = new ArrayList<>();
            }
        }

        long total = 0;

        for (List<Direction> dirChunk : chunks) {
            List<List<Direction>> chunkDirections =
                    findPaths(Collections.singletonList(dirChunk), dPadMap);
            total += chunkDirections.stream()
                    .map(p -> countShortestPathLength(p, numPads - 1, dPadMap, cache))
                    .min(Long::compareTo).get();
        }

        cache.put(key, total);

        return total;
    }

    public static long part2() throws IOException {
        List<String> rows = Utils.loadFile("day21.txt");

        long total = 0;
        for (String code : rows) {
            List<List<Direction>> numPadPaths = new ArrayList<>(shortestPaths('A', code.charAt(0)));
            for (int i = 0; i < code.length() - 1; i++) {
                List<List<Direction>> newFullPaths = new ArrayList<>();
                List<List<Direction>> newShortestPaths =
                        shortestPaths(code.charAt(i), code.charAt(i + 1));
                for (List<Direction> path : numPadPaths) {
                    for (List<Direction> shortestPath : newShortestPaths) {
                        newFullPaths
                                .add(Stream.concat(path.stream(), shortestPath.stream()).toList());
                    }
                }
                numPadPaths = newFullPaths;
            }

            Map<CacheKey, Long> cache = new HashMap<>();

            Map<DirPair, List<List<Direction>>> dPadMap = buildDPadMap();
            long minLength =
                    numPadPaths.stream().map(p -> countShortestPathLength(p, 25, dPadMap, cache))
                            .min(Long::compareTo).get();
            total += (minLength * Integer.parseInt(code.substring(0, 3)));
        }

        return total;
    }
}
