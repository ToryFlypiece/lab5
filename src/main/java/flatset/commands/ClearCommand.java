package flatset.commands;

import flatset.Flat;
import flatset.auth.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;

public class ClearCommand implements Command {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/flatset";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "admin";

    @Override
    public void execute(HashSet<Flat> flatSet, String argument, User currentUser) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            conn.setAutoCommit(false);

            String deleteSQL = "DELETE FROM flats WHERE owner_id = ?";

            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSQL)) {
                deleteStmt.setInt(1, currentUser.getId());
                int deletedCount = deleteStmt.executeUpdate();

                conn.commit();

                if (deletedCount > 0) {
                    Iterator<Flat> iterator = flatSet.iterator();
                    while (iterator.hasNext()) {
                        Flat flat = iterator.next();
                        if (flat.getOwnerId() == currentUser.getId()) {
                            iterator.remove();
                        }
                    }
                    System.out.println("Removed " + deletedCount + " flats created by you.");
                } else {
                    System.out.println("No flats created by you were found to remove.");
                }
            } catch (SQLException e) {
                conn.rollback();
                System.err.println("Failed to clear flats: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
        }
    }
}
