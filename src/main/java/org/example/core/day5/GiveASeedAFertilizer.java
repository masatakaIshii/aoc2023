package org.example.core.day5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.example.core.day5.RangesSeeds.buildRangesSeedslist;

public class GiveASeedAFertilizer {

    private GiveASeedAFertilizer() {
        throw new IllegalStateException("Utility class");
    }

    public static long rangeSeedsToLowerLocationNumber(String content) {
        var splitStr = Arrays.asList(content.split("\\w+-to-\\w+ map:\n"));
        var seedsStr = splitStr.getFirst().split("\\s");
        List<Long> seeds = buildSeeds(seedsStr);

        List<Maps> listMaps = splitStr.subList(1, splitStr.size())
                .stream()
                .map(Maps::fromContent)
                .toList();

        return updateAndGetElements(listMaps, seeds)
                .stream()
                .min(Comparator.comparingLong(seed -> seed))
                .orElseThrow();
    }

    private static List<Long> buildSeeds(String[] seedsStr) {
        return Arrays.stream(seedsStr)
                .filter(str -> !str.equals("seeds:"))
                .map(Long::parseLong)
                .toList();
    }

    private static List<Long> updateAndGetElements(List<Maps> listMaps, List<Long> elements) {
        List<Long> newElements = new ArrayList<>(elements);
        for (Maps maps : listMaps) {
            newElements = mapElements(newElements, maps.value());
        }
        return newElements;
    }

    private static List<Long> mapElements(List<Long> elements, List<Map> firstMaps) {
        return elements
                .stream()
                .map(element -> mapOneElement(firstMaps, element))
                .toList();
    }

    private static Long mapOneElement(List<Map> firstMaps, Long element) {
        return firstMaps.stream()
                .filter(map -> map.sourceRangeStart() <= element && element <= map.sourceRangeStart() + map.rangeLength())
                .findAny()
                .map(map -> element - map.sourceRangeStart() + map.destinationRangeStart())
                .orElse(element);
    }

    public static long getLowerLocationNumberToAnyInitSeedNumbers(String content) {
        var splitStr = Arrays.asList(content.split("\\w+-to-\\w+ map:\n"));
        var seedsStr = splitStr.getFirst().split("\\s");
        var listRangesSeeds = buildRangesSeedslist(seedsStr);

        List<Maps> listMaps = splitStr.subList(1, splitStr.size())
                .stream()
                .map(Maps::fromContent)
                .toList();

        return listRangesSeeds.stream()
                .map(rangesSeeds -> rangeSeedsToLowerLocationNumber(rangesSeeds, listMaps))
                .min(Comparator.comparingLong(result -> result))
                .orElseThrow();
    }

    private static long rangeSeedsToLowerLocationNumber(RangesSeeds rangesSeeds, List<Maps> listMaps) {
        long result = Long.MAX_VALUE;
        for (long i = rangesSeeds.start(); i < rangesSeeds.start() + rangesSeeds.length(); i++) {
            long elementValue = i;
            for (Maps maps : listMaps) {
                elementValue = mapOneElement(maps.value(), elementValue);
            }
            if (elementValue < result) {
                result = elementValue;
            }
        }
        return result;
    }


}

record Maps(List<Map> value) {
    static Maps fromContent(String content) {
        return new Maps(Arrays.stream(content.split("\n"))
                .map(Map::fromLine)
                .toList());
    }
}

record Map(long destinationRangeStart, long sourceRangeStart, long rangeLength) {
    static Map fromLine(String line) {
        var mapValues = Arrays.stream(line.split("\\s")).map(Long::parseLong).toList();
        return new Map(mapValues.get(0), mapValues.get(1), mapValues.get(2));
    }
}

record RangesSeeds(long start, long length) {
    static List<RangesSeeds> buildRangesSeedslist(String[] seedsStr) {
        var startAndLengthList = Arrays.stream(seedsStr)
                .filter(str -> !str.equals("seeds:"))
                .map(Long::parseLong)
                .toList();
        List<RangesSeeds> rangesSeedsList = new ArrayList<>();

        for (int i = 0; i < startAndLengthList.size() - 1; i += 2) {
            rangesSeedsList.add(new RangesSeeds(startAndLengthList.get(i), startAndLengthList.get(i + 1)));
        }

        return rangesSeedsList;
    }
}