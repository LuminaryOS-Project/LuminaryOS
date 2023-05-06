package com.luminary.os.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
public class Pair<T, V> {
    @Getter
    T first;
    @Getter
    V second;
}
