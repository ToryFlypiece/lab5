package flatset;

import flatset.commands.CommandManager;
import flatset.utils.FlatLoader;

import java.util.HashSet;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        HashSet<Flat> flatSet = FlatLoader.loadInitialData();

        CommandManager commandManager = new CommandManager(flatSet);
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n=== Flat Collection Manager ===");
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