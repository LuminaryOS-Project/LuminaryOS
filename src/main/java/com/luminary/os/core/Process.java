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

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@Getter
@RequiredArgsConstructor
public class Process {
    private final @NotNull Thread thread;
    @Setter
    private Priority priority = Priority.NORMAL;
    private final long timeoutMillis;
    @Setter
    private int id;
    private @NotNull Optional<Callback> callback = Optional.empty();
    public void setCallback(@NotNull Callback callback) {
        this.callback = Optional.of(callback);
    }
    public Thread start() {
        if(!thread.isAlive()) {
            thread.setPriority(1);
            thread.start();
        }
        return thread;

    }

    public void safeStop() {
        if(thread.isAlive()) {
            thread.interrupt();
            callback.ifPresent(callback -> callback.accept(StatusCode.SUCCESS));
        }
    }
    //
    public void safeStop(@NotNull StatusCode sc) {
        if(thread.isAlive()) {
            thread.interrupt();
            callback.ifPresent(callback -> callback.accept(sc));
        }
    }
    //
    @SuppressWarnings("deprecation")
    public void stop() {
        if(thread.isAlive()) {
            thread.stop();
            callback.ifPresent(callback -> callback.accept(StatusCode.SUCCESS));
        }
    }

    @SuppressWarnings("deprecation")
    public void stop(@NotNull StatusCode sc) {
        if(thread.isAlive()) {
            thread.stop();
            callback.ifPresent(callback -> callback.accept(sc));
        }
    }


}