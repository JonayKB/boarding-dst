package it.dst.garage.enums;

import java.util.Arrays;

public enum CarSortField {
    CREATED_AT("createdAt"),
    BRAND("brand"),
    MODEL("model"),
    YEAR("year"),
    PLATE("plate");

    private final String fieldName;

    CarSortField(String fieldName) {
        this.fieldName = fieldName;
    }

    public static String resolve(String input) {
        if (input == null)
            return CREATED_AT.fieldName;

        return Arrays.stream(values())
                .filter(f -> f.fieldName.equalsIgnoreCase(input))
                .map(f -> f.fieldName)
                .findFirst()
                .orElse(CREATED_AT.fieldName);
    }
}