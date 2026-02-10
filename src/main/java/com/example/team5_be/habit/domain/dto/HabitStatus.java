package com.example.team5_be.habit.domain.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum HabitStatus {
    NOT_STARTED("미시작"),
    IN_PROGRESS("진행중"),
    COMPLETED("완료");

    private final String value;

    HabitStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static HabitStatus from(String input) {
        if (input == null) return null;
        return Arrays.stream(values())
                .filter(v -> v.value.equals(input))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid status: " + input));
    }
}
