package org.example.core.day3;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class GearRatiosTest {
    @Test
    void acceptance_test() {
        var content = """
                467..114..
                ...*......
                ..35..633.
                ......#...
                617*......
                .....+.58.
                ..592.....
                ......755.
                ...$.*....
                .664.598..
                """;

        assertThat(GearRatios.sumOfPartNumbers(content)).isEqualTo(4361);
    }

    @Test
    void should_return_2_when_first_line_content_two_1_and_in_between_symbol() {
        var content = """
                1*1.......
                ..........
                ..........
                """;

        assertThat(GearRatios.sumOfPartNumbers(content)).isEqualTo(2);
    }

    @Test
    void acceptance_test2() {
        var content = """
                467..114..
                ...*......
                ..35..633.
                ......#...
                617*......
                .....+.58.
                ..592.....
                ......755.
                ...$.*....
                .664.598..
                """;

        assertThat(GearRatios.sumOfGearRatios(content)).isEqualTo(467835);
    }
}