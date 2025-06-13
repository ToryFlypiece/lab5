package flatset.commands;

import flatset.Flat;
import flatset.auth.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.HashSet;

public class RemoveByIdCommand implements Command {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/flatset";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "admin";

    @Override
    public void execute(HashSet<Flat> flatSet, String argument, User currentUser) {
        try {
            if (argument == null || argument.trim().isEmpty()) {
                System.out.println("Error: ID is not defined. Usage: remove_by_id <id>");
                return;
            }

            long id = Long.parseLong(argument.trim());

            Flat flatToRemove = flatSet.stream()
                    .filter(flat -> flat.getId() == id)
                    .findFirst()
                    .orElse(null);

            if (flatToRemove == null) {
                System.out.println("No apartment found with ID " + id + ".");
                return;
            }

            if (flatToRemove.getOwnerId() == null || !flatToRemove.getOwnerId().equals(currentUser.getId())) {
                System.out.println("You do not have permission to delete this apartment.");
                return;
            }

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                PreparedStatement ps = conn.prepareStatement("DELETE FROM flats WHERE id = ? AND owner_id = ?");
                ps.setLong(1, id);
                ps.setInt(2, currentUser.getId());

                int rowsDeleted = ps.executeUpdate();

                if (rowsDeleted == 0) {
                    System.out.println("Failed to delete apartment from database.");
                    return;
                }
            }

            flatSet.remove(flatToRemove);
            System.out.println("Apartment with ID " + id + " has been successfully removed.");
        } catch (NumberFormatException e) {
            System.err.println("Error: Invalid ID format. ID must be a number.");
        } catch (Exception e) {
            System.err.println("Error deleting apartment: " + e.getMessage());
        }
    }
}
