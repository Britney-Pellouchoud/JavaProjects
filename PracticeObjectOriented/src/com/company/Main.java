package com.company;

public class Main {

    public static void main(String[] args) {
        Woman Felicia = new Woman("Felicia");
        System.out.println(Felicia.name);
        Felicia.talk();
        Woman Karen = new Woman("Karen");
        System.out.println(Karen.name);
        Prostitute Lauren = new Prostitute("Lauren", 5);
        System.out.println(Lauren.name);
        Lauren.name = "Olga";
        System.out.println(Lauren.name);
        System.out.println(Lauren.num_teeth);
        Lauren.talk();
        Woman Airwrecka = new Prostitute("Airwrecka", 3);
        Airwrecka.talk();
        ToothlessHoe Nadia = new ToothlessHoe("Nadia", false, 69);
        System.out.println(Nadia.num_teeth);
        Nadia.talk();
        System.out.println(Nadia.bites() == Lauren.bites());
        Man Josh = new Man("Josh", 20);
        Josh.talk();
    }

    public interface Looks {
        public String haircolor();
        public boolean plastic_surgery();

    }

    public abstract static class Human {

        int age;
        abstract int num_chromosomes();
        int toes = 10;
        String name;
    }


    public static class Man extends Human {
        public int num_chromosomes() {
            return 2;
        }
        private final int age;
        public Man(String name, int age) {
            this.name = name;
            this.age = age;
        }
        public void talk() {
            System.out.println("I am superior.");
        }
    }

    public static class Woman implements Looks {
        public String name;
        public Woman(){
            this.name = "Karen";
        }
        public String haircolor() {
            return "brown";
        }
        public boolean plastic_surgery() {
            return false;
        }
        public Woman(String name) {
            this.name = name;
        }
        public void talk() {
            System.out.println("I am lesser than a man.");
        }
    }

    public static class Prostitute extends Woman{
        public int num_teeth;
        public String name;
        public Prostitute() {
            super();
            num_teeth = 2;
        }
        public Prostitute(String name, int num_teeth) {
            this.name = name;
            this.num_teeth = num_teeth;
        }
        public boolean bites(){
            return true;
        }

        public void talk() {
            System.out.println("Black guys get a group rate.");
        }

    }
    public static class ToothlessHoe extends Prostitute implements Looks{
        public String name;
        public boolean SmokesMeth;
        public int num_STDs;

        @Override
        public String haircolor() {
            return "blonde";
        }

        @Override
        public boolean plastic_surgery() {
            return true;
        }

        public ToothlessHoe(String name, boolean SmokesMeth, int num_STDs) {
            this.name = name;
            this.SmokesMeth = SmokesMeth;
            this.num_STDs = num_STDs;
            this.num_teeth = 0;
        }

        public void talk() {
            System.out.println("My boobs are like old dress socks stuffed with oranges");
        }

        public boolean bites() {
            System.out.println("I'll gum at you until you give me 25 cents");
            return false;
        }

    }
}
