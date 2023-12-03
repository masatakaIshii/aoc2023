package org.example.core.day3;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.core.day3.Case.isNotPoint;
import static org.example.core.day3.Cases.getCase;
import static org.example.core.day3.Cases.toListNumberCases;

public class GearRatios {

    private GearRatios() {
        throw new IllegalStateException("Utility class");
    }

    public static long sumOfPartNumbers(String content) {
        Cases cases = Cases.from(content);

        return PartNumbersCases.from(cases)
                .listNumberCases()
                .stream()
                .mapToLong(NumberCases::toNumber)
                .sum();
    }

    public static long sumOfGearRatios(String content) {
        Cases cases = Cases.from(content);

        return toStarSymbolCases(cases)
                .stream()
                .map(starSymbolCase -> find2NearNumbers(starSymbolCase, PartNumbersCases.from(cases)))
                .filter(list -> !list.isEmpty())
                .mapToLong(numbers -> numbers.get(0) * numbers.get(1))
                .sum();
    }

    private static List<Case> toStarSymbolCases(Cases cases) {
        return cases
                .value()
                .stream()
                .filter(currentCase -> currentCase.value() == '*')
                .toList();
    }

    private static List<Long> find2NearNumbers(Case starSymbolCase, PartNumbersCases partNumberCases) {
        List<Long> nearNumbers = new ArrayList<>(partNumberCases
                .listNumberCases()
                .stream()
                .filter(numberCases -> numberCases.cases().stream()
                        .anyMatch(numberCase -> isNearTo(starSymbolCase, numberCase)))
                .map(NumberCases::toNumber)
                .toList());

        if (nearNumbers.size() < 2) {
            return List.of();
        }
        return nearNumbers;
    }

    private static boolean isNearTo(Case starSymbolCase, Case numberCase) {
        if (starSymbolCase.height() + 1 == numberCase.height()) {
            return isSameOrCloseWidth(starSymbolCase, numberCase);
        } else if (starSymbolCase.height() - 1 == numberCase.height()) {
            return isSameOrCloseWidth(starSymbolCase, numberCase);
        } else if (starSymbolCase.height() == numberCase.height()) {
            return starSymbolCase.width() - 1 == numberCase.width() ||
                    starSymbolCase.width() + 1 == numberCase.width();
        }
        return false;
    }

    private static boolean isSameOrCloseWidth(Case starSymbolCase, Case numberCase) {
        return starSymbolCase.width() - 1 == numberCase.width() ||
                starSymbolCase.width() == numberCase.width() ||
                starSymbolCase.width() + 1 == numberCase.width();
    }
}

record PartNumbersCases(List<NumberCases> listNumberCases){

    static PartNumbersCases from(Cases cases) {
        return new PartNumbersCases(toListNumberCases(cases).stream()
                .filter(currentNumberCases -> isNumberCasesPartNumber(currentNumberCases, cases))
                .toList());
    }

    static boolean isNumberCasesPartNumber(NumberCases numberCases, Cases allCases) {
        Case firstDigitCase = numberCases.cases().getFirst();
        if (isFirstDigitCaseLeftPartsHaveSymbol(firstDigitCase, allCases)) {
            return true;
        }
        Case lastDigitCase = numberCases.cases().getLast();
        if (isLastDigitCaseRightPartsHaveSymbol(lastDigitCase, allCases)) {
            return true;
        }

        return numberCases.cases().stream()
                .anyMatch(numberCase -> isDigitCaseHasSymbolOnTopOrDown(numberCase, allCases));
    }

    private static boolean isLastDigitCaseRightPartsHaveSymbol(Case lastDigitCase, Cases allCases) {
        if (lastDigitCase.width() < allCases.widthMax()) {
            if (lastDigitCase.height() > 0 && isNotPoint(getCase(allCases, lastDigitCase.height() - 1, lastDigitCase.width() + 1))) {
                return true;
            }
            if (lastDigitCase.height() < allCases.heightMax() && isNotPoint(getCase(allCases, lastDigitCase.height() + 1, lastDigitCase.width() + 1))) {
                return true;
            }
            return isNotPoint(getCase(allCases, lastDigitCase.height(), lastDigitCase.width() + 1));
        }
        return false;
    }

    private static boolean isFirstDigitCaseLeftPartsHaveSymbol(Case firstDigitCase, Cases allCases) {
        if (firstDigitCase.width() > 0) {
            if (isNotPoint(getCase(allCases, firstDigitCase.height(), firstDigitCase.width() - 1))) {
                return true;
            }
            if (firstDigitCase.height() > 0 && isNotPoint(getCase(allCases, firstDigitCase.height() - 1, firstDigitCase.width() - 1))) {
                return true;
            }
            return firstDigitCase.height() < allCases.heightMax() &&
                    isNotPoint(getCase(allCases,
                            firstDigitCase.height() + 1,
                            firstDigitCase.width() - 1));
        }
        return false;
    }

    private static boolean isDigitCaseHasSymbolOnTopOrDown(Case digitCase, Cases allCases) {
        if (digitCase.height() < allCases.heightMax() &&
                isNotPoint(getCase(allCases, digitCase.height() + 1, digitCase.width()))) {
            return true;
        }
        return digitCase.height() > 0 && isNotPoint(getCase(allCases, digitCase.height() - 1, digitCase.width()));
    }
}

record Cases(List<Case> value, int heightMax, int widthMax) {
    static Cases from(String content) {
        var split = content.split("\n");
        List<Case> cases = new ArrayList<>();
        int heightMax = split.length - 1;
        int widthMax = heightMax > 0 ? split[0].length() - 1 : 0;

        for (int height = 0; height < split.length; height++) {
            for (int width = 0; width < split[height].length(); width++) {
                cases.add(new Case(split[height].charAt(width), height, width));
            }
        }

        return new Cases(cases, heightMax, widthMax);
    }

    static List<NumberCases> toListNumberCases(Cases cases) {
        List<NumberCases> listNumberCases = new ArrayList<>();
        List<Case> numberCases = new ArrayList<>();
        for (Case currentCase : cases.value()) {
            if (Character.isDigit(currentCase.value())) {
                numberCases.add(currentCase);
            } else if (!numberCases.isEmpty() && !Character.isDigit(currentCase.value())) {
                listNumberCases.add(new NumberCases(numberCases));
                numberCases = new ArrayList<>();
            }
        }
        if (!numberCases.isEmpty()) {
            listNumberCases.add(new NumberCases(numberCases));
        }
        return listNumberCases;
    }

    static Case getCase(Cases cases, int height, int width) {
        return cases
                .value()
                .stream()
                .filter(currentCase -> currentCase.height() == height && currentCase.width() == width)
                .findAny()
                .orElseThrow();
    }
}

record NumberCases(List<Case> cases) {
    static long toNumber(NumberCases numberCases) {
        var numberStr = numberCases.cases().stream()
                .map(Case::value)
                .map(String::valueOf)
                .collect(Collectors.joining(""));
        return Long.parseLong(numberStr);
    }
}

record Case(char value, int height, int width) {
    static boolean isNotPoint(Case currentCase) {
        return currentCase.value() != '.';
    }
}