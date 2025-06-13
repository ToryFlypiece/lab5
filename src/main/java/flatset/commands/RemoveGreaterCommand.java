package flatset.commands;

import flatset.Flat;
import flatset.utils.FlatParser;
import flatset.auth.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.HashSet;
import java.util.Iterator;

public class RemoveGreaterCommand implements Command {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/flatset";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "admin";

    @Override
    public void execute(HashSet<Flat> flatSet, String argument, User currentUser) {
        try {
            Flat comparisonFlat = FlatParser.parseFlat(argument);
            int removedCount = 0;

            Iterator<Flat> iterator = flatSet.iterator();

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                while (iterator.hasNext()) {
                    Flat flat = iterator.next();
                    if (flat.compareTo(comparisonFlat) > 0) {
                        if (flat.getOwnerId() != null && flat.getOwnerId().equals(currentUser.getId())) {
                            PreparedStatement ps = conn.prepareStatement("DELETE FROM flats WHERE id = ? AND owner_id = ?");
                            ps.setLong(1, flat.getId());
                            ps.setInt(2, currentUser.getId());
                            int rowsDeleted = ps.executeUpdate();
                            if (rowsDeleted > 0) {
                                iterator.remove();
                                removedCount++;
                            }
                        }
                    }
                }
            }

            System.out.println("Removed " + removedCount + " apartments with values greater than the given one.");
        } catch (Exception e) {
            System.err.println("Error deleting apartments: " + e.getMessage());
        }
    }
}
