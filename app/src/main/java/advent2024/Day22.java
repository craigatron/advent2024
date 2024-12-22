package advent2024;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day22 {
    public static long part1() throws IOException {
        List<String> rows = Utils.loadFile("day22.txt");

        long total = 0;
        for (long secret : rows.stream().map(s -> Long.parseLong(s)).toList()) {
            long val = secret;
            for (int i = 0; i < 2000; i++) {
                val = ((val * 64L) ^ val) % 16777216L;
                val = ((val / 32L) ^ val) % 16777216L;
                val = ((val * 2048L) ^ val) % 16777216L;
            }
            total += val;
        }

        return total;
    }

    private record Pair(int price, int change) {
    }

    public static long part2() throws IOException {
        List<String> rows = Utils.loadFile("day22.txt");

        List<List<Pair>> allPricesAndChanges = new ArrayList<>();

        for (long secret : rows.stream().map(s -> Long.parseLong(s)).toList()) {
            int lastPrice = (int) (secret % 10L);
            List<Pair> pricesAndChanges = new ArrayList<>();

            long val = secret;
            for (int i = 0; i < 2000; i++) {
                val = ((val * 64L) ^ val) % 16777216L;
                val = ((val / 32L) ^ val) % 16777216L;
                val = ((val * 2048L) ^ val) % 16777216L;

                int price = (int) (val % 10L);
                pricesAndChanges.add(new Pair(price, price - lastPrice));
                lastPrice = price;
            }
            allPricesAndChanges.add(pricesAndChanges);
        }

        int maxPrice = 0;
        Set<List<Integer>> seenIndices = new HashSet<>();
        List<List<Integer>> allChanges = allPricesAndChanges.stream()
                .map(pcl -> pcl.stream().map(pc -> pc.change).toList()).toList();

        // lol this takes a couple minutes but my brain is cooked from day 21 so
        for (int acIndex = 0; acIndex < allChanges.size(); acIndex++) {
            List<Integer> changes = allChanges.get(acIndex);
            for (int i = 0; i < changes.size() - 4; i++) {
                int newPrice = 0;
                List<Integer> fourChanges = changes.subList(i, i + 4);
                if (!seenIndices.add(fourChanges)) {
                    continue;
                }

                for (int chIndex = acIndex; chIndex < allChanges.size(); chIndex++) {
                    int triggerIndex =
                            Collections.indexOfSubList(allChanges.get(chIndex), fourChanges);
                    if (triggerIndex != -1) {
                        newPrice += allPricesAndChanges.get(chIndex).get(triggerIndex + 3).price;
                    }
                }

                if (newPrice > maxPrice) {
                    maxPrice = newPrice;
                }
            }
        }

        return maxPrice;
    }
}
