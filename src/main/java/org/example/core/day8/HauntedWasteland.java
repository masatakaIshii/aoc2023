package org.example.core.day8;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HauntedWasteland {
    public static long getStepsToReachZZZ(String content) {
        String[] splitContent = content.split("\n");

        String instructions = splitContent[0].trim();
        Map<String, Node> map = Arrays.stream(splitContent).skip(2)
                .map(Node::build)
                .collect(Collectors.toMap(Node::value, Function.identity(), (existing, replacement) -> existing));

        return getSteps(map, instructions);
    }

    private static long getSteps(Map<String, Node> map, String instructions) {
        Node currentNode = map.get("AAA");
        long numberSteps = 0;

        while (!currentNode.value().equals("ZZZ")) {
            for (Character instruction : instructions.toCharArray()) {
                if (instruction == 'L') {
                    currentNode = map.get(currentNode.left());
                }
                if (instruction == 'R') {
                    currentNode = map.get(currentNode.right());
                }
                numberSteps++;
            }
        }

        return numberSteps;
    }

    public static long getStepsToReachAllNodesEndedByZ(String content) {
        String[] splitContent = content.split("\n");

        String instructions = splitContent[0].trim();
        Map<String, Node> map = Arrays.stream(splitContent).skip(2)
                .map(Node::build)
                .collect(Collectors.toMap(Node::value, Function.identity(), (existing, replacement) -> existing));
        List<Node> concernedNode = map.values().stream().filter(node -> node.value().endsWith("A")).toList();

        List<Long> lengthsOfNodesToReachZ = concernedNode.stream().map(node -> getStepToReachInNodeEndedZ(node, map, instructions)).map(value -> (long) value).toList();

        return lengthsOfNodesToReachZ.stream().reduce(HauntedWasteland::lcm).orElseThrow();
    }

    private static long lcm(long first, long second) {
        return first * second / gcd(first, second);
    }

    private static long gcd(long first, long second) {
        var reminder = first % second;
        return reminder == 0 ? second : gcd(second, reminder);
    }

    private static int getStepToReachInNodeEndedZ(Node node, Map<String, Node> map, String instructions) {
        int result = 0;
        Node currentNode = node;

        while (!currentNode.value().endsWith("Z")) {
            for (char instruction : instructions.toCharArray()) {
                if (instruction == 'L') {
                    currentNode = map.get(currentNode.left());
                } else {
                    currentNode = map.get(currentNode.right());
                }
                result++;
                if (currentNode.value().endsWith("Z")) {
                    return result;
                }
            }
        }

        return result;
    }
}

record Node(String value, String left, String right) {
    static Node build(String line) {
        String value = line.substring(0, 3);
        String left = line.substring(line.indexOf('(') + 1, line.indexOf(','));
        String right = line.substring(line.indexOf(", ") + 2, line.indexOf(')'));

        return new Node(value, left, right);
    }
}