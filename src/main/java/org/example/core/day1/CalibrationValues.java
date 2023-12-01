package org.example.core.day1;

import java.util.Arrays;
import java.util.List;

public class CalibrationValues {

  private CalibrationValues() {

  }
  public static long getSum(String documentContent) {
    return Arrays.stream(documentContent.split("\n"))
        .map(CalibrationValues::getDigitsInLine)
        .mapToLong(CalibrationValues::getFirstAndLastDigits)
        .sum();
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
