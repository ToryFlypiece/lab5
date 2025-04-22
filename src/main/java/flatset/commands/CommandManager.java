package flatset.commands;

import flatset.Flat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Менеджер команд для управления коллекцией квартир.
 * Обеспечивает регистрацию, хранение и выполнение команд.
 */
public class CommandManager {
    private final Map<String, Command> commands = new HashMap<>();
    private final HashSet<Flat> flatSet;
    private boolean isRunning = true;

    /**
     * Создает менеджер команд для работы с указанной коллекцией квартир
     * @param flatSet коллекция квартир для управления
     */
    public CommandManager(HashSet<Flat> flatSet) {
        this.flatSet = flatSet;
        initializeCommands();
    }

    /**
     * Инициализирует все доступные команды
     */
    private void initializeCommands() {
        registerCommand("help", new HelpCommand());
        registerCommand("info", new InfoCommand());
        registerCommand("show", new ShowCommand());
        registerCommand("add", new AddCommand());
        registerCommand("remove_by_id", new RemoveByIdCommand());
        registerCommand("clear", new ClearCommand());
        registerCommand("save", new SaveCommand());

        // Условные операции
        registerCommand("add_if_min", new AddIfMinCommand());
        registerCommand("add_if_max", new AddIfMaxCommand());
        registerCommand("remove_greater", new RemoveGreaterCommand());

        // Операции обновления
        registerCommand("update", new UpdateCommand());
        registerCommand("update_by_id", new UpdateByIdCommand());

        // Операции отображения полей
        registerCommand("print_unique_house", new PrintUniqueHouseCommand());
        registerCommand("print_field_ascending_number_of_rooms", new PrintFieldAscendingNumberOfRoomsCommand());
        registerCommand("print_field_descending_house", new PrintFieldDescendingHouseCommand());

        // Системные операции
        registerCommand("execute_script", new ExecuteScriptCommand());
        registerCommand("exit", (set, arg) -> isRunning = false);
    }

    /**
     * Регистрирует новую команду
     * @param name название команды
     * @param command объект команды
     */
    public void registerCommand(String name, Command command) {
        commands.put(name.toLowerCase(), command);
    }

    /**
     * Выполняет команду по строковому вводу
     * @param input строка с командой и аргументами
     */
    public void executeCommand(String input) {
        String[] parts = input.split(" ", 2);
        String commandName = parts[0].toLowerCase();
        String argument = parts.length > 1 ? parts[1] : "";

        Command command = commands.get(commandName);
        if (command != null) {
            command.execute(flatSet, argument);
        } else {
            System.out.println("Unknown command: " + commandName);
        }
    }

    /**
     * Проверяет, работает ли менеджер команд
     * @return true если менеджер продолжает работу, false если получена команда выхода
     */
    public boolean isRunning() {
        return isRunning;
    }
}