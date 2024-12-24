package advent2024;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Day24 {

    private enum Operation {
        AND, OR, XOR;
    }

    private record Gate(String w1, String w2, String wout, Operation op) {
        public boolean isValid() {
            if (wout.startsWith("z") && !wout.equals("z45") && op != Operation.XOR) {
                return false;
            }

            if (!wout.startsWith("z") && !(w1.startsWith("x") || w1.startsWith("y"))
                    && op == Operation.XOR) {
                return false;
            }

            return true;
        }
    }

    private record GatePair(Gate g1, Gate g2) {
        public boolean overlaps(GatePair other) {
            return g1.equals(other.g1) || g1.equals(other.g2) || g2.equals(other.g1)
                    || g2.equals(other.g2);
        }

        public GatePair swapOuts() {
            return new GatePair(new Gate(g1.w1, g1.w2, g2.wout, g1.op),
                    new Gate(g2.w1, g2.w2, g1.wout, g2.op));
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof GatePair)) {
                return false;
            }
            GatePair ogp = (GatePair) o;
            return (g1.equals(ogp.g1) && g2.equals(ogp.g2))
                    || (g1.equals(ogp.g2) && g2.equals(ogp.g1));
        }
    }

    private static long getValue(Map<String, Boolean> wires, String prefix) {
        String bin = wires.entrySet().stream().filter(e -> e.getKey().startsWith(prefix))
                .sorted((e1, e2) -> e2.getKey().compareTo(e1.getKey()))
                .map(e -> e.getValue() ? "1" : "0").collect(Collectors.joining());
        return Long.parseLong(bin, 2);
    }

    private static long calculateZ(Map<String, Boolean> wires, List<Gate> gates,
            boolean[] desiredBits) {

        Set<Gate> invalidGates = new HashSet<>();
        while (!gates.isEmpty()) {
            Gate g = gates.removeFirst();

            if (invalidGates.contains(g)) {
                return -1;
            }

            if (!wires.containsKey(g.w1) || !wires.containsKey(g.w2)) {
                gates.add(g);
                invalidGates.add(g);
                continue;
            }

            boolean v1 = wires.get(g.w1);
            boolean v2 = wires.get(g.w2);

            boolean result = switch (g.op) {
                case AND -> v1 && v2;
                case OR -> v1 || v2;
                case XOR -> v1 ^ v2;
            };

            if (desiredBits != null && g.wout.startsWith("z")
                    && desiredBits[Integer.parseInt(g.wout.substring(1))] != result) {
                return -1;
            }

            invalidGates.clear();

            wires.put(g.wout, result);
        }

        return getValue(wires, "z");
    }

    public static long part1() throws IOException {
        List<String> rows = Utils.loadFile("day24.txt");

        int split = rows.indexOf("");

        Map<String, Boolean> wires = new HashMap<>();
        rows.subList(0, split).stream().map(s -> s.split(": "))
                .forEach(s -> wires.put(s[0], Integer.parseInt(s[1]) == 1));

        List<Gate> gates = new ArrayList<>();
        for (String row : rows.subList(split + 1, rows.size())) {
            String[] parts = row.split(" ");
            Operation op = switch (parts[1]) {
                case "AND" -> Operation.AND;
                case "OR" -> Operation.OR;
                case "XOR" -> Operation.XOR;
                default -> throw new IllegalArgumentException();
            };
            gates.add(new Gate(parts[0], parts[2], parts[4], op));
        }

        return calculateZ(wires, gates, null);
    }

    public static String part2() throws IOException {
        List<String> rows = Utils.loadFile("day24.txt");

        int split = rows.indexOf("");

        Map<String, Boolean> wires = new HashMap<>();
        rows.subList(0, split).stream().map(s -> s.split(": "))
                .forEach(s -> wires.put(s[0], Integer.parseInt(s[1]) == 1));

        List<Gate> gates = new ArrayList<>();
        for (String row : rows.subList(split + 1, rows.size())) {
            String[] parts = row.split(" ");
            Operation op = switch (parts[1]) {
                case "AND" -> Operation.AND;
                case "OR" -> Operation.OR;
                case "XOR" -> Operation.XOR;
                default -> throw new IllegalArgumentException();
            };
            gates.add(new Gate(parts[0], parts[2], parts[4], op));
        }

        long x = getValue(wires, "x");
        long y = getValue(wires, "y");
        long desiredZ = x + y;

        String desiredZBin = new StringBuilder(Long.toBinaryString(desiredZ)).reverse().toString();
        boolean[] desiredBits = new boolean[46];
        for (int i = 0; i < desiredBits.length; i++) {
            desiredBits[i] = desiredZBin.charAt(i) == '1';
        }

        List<Gate> invalidGates = gates.stream().filter(g -> !g.isValid()).toList();
        if (invalidGates.size() != 6) {
            // dunno if this is true for all inputs but it is for mine
            throw new IllegalArgumentException();
        }
        for (Gate g : invalidGates) {
            gates.remove(g);
        }

        for (int a = 0; a < gates.size() - 1; a++) {
            Gate g1 = gates.get(a);
            for (int b = a + 1; b < gates.size(); b++) {
                Gate g2 = gates.get(b);

                List<Gate> invalidGatesCopy = new ArrayList<>(invalidGates);
                invalidGatesCopy.add(g1);
                invalidGatesCopy.add(g2);

                for (Set<GatePair> pairings : findPairings(Set.of(), invalidGatesCopy)) {
                    List<Gate> gatesCopy = new ArrayList<Gate>(gates);
                    gatesCopy.remove(g1);
                    gatesCopy.remove(g2);

                    for (GatePair gp : pairings) {
                        gatesCopy.add(gp.g1);
                        gatesCopy.add(gp.g2);
                    }

                    Map<String, Boolean> wiresCopy = new HashMap<>(wires);
                    if (calculateZ(wiresCopy, gatesCopy, desiredBits) == desiredZ) {
                        return invalidGatesCopy.stream().map(g -> g.wout).sorted()
                                .collect(Collectors.joining(","));
                    }
                }
            }
        }

        return "";
    }

    private static Set<Set<GatePair>> findPairings(Set<GatePair> current, List<Gate> gates) {
        if (gates.isEmpty()) {
            return Set.of(current);
        }

        Set<Set<GatePair>> result = new HashSet<>();
        for (int i = 0; i < gates.size() - 1; i++) {
            for (int j = i + 1; j < gates.size(); j++) {
                GatePair gp = new GatePair(gates.get(i), gates.get(j)).swapOuts();

                if (!gp.g1.isValid() || !gp.g2.isValid()) {
                    continue;
                }

                List<Gate> gatesCopy = new ArrayList<>(gates);
                gatesCopy.remove(gates.get(i));
                gatesCopy.remove(gates.get(j));

                Set<GatePair> currentCopy = new HashSet<>(current);
                currentCopy.add(gp);

                result.addAll(findPairings(currentCopy, gatesCopy));
            }
        }

        return result;
    }
}
