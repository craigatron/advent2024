package advent2024;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day11 {

    private record Pair(BigInteger stone, int blinks) {
    }

    private static long numStones(BigInteger initialStone, int blinks, Map<Pair, Long> cache) {
        Pair key = new Pair(initialStone, blinks);
        if (blinks == 0) {
            return 1;
        }
        if (cache.containsKey(key)) {
            return cache.get(key);
        }
        long total = 0;

        if (initialStone.longValue() == 0) {
            total += numStones(BigInteger.valueOf(1L), blinks - 1, cache);
        } else {
            String stoneStr = initialStone.toString();
            if (stoneStr.length() % 2 == 0) {
                BigInteger s1 = new BigInteger(stoneStr.substring(0, stoneStr.length() / 2));
                BigInteger s2 = new BigInteger(stoneStr.substring(stoneStr.length() / 2));
                total += numStones(s1, blinks - 1, cache);
                total += numStones(s2, blinks - 1, cache);
            } else {
                total += numStones(initialStone.multiply(BigInteger.valueOf(2024L)), blinks - 1,
                        cache);
            }
        }

        cache.put(key, total);
        return total;
    }

    public static long part1() throws IOException {
        List<String> rows = Utils.loadFile("day11.txt");

        List<Long> stones = new ArrayList<>(
                Arrays.stream(rows.getFirst().split(" ")).map(s -> Long.parseLong(s)).toList());

        long total = 0;
        Map<Pair, Long> cache = new HashMap<>();
        for (long stone : stones) {
            total += numStones(BigInteger.valueOf(stone), 25, cache);
        }

        return total;
    }

    public static long part2() throws IOException {
        List<String> rows = Utils.loadFile("day11.txt");

        List<Long> stones = new ArrayList<>(
                Arrays.stream(rows.getFirst().split(" ")).map(s -> Long.parseLong(s)).toList());

        long total = 0;
        Map<Pair, Long> cache = new HashMap<>();
        for (long stone : stones) {
            total += numStones(BigInteger.valueOf(stone), 75, cache);
        }

        return total;
    }
}
