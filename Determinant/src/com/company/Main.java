package com.company;


import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
	/* Test for 2x2 */
        int [][] test = new int[][] {{1, 2}, {3, 4}};
        det_finder(test);
        System.out.println("The determinant of " + Arrays.toString(printable(test)) + " is " + det_finder(test));

    /* Test for a nonsquare matrix */
        int[][] test1 = new int[][] {{1, 2}, {3}};
        det_finder(test1);

    /* Test for smaller matrix */
        int [][] test2 = new int[][] {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 16}};
        smaller_matrix(test2, 2);

        int [][] test3 = new int[][] {{1, 2, 3}, {4, 5, 6}, {7,8,9}};
        smaller_matrix(test3, 0);

    /* Test for 3x3 matrix */
        int [][] test4 = new int[][] {{1, 2, 3}, {4, 5, 6}, {7,8,9}};
        det_finder(test4);
        System.out.println("The determinant of " + Arrays.toString(printable(test4)) + " is " + det_finder(test4));

    /* Test for 4x4 matrix */
        int [][] test5 = new int[][] {{1, 2, 3, 4}, {1, 0, 0, 0}, {0, 1, 0, 0}, {0, 0, 0, 1}};
        det_finder(test5);
        System.out.println("The determinant of " + Arrays.toString(printable(test5)) + " is " + det_finder(test5));

    /* Test for 100x100 matrix*/
        int [][] test6 = new int[][]{{0,2,4,1}, {3,4,9,1}, {2,3,8,1}, {3,1,7,9}};
        //this test will run far too long because of recursion, how can I make it shorter?
        det_finder(test6);
        System.out.println("The determinant of " + Arrays.toString(printable(test6)) + " is " + det_finder(test6));

    }


    //finds the det
    public static int det_finder(int[][] matrix) {
        if (!square_check(matrix)) {
            System.out.println("Cannot find the determinant of a nonsquare matrix.");
            return 0;
        }
        if (matrix.length == 2 && matrix[0].length == 2) {
            int answer =  two_by_two(matrix);
            return answer;
        }
        else {
            int[] summer = new int[matrix.length];
            int counter = 0; //if counter is even, pos.  If counter is odd, negative
            for (int i = 0; i < matrix.length; i++) {
                if (counter % 2 == 0) {
                    summer[i] = matrix[0][i] * det_finder(smaller_matrix(matrix, i));
                }
                else if (counter % 2 == 1) {
                    summer[i] = -1 * matrix[0][i] * det_finder(smaller_matrix(matrix, i));
                }
                counter += 1;
            }
            int adder = 0;
            for (int i: summer) {
                adder += i;
            }
            int answer = adder;
            return answer;
        }

    }

    public static int[][] smaller_matrix(int[][] matrix, int colnum) {
        int[][] answer = new int[matrix.length - 1][matrix.length - 1];
        for (int row = 1; row < matrix.length; row++) {
            for (int entry = 0; entry < matrix.length; entry++) {
                if (entry != colnum) {
                    if (entry < colnum) {
                        answer[row - 1][entry] = matrix[row][entry];
                    }
                    else {
                        answer[row - 1][entry - 1] = matrix[row][entry];
                    }
                }
            }
        }
        return answer;
    }

    //makes sure it is a square matrix
    public static boolean square_check(int[][] matrix) {
        int num_rows = matrix.length;
        for (int i = 0; i < matrix.length; i ++) {
            if (matrix[i].length != matrix.length) {
                return false;
            }
        }
        return true;
    }

    //base case, finds det of two by two
    public static int two_by_two(int[][] matrix) {
        int det = matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
        return det;
    }

    //creates a readable matrix
    public static String[] printable(int[][] matrix) {
        String[] printable_matrix = new String[matrix.length];
        for (int row = 0; row < matrix.length; row++) {
            printable_matrix[row] = Arrays.toString(matrix[row]);
        }
        return printable_matrix;
        }







}


