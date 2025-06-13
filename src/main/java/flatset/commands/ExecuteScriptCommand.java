package flatset.commands;

import flatset.Flat;
import flatset.auth.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Команда для выполнения скрипта из файла.
 */
public class ExecuteScriptCommand implements Command {
    private final User currentUser;

    public ExecuteScriptCommand(User user) {
        this.currentUser = user;
    }

    @Override
    public void execute(HashSet<Flat> flatSet, String argument, User currentUser) {
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
            CommandManager tempCommandManager = new CommandManager(flatSet, currentUser);

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
}
