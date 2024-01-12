package com.luminary.os.core;
import java.util.Arrays;

import static com.luminary.os.utils.Utils.clearScreen;

public class Screensaver {
    private Thread inThread;

    public Screensaver(String type) {
    }
    public void start() throws InterruptedException {
        float A = 0, B = 0;
        float[] z = new float[1760];
        char[] b = new char[1760];
        int frames = 0;
        long startTime = System.currentTimeMillis();

        for (;;) {
            Arrays.fill(b, ' ');
            Arrays.fill(z, 0);
            for (float j = 0; j < 6.28; j += 0.07F) {
                for (float i = 0; i < 6.28; i += 0.02F) {
                    float c = (float) Math.sin(i);
                    float d = (float) Math.cos(j);
                    float e = (float) Math.sin(A);
                    float f = (float) Math.sin(j);
                    float g = (float) Math.cos(A);
                    float h = d + 2;
                    float D = 1 / (c * h * e + f * g + 5);
                    float l = (float) Math.cos(i);
                    float m = (float) Math.cos(B);
                    float n = (float) Math.sin(B);
                    float t = c * h * g - f * e;
                    int x = 40 + (int) (30 * D * (l * h * m - t * n));
                    int y = 12 + (int) (15 * D * (l * h * n + t * m));
                    int o = x + 80 * y;
                    int N = (int) (8 * ((f * e - c * d * g) * m - c * d * e - f * g - l * d * n));
                    if (22 > y && y > 0 && x > 0 && 80 > x && D > z[o]) {
                        z[o] = D;
                        b[o] = ".,-~:;=!*#$@".charAt(Math.max(N, 0));
                    }
                }
            }
            long elapsedTime = System.currentTimeMillis() - startTime;
            double fps = (double) frames / (elapsedTime / 1000.0);
            System.out.printf("\033[H\033[2JFPS: %.2f\n", fps); // clear the screen and print FPS
            for (int k = 0; k < 1761; k++) {
                System.out.print(k % 80 == 0 ? "\n" : b[k]);
                A += 0.00004F;
                B += 0.00002F;
            }
            frames++;
            Thread.sleep(1000 / 24);
        }
    }
    public void stop() {
        inThread.stop();
        clearScreen();
    }
}
