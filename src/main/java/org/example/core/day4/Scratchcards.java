package org.example.core.day4;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class Scratchcards {

    private Scratchcards() {
        throw new IllegalStateException("Utility class");
    }

    public static long getTotalPoints(String content) {
        return Arrays.stream(content.split("\n"))
                .map(Card::fromLine)
                .mapToLong(Card::getPoints)
                .sum();
    }

    public static long getTotalScratchcards(String content) {
        var cards = Arrays.stream(content.split("\n"))
                .map(Card::fromLine)
                .toList();

        return Card.getTotalScratchcards(cards);
    }
}

record Card(long id, List<Integer> winningNumbers, List<Integer> ourNumbers) {
    static Card fromLine(String line) {
        var cardIdAndNumbers = line.split(":");
        var cardId = toId(cardIdAndNumbers);
        var winningAndOurNumbers = cardIdAndNumbers[1].split("\\|");
        var winningNumbers = getOurNumbers(winningAndOurNumbers[0]);
        var ourNumbers = getOurNumbers(winningAndOurNumbers[1]);

        return new Card(cardId, winningNumbers, ourNumbers);
    }

    private static long toId(String[] cardIdAndNumbers) {
        var split = cardIdAndNumbers[0].split(" ");
        return Long.parseLong(split[split.length - 1].trim());
    }

    private static List<Integer> getOurNumbers(String strNumbers) {
        return Arrays.stream(strNumbers.trim().split(" "))
                .filter(numberStr -> !numberStr.isBlank())
                .map(Integer::parseInt)
                .toList();
    }

    static long getPoints(Card card) {
        var count = getOurWinningNumbers(card) - 1;
        return (long) Math.pow(2, count);
    }

    private static long getOurWinningNumbers(Card card) {
        return card.ourNumbers.stream()
                .filter(card.winningNumbers::contains)
                .count();
    }

    static long getTotalScratchcards(List<Card> cards) {
        return getInstance(cards);
    }

    private static long getInstance(List<Card> cards) {
        long result = 0;
        Map<Long, Long> copies = new HashMap<>();

        for (Card card : cards) {
            var numberInstance = getNumberInstance(copies, card.id());
            copies = mergeCopies(
                    getFilteredCopies(copies, card),
                    getNumbersToMerge(card, getOurWinningNumbers(card)),
                    numberInstance
            );
            result += numberInstance;
        }

        return result;
    }

    private static HashMap<Long, Long> getFilteredCopies(Map<Long, Long> copies, Card first) {
        var filteredCopies = new HashMap<>(copies);
        filteredCopies.remove(first.id());
        return filteredCopies;
    }

    private static Map<Long, Long> getNumbersToMerge(Card first, long countWinningNumbers) {
        var result = new HashMap<Long, Long>();
        LongStream.rangeClosed(first.id() + 1L, first.id() + countWinningNumbers).boxed()
                .forEach(value -> result.put(value, 1L));
        return result;
    }

    private static long getNumberInstance(Map<Long, Long> copies, long first) {
        if (!copies.containsKey(first)) return 1;

        return copies.get(first) + 1;
    }

    private static Map<Long, Long> mergeCopies(Map<Long, Long> copies, Map<Long, Long> numbersToMerge, long count) {
        var newCopies = new HashMap<>(copies);
        IntStream.iterate(0, i -> i < count, i -> i + 1)
                .mapToObj(i -> numbersToMerge.entrySet().stream())
                .flatMap(Function.identity())
                .forEach(entry -> {
                    Long k = entry.getKey();
                    Long v = entry.getValue();
                    if (newCopies.containsKey(k)) {
                        newCopies.put(k, newCopies.get(k) + v);
                    } else {
                        newCopies.put(k, v);
                    }
                });
        return newCopies;
    }
}