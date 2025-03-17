package flatset;

import java.util.HashSet;
import java.util.Scanner;

/**
 * Класс CommandProcessor обрабатывает команды, введенные пользователем в консоли.
 * Управляет коллекцией квартир через FlatManager.
 */
public class CommandProcessor {

    private FlatManager flatManager;
    private Scanner scanner;
    /**
     * Конструктор класса CommandProcessor.
     *
     * @param flatManager Объект FlatManager для управления коллекцией квартир.
     */
    public CommandProcessor(FlatManager flatManager) {
        this.flatManager = flatManager;
        this.scanner = new Scanner(System.in);
    }
    /**
     * Запускает консольное приложение для обработки команд.
     */
    public void startConsoleApp() {
        boolean running = true;
        System.out.println("Console application started. Enter commands (type 'help' for a list of commands).");

        while (running) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            String[] parts = input.split(" ", 2);
            String command = parts[0].toLowerCase();
            String argument = parts.length > 1 ? parts[1] : "";

            switch (command) {
                case "show":
                    showCollection();
                    break;
                case "add":
                    addFlat(argument);
                    break;
                case "remove_by_id":
                    removeById(argument);
                    break;
                case "clear":
                    clearCollection();
                    break;
                case "save":
                    saveCollection(argument);
                    break;
                case "exit":
                    running = false;
                    System.out.println("Exiting the application...");
                    break;
                default:
                    System.out.println("Unknown command. Type 'help' for a list of valid commands.");
                    break;
            }
        }

        scanner.close();
    }

    private void showCollection() {
        flatManager.getFlatSet().forEach(System.out::println);
    }

    private void addFlat(String argument) {
        // Логика добавления квартиры
    }

    private void removeById(String argument) {
        try {
            long id = Long.parseLong(argument.trim());
            flatManager.removeById(id);
            System.out.println("Element with ID " + id + " removed successfully.");
        } catch (Exception e) {
            System.err.println("Error removing element: " + e.getMessage());
        }
    }

    private void clearCollection() {
        flatManager.clearCollection();
        System.out.println("Collection cleared successfully.");
    }

    private void saveCollection(String filePath) {
        FlatSaver.saveCollection(flatManager.getFlatSet(), filePath);
    }
}