package com.company;

import java.sql.SQLOutput;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        /* Test for 2x2 Matrix */
        int [][] test_matrix = new int[][] {{1, 2}, {3, 4}};
        int [] test_vector = new int[]{5, 6};
        Transform(test_matrix, test_vector);

        /* Test for 3x3 Matrix */
        int [][] test_matrix2 = new int[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        int [] test_vector2 = new int[]{1, 2, 3};
        Transform(test_matrix2, test_vector2);

        /* Test for incorrect dimensions */
        int [][] test_matrix3 = new int[][]{{1, 0}, {2,1}};
        int [] test_vector3 = new int[]{1};
        Transform(test_matrix3, test_vector3);
    }

    public static int[] Transform(int[][] matrix, int[] vector) {
        if (vector.length != matrix[0].length) {
            System.out.println("Vector not transformable by matrix, wrong dimensions.");
            int[] null_list = new int[0];
            return null_list;
        } else {
            int[] answer = new int[matrix[0].length];
            int counter = 0;
            int answer_row = 0;
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < vector.length; j++) {
                    int entry = matrix[i][j] * vector[j];
                    counter += entry;
                    if (j == vector.length - 1) {
                        answer[answer_row] = counter;
                        answer_row += 1;
                        counter = 0;
                    }
                }

            }
            String[] printable_matrix = new String[matrix.length];
            for (int row = 0; row < matrix.length; row++) {
                printable_matrix[row] = Arrays.toString(matrix[row]);
            }
            System.out.println("The vector " + Arrays.toString(vector) + " has been transformed by the matrix "
                    + Arrays.toString(printable_matrix) + " to the vector " + Arrays.toString(answer));
            return answer;
        }
    }
}


