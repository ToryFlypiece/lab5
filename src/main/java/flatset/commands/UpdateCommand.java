package flatset.commands;

import flatset.Flat;
import flatset.FlatParser;
import java.util.HashSet;
import java.util.Optional;

public class UpdateCommand implements Command {
    @Override
    public void execute(HashSet<Flat> flatSet, String argument) {
        try {
            String[] parts = argument.split(" ", 2);
            if (parts.length != 2) {
                throw new IllegalArgumentException("Usage: update id {flat_data}");
            }

            long id = Long.parseLong(parts[0]);
            Flat updatedFlat = FlatParser.parseFlat(parts[1]);
            updatedFlat.setId(id);

            Optional<Flat> existing = flatSet.stream()
                    .filter(f -> f.getId() == id)
                    .findFirst();

            if (existing.isPresent()) {
                flatSet.remove(existing.get());
                flatSet.add(updatedFlat);
                System.out.println("Updated flat with ID " + id);
            } else {
                System.out.println("No flat found with ID " + id);
            }
        } catch (Exception e) {
            System.err.println("Update error: " + e.getMessage());
        }
    }
}