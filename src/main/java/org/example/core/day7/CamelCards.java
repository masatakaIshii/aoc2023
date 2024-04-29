package org.example.core.day7;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class CamelCards {
    private CamelCards() {

    }
    public static long getTotalWinnings(String content) {
        Map<String, Integer> mapCardValue = getStringIntegerMap();
        List<CardsHandWithType> cardsHandWithTypes = Arrays.stream(content.split("\n"))
                .map(line -> line.split(" "))
                .map(splitLine -> new CardsHand(splitLine[0], Long.parseLong(splitLine[1])))
                .map(CardsHandWithType::from)
                .toList();
        List<CardsHandWithType> test = cardsHandWithTypes
                .stream()
                .sorted((cards1, cards2) -> compareCards(cards1, cards2, mapCardValue)).toList();
        return LongStream.range(0, test.size())
                .reduce(0, (total, index) -> total + test.get((int) index).bid() * (index + 1));
    }

    private static int compareCards(CardsHandWithType cards1, CardsHandWithType cards2, Map<String, Integer> mapCardValue) {
        if (cards1.cardType().getCardTypePower() < cards2.cardType().getCardTypePower()) {
            return -1;
        }
        if (cards1.cardType().getCardTypePower() > cards2.cardType().getCardTypePower()) {
            return 1;
        }
        List<Integer> cards1Values = Arrays.stream(cards1.cards().split(""))
                .map(mapCardValue::get).toList();
        List<Integer> cards2Values = Arrays.stream(cards2.cards().split(""))
                .map(mapCardValue::get).toList();
        for (int i = 0; i < cards1Values.size(); i++) {
            if (cards1Values.get(i) < cards2Values.get(i)) return -1;
            if (cards1Values.get(i) > cards2Values.get(i)) return 1;
        }
        return 0;
    }

    private static Map<String, Integer> getStringIntegerMap() {
        Map<String, Integer> mapCardValue = new HashMap<>();
        mapCardValue.put("A", 14);
        mapCardValue.put("K", 13);
        mapCardValue.put("Q", 12);
        mapCardValue.put("J", 11);
        mapCardValue.put("T", 10);
        mapCardValue.put("9", 9);
        mapCardValue.put("8", 8);
        mapCardValue.put("7", 7);
        mapCardValue.put("6", 6);
        mapCardValue.put("5", 5);
        mapCardValue.put("4", 4);
        mapCardValue.put("3", 3);
        mapCardValue.put("2", 2);
        return mapCardValue;
    }

    public static long getTotalWinningsWithJoker(String content) {
        Map<String, Integer> mapCardValue = getStringIntegerMapWithJoker();
        List<CardsHandWithType> cardsHandWithTypes = Arrays.stream(content.split("\n"))
                .map(line -> line.split(" "))
                .map(splitLine -> new CardsHand(splitLine[0], Long.parseLong(splitLine[1])))
                .map(CardsHandWithType::fromWithJokers)
                .toList();
        List<CardsHandWithType> test = cardsHandWithTypes
                .stream()
                .sorted((cards1, cards2) -> compareCards(cards1, cards2, mapCardValue)).toList();
        return LongStream.range(0, test.size())
                .reduce(0, (total, index) -> total + test.get((int) index).bid() * (index + 1));
    }

    private static Map<String, Integer> getStringIntegerMapWithJoker() {
        Map<String, Integer> mapCardValue = new HashMap<>();
        mapCardValue.put("A", 14);
        mapCardValue.put("K", 13);
        mapCardValue.put("Q", 12);
        mapCardValue.put("T", 10);
        mapCardValue.put("9", 9);
        mapCardValue.put("8", 8);
        mapCardValue.put("7", 7);
        mapCardValue.put("6", 6);
        mapCardValue.put("5", 5);
        mapCardValue.put("4", 4);
        mapCardValue.put("3", 3);
        mapCardValue.put("2", 2);
        mapCardValue.put("J", 1);
        return mapCardValue;
    }
}

record CardsHand(String cards, long bid) {

}

record CardsHandWithType(String cards, long bid, CardType cardType) {
    static CardsHandWithType from(CardsHand cardsHand) {
        CardType type = getCardType(cardsHand.cards());
        return new CardsHandWithType(cardsHand.cards(), cardsHand.bid(), type);
    }

    static CardsHandWithType fromWithJokers(CardsHand cardsHand) {
        CardType type = getCardTypeWithJoker(cardsHand.cards());
        return new CardsHandWithType(cardsHand.cards(), cardsHand.bid(), type);
    }

    private static CardType getCardTypeWithJoker(String cards) {
        if (!cards.contains("J")) return getCardType(cards);
        Map<String, Long> cardAndCount = Arrays.stream(cards.split("")).collect(Collectors.groupingBy(value -> value, Collectors.counting()));
        int numberJoker = cardAndCount.entrySet().stream()
                .filter(stringLongEntry -> stringLongEntry.getKey().contains("J"))
                .map(Map.Entry::getValue)
                .mapToInt(Long::intValue)
                .findAny().orElse(0);
        int max = cardAndCount.entrySet().stream()
                .filter(entry -> !entry.getKey().contains("J"))
                .map(Map.Entry::getValue)
                .mapToInt(Long::intValue)
                .max().orElse(0) + numberJoker;

        return switch (max) {
            case 2 -> CardType.ONE_PAIR;
            case 3 -> {
                var numberCardsOccurrenceWithoutJoker = cardAndCount.entrySet().stream()
                        .filter(entry -> !entry.getKey().contains("J")).count();
                if (numberCardsOccurrenceWithoutJoker == 2) yield CardType.FULL_HOUSE;
                yield CardType.THREE_OF_A_KIND;
            }
            case 4 -> CardType.FOUR_OF_A_KIND;
            case 5 -> CardType.FIVE_OF_A_KIND;
            default -> throw new IllegalStateException("Unexpected value: " + max);
        };
    }

    private static CardType getCardType(String cards) {
        Map<String, Long> cardAndCount = Arrays.stream(cards.split("")).collect(Collectors.groupingBy(value -> value, Collectors.counting()));
        int max = cardAndCount.values().stream().mapToInt(Long::intValue).max().orElseThrow();

        if (max == 3 && cardAndCount.size() == 2) {
            return CardType.FULL_HOUSE;
        }
        if (max == 2 && cardAndCount.size() == 3) {
            return CardType.TWO_PAIR;
        }
        return switch (max) {
            case 1 -> CardType.HIGH_CARD;
            case 2 -> CardType.ONE_PAIR;
            case 3 -> CardType.THREE_OF_A_KIND;
            case 4 -> CardType.FOUR_OF_A_KIND;
            case 5 -> CardType.FIVE_OF_A_KIND;
            default -> throw new IllegalStateException("Unexpected value: " + max);
        };
    }
}

enum CardType {
    FIVE_OF_A_KIND(7),
    FOUR_OF_A_KIND(6),
    FULL_HOUSE(5),
    THREE_OF_A_KIND(4),
    TWO_PAIR(3),
    ONE_PAIR(2),
    HIGH_CARD(1);

    private final int cardTypePower;

    CardType(int cardTypePower) {
        this.cardTypePower = cardTypePower;
    }

    public int getCardTypePower() {
        return cardTypePower;
    }
}