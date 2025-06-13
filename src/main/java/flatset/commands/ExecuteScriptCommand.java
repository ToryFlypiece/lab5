package flatset.commands;

import flatset.Flat;
import flatset.auth.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 * Команда для выполнения скрипта из файла.
 */
public class ExecuteScriptCommand implements InteractiveCommand {
    private final User currentUser;
    private final ExecutorService responsePool;

    public ExecuteScriptCommand(User user, ExecutorService responsePool) {
        this.currentUser = user;
        this.responsePool = responsePool;
    }

    @Override
    public void execute(Set<Flat> flatSet, String argument, User currentUser, Scanner scanner) {
        if (argument == null || argument.isEmpty()) {
            System.out.println("Файл скрипта не указан.");
            return;
        }

        File scriptFile = new File(argument);
        if (!scriptFile.exists() || !scriptFile.canRead()) {
            System.out.println("Файл не найден или недоступен: " + argument);
            return;
        }

        try (Scanner fileScanner = new Scanner(scriptFile)) {
            CommandManager tempCommandManager = new CommandManager(flatSet, currentUser, fileScanner);

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (!line.isEmpty()) {
                    System.out.println("> " + line);
                    tempCommandManager.executeCommand(line);
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("Ошибка при открытии файла: " + e.getMessage());
        }
    }

    @Override
    public void execute(Set<Flat> flatSet, String argument, User currentUser) {
    }
}
