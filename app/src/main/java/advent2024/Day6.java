package advent2024;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class Day6 {

    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    private record Point(int row, int col) {
    }

    public static int part1() throws IOException {
        List<String> rows = Utils.loadFile("day6.txt");

        char[][] grid = new char[130][130];

        ListIterator<String> rowIterator = rows.listIterator();

        Point guardPos = null;
        Direction guardDir = Direction.UP;

        while (rowIterator.hasNext()) {
            int rowIndex = rowIterator.nextIndex();
            grid[rowIndex] = rowIterator.next().toCharArray();

            if (guardPos == null) {
                for (int i = 0; i < grid[rowIndex].length; i++) {
                    // cheating, input has a ^ so don't care about other starting directions
                    if (grid[rowIndex][i] == '^') {
                        guardPos = new Point(rowIndex, i);
                        grid[rowIndex][i] = '.';
                        break;
                    }
                }
            }
        }

        Set<Point> visited = new HashSet<>();
        visited.add(guardPos);

        while (true) {
            Point nextPos = switch (guardDir) {
                case UP -> new Point(guardPos.row - 1, guardPos.col);
                case DOWN -> new Point(guardPos.row + 1, guardPos.col);
                case LEFT -> new Point(guardPos.row, guardPos.col - 1);
                case RIGHT -> new Point(guardPos.row, guardPos.col + 1);
            };
            if (nextPos.row < 0 || nextPos.row >= 130 || nextPos.col < 0 || nextPos.col >= 130) {
                break;
            }
            if (grid[nextPos.row][nextPos.col] == '.') {
                guardPos = nextPos;
                visited.add(guardPos);
            } else {
                guardDir = switch (guardDir) {
                    case UP -> Direction.RIGHT;
                    case DOWN -> Direction.LEFT;
                    case LEFT -> Direction.UP;
                    case RIGHT -> Direction.DOWN;
                };
            }
        }

        return visited.size();
    }

    private record Pos(Point p, Direction dir) {
    }

    public static int part2() throws IOException {
        List<String> rows = Utils.loadFile("day6.txt");

        char[][] grid = new char[130][130];

        ListIterator<String> rowIterator = rows.listIterator();

        Point initialGuardPos = null;
        Direction guardDir = Direction.UP;

        while (rowIterator.hasNext()) {
            int rowIndex = rowIterator.nextIndex();
            grid[rowIndex] = rowIterator.next().toCharArray();

            if (initialGuardPos == null) {
                for (int i = 0; i < grid[rowIndex].length; i++) {
                    // cheating, input has a ^ so don't care about other starting directions
                    if (grid[rowIndex][i] == '^') {
                        initialGuardPos = new Point(rowIndex, i);
                        grid[rowIndex][i] = '.';
                        break;
                    }
                }
            }
        }

        Set<Point> visited = new HashSet<>();
        Point guardPos = initialGuardPos;

        while (true) {
            Point nextPos = switch (guardDir) {
                case UP -> new Point(guardPos.row - 1, guardPos.col);
                case DOWN -> new Point(guardPos.row + 1, guardPos.col);
                case LEFT -> new Point(guardPos.row, guardPos.col - 1);
                case RIGHT -> new Point(guardPos.row, guardPos.col + 1);
            };
            if (nextPos.row < 0 || nextPos.row >= 130 || nextPos.col < 0 || nextPos.col >= 130) {
                break;
            }
            if (grid[nextPos.row][nextPos.col] == '.') {
                guardPos = nextPos;
                visited.add(guardPos);
            } else {
                guardDir = switch (guardDir) {
                    case UP -> Direction.RIGHT;
                    case DOWN -> Direction.LEFT;
                    case LEFT -> Direction.UP;
                    case RIGHT -> Direction.DOWN;
                };
            }
        }

        int count = 0;
        for (Point p : visited) {
            Set<Pos> visitedPos = new HashSet<>();
            visitedPos.add(new Pos(initialGuardPos, Direction.UP));

            guardPos = initialGuardPos;
            guardDir = Direction.UP;
            while (true) {
                Point nextPos = switch (guardDir) {
                    case UP -> new Point(guardPos.row - 1, guardPos.col);
                    case DOWN -> new Point(guardPos.row + 1, guardPos.col);
                    case LEFT -> new Point(guardPos.row, guardPos.col - 1);
                    case RIGHT -> new Point(guardPos.row, guardPos.col + 1);
                };
                if (nextPos.row < 0 || nextPos.row >= 130 || nextPos.col < 0
                        || nextPos.col >= 130) {
                    break;
                }
                if (grid[nextPos.row][nextPos.col] == '.' && !nextPos.equals(p)) {
                    guardPos = nextPos;
                } else {
                    guardDir = switch (guardDir) {
                        case UP -> Direction.RIGHT;
                        case DOWN -> Direction.LEFT;
                        case LEFT -> Direction.UP;
                        case RIGHT -> Direction.DOWN;
                    };
                }
                if (!visitedPos.add(new Pos(guardPos, guardDir))) {
                    count += 1;
                    break;
                }
            }
        }

        return count;
    }
}
