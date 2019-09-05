public class threeSumFinderDis {
    private static boolean threeSUMFinder(int[] a) {
        for (int i = 0; i < a.length ; i++) {
            int val1 = a[i];
            int start = i+1;
            int end = a.length - 1;
            while (start < end) {
                if (val1 + a[start] + a[end] == 0) {
                    return true;
                }
                else if (val1 + a[start] + a[end] > 0) {
                    end -= 1;
                }
                else {
                    start += 1;
                }
            }

        }
        return false;

    }

    public static void main(String[] args) {
        int[] test = new int[] {8, 2, -1, -1, 15};
        System.out.println(threeSUMFinder(test));
    }
}

