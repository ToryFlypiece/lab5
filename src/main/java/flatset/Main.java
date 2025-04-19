package flatset;

import flatset.commands.CommandManager;
import java.util.HashSet;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        HashSet<Flat> flatSet = FlatLoader.loadInitialData();

        CommandManager commandManager = new CommandManager(flatSet);
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n=== Flat Collection Manager ===");
        System.out.println("Введите 'help' чтобы вывести список команда\n");



        // Main command loop
        while (commandManager.isRunning()) {
            try {
                System.out.print("> ");
                String input = scanner.nextLine().trim();

                if (!input.isEmpty()) {
                    commandManager.executeCommand(input);
                }
            } catch (Exception e) {
                System.err.println("Ошибка: " + e.getMessage());
            }
        }

        // Cleanup
        scanner.close();
        System.out.println("Программа завершена");
    }
}