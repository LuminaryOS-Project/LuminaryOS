package com.luminary.os.utils;

import com.google.common.collect.ImmutableList;

public class Tuple<T> {
    ImmutableList<T> list;
    @SafeVarargs
    public Tuple(T... o) {
        list = ImmutableList.copyOf(o);
    }
    public T get(int i) {
        return list.get(i);
    }
}
