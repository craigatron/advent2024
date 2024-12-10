package advent2024;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day3 {
    public static int part1() throws IOException {
        List<String> input = Utils.loadFile("day3.txt");
        Pattern p = Pattern.compile("mul\\((\\d+),(\\d+)\\)");

        int total = 0;
        for (String s : input) {
            Matcher m = p.matcher(s);
            while (m.find()) {
                total += (Integer.parseInt(m.group(1)) * Integer.parseInt(m.group(2)));
            }
        }
        return total;
    }

    public static int part2() throws IOException {
        List<String> input = Utils.loadFile("day3.txt");

        Pattern dos = Pattern.compile("do\\(\\)");
        Pattern donts = Pattern.compile("don't\\(\\)");
        Pattern p = Pattern.compile("mul\\((\\d+),(\\d+)\\)");

        boolean doFlag = true;
        int total = 0;
        for (String s : input) {
            List<Integer> doIndices = matchIndices(dos, s);
            List<Integer> dontIndices = matchIndices(donts, s);

            int currentIndex = 0;
            Matcher m = p.matcher(s);
            while (m.find()) {
                int startIndex = m.start();
                while (currentIndex < startIndex) {
                    int doIndex = doIndices.isEmpty() ? Integer.MAX_VALUE : doIndices.get(0);
                    int dontIndex = dontIndices.isEmpty() ? Integer.MAX_VALUE : dontIndices.get(0);
                    if (startIndex < doIndex && startIndex < dontIndex) {
                        currentIndex = startIndex;
                    } else if (doIndex < dontIndex) {
                        doIndices.remove(0);
                        currentIndex = doIndex;
                        doFlag = true;
                    } else if (dontIndex < doIndex) {
                        dontIndices.remove(0);
                        currentIndex = dontIndex;
                        doFlag = false;
                    } else {
                        throw new RuntimeException("wtf");
                    }
                }

                if (doFlag) {
                    total += (Integer.parseInt(m.group(1)) * Integer.parseInt(m.group(2)));
                }
            }
        }
        return total;
    }

    private static List<Integer> matchIndices(Pattern p, String s) {
        List<Integer> indices = new ArrayList<>();

        Matcher m = p.matcher(s);
        while (m.find()) {
            indices.add(m.start());
        }
        return indices;
    }
}
