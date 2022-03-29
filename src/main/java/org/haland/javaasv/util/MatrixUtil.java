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
 * Provides matrix utility functions
 */
public final class MatrixUtil {
    private MatrixUtil() {
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


    static final int N = 4;

    // Function to get cofactor of A[p][q] in temp[][]. n is current
// dimension of A[][]
    static void getCofactor(int A[][], int temp[][], int p, int q, int n)
    {
        int i = 0, j = 0;

        // Looping for each element of the matrix
        for (int row = 0; row < n; row++)
        {
            for (int col = 0; col < n; col++)
            {
                // Copying into temporary matrix only those element
                // which are not in given row and column
                if (row != p && col != q)
                {
                    temp[i][j++] = A[row][col];

                    // Row is filled, so increase row index and
                    // reset col index
                    if (j == n - 1)
                    {
                        j = 0;
                        i++;
                    }
                }
            }
        }
    }

    /* Recursive function for finding determinant of matrix.
    n is current dimension of A[][]. */
    static int determinant(int A[][], int n)
    {
        int D = 0; // Initialize result

        // Base case : if matrix contains single element
        if (n == 1)
            return A[0][0];

        int [][]temp = new int[N][N]; // To store cofactors

        int sign = 1; // To store sign multiplier

        // Iterate for each element of first row
        for (int f = 0; f < n; f++)
        {
            // Getting Cofactor of A[0][f]
            getCofactor(A, temp, 0, f, n);
            D += sign * A[0][f] * determinant(temp, n - 1);

            // terms are to be added with alternate sign
            sign = -sign;
        }

        return D;
    }

    // Function to get adjoint of A[N][N] in adj[N][N].
    static void adjoint(int A[][],int [][]adj)
    {
        if (N == 1)
        {
            adj[0][0] = 1;
            return;
        }

        // temp is used to store cofactors of A[][]
        int sign = 1;
        int [][]temp = new int[N][N];

        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                // Get cofactor of A[i][j]
                getCofactor(A, temp, i, j, N);

                // sign of adj[j][i] positive if sum of row
                // and column indexes is even.
                sign = ((i + j) % 2 == 0)? 1: -1;

                // Interchanging rows and columns to get the
                // transpose of the cofactor matrix
                adj[j][i] = (sign)*(determinant(temp, N-1));
            }
        }
    }

    // Function to calculate and store inverse, returns false if
// matrix is singular
    static boolean inverse(int A[][], float [][]inverse)
    {
        // Find determinant of A[][]
        int det = determinant(A, N);
        if (det == 0)
        {
            System.out.print("Singular matrix, can't find its inverse");
            return false;
        }

        // Find adjoint
        int [][]adj = new int[N][N];
        adjoint(A, adj);

        // Find Inverse using formula "inverse(A) = adj(A)/det(A)"
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                inverse[i][j] = adj[i][j]/(float)det;

        return true;
    }










// Generic function to display the matrix. We use it to display
// both adjoin and inverse. adjoin is integer matrix and inverse
// is a float.

    static void display(int A[][])
    {
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
                System.out.print(A[i][j]+ " ");
            System.out.println();
        }
    }
    static void display(float A[][])
    {
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
                System.out.printf("%.6f ",A[i][j]);
            System.out.println();
        }
    }

    // Driver program
    public static void main(String[] args)
    {
        int A[][] = { {5, -2, 2, 7},
                {1, 0, 0, 3},
                {-3, 1, 5, 0},
                {3, -1, -9, 4}};

        int [][]adj = new int[N][N]; // To store adjoint of A[][]

        float [][]inv = new float[N][N]; // To store inverse of A[][]

        System.out.print("Input matrix is :\n");
        display(A);

        System.out.print("\nThe Adjoint is :\n");
        adjoint(A, adj);
        display(adj);

        System.out.print("\nThe Inverse is :\n");
        if (inverse(A, inv))
            display(inv);

    }

}

// This code is contributed by Rajput-Ji




