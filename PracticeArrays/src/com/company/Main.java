package com.company;

public class Main {
    public static void multiplyBy3(int[] A) {
        for (int x : A) {
            x = x * 3;
        }
    }

    public static void multiplyBy5(int[] A) {
        int[] temp = new int[A.length];
        System.arraycopy(A, 0, temp, 0, A.length);
        for (int i = 0; i < temp.length; i += 1) {
            temp[i] *= 5;
        }
        A = temp;
    }

    public static void multiplyBy2(int[] A) {
        int[] B = A;
        for (int i = 0; i < B.length; i += 1) {
            B[i] *= 2;
        }
    }

    public static void swap(int A, int B) {
        int temp = B;
        B = A;
        A = temp;
    }
}

    public static void main(String[] args){
        int[] arr;
        arr = new int[]{2, 3, 3, 4};
        multiplyBy3(arr);
        /* Value of arr: { } */
        arr = new int[]{2, 3, 3, 4};
        multiplyBy5(arr);
        /* Value of arr: { } */
        arr = new int[]{2, 3, 3, 4};
        multiplyBy2(arr);
        /* Value of arr: { } */
        int a = 6;
        int b = 7;
        swap(a, b);
        /* Value of a: Value of b: */
    }
}