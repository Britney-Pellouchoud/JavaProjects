package com.company;
import java.util.Iterator;

public class Main {

    public static class Lst<T> implements Iterable<T> {
        private T[] items;
        private int size;

        //constructor
        public Lst() {
            items = (T[]) new Object[100];
            size = 0;
        }

        //returns an iterator
        public Iterator<T> iterator() {
            return new Iter();
        }

        //defines iterator class
        public static class Iter implements Iterator<T> {
            private int counter;
            public Iter() {
                counter = 0;
            }
            public boolean hasNext() {
                return counter < size;
            }
            public T next() {
                T returned = items[counter];
                counter += 1;
                return returned;
            }
        }

    }

    public static void main(String[] args) {
	    Lst test = new Lst();
	    Lst.Iter test_iter = new Lst.Iter();
    }
}
