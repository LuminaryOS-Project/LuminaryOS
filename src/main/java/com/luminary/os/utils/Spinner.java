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
