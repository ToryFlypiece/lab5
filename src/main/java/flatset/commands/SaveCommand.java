package flatset.commands;

import flatset.Flat;
import flatset.House;
import flatset.auth.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.sql.ResultSet;

/**
 * Команда для сохранения данных о квартирах в базу данных PostgreSQL.
 */
public class SaveCommand implements Command {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/flatset";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "admin";

    @Override
    public void execute(HashSet<Flat> flatSet, String argument, User currentUser) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            conn.setAutoCommit(false);

            String houseSQL = "INSERT INTO houses (name, year, number_of_flats_on_floor) " +
                    "VALUES (?, ?, ?) " +
                    "RETURNING id";

            String flatSQL = "INSERT INTO flats (name, x, y, creation_date, area, number_of_rooms, is_new, " +
                    "time_to_metro_by_transport, view, house_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?::flat_view_enum, ?) RETURNING id";

            for (Flat flat : flatSet) {
                Integer houseId = null;
                House house = flat.getHouse();

                if (house != null) {
                    try (PreparedStatement houseStmt = conn.prepareStatement(houseSQL)) {
                        houseStmt.setString(1, house.getName());
                        houseStmt.setInt(2, house.getYear());
                        houseStmt.setInt(3, house.getNumberOfFlatsOnFloor());
                        ResultSet rs = houseStmt.executeQuery();
                        if (rs.next()) {
                            houseId = rs.getInt("id");
                        }
                    }
                }

                try (PreparedStatement flatStmt = conn.prepareStatement(flatSQL)) {
                    flatStmt.setString(1, flat.getName());
                    flatStmt.setInt(2, flat.getCoordinates().getX());
                    flatStmt.setInt(3, flat.getCoordinates().getY());
                    flatStmt.setTimestamp(4, java.sql.Timestamp.valueOf(flat.getCreationDate().toLocalDateTime()));
                    flatStmt.setLong(5, flat.getArea());
                    flatStmt.setLong(6, flat.getNumberOfRooms());
                    flatStmt.setObject(7, flat.isNew()); // может быть null
                    flatStmt.setDouble(8, flat.getTimeToMetroByTransport());
                    flatStmt.setString(9, flat.getView().toString());

                    if (houseId != null) {
                        flatStmt.setInt(10, houseId);
                    } else {
                        flatStmt.setNull(10, java.sql.Types.INTEGER);
                    }

                    ResultSet rs = flatStmt.executeQuery();
                    if (rs.next()) {
                        flat.setId(rs.getLong("id")); // обновляем объект в памяти
                    }
                }
            }

            conn.commit();
            System.out.println("Successfully saved " + flatSet.size() + " apartment(s) to the database.");

        } catch (SQLException e) {
            System.err.println("Database error while saving flats: " + e.getMessage());
        }
    }
}
