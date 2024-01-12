package com.luminary.os.utils;

public class Spinner {
    private int idx = 0;
    private Thread spinThread;
    private final String[] text;
    private final int delay;
    public Spinner(Pair<String[], Integer> pair) {
        this.text = pair.getFirst();
        this.delay = pair.getSecond();
    }

    public void start(String prompt) {
        if(spinThread != null && spinThread.isAlive()) {
            throw new IllegalArgumentException("Spinner has already been started");
        }
        spinThread = new Thread(() -> {
            try {
                for (;;) {
                    System.out.print(prompt + text[idx] + "\r");
                    System.out.flush();
                    idx = (idx + 1) % text.length;
                    Thread.sleep(delay);
                }
            } catch (InterruptedException ignored) {}
        });
        spinThread.start();
    }

    public void stop() {
        spinThread.interrupt();
        spinThread=null;
    }
}
