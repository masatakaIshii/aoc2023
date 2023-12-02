package org.example.core.day2;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.example.core.day2.Color.BLUE;
import static org.example.core.day2.Color.GREEN;
import static org.example.core.day2.Color.RED;

public class CubeConundrum {
  private CubeConundrum() {
    throw new IllegalStateException("Utility class");
  }

  public static long sumOfPossibleGames(String content) {
    return Arrays.stream(content.split("\n"))
        .map(Game::fromLine)
        .filter(Game::isPossible)
        .mapToInt(Game::id)
        .sum();
  }

  public static long sumOfPowerOfSets(String content) {
    return Arrays.stream(content.split("\n"))
        .map(Game::fromLine)
        .mapToLong(Game::powerOfSets)
        .sum();
  }
}

record Game(int id, List<Cubes> cubesList) {
  static Game fromLine(String line) {
    var gameAndCubesParts = line.split(":");

    return new Game(
        Integer.parseInt(gameAndCubesParts[0].split(" ")[1]),
        toCubesList(gameAndCubesParts[1])
    );
  }

  private static List<Cubes> toCubesList(String cubesListStr) {
    return Arrays.stream(cubesListStr.trim().split(";"))
        .flatMap(setStr -> Arrays.stream(setStr.split(",")).map(String::trim))
        .map(Game::toCubes)
        .toList();
  }

  private static Cubes toCubes(String cubesStr) {
    var numberAndColor = cubesStr.split(" ");
    return new Cubes(Integer.parseInt(numberAndColor[0]), getColor(numberAndColor[1]));
  }

  private static Color getColor(String colorStr) {
    return switch (colorStr) {
      case "red" -> RED;
      case "blue" -> BLUE;
      case "green" -> GREEN;
      default -> throw new IllegalArgumentException("color %s doesn't exist".formatted(colorStr));
    };
  }

  static boolean isPossible(Game game) {
    return game.cubesList().stream().allMatch(cubes -> switch (cubes.color()) {
      case RED -> cubes.number() <= 12;
      case GREEN -> cubes.number() <= 13;
      case BLUE -> cubes.number() <= 14;
    });
  }

  public static long powerOfSets(Game game) {
    return game.cubesList().stream()
        .collect(Collectors
            .groupingBy(
                Cubes::color,
                Collectors.maxBy(Comparator.comparingInt(Cubes::number))))
        .values()
        .stream().filter(Optional::isPresent)
        .map(Optional::get)
        .mapToInt(Cubes::number)
        .reduce(1, (acc, sum) -> acc * sum);
  }
}

enum Color {
  RED, GREEN, BLUE
}

record Cubes(int number, Color color) {

}