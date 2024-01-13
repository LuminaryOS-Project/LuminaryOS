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

package com.luminary.os.utils.versioning;

import lombok.AllArgsConstructor;


import java.util.function.BiPredicate;
import java.util.function.Predicate;

@AllArgsConstructor
public enum Versioning {
    EQUALS((required, provided) -> provided == required),
    GT((required, provided) -> required < provided),
    GTOE((required, provided) -> required <= provided),
    LT((required, provided) -> required > provided);
    private final BiPredicate<Integer, Integer> condititon;

    public static boolean test(String condition) {
        Versioning ver;
        final int javaVersion = Runtime.version().feature();
        int required;
        if(condition.startsWith(">=")) {
            required = asInt(condition.substring(2));
            ver = GTOE;
        } else if (condition.startsWith("==")) {
            required = asInt(condition.substring(2));
            ver = EQUALS;
        } else if (condition.startsWith(">")) {
            required = asInt(condition.substring(1));
            ver = GT;
        } else if (condition.startsWith("<")) {
            required = asInt(condition.substring(1));
            ver = LT;
        } else {
            throw new IllegalArgumentException("Invalid versioning string provided");
        }
        return ver.condititon.test(required, javaVersion);
    }

    private static int asInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }
}
