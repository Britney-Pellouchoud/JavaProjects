/** Functions to increment and sum the elements of a WeirdList. */
class WeirdListClient {

    /** Return the result of adding N to each element of L. */
    static WeirdList add(WeirdList L, int n) {
        L.head = L.head + n;
        WeirdList x;
        x = L;
        if (x.tail != null) {
            return helperadd
        }
        return L;
    }

    /** Return the sum of all the elements in L. */
    static int sum(WeirdList L, total) {
        WeirdList x;
        x = L;
        int summer = x.head;
        if (x.tail != null) {
            return sumhelper(L, n);
        }
        return summer; // TODO: REPLACE THIS LINE
    }

    private static helperadd (WeirdList L, int n) {
        L.head = L.head + n;
        WeirdList x = L.tail
        return add(x, n);
    }

    private static sumhelper (WeirdList L, total) {
        summer += L.head;
        return sum(L.tail, summer)
    }
    /* IMPORTANT: YOU ARE NOT ALLOWED TO USE RECURSION IN ADD AND SUM
     *
     * As with WeirdList, you'll need to add an additional class or
     * perhaps more for WeirdListClient to work. Again, you may put
     * those classes either inside WeirdListClient as private static
     * classes, or in their own separate files.

     * You are still forbidden to use any of the following:
     *       if, switch, while, for, do, try, or the ?: operator.
     *
     * HINT: Try checking out the IntUnaryFunction interface.
     *       Can we use it somehow?
     */
}
