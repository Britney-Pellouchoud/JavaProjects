package com.company;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        System.out.println("Welcome to Britney's Wonderland, a virtual" +
                " storyline.  You get to choose your destiny.  Ready?" +
                " [y/n]");
        Scanner input = new Scanner(System.in);
        String msg = input.nextLine();

        if (msg.equals("y")) {
            System.out.println("You have three lives.  Any death, accidental or deserved, removes a life.  First," +
                    "you will be transported to the Juicy Jungle.");
            firstscene();

       } else if (msg.equals("n")) {
           System.out.println("Oh well.  See you next time!");
           return;
       } else {
           System.out.println("I didn't quite catch that.  Let's try this again.");
           Main.main(args);
           return;
       }
    }

    public static void firstscene() {
        System.out.println("The Juicy Jungle greets you!  Be careful.  You have three lives, and any wrong move could result " +
                "in your untimely death.  Would you rather go back to your last life or stay here? [Type 'last life' or 'stay']");
        Scanner staygo = new Scanner(System.in);
        String sg = staygo.nextLine();
        if (sg.equals("stay")) {
            System.out.println("You wake up in a dark, humid jungle, under a large tree.  Ahead, you see a giant glowing flower. " +
                    "But next to you stands an infinitely tall tree.  Do you A) go towards the flower or B) climb the tree? [Type A/B]");
            Scanner ft = new Scanner(System.in);
            String fltr = ft.nextLine();
            if (fltr.equals("A")) {
                System.out.println("The flower is carnivorous.  It's petals grab you, turn you upside down, and guzzle you whole." +
                        " You die, and lose a life.  You must start over.");
                Main.main(null);
            }
        } else if (sg.equals("last life")) {
            //MAKE A STACK FOR PAST LIVES
            Main.main(null);

        }
         else {
            System.out.println("I didn't understand that.  Try again.");
            firstscene();
        }

    }




}
