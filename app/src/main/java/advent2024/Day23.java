package advent2024;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Day23 {

    public static long part1() throws IOException {
        List<String> rows = Utils.loadFile("day23.txt");

        Map<String, Set<String>> connections = new HashMap<>();

        for (String row : rows) {
            String[] computers = row.split("-");
            String c1 = computers[0];
            String c2 = computers[1];
            if (!connections.containsKey(c1)) {
                connections.put(c1, new HashSet<>());
            }
            if (!connections.containsKey(c2)) {
                connections.put(c2, new HashSet<>());
            }
            connections.get(c1).add(c2);
            connections.get(c2).add(c1);
        }

        Set<List<String>> tTriples = new HashSet<>();

        for (String c : connections.keySet().stream().filter(k -> k.startsWith("t")).toList()) {
            for (Map.Entry<String, Set<String>> e : connections.entrySet().stream()
                    .filter(e -> e.getValue().contains(c)).toList()) {
                for (String c2 : e.getValue()) {
                    if (connections.get(c2).contains(c)) {
                        List<String> cycle = Arrays.asList(c, e.getKey(), c2);
                        cycle.sort(String::compareTo);
                        tTriples.add(cycle);
                    }
                }
            }
        }

        return tTriples.size();
    }

    private static Set<String> bronKerbosch(Map<String, Set<String>> graph, Set<String> r,
            Set<String> p, Set<String> x) {
        if (p.isEmpty() && x.isEmpty()) {
            return r;
        }

        Set<String> max = Set.of();

        Set<String> pCopy = new HashSet<>(p);
        Set<String> xCopy = new HashSet<>(x);

        for (String s : p) {
            Set<String> newR = new HashSet<>(r);
            newR.add(s);
            Set<String> newP = new HashSet<>(pCopy);
            newP.retainAll(graph.get(s));
            Set<String> newX = new HashSet<>(xCopy);
            newX.retainAll(graph.get(s));
            Set<String> ret = bronKerbosch(graph, newR, newP, newX);
            if (ret.size() > max.size()) {
                max = ret;
            }

            pCopy.remove(s);
            xCopy.add(s);
        }

        return max;
    }

    public static String part2() throws IOException {
        List<String> rows = Utils.loadFile("day23.txt");

        Map<String, Set<String>> connections = new HashMap<>();

        for (String row : rows) {
            String[] computers = row.split("-");
            String c1 = computers[0];
            String c2 = computers[1];
            if (!connections.containsKey(c1)) {
                connections.put(c1, new HashSet<>());
            }
            if (!connections.containsKey(c2)) {
                connections.put(c2, new HashSet<>());
            }
            connections.get(c1).add(c2);
            connections.get(c2).add(c1);
        }

        return bronKerbosch(connections, Set.of(), connections.keySet(), Set.of()).stream().sorted()
                .collect(Collectors.joining(","));
    }
}
