public class threeSumFinder {
    private static boolean threeSumFinderReg(int[] a) {
        for (int i = 0 ; i < a.length ; i++) {
            //System.out.println("first");
            for (int j = 0 ; j < a.length ; j++) {
                //System.out.println("second");
                for (int k = 0 ; k < a.length ; k++) {
                    //System.out.println("set is " + a[i] + " " + a[j] + " " + a[k]);
                    if (a[i] + a[j] + a[k] == 0) {
                        return true;
                    }
                }
            }

        }

        return false;
    }
    public static void main(String[] args) {
        int[] test =  new int[] {8, 2, -1, 15};
        System.out.println(threeSumFinderReg(test));
    }
}
