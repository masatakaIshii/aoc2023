package org.example.core.day6;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.example.core.day6.TimeAndDistance.buildLongRageTimeAndDistance;
import static org.example.core.day6.TimeAndDistance.buildTimesAndDistances;
import static org.example.core.day6.TimeAndDistance.numberWaysToBeat;

public class BoatRaces {
    private BoatRaces() {
        throw new IllegalStateException("Utility class");
    }

    public static long multiplyNumberWaysToBeatRecord(String content) {
        return buildTimesAndDistances(content)
                .stream()
                .map(TimeAndDistance::numberWaysToBeat)
                .reduce(1L, (total, acc) -> total * acc);
    }

    public static long numberWaysToBeatRecordLongRace(String content) {
        return numberWaysToBeat(buildLongRageTimeAndDistance(content));
    }
}

record TimeAndDistance(long time, long distanceToBeat) {
    static TimeAndDistance buildLongRageTimeAndDistance(String content) {
        var timesStrAndDistancesStr = content.split("\n");
        var time = concatNumbers(toNumbers(timesStrAndDistancesStr[0]));
        var distance = concatNumbers(toNumbers(timesStrAndDistancesStr[1]));

        return new TimeAndDistance(time, distance);
    }

    static List<TimeAndDistance> buildTimesAndDistances(String content) {
        var timesStrAndDistancesStr = content.split("\n");
        var times = toNumbers(timesStrAndDistancesStr[0]);
        var distances = toNumbers(timesStrAndDistancesStr[1]);
        return IntStream.range(0, times.size())
                .mapToObj(i -> new TimeAndDistance(times.get(i), distances.get(i)))
                .toList();
    }

    private static Long concatNumbers(List<Long> numbers) {
        return Long.parseLong(numbers.stream()
                .map(String::valueOf)
                .collect(Collectors.joining("")));
    }

    private static List<Long> toNumbers(String timesAndDistances) {
        return Arrays.stream(timesAndDistances.split("\\s+"))
                .filter(content -> Character.isDigit(content.charAt(0)))
                .map(Long::parseLong)
                .toList();
    }

    static long numberWaysToBeat(TimeAndDistance timeAndDistance) {
        return LongStream.range(1, timeAndDistance.time())
                .filter(holdTime -> (timeAndDistance.time() - holdTime) * holdTime > timeAndDistance.distanceToBeat())
                .count();
    }
}