package org.example.core.day1;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CalibrationValuesTest {
  @Test
  void acceptance_test() {
    var content = """
        1abc2
        pqr3stu8vwx
        a1b2c3d4e5f
        treb7uchet
        """;
    assertThat(CalibrationValues.getSum(content)).isEqualTo(142);
  }

  @Test
  void acceptance_test_2() {
    var content = """
        two1nine
        eightwothree
        abcone2threexyz
        xtwone3four
        4nineeightseven2
        zoneight234
        7pqrstsixteen
                """;
    assertThat(CalibrationValues.getSum(content)).isEqualTo(281);
  }

  @Test
  void should_return_29_when_content_is_two1nine() {
    var content = """
        two1nine
                """;
    assertThat(CalibrationValues.getSum(content)).isEqualTo(29);
  }

  @Test
  void should_return_38_when_content_is_threeight() {
    var content = """
        threeight
                """;
    assertThat(CalibrationValues.getSum(content)).isEqualTo(38);
  }
}