package org.example.core.day1;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class CalibrationValues {

  private CalibrationValues() {
    throw new IllegalStateException("Utility class");
  }

  public static long getSum(String documentContent) {
    return Arrays.stream(documentContent.split("\n"))
        .map(CalibrationValues::mapNumberStrToDigit)
        .map(CalibrationValues::getDigitsInLine)
        .mapToLong(CalibrationValues::getFirstAndLastDigits)
        .sum();
  }

  private static String mapNumberStrToDigit(String line) {
    var map = Map.of(
        "one", "o1e",
        "two", "t2o",
        "three", "t3e",
        "four", "f4r",
        "five", "f5e",
        "six", "s6x",
        "seven", "s7n",
        "eight", "e8t",
        "nine", "n9e"
    );
    return transformStrNumbersToNumbers(line, map);
  }

  private static String transformStrNumbersToNumbers(String line, Map<String, String> map) {
    if (map.keySet().stream().filter(line::contains).findAny().isEmpty()) {
      return line;
    }
    return getFirstNumber(line, map.keySet())
        .map(firstNumber -> line.replace(firstNumber, map.get(firstNumber)))
        .map(replacedFirstNumberLine -> transformStrNumbersToNumbers(replacedFirstNumberLine, map))
        .orElse(line);
  }

  private static Optional<String> getFirstNumber(String line, Set<String> numbersStr) {
    return numbersStr.stream()
        .filter(line::contains)
        .map(numStr -> new NumberStringAndIndex(numStr, line.indexOf(numStr)))
        .min(Comparator.comparingInt(NumberStringAndIndex::index))
        .map(NumberStringAndIndex::numberString);
  }

  private static List<Long> getDigitsInLine(String documentContent) {
    return Arrays.stream(documentContent.split(""))
        .filter(letter -> Character.isDigit(letter.charAt(0)))
        .map(Long::valueOf)
        .toList();
  }

  private static long getFirstAndLastDigits(List<Long> digits) {
    return Long.parseLong("%d%d".formatted(digits.getFirst(), digits.getLast()));
  }
}

record NumberStringAndIndex(String numberString, int index) {

}
