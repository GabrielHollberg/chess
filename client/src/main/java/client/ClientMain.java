package client;

import chess.*;

import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) {
        System.out.println("type help to see options\n");
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("----------Chess----------\n");
            String line = scanner.nextLine();
            System.out.println();
            if (line.equalsIgnoreCase("help") || line.equalsIgnoreCase("h")) {
                System.out.println("Options\n");
            }
        }
    }
}
