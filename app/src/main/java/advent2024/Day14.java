package advent2024;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day14 {

    private record Pair(int x, int y) {
    }

    private record Robot(Pair p, Pair v) {
    }

    public static long part1() throws IOException {
        List<String> rows = Utils.loadFile("day14.txt");

        int w = 101;
        int h = 103;

        List<Robot> robots = new ArrayList<>();
        for (String r : rows) {
            String[] parts = r.split(" ");
            String[] posParts = parts[0].split("=")[1].split(",");
            String[] velParts = parts[1].split("=")[1].split(",");
            robots.add(new Robot(
                    new Pair(Integer.parseInt(posParts[0]), Integer.parseInt(posParts[1])),
                    new Pair(Integer.parseInt(velParts[0]), Integer.parseInt(velParts[1]))));
        }

        int q1 = 0;
        int q2 = 0;
        int q3 = 0;
        int q4 = 0;

        for (Robot r : robots) {
            Pair finalPos = new Pair(((r.p.x + (100 * r.v.x) % w) + w) % w,
                    ((r.p.y + (100 * r.v.y) % h) + h) % h);

            if (finalPos.x < w / 2 && finalPos.y < h / 2) {
                q1 += 1;
            } else if (finalPos.x > w / 2 && finalPos.y < h / 2) {
                q2 += 1;
            } else if (finalPos.x < w / 2 && finalPos.y > h / 2) {
                q3 += 1;
            } else if (finalPos.x > w / 2 && finalPos.y > h / 2) {
                q4 += 1;
            }
        }
        return q1 * q2 * q3 * q4;
    }

    public static long part2() throws IOException {
        List<String> rows = Utils.loadFile("day14.txt");

        int w = 101;
        int h = 103;

        List<Robot> robots = new ArrayList<>();
        for (String r : rows) {
            String[] parts = r.split(" ");
            String[] posParts = parts[0].split("=")[1].split(",");
            String[] velParts = parts[1].split("=")[1].split(",");
            robots.add(new Robot(
                    new Pair(Integer.parseInt(posParts[0]), Integer.parseInt(posParts[1])),
                    new Pair(Integer.parseInt(velParts[0]), Integer.parseInt(velParts[1]))));
        }

        int iteration = 1;
        while (true) {
            Set<Pair> finalPositions = new HashSet<>();
            boolean symmetrical = true;
            for (Robot r : robots) {
                Pair finalPos = new Pair(((r.p.x + (iteration * r.v.x) % w) + w) % w,
                        ((r.p.y + (iteration * r.v.y) % h) + h) % h);
                if (!finalPositions.add(finalPos)) {
                    symmetrical = false;
                    break;
                }
            }

            if (symmetrical) {
                for (Pair p : finalPositions) {
                    System.out.println(p.x + "," + p.y);
                }
                return iteration;
            }
            iteration += 1;
        }
    }
}
