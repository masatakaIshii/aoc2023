package org.example.core.day7;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CamelCardsTest {
    @Test
    void acceptance_test() {
        var content = """
32T3K 765
T55J5 684
KK677 28
KTJJT 220
QQQJA 483
                """;
        assertThat(CamelCards.getTotalWinnings(content)).isEqualTo(6440);
    }

    @Test
    void acceptance_test2() {
        var content = """
32T3K 765
T55J5 684
KK677 28
KTJJT 220
QQQJA 483
                """;
        assertThat(CamelCards.getTotalWinningsWithJoker(content)).isEqualTo(5905);
    }
}