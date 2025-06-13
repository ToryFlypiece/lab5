package flatset.commands;

import flatset.Flat;
import flatset.auth.User;
import flatset.utils.FlatUpdater;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.HashSet;
import java.util.Optional;

/**
 * Команда для обновления данных квартиры по ID.
 */
public class UpdateByIdCommand implements Command {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/flatset";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "admin";

    /**
     * Выполняет команду обновления данных квартиры по ID.
     * Для этого извлекаются данные поля, которые нужно обновить, в формате {field:value}.
     * Если квартира с заданным ID найдена, ее поля обновляются.
     * В случае ошибки или некорректного ввода выводится соответствующее сообщение.
     *
     * @param flatSet Коллекция квартир, в которой необходимо обновить данные.
     * @param argument Аргумент команды, содержащий ID квартиры и строку с полями для обновления в формате {field:value}.
     */
    @Override
    public void execute(HashSet<Flat> flatSet, String argument, User currentUser) {
        try {
            String[] parts = argument.split(" ", 2);
            if (parts.length != 2) {
                throw new IllegalArgumentException("Usage: update_by_id id {field:value}");
            }

            long id = Long.parseLong(parts[0]);
            String jsonData = parts[1];

            Optional<Flat> toUpdateOpt = flatSet.stream()
                    .filter(f -> f.getId() == id)
                    .findFirst();

            if (toUpdateOpt.isPresent()) {
                Flat toUpdate = toUpdateOpt.get();

                if (toUpdate.getOwnerId() == null || !toUpdate.getOwnerId().equals(currentUser.getId())) {
                    System.out.println("You do not have permission to update this apartment.");
                    return;
                }

                try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                    FlatUpdater.updateFields(toUpdate, jsonData);

                    PreparedStatement ps = conn.prepareStatement(
                            "UPDATE flats SET name = ?, x = ?, y = ?, area = ?, number_of_rooms = ?, is_new = ?, " +
                                    "time_to_metro_by_transport = ?, view = ?, house_name = ?, house_year = ?, number_of_flats_on_floor = ? " +
                                    "WHERE id = ? AND owner_id = ?"
                    );

                    ps.setString(1, toUpdate.getName());
                    ps.setInt(2, toUpdate.getCoordinates().getX());
                    ps.setInt(3, toUpdate.getCoordinates().getY());
                    ps.setLong(4, toUpdate.getArea());
                    ps.setLong(5, toUpdate.getNumberOfRooms());
                    ps.setBoolean(6, toUpdate.isNew());
                    ps.setDouble(7, toUpdate.getTimeToMetroByTransport());
                    ps.setString(8, toUpdate.getView() != null ? toUpdate.getView().name() : null);
                    if (toUpdate.getHouse() != null) {
                        ps.setString(9, toUpdate.getHouse().getName());
                        ps.setInt(10, toUpdate.getHouse().getYear());
                        ps.setInt(11, toUpdate.getHouse().getNumberOfFlatsOnFloor());
                    } else {
                        ps.setNull(9, java.sql.Types.VARCHAR);
                        ps.setNull(10, java.sql.Types.INTEGER);
                        ps.setNull(11, java.sql.Types.INTEGER);
                    }
                    ps.setLong(12, id);
                    ps.setInt(13, currentUser.getId());

                    int rowsUpdated = ps.executeUpdate();

                    if (rowsUpdated > 0) {
                        System.out.println("Updated fields of apartment with ID " + id);
                    } else {
                        System.out.println("Apartment with ID " + id + " not found or you do not have permission to update.");
                    }
                }
            } else {
                System.out.println("Apartment with ID " + id + " not found.");
            }
        } catch (Exception e) {
            System.err.println("Error updating: " + e.getMessage());
        }
    }
}
