package flatset.commands;

import flatset.*;
import flatset.auth.User;
import flatset.utils.FlatParser;

import java.sql.*;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.Optional;

public class AddIfMaxCommand implements Command {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/flatset";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "admin";

    @Override
    public void execute(Set<Flat> flatSet, String argument, User currentUser) {
        try {
            Flat newFlat = FlatParser.parseFlat(argument);

            Optional<Flat> maxFlat = flatSet.stream().max(Flat::compareTo);

            if (!maxFlat.isPresent() || newFlat.compareTo(maxFlat.get()) > 0) {
                Flat flatFromDb = addFlatToDb(newFlat, currentUser);
                flatSet.add(flatFromDb);
                System.out.println("Added new flat: " + flatFromDb);
            } else {
                System.out.println("Flat value is lower than the maximum in the collection.");
            }
        } catch (Exception e) {
            System.err.println("Error adding a flat: " + e.getMessage());
        }
    }

    private Flat addFlatToDb(Flat flat, User currentUser) throws SQLException {
        ZonedDateTime creationDate = ZonedDateTime.now();
        flat.setCreationDate(creationDate);

        Coordinates coordinates = flat.getCoordinates();
        House house = flat.getHouse();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            conn.setAutoCommit(false);

            Integer houseId = null;
            if (house != null) {
                String houseSQL = "INSERT INTO houses (name, year, number_of_flats_on_floor) VALUES (?, ?, ?) RETURNING id";
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

            String flatSQL = "INSERT INTO flats " +
                    "(name, x, y, creation_date, area, number_of_rooms, is_new, time_to_metro_by_transport, view, house_id, owner_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?::flat_view_enum, ?, ?) RETURNING id";

            long flatId;
            try (PreparedStatement flatStmt = conn.prepareStatement(flatSQL)) {
                flatStmt.setString(1, flat.getName());
                flatStmt.setInt(2, coordinates.getX());
                flatStmt.setInt(3, coordinates.getY());
                flatStmt.setTimestamp(4, Timestamp.valueOf(creationDate.toLocalDateTime()));
                flatStmt.setLong(5, flat.getArea());
                flatStmt.setLong(6, flat.getNumberOfRooms());
                flatStmt.setObject(7, flat.isNew());
                flatStmt.setDouble(8, flat.getTimeToMetroByTransport());
                flatStmt.setString(9, flat.getView().toString());
                if (houseId != null) {
                    flatStmt.setInt(10, houseId);
                } else {
                    flatStmt.setNull(10, Types.INTEGER);
                }
                flatStmt.setInt(11, currentUser.getId());

                ResultSet rs = flatStmt.executeQuery();
                if (rs.next()) {
                    flatId = rs.getLong("id");
                } else {
                    conn.rollback();
                    throw new SQLException("Failed to insert flat into database");
                }
            }

            conn.commit();

            flat.setId(flatId);
            flat.setHouse(house);
            return flat;

        }
    }
}
