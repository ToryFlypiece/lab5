package flatset.commands;

import flatset.Flat;
import flatset.auth.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class CommandManager {
    private final Map<String, Command> commands = new HashMap<>();
    private final HashSet<Flat> flatSet;
    private User currentUser;
    private boolean isRunning = true;
    private boolean isLoggedIn = true;

    public CommandManager(HashSet<Flat> flatSet, User user) {
        this.flatSet = flatSet;
        this.currentUser = user;
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
        registerCommand("execute_script", new ExecuteScriptCommand(currentUser));
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
            command.execute(flatSet, argument, currentUser);
            if ("logout".equals(commandName)) {
                currentUser = null;
                isLoggedIn = false;
                isRunning = false; // или можно просто прервать работу, если нужно
            }
        } else {
            System.out.println("Unknown command: " + commandName);
        }
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
}
