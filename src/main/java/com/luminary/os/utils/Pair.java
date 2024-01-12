package com.luminary.os.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
@AllArgsConstructor
public class Pair<T, V> {
    T first;
    V second;
}
