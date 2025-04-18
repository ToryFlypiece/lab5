package flatset.commands;

import flatset.Flat;
import flatset.FlatUpdater;
import java.util.HashSet;
import java.util.Optional;

public class UpdateByIdCommand implements Command {
    @Override
    public void execute(HashSet<Flat> flatSet, String argument) {
        try {
            String[] parts = argument.split(" ", 2);
            if (parts.length != 2) {
                throw new IllegalArgumentException("Usage: update_by_id id {field:value}");
            }

            long id = Long.parseLong(parts[0]);
            String jsonData = parts[1];

            Optional<Flat> toUpdate = flatSet.stream()
                    .filter(f -> f.getId() == id)
                    .findFirst();

            if (toUpdate.isPresent()) {
                FlatUpdater.updateFields(toUpdate.get(), jsonData);
                System.out.println("Updated fields for flat ID " + id);
            } else {
                System.out.println("No flat found with ID " + id);
            }
        } catch (Exception e) {
            System.err.println("Update error: " + e.getMessage());
        }
    }
}