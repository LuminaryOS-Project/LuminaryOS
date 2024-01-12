package com.luminary.os.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.NoSuchElementException;

@Getter
@AllArgsConstructor
public enum StatusCode {
    SUCCESS(100),
    FAILURE(101),
    DENIED(102),
    INSUFFICIENT_PERMISSION(103),
    INVALID_ARGUMENT(104),
    TIMEOUT(105),
    INVALID_HANDLE(106),
    ACCESS_DENIED(107),
    INTERRUPTED(108),
    OPERATION_UNSUPPORTED(109),
    ILLEGAL(110);
    final int code;
    public static StatusCode of(int toFind) {
        if(toFind > 110 || toFind < 100) {
            throw new NoSuchElementException();
        }
        return Arrays.stream(StatusCode.values()).filter(code -> code.getCode() == toFind).toList().get(0);
    }
}
