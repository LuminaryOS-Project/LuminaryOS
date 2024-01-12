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
