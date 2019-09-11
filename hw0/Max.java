public class Max{

    private static int maxFinder(int[] a) {
        int biggest = a[0];
        for (int i : a) {
            if (biggest < i) {
                biggest = i;
            }
        }
        return biggest;
    }
    public static void main(String[] args){
        int[] ArrayTester = new int[] {3, 0, -12, 47};
        System.out.println("The maximum of your array is " + maxFinder(ArrayTester));
    }
}


