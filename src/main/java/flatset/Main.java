package flatset;

import flatset.auth.AuthManager;
import flatset.auth.User;
import flatset.commands.CommandManager;
import flatset.utils.FlatLoader;

import java.util.HashSet;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        HashSet<Flat> flatSet = FlatLoader.loadInitialData();
        Scanner scanner = new Scanner(System.in);
        User currentUser = null;

        System.out.println("=== Flat Collection Manager ===");

        while (currentUser == null) {
            System.out.print("Login (l) or Register (r)? ");
            String choice = scanner.nextLine().trim().toLowerCase();

            System.out.print("Username: ");
            String username = scanner.nextLine().trim();

            System.out.print("Password: ");
            String password = scanner.nextLine().trim();

            if (choice.equals("r")) {
                currentUser = AuthManager.register(username, password);
                if (currentUser != null) {
                    System.out.println("Registration successful.\n");
                }
            } else if (choice.equals("l")) {
                currentUser = AuthManager.login(username, password);
                if (currentUser != null) {
                    System.out.println("Login successful.\n");
                }
            }

            if (currentUser == null) {
                System.out.println("Authentication failed, try again.\n");
            }
        }

        CommandManager commandManager = new CommandManager(flatSet, currentUser);

        System.out.println("Print 'help' to see the commands list\n");

        while (commandManager.isRunning()) {
            System.out.print("> ");
            if (!scanner.hasNextLine()) {
                System.out.println("\nEOF: Exiting");
                break;
            }
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                commandManager.executeCommand(input);
            }
        }

        scanner.close();
        System.out.println("Program is terminated");
    }
}
