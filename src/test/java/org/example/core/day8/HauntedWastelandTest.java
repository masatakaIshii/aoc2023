package org.example.core.day8;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HauntedWastelandTest {
    @Test
    void acceptance_test() {
        var content = """
LLR

AAA = (BBB, BBB)
BBB = (AAA, ZZZ)
ZZZ = (ZZZ, ZZZ)
                """;
        assertThat(HauntedWasteland.getStepsToReachZZZ(content)).isEqualTo(6);
    }

    @Test
    void acceptance_test2() {
        var content = """
LR

11A = (11B, XXX)
11B = (XXX, 11Z)
11Z = (11B, XXX)
22A = (22B, XXX)
22B = (22C, 22C)
22C = (22Z, 22Z)
22Z = (22B, 22B)
XXX = (XXX, XXX)
                """;

        assertThat(HauntedWasteland.getStepsToReachAllNodesEndedByZ(content)).isEqualTo(6);
    }
}