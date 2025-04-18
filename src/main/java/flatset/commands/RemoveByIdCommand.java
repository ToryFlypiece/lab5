package flatset.commands;

import flatset.Flat;
import java.util.HashSet;

public class RemoveByIdCommand implements Command {
    @Override
    public void execute(HashSet<Flat> flatSet, String argument) {
        try {
            if (argument == null || argument.trim().isEmpty()) {
                System.out.println("Error: No ID specified. Usage: remove_by_id <id>");
                return;
            }

            long id = Long.parseLong(argument.trim());
            boolean removed = flatSet.removeIf(flat -> flat.getId() == id);

            if (removed) {
                System.out.println("Flat with ID " + id + " was successfully removed.");
            } else {
                System.out.println("No flat found with ID " + id + ".");
            }
        } catch (NumberFormatException e) {
            System.err.println("Error: Invalid ID format. Please provide a numeric ID.");
        } catch (Exception e) {
            System.err.println("Error removing flat: " + e.getMessage());
        }
    }
}