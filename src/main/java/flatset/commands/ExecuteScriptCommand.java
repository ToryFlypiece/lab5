package flatset.commands;


import java.util.HashSet;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import flatset.Flat;

public class ExecuteScriptCommand implements Command {
    @Override
    public void execute(HashSet<Flat> flatSet, String argument)
    {
        try {
            List<String> lines = Files.readAllLines(Paths.get(argument));
            CommandManager tempManager = new CommandManager(flatSet);

            for (String line : lines) {
                if (line.trim().isEmpty() || line.startsWith("#")) {
                    continue; // Skip empty lines and comments
                }
                System.out.println("Executing: " + line);
                tempManager.executeCommand(line);
            }
        } catch (Exception e) {
            System.err.println("Script execution failed: " + e.getMessage());
        }
    }
}