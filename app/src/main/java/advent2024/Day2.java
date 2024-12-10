package advent2024;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day2 {
    enum Mode {
        INCREASING,
        DECREASING
    }

    public static int part1() throws IOException {
        List<String> input = Utils.loadFile("day2.txt");

        int count = 0;

        for (String s : input) {
            List<Integer> nums = Arrays.stream(s.split("\s+")).map(x -> Integer.parseInt(x))
                    .collect(Collectors.toList());
            if (testLine(nums)) {
                count += 1;
            }
        }

        return count;
    }

    public static int part2() throws IOException {
        List<String> input = Utils.loadFile("day2.txt");

        int count = 0;

        for (String s : input) {
            List<Integer> nums = Arrays.stream(s.split("\s+")).map(x -> Integer.parseInt(x))
                    .collect(Collectors.toList());

            if (testLine(nums)) {
                count += 1;
            } else {
                for (int i = 0; i < nums.size(); i++) {
                    List<Integer> numsCopy = new ArrayList<>(nums);
                    numsCopy.remove(i);
                    if (testLine(numsCopy)) {
                        count += 1;
                        break;
                    }
                }
            }
        }

        return count;
    }

    private static boolean testLine(List<Integer> nums) {
        Mode mode = null;
        for (int i = 1; i < nums.size(); i++) {
            int diff = Math.abs(nums.get(i) - nums.get(i - 1));
            if (diff == 0 || diff > 3) {
                return false;
            }
            boolean isIncreasing = nums.get(i) > nums.get(i - 1);
            if (i == 1) {
                mode = isIncreasing ? Mode.INCREASING : Mode.DECREASING;
            } else if ((isIncreasing && mode == Mode.DECREASING) || (!isIncreasing && mode == Mode.INCREASING)) {
                return false;
            }
        }
        return true;
    }
}
