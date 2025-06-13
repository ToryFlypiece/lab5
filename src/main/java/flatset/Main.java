package flatset;

import flatset.auth.AuthManager;
import flatset.auth.User;
import flatset.commands.CommandManager;
import flatset.utils.FlatLoader;

import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static void main(String[] args) {
        Set<Flat> flatSet = ConcurrentHashMap.newKeySet();
        flatSet.addAll(FlatLoader.loadInitialData());
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Flat Collection Manager ===");

        while (true) {
            User currentUser = null;

            while (currentUser == null) {
                System.out.print("Login (l), Register (r), or Exit (e)? ");
                String choice = scanner.nextLine().trim().toLowerCase();

                if (choice.equals("e")) {
                    System.out.println("Exiting program.");
                    scanner.close();
                    CommandManager.shutdownPools();
                    System.exit(0);
                }

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

            CommandManager commandManager = new CommandManager(flatSet, currentUser, scanner);

            System.out.println("Print 'help' to see the commands list\n");

            while (commandManager.isRunning()) {
                System.out.print("> ");
                if (!scanner.hasNextLine()) {
                    System.out.println("\nEOF: Exiting");
                    scanner.close();
                    CommandManager.shutdownPools();
                    System.exit(0);
                }
                String input = scanner.nextLine().trim();
                if (!input.isEmpty()) {
                    commandManager.executeCommand(input);
                }
            }

            System.out.println("You have been logged out.\n");
        }
    }
}
