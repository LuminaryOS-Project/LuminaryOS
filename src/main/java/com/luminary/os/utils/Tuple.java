package com.luminary.os.utils;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class Tuple<T> implements Iterable<T> {
    ImmutableList<T> list;
    @SafeVarargs
    public Tuple(T... o) {
        list = ImmutableList.copyOf(o);
    }
    public T get(int i) {
        return list.get(i);
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return list.stream().iterator();
    }
}
