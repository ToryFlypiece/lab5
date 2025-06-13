package flatset.utils;

import flatset.Coordinates;
import flatset.Flat;
import flatset.House;
import flatset.View;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashSet;

public class FlatLoader {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/flatset";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "admin";

    /**
     * Загружает исходные данные из базы данных
     * @return Множество загруженных квартир или пустое множество при ошибках
     */
    public static HashSet<Flat> loadInitialData() {
        HashSet<Flat> flatSet = new HashSet<>();

        String sql = "SELECT f.id, f.name, f.x, f.y, f.creation_date, f.area, f.number_of_rooms, " +
                "f.is_new, f.time_to_metro_by_transport, f.view, " +
                "h.name AS house_name, h.year AS house_year, h.number_of_flats_on_floor " +
                "FROM flats f LEFT JOIN houses h ON f.house_id = h.id";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Flat flat = new Flat();

                flat.setId(rs.getLong("id"));
                flat.setName(rs.getString("name"));

                // Парсинг координат
                flat.setCoordinates(new Coordinates(rs.getInt("x"), rs.getInt("y")));

                // Время создания
                java.sql.Timestamp ts = rs.getTimestamp("creation_date");
                if (ts != null) {
                    flat.setCreationDate(ts.toInstant().atZone(ZoneId.systemDefault()));
                }

                // Числовые поля
                flat.setArea(rs.getLong("area"));
                flat.setNumberOfRooms(rs.getLong("number_of_rooms"));

                // Опциональное булево поле
                Boolean isNew = (Boolean) rs.getObject("is_new");
                flat.setNew(isNew != null ? isNew : false);

                // Время до метро
                flat.setTimeToMetroByTransport(rs.getDouble("time_to_metro_by_transport"));

                // View
                String viewStr = rs.getString("view");
                if (viewStr != null) {
                    try {
                        flat.setView(View.valueOf(viewStr));
                    } catch (IllegalArgumentException e) {
                        System.err.println("Warning: unknown view enum value in DB: \"" + viewStr + "\" for flat ID " + rs.getLong("id"));
                        continue;
                    }
                }

                // Дом (опционально)
                String houseName = rs.getString("house_name");
                if (houseName != null) {
                    House house = new House();
                    house.setName(houseName);
                    house.setYear(rs.getInt("house_year"));
                    house.setNumberOfFlatsOnFloor(rs.getInt("number_of_flats_on_floor"));
                    flat.setHouse(house);
                }

                flatSet.add(flat);
            }

        } catch (SQLException e) {
            System.err.println("Error loading flats from database: " + e.getMessage());
        }

        System.out.println("Loaded " + flatSet.size() + " flats from database.");
        return flatSet;
    }
}
