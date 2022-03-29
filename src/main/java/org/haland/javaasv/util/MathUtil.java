/*
 * This file is part of JavaASV, an open-source ASV navigation controller.
 * Copyright (C) 2020  Max Haland
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.haland.javaasv.util;

/**
 * Provides math utility functions
 */
public final class MathUtil {
    private MathUtil() {
        throw new AssertionError("utility class");
    }

    /**
     * Returns value clamped between low and high boundaries.
     *
     * @param value Value to clamp.
     * @param low   The lower boundary to which to clamp value.
     * @param high  The higher boundary to which to clamp value.
     */
    public static int clamp(int value, int low, int high) {
        return Math.max(low, Math.min(value, high));
    }

    /**
     * Returns value clamped between low and high boundaries.
     *
     * @param value Value to clamp.
     * @param low   The lower boundary to which to clamp value.
     * @param high  The higher boundary to which to clamp value.
     */
    public static double clamp(double value, double low, double high) {
        return Math.max(low, Math.min(value, high));
    }
}

    /**
     * Returns the value of two matricies multiplied together (a * b).
     * Copied from Jama, adapted to work for 2d arrays
     * @param a matrix 1.
     * @param b matrix 2.
     */
    public static double [][] mmult (double [][]a, double [][]b){
        if (b[0].length != a.length) {
            throw new IllegalArgumentException("Matrix inner dimensions must agree.");
        }
        int n = a.length;
        int m = a[0].length;
        int bn = b.length;
        int bm = b[0].length;
        double[] Bcolj = new double[n];
        double [][] out = new double [m][bn];
        for (int j = 0; j < bn; j++) {
            for (int k = 0; k < n; k++) {
                Bcolj[k] = b[k][j];
            }
            for (int i = 0; i < m; i++) {
                double[] Arowi = a[i];
                double s = 0;
                for (int k = 0; k < n; k++) {
                    s += Arowi[k]*Bcolj[k];
                }
                out[i][j] = s;
            }
        }
        return out;
}

    /**
     * Returns the inverse of a matrix.
     * Copied from Jama, adapted to work for 2d arrays
     * @param a matrix to be inverted
     */
    public static double [][] mmult (double [][]a, double [][]b){
        if (b[0].length != a.length) {
            throw new IllegalArgumentException("Matrix inner dimensions must agree.");
        }
        int n = a.length;
        int m = a[0].length;
        int bn = b.length;
        int bm = b[0].length;
        double[] Bcolj = new double[n];
        double [][] out = new double [m][bn];
        for (int j = 0; j < bn; j++) {
            for (int k = 0; k < n; k++) {
                Bcolj[k] = b[k][j];
            }
            for (int i = 0; i < m; i++) {
                double[] Arowi = a[i];
                double s = 0;
                for (int k = 0; k < n; k++) {
                    s += Arowi[k]*Bcolj[k];
                }
                out[i][j] = s;
            }
        }
        return out;
    }
