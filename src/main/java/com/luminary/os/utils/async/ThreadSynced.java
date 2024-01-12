package com.luminary.os.utils.async;

public class ThreadSynced<T> {
    private T data;
    private volatile boolean dirty;

    public synchronized void set(T newData) {
        this.data = newData;
        dirty = true;
        notifyAll();
    }

    public synchronized T get() {
        while(!dirty) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return data;
    }
}
