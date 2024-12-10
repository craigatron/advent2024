package advent2024;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day9 {
    public static long part1() throws IOException {
        List<String> rows = Utils.loadFile("day9.txt");

        String row = rows.getFirst();

        List<Integer> blocks = new ArrayList<>();
        int fileId = 0;
        boolean isFile = true;
        for (Character c : row.toCharArray()) {
            int blockCount = Integer.parseInt(c.toString());
            if (isFile) {
                for (int i = 0; i < blockCount; i++) {
                    blocks.add(fileId);
                }
                fileId += 1;
            } else {
                for (int i = 0; i < blockCount; i++) {
                    blocks.add(null);
                }
            }
            isFile = !isFile;
        }

        int firstBlankIndex = p1nextBlankIndex(blocks);
        int nextBlockIndex = p1nextBlockToMove(blocks);

        while (nextBlockIndex > firstBlankIndex) {
            int blockToMove = blocks.get(nextBlockIndex);
            blocks.set(nextBlockIndex, null);
            blocks.set(firstBlankIndex, blockToMove);

            firstBlankIndex = p1nextBlankIndex(blocks);
            nextBlockIndex = p1nextBlockToMove(blocks);
        }

        long total = 0;

        for (int i = 0; i < blocks.size(); i++) {
            if (blocks.get(i) == null) {
                break;
            }
            total += (i * (long) blocks.get(i));
        }

        return total;
    }

    private static int p1nextBlankIndex(List<Integer> blocks) {
        for (int i = 0; i < blocks.size(); i++) {
            if (blocks.get(i) == null) {
                return i;
            }
        }
        return Integer.MAX_VALUE;
    }

    private static int p1nextBlockToMove(List<Integer> blocks) {
        for (int i = blocks.size() - 1; i >= 0; i--) {
            if (blocks.get(i) != null) {
                return i;
            }
        }
        return -1;
    }

    private record BlockSpan(int startIndex, int endIndex) {
        public int size() {
            return this.endIndex - this.startIndex + 1;
        }
    }

    public static long part2() throws IOException {
        List<String> rows = Utils.loadFile("day9.txt");

        String row = rows.getFirst();

        List<Integer> blocks = new ArrayList<>();
        int fileId = 0;
        boolean isFile = true;
        Map<Integer, BlockSpan> blockSpans = new HashMap<>();
        for (Character c : row.toCharArray()) {
            int blockCount = Integer.parseInt(c.toString());
            if (isFile) {
                blockSpans.put(fileId,
                        new BlockSpan(blocks.size(), blocks.size() + blockCount - 1));
                for (int i = 0; i < blockCount; i++) {
                    blocks.add(fileId);
                }
                fileId += 1;
            } else {
                for (int i = 0; i < blockCount; i++) {
                    blocks.add(null);
                }
            }
            isFile = !isFile;
        }

        int blockIndexToMove = fileId - 1;

        while (blockIndexToMove > 0) {
            BlockSpan blockToMove = blockSpans.get(blockIndexToMove);
            BlockSpan nextBlankBlock = nextBlankBlock(0, blocks);
            while (nextBlankBlock.startIndex < blockToMove.startIndex) {
                if (blockToMove.size() <= nextBlankBlock.size()) {
                    for (int i = nextBlankBlock.startIndex; i < nextBlankBlock.startIndex
                            + blockToMove.size(); i++) {
                        blocks.set(i, blockIndexToMove);
                    }
                    for (int i = blockToMove.startIndex; i < blockToMove.startIndex
                            + blockToMove.size(); i++) {
                        blocks.set(i, null);
                    }
                    break;
                }
                nextBlankBlock = nextBlankBlock(nextBlankBlock.endIndex + 1, blocks);
            }

            blockIndexToMove -= 1;
        }

        long total = 0;
        for (int i = 0; i < blocks.size(); i++) {
            if (blocks.get(i) == null) {
                continue;
            }
            total += (i * (long) blocks.get(i));
        }

        return total;
    }

    private static BlockSpan nextBlankBlock(int startIndex, List<Integer> blocks) {
        for (int i = startIndex; i < blocks.size(); i++) {
            if (blocks.get(i) == null) {
                for (int j = i + 1; j < blocks.size(); j++) {
                    if (blocks.get(j) != null) {
                        return new BlockSpan(i, j - 1);
                    }
                }
            }
        }
        return new BlockSpan(blocks.size(), blocks.size());
    }
}
