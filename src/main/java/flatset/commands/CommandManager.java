package flatset.commands;

import flatset.Flat;
import flatset.auth.User;

import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Менеджер команд.
 */
public class CommandManager {
    private final Map<String, Command> commands = new ConcurrentHashMap<>();
    private final Set<Flat> flatSet;
    private final Scanner scanner;
    private User currentUser;
    private boolean isRunning = true;
    private boolean isLoggedIn = true;

    // Пулы потоков
    private static final ForkJoinPool readPool = new ForkJoinPool();                      // для чтения команд
    private static final ExecutorService commandPool = Executors.newCachedThreadPool();   // выполнение команд
    private static final ExecutorService responsePool = Executors.newFixedThreadPool(4);  // вывод ответов

    public CommandManager(Set<Flat> flatSet, User user, Scanner scanner) {
        this.flatSet = flatSet;
        this.currentUser = user;
        this.scanner = scanner;
        initializeCommands();
    }

    private void initializeCommands() {
        registerCommand("help", new HelpCommand());
        registerCommand("info", new InfoCommand());
        registerCommand("show", new ShowCommand());
        registerCommand("add", new AddCommand());
        registerCommand("remove_by_id", new RemoveByIdCommand());
        registerCommand("clear", new ClearCommand());
        registerCommand("save", new SaveCommand());
        registerCommand("add_if_min", new AddIfMinCommand());
        registerCommand("add_if_max", new AddIfMaxCommand());
        registerCommand("remove_greater", new RemoveGreaterCommand());
        registerCommand("update", new UpdateCommand());
        registerCommand("update_by_id", new UpdateByIdCommand());
        registerCommand("print_unique_house", new PrintUniqueHouseCommand());
        registerCommand("print_field_ascending_number_of_rooms", new PrintFieldAscendingNumberOfRoomsCommand());
        registerCommand("print_field_descending_house", new PrintFieldDescendingHouseCommand());
        // Исправлено: создаём ExecuteScriptCommand как InteractiveCommand
        registerCommand("execute_script", new ExecuteScriptCommand(currentUser, responsePool));
        registerCommand("exit", (set, arg, user) -> isRunning = false);
        registerCommand("logout", new LogoutCommand());
    }

    public void registerCommand(String name, Command command) {
        commands.put(name.toLowerCase(), command);
    }

    public void executeCommand(String input) {
        String[] parts = input.split(" ", 2);
        String commandName = parts[0].toLowerCase();
        String argument = parts.length > 1 ? parts[1] : "";

        Command command = commands.get(commandName);

        if (command != null) {
            boolean isInteractive = (command instanceof InteractiveCommand);

            Runnable task = () -> {
                if (isInteractive) {
                    ((InteractiveCommand) command).execute(flatSet, argument, currentUser, scanner);
                } else {
                    command.execute(flatSet, argument, currentUser);
                }

                if ("logout".equals(commandName)) {
                    currentUser = null;
                    isLoggedIn = false;
                    isRunning = false;
                }
            };

            if (isInteractive) {
                task.run(); // интерактивные команды выполняем синхронно
            } else {
                readPool.submit(task); // остальные — в пуле
            }
        } else {
            responsePool.submit(() ->
                    System.out.println("Неизвестная команда: " + commandName)
            );
        }
    }

    private boolean isInteractiveCommand(String commandName) {
        String name = commandName.toLowerCase();
        return name.equals("add")
                || name.equals("update")
                || name.equals("update_by_id")
                || name.equals("execute_script");
    }

    public boolean isRunning() {
        return isRunning && isLoggedIn;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        this.isLoggedIn = user != null;
        this.isRunning = user != null;
    }

    public static ExecutorService getResponsePool() {
        return responsePool;
    }

    public static void shutdownPools() {
        readPool.shutdown();
        commandPool.shutdown();
        responsePool.shutdown();
    }
}
