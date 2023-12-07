package org.example.core.day6;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class BoatRacesTest {

    @Test
    void acceptance_test() {
        var content = """
                Time:      7  15   30
                Distance:  9  40  200
                """;
        assertThat(BoatRaces.multiplyNumberWaysToBeatRecord(content)).isEqualTo(288);
    }

    @Test
    void acceptance_test2() {
        var content = """
                Time:      7  15   30
                Distance:  9  40  200
                """;
        assertThat(BoatRaces.numberWaysToBeatRecordLongRace(content)).isEqualTo(71503);
    }
}