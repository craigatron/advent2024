package advent2024;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Day15 {

    private record Point(int r, int c) {
        public Point plus(Point p) {
            return new Point(this.r + p.r, this.c + p.c);
        }

        public Point minus(Point p) {
            return new Point(this.r - p.r, this.c - p.c);
        }
    }

    public static long part1() throws IOException {
        List<String> rows = Utils.loadFile("day15.txt");

        char[][] grid = new char[50][50];
        Point robot = null;
        String instructions = "";

        boolean loadMap = true;

        int r = 0;
        for (String row : rows) {
            if (loadMap) {
                if ("".equals(row.strip())) {
                    loadMap = false;
                    continue;
                }
                grid[r] = row.toCharArray();
                for (int c = 0; c < grid.length; c++) {
                    if (grid[r][c] == '@') {
                        robot = new Point(r, c);
                    }
                }
                r += 1;
            } else {
                instructions += row.strip();
            }
        }

        for (char c : instructions.toCharArray()) {
            Point delta = switch (c) {
                case '^' -> new Point(-1, 0);
                case '>' -> new Point(0, 1);
                case 'v' -> new Point(1, 0);
                case '<' -> new Point(0, -1);
                default -> throw new RuntimeException("invalid char");
            };

            Point nextPoint = robot.plus(delta);
            if (grid[nextPoint.r][nextPoint.c] == '#') {
                // wall, can't move
                continue;
            } else if (grid[nextPoint.r][nextPoint.c] == '.') {
                // empty space, can move
                grid[nextPoint.r][nextPoint.c] = '@';
                grid[robot.r][robot.c] = '.';
                robot = nextPoint;
                continue;
            }

            // box, figure out if we can move
            Point endPoint = nextPoint;
            while (grid[endPoint.r][endPoint.c] == 'O') {
                endPoint = endPoint.plus(delta);
            }
            if (grid[endPoint.r][endPoint.c] == '#') {
                // hit a wall, can't move
                continue;
            }

            // empty spot, move box into it
            grid[endPoint.r][endPoint.c] = 'O';

            // clear the spot where robot will go
            grid[nextPoint.r][nextPoint.c] = '@';
            grid[robot.r][robot.c] = '.';
            robot = nextPoint;
        }

        int total = 0;

        for (int ri = 0; ri < grid.length; ri++) {
            for (int ci = 0; ci < grid.length; ci++) {
                if (grid[ri][ci] == 'O') {
                    total += ((100 * ri) + ci);
                }
            }
        }

        return total;
    }

    public static long part2() throws IOException {
        List<String> rows = Utils.loadFile("day15.txt");

        Grid grid = Grid.create(rows.subList(0, 50));

        String instructions = rows.subList(51, rows.size()).stream().collect(Collectors.joining());

        for (char c : instructions.toCharArray()) {
            Point delta = switch (c) {
                case '^' -> new Point(-1, 0);
                case '>' -> new Point(0, 1);
                case 'v' -> new Point(1, 0);
                case '<' -> new Point(0, -1);
                default -> throw new RuntimeException("invalid char");
            };

            grid.moveRobot(delta);
        }

        return grid.score();
    }

    private static class Grid {
        private final Set<Point> walls;
        // point is left most point of box
        private final Set<Point> boxes;
        private Point robot;
        private final int rows;
        private final int cols;

        private Grid(Set<Point> walls, Set<Point> boxes, Point robot, int rows, int cols) {
            this.walls = walls;
            this.boxes = boxes;
            this.robot = robot;
            this.rows = rows;
            this.cols = cols;
        }

        public void moveRobot(Point delta) {
            Point nextPoint = robot.plus(delta);
            if (walls.contains(nextPoint)) {
                return;
            }

            Optional<Point> boxOpt = this.boxes.stream()
                    .filter(p -> nextPoint.equals(p) || nextPoint.equals(new Point(p.r, p.c + 1)))
                    .findAny();
            if (boxOpt.isEmpty()) {
                // no wall or box, move the bot
                this.robot = nextPoint;
                return;
            }

            List<Point> pushedBoxes = new ArrayList<>();
            pushedBoxes.add(boxOpt.get());
            System.out.println(pushedBoxes);
            if (delta.r == 0) {
                int c = boxOpt.get().c;
                if (delta.c == 1) {
                    while (true) {
                        Point nextP = new Point(nextPoint.r, c + 2);
                        if (walls.contains(nextP)) {
                            return;
                        } else if (boxes.contains(nextP)) {
                            pushedBoxes.add(nextP);
                            c += 2;
                        } else {
                            break;
                        }
                    }
                } else {
                    while (true) {
                        if (walls.contains(new Point(nextPoint.r, c - 1))) {
                            return;
                        } else if (boxes.contains(new Point(nextPoint.r, c - 2))) {
                            pushedBoxes.add(new Point(nextPoint.r, c - 2));
                            c -= 2;
                        } else {
                            break;
                        }
                    }
                }
            } else {
                int lastR = nextPoint.r;
                int r = nextPoint.plus(delta).r;
                while (true) {
                    boolean boxesAdded = false;
                    final int lr = lastR;
                    List<Integer> nextColumns = pushedBoxes.stream().filter(p -> p.r == lr)
                            .flatMap(p -> Arrays.asList(p.c, p.c + 1).stream()).toList();
                    for (int c : nextColumns) {
                        Point p = new Point(r, c);
                        if (walls.contains(p)) {
                            return;
                        }
                        if (boxes.contains(p) || boxes.contains(new Point(p.r, p.c - 1))) {
                            boxesAdded = true;
                            pushedBoxes.add(boxes.contains(p) ? p : new Point(p.r, p.c - 1));
                        }
                    }
                    if (!boxesAdded) {
                        break;
                    }

                    lastR = r;
                    r += delta.r;
                }
            }

            for (Point p : pushedBoxes) {
                boxes.remove(p);
            }
            for (Point p : pushedBoxes) {
                boxes.add(p.plus(delta));
            }
            this.robot = nextPoint;
        }

        public void print() {
            for (int r = 0; r < this.rows; r++) {
                String row = "";
                for (int c = 0; c < this.cols; c++) {
                    Point p = new Point(r, c);
                    if (walls.contains(p)) {
                        row += "#";
                    } else if (boxes.contains(p)) {
                        row += "[";
                    } else if (boxes.contains(new Point(p.r, p.c - 1))) {
                        row += "]";
                    } else if (p.equals(robot)) {
                        row += "@";
                    } else {
                        row += ".";
                    }
                }
                System.out.println(row);
            }
            System.out.println();
        }

        public int score() {
            int total = 0;
            for (Point p : boxes) {
                total += (100 * p.r) + p.c;
            }
            return total;
        }

        public static Grid create(List<String> rows) {
            Set<Point> walls = new HashSet<>();
            Set<Point> boxes = new HashSet<>();
            Point robot = null;

            for (int r = 0; r < rows.size(); r++) {
                char[] chars = rows.get(r).toCharArray();

                int actualC = 0;
                for (int c = 0; c < chars.length; c++) {
                    char ch = chars[c];
                    if (ch == '#') {
                        walls.add(new Point(r, actualC));
                        walls.add(new Point(r, actualC + 1));
                    } else if (ch == '@') {
                        robot = new Point(r, actualC);
                    } else if (ch == 'O') {
                        boxes.add(new Point(r, actualC));
                    }
                    actualC += 2;
                }
            }

            return new Grid(walls, boxes, robot, rows.size(), rows.size() * 2);
        }
    }
}
