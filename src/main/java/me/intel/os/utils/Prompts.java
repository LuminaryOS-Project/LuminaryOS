package me.intel.os.utils;

import java.util.Objects;
import java.util.Scanner;

public class Prompts {
    public static boolean YNPrompt() {
        System.out.print("Continue? (Y/N): ");
        Scanner scan = new Scanner(System.in);
        while(true) {
            String input = scan.nextLine();
            if(input.equalsIgnoreCase("Y")) {
                return true;
            } else if(input.equalsIgnoreCase("N")) {
                return false;
            }
        }
    }

    public static boolean YNPrompt(String msg) {
        System.out.print(msg + " (Y/N): ");
        Scanner scan = new Scanner(System.in);
        while(true) {
            String input = scan.nextLine();
            if(input.equalsIgnoreCase("Y")) {
                return true;
            } else if(input.equalsIgnoreCase("N")) {
                return false;
            }
        }
    }
}
