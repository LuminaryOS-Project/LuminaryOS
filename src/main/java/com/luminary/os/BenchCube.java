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

package com.luminary.os;

import com.luminary.os.core.Native;

import java.util.Arrays;
class BenchCube {
    static float A, B, C;
    static float cubeWidth = 20;
    static final int width = 160;
    static final int height = 44;
    static final float[] zBuffer = new float[160 * 44];
    static final char[] buffer = new char[160 * 44];
    static final char backgroundASCIICode = ' ';
    static final int distanceFromCam = 100;
    static float horizontalOffset;
    static final float K1 = 40;
    static final float incrementSpeed = 1f;
    static float x, y, z;
    static float ooz;
    static int xp, yp;
    static int idx;
    static float calculateX(int i, int j, int k, float A, float B, float C) {
        return j * (float) Math.sin(A) * (float) Math.sin(B) * (float) Math.cos(C) -
                k * (float) Math.cos(A) * (float) Math.sin(B) * (float) Math.cos(C) +
                j * (float) Math.cos(A) * (float) Math.sin(C) + k * (float) Math.sin(A) * (float) Math.sin(C) +
                i * (float) Math.cos(B) * (float) Math.cos(C);
    }
    static float calculateY(int i, int j, int k, float A, float B, float C) {
        return j * (float) Math.cos(A) * (float) Math.cos(C) + k * (float) Math.sin(A) * (float) Math.cos(C) -
                j * (float) Math.sin(A) * (float) Math.sin(B) * (float) Math.sin(C) +
                k * (float) Math.cos(A) * (float) Math.sin(B) * (float) Math.sin(C) -
                i * (float) Math.cos(B) * (float) Math.sin(C);
    }
    static float calculateZ(int i, int j, int k, float A, float B, float C) {
        return k * (float) Math.cos(A) * (float) Math.cos(B) -
                j * (float) Math.sin(A) * (float) Math.cos(B) + i * (float) Math.sin(B);
    }
    static void calculateForSurface(float cubeX, float cubeY, float cubeZ, char ch) {
        if(Native.supportsNative() && Native.isLoaded()) {
            Native n = Native.getInstance();
            x = n.calculateX((int) cubeX, (int) cubeY, (int) cubeZ, A, B, C);
            y = n.calculateY((int) cubeX, (int) cubeY, (int) cubeZ, A, B, C);
            z = n.calculateZ((int) cubeX, (int) cubeY, (int) cubeZ, A, B, C) + distanceFromCam;
        } else {
            x = calculateX((int) cubeX, (int) cubeY, (int) cubeZ, A, B, C);
            y = calculateY((int) cubeX, (int) cubeY, (int) cubeZ, A, B, C);
            z = calculateZ((int) cubeX, (int) cubeY, (int) cubeZ, A, B, C) + distanceFromCam;
        }
        ooz = 1 / z;
        xp = (int) (width / 2 + horizontalOffset + K1 * ooz * x * 2);
        yp = (int) (height / 2 + K1 * ooz * y);
        idx = xp + yp * width;
        if (idx >= 0 && idx < width * height) {
            if (ooz > zBuffer[idx]) {
                zBuffer[idx] = ooz;
                buffer[idx] = ch;
            }
        }
    }
    public static void run() {
        System.out.print("\033[2J");
        long prevTime = System.currentTimeMillis();
        int frames = 0;
        while (true) {
            Arrays.fill(buffer, backgroundASCIICode);
            Arrays.fill(zBuffer, 0);
            cubeWidth = 20;
            horizontalOffset = (float) (-0.25 * cubeWidth);
            for (float cubeX = -cubeWidth; cubeX < cubeWidth; cubeX += incrementSpeed) {
                for (float cubeY = -cubeWidth; cubeY < cubeWidth; cubeY += incrementSpeed) {
                    calculateForSurface(cubeX, cubeY, -cubeWidth, '@');
                    calculateForSurface(cubeWidth, cubeY, cubeX, '$');
                    calculateForSurface(-cubeWidth, cubeY, -cubeX, '~');
                    calculateForSurface(-cubeX, cubeY, cubeWidth, '#');
                    calculateForSurface(cubeX, -cubeWidth, -cubeY, ';');
                    calculateForSurface(cubeX, cubeWidth, cubeY, '+');
                }
            }
            System.out.print("\033[H");
            for (int k = 0; k < width * height; k++) {
                System.out.print(k % width != 0 ? buffer[k] : "\n");
            }
            A += 0.05F;
            B += 0.05F;
            C += 0.01F;
            frames++;
            long currTime = System.currentTimeMillis();
            long elapsed = currTime - prevTime;
            if (elapsed >= 1000) {
                int fps = frames * 1000 / (int) elapsed;
                System.out.println("FPS: " + fps);
                frames = 0;
                prevTime = currTime;
            }
        }
    }
}
