package flatset.commands;

import java.util.HashSet;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import flatset.Flat;

/**
 * Команда для выполнения скрипта из файла.
 * Скрипт представляет собой последовательность команд, записанных построчно.
 */
public class ExecuteScriptCommand implements Command {

    /**
     * Выполняет команды из указанного скрипта (текстового файла).
     *
     * @param flatSet Коллекция квартир, к которой применяются команды из скрипта.
     * @param argument Путь к файлу скрипта. Ожидается, что каждая строка файла — отдельная команда.
     */
    @Override
    public void execute(HashSet<Flat> flatSet, String argument) {
        try {
            // Чтение всех строк из файла
            List<String> lines = Files.readAllLines(Paths.get(argument));

            // Создание временного менеджера команд для выполнения скрипта
            CommandManager tempManager = new CommandManager(flatSet);

            for (String line : lines) {
                // Пропуск пустых строк и комментариев
                if (line.trim().isEmpty() || line.startsWith("#")) {
                    continue;
                }

                try {
                    System.out.println("Выполняется: " + line);
                    tempManager.executeCommand(line);
                } catch (Exception e) {
                    System.err.println("Ошибка при выполнении команды: " + line);
                    System.err.println("Ошибка: " + e.getMessage());
                    // Продолжаем выполнение следующих команд
                }
            }
        } catch (Exception e) {
            System.err.println("Выполнение скрипта не удалось: " + e.getMessage());
        }
    }
}
