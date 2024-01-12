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

package com.luminary.os.utils.async;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public class Promise<T> {
    private T result;
    private Exception exception;
    private boolean isResolved;
    private final List<Consumer<T>> thenCallbacks;
    private final List<Consumer<Exception>> catchCallbacks;

    public Promise() {
        thenCallbacks = new ArrayList<>();
        catchCallbacks = new ArrayList<>();
    }

    public void resolve(T result) {
        if (isResolved) {
            throw new IllegalStateException("Promise has already been resolved.");
        }
        this.result = result;
        this.isResolved = true;
        for (Consumer<T> callback : thenCallbacks) {
            callback.accept(result);
        }
    }

    public void reject(Exception exception) {
        if (isResolved) {
            throw new IllegalStateException("Promise has already been resolved.");
        }
        this.exception = exception;
        this.isResolved = true;
        for (Consumer<Exception> callback : catchCallbacks) {
            callback.accept(exception);
        }
    }

    public <U> Promise<U> then(Function<T, U> callback) {
        Promise<U> promise = new Promise<>();
        if (isResolved) {
            U result = callback.apply(this.result);
            promise.resolve(result);
        } else {
            thenCallbacks.add(result -> CompletableFuture.supplyAsync(() -> callback.apply(result)).thenAccept(promise::resolve));
        }
        return promise;
    }

    public Promise<T> catchException(Consumer<Exception> callback) {
        if (isResolved && exception != null) {
            callback.accept(exception);
        } else {
            catchCallbacks.add(callback);
        }
        return this;
    }
}
