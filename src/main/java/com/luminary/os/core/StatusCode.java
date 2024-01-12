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
            throw new NoSuchElementException("Tried to fetch invalid status code");
        }

        return Arrays.stream(StatusCode.values()).filter(code -> code.getCode() == toFind).toList().get(0);
    }

}
