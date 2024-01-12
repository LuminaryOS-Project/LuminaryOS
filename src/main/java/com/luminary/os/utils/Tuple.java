/*
 * Copyright (c) 2024. Intel
 *
 * This file is part of LuminaryOS
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.luminary.os.utils;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class Tuple<T> implements Iterable<T> {
    final ImmutableList<T> list;
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
