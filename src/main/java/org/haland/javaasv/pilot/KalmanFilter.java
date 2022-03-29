/*
 * This file is part of JavaASV, an open-source ASV navigation controller.
 * Copyright (C) 2022  Max Haland
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

package org.haland.javaasv.pilot;

// Using the following website as a reference:
// https://thekalmanfilter.com/kalman-filter-explained-simply/


public class KalmanFilter {

    private double   []x; // state matrix
    private double [][]p; // state covariance
    private double [][]a; // state transition
    private double [][]h; // state-to-measurement
    private double [][]r; // measurement covariance
    private double [][]q; // process noise covariance
    private double [][]k; // kalman gain

    private int initialize;

    private float t_last;

    public KalmanFilter (double [] variance, double [] transition) {
        this.initialize = 1;
        this.r = variance;
        this.a = transition;
    }

    public void newData (double [] sensorInput, double timestamp, double[][] mes_variance){
        this.t_last = timestamp;
        if (initalize == 1){
            this.x[0] = sensorInput[0];
            this.x[1] = sensorInput[1];
            this.r = mes_variance;
            this.initialize ++;
            this.p = {  {this.r[0][0], this.r[0][1], 0, 0}
                        {this.r[1][0], this.r[1][1], 0, 0}
                        {0, 0, 0, 0}
                        {0, 0, 0, 0}
                  };
        }
        if (initialize == 2) {
            double deltaT = timestamp - t_last;
            this.x[2] = (sensorInput[0]-x[0] ) / deltaT;
            this.x[3] = (sensorInput[1]-x[1] ) / deltaT;
            this.x[0] = sensorInput[0];
            this.x[1] = sensorInput[1];
            this.p = [  [this.r[0][0], this.r[0][1], 0,0]
                        [this.r[1][0], this.r[1][1], 0, 0]
                        [0, 0, 10000, 0]
                        [0, 0, 0, 10000]
                  ];
            initialize == 0;
        }
        if (initialize == 0){
            this.x = A*x;
            P = A*P*A^-1 + Q;

        }

        this.update_state(double [] sensorInput, double timestamp);
        this.update_
    }

    private void update_state(double [] sensorInput, double timestamp){

    }









    private double [][] mmult (double [][]a, double [][]b){
        double [][] out = new double[0][];
        int r1 = a.length();
        int c1 = a[0].length();
        int r2 = b.length();
        for (int i = 0; i < r1; i++){
            for (int j = 0; j < c1; j++){
                out[i][j] = 0;
                for (int k = 0; k < r2; k++){
                    out[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return out;
    }
    private double [][] minverse (double [][]a){

    }
    private double determinant (double [][]a){
        int n = a.length;

        int determinant = 0;
        if (n == 1) {
            return matrix[0][0];
        }
        if (n == 2) {
            return (matrix[0][0] * matrix[1][1]) - (matrix[0][1] * matrix[1][0]);
        }
        int temp[N][N], sign = 1;
        for (int i = 0; i < n; i++) {
            subMatrix(matrix, temp, 0, i, n);
            determinant += sign * matrix[0][i] * determinantOfMatrix(temp, n - 1);
            sign = -sign;
        }
        return determinant;
    }

    public double [] getState (){
        return state;
    }


}

