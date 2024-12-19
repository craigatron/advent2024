package advent2024;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day17 {

    public static String part1() throws IOException {
        List<String> rows = Utils.loadFile("day17.txt");

        BigInteger regA = new BigInteger(rows.get(0).split(": ")[1]);
        BigInteger regB = new BigInteger(rows.get(1).split(": ")[1]);
        BigInteger regC = new BigInteger(rows.get(2).split(": ")[1]);

        List<Integer> program = Arrays.stream(rows.get(4).split(": ")[1].split(","))
                .map(i -> Integer.parseInt(i)).toList();


        List<Integer> outs = new ArrayList<>();

        int index = 0;
        while (index < program.size()) {
            int instruction = program.get(index);
            int operand = program.get(index + 1);

            BigInteger comboOperandValue = switch (operand) {
                case 0 -> BigInteger.ZERO;
                case 1 -> BigInteger.ONE;
                case 2 -> BigInteger.TWO;
                case 3 -> BigInteger.valueOf(3);
                case 4 -> regA;
                case 5 -> regB;
                case 6 -> regC;
                default -> null;
            };

            if (instruction == 0) {
                regA = regA.divide(BigInteger.TWO.pow(comboOperandValue.intValue()));
            } else if (instruction == 1) {
                regB = regB.xor(BigInteger.valueOf(operand));
            } else if (instruction == 2) {
                regB = comboOperandValue.mod(BigInteger.valueOf(8));
            } else if (instruction == 3) {
                if (!regA.equals(BigInteger.ZERO)) {
                    index = operand;
                    continue;
                }
            } else if (instruction == 4) {
                regB = regB.xor(regC);
            } else if (instruction == 5) {
                outs.add(comboOperandValue.mod(BigInteger.valueOf(8)).intValue());
            } else if (instruction == 6) {
                regB = regA.divide(BigInteger.TWO.pow(comboOperandValue.intValue()));
            } else if (instruction == 7) {
                regC = regA.divide(BigInteger.TWO.pow(comboOperandValue.intValue()));
            }

            index += 2;
        }

        return String.join(",", outs.stream().map(i -> Integer.toString(i)).toList());
    }

    private static final BigInteger FOUR = BigInteger.valueOf(4);
    private static final BigInteger SIX = BigInteger.valueOf(6);
    private static final BigInteger EIGHT = BigInteger.valueOf(8);

    public static BigInteger part2() throws IOException {
        List<String> rows = Utils.loadFile("day17.txt");

        List<Integer> program = Arrays.stream(rows.get(4).split(": ")[1].split(","))
                .map(i -> Integer.parseInt(i)).toList();

        List<String> searchBinary = new ArrayList<>();
        List<String> results = new ArrayList<>();
        searchBinary.add("");

        while (!searchBinary.isEmpty()) {
            String state = searchBinary.removeFirst();

            if (state.length() == 3 * program.size()) {
                results.add(state);
                continue;
            }

            int instructionIndex = state.length() / 3;

            int instruction = program.get(program.size() - instructionIndex - 1);

            for (int i = 0; i < 8; i++) {
                String bin = Integer.toBinaryString(i);
                while (bin.length() < 3) {
                    bin = "0" + bin;
                }
                String fullBin = state + bin;

                BigInteger val = new BigInteger(fullBin, 2);
                // could probably generalize this now that I know it works but so tired
                if (BigInteger.valueOf(instruction)
                        .equals(val.mod(EIGHT).xor(SIX).xor(
                                val.divide(BigInteger.TWO.pow(val.mod(EIGHT).xor(SIX).intValue()))
                                        .xor(FOUR).mod(EIGHT)))) {
                    searchBinary.add(fullBin);
                }
            }
        }

        return results.stream().map(s -> new BigInteger(s, 2)).min(BigInteger::compareTo).get();
    }

}
