package com.luminary.os.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

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
    @Getter
    final int code;
    public StatusCode of(int toFind) {
        return Arrays.stream(StatusCode.values()).filter(code -> code.getCode() == toFind).toList().get(0);
    }
}
