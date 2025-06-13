package flatset.commands;

import flatset.Coordinates;
import flatset.Flat;
import flatset.House;
import flatset.View;
import flatset.auth.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.NoSuchElementException;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Scanner;

/**
 * Команда для обновления значений элемента в коллекции квартир.
 * Поддерживает два режима:
 * 1. Аргументный режим: все параметры передаются одной строкой
 * 2. Интерактивный режим: запрашивает параметры по одному
 */

public class UpdateCommand implements Command {

    private final Scanner scanner;

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/flatset";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "admin";

    public UpdateCommand() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void execute(HashSet<Flat> flatSet, String argument, User currentUser) {
        try {
            long id;
            Flat updatedFlat;

            if (argument == null || argument.trim().isEmpty()) {
                System.out.print("Enter ID of the flat to update: ");
                String idInput = scanner.nextLine().trim();
                id = Long.parseLong(idInput);
                updatedFlat = promptForFlatData();
            } else {
                String[] parts = argument.trim().split(" ", 2);
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Usage: update <id> {flat_data} or just 'update' for interactive mode.");
                }
                id = Long.parseLong(parts[0]);
                updatedFlat = flatset.utils.FlatParser.parseFlat(parts[1]);
            }

            Optional<Flat> existingOpt = flatSet.stream()
                    .filter(f -> f.getId() == id)
                    .findFirst();

            if (existingOpt.isPresent()) {
                Flat existing = existingOpt.get();

                if (existing.getOwnerId() == null || !existing.getOwnerId().equals(currentUser.getId())) {
                    System.out.println("You do not have permission to update this apartment.");
                    return;
                }

                updatedFlat.setId(id);

                try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                    PreparedStatement ps = conn.prepareStatement(
                            "UPDATE flats SET name = ?, x = ?, y = ?, area = ?, number_of_rooms = ?, is_new = ?, " +
                                    "time_to_metro_by_transport = ?, view = ?, house_name = ?, house_year = ?, number_of_flats_on_floor = ? " +
                                    "WHERE id = ? AND owner_id = ?"
                    );

                    ps.setString(1, updatedFlat.getName());
                    ps.setInt(2, updatedFlat.getCoordinates().getX());
                    ps.setInt(3, updatedFlat.getCoordinates().getY());
                    ps.setLong(4, updatedFlat.getArea());
                    ps.setLong(5, updatedFlat.getNumberOfRooms());
                    ps.setBoolean(6, updatedFlat.isNew());
                    ps.setDouble(7, updatedFlat.getTimeToMetroByTransport());
                    ps.setString(8, updatedFlat.getView() != null ? updatedFlat.getView().name() : null);
                    if (updatedFlat.getHouse() != null) {
                        ps.setString(9, updatedFlat.getHouse().getName());
                        ps.setInt(10, updatedFlat.getHouse().getYear());
                        ps.setInt(11, updatedFlat.getHouse().getNumberOfFlatsOnFloor());
                    } else {
                        ps.setNull(9, java.sql.Types.VARCHAR);
                        ps.setNull(10, java.sql.Types.INTEGER);
                        ps.setNull(11, java.sql.Types.INTEGER);
                    }
                    ps.setLong(12, id);
                    ps.setInt(13, currentUser.getId());

                    int rowsUpdated = ps.executeUpdate();

                    if (rowsUpdated > 0) {
                        flatSet.remove(existing);
                        flatSet.add(updatedFlat);
                        System.out.println("Updated apartment with ID " + id);
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

    private Flat promptForFlatData() {
        System.out.println("\nUpdating flat (interactive mode)");
        System.out.println("---------------------------------");

        String name = prompt("Enter flat name: ", false);
        int x = promptInt("Enter coordinate X (integer): ");
        int y = promptInt("Enter coordinate Y (integer): ");
        long area = promptPositiveLong("Enter area (positive long): ");
        long numberOfRooms = promptPositiveLong("Enter number of rooms (positive long): ");
        Boolean isNew = promptBoolean("Is it new? (true/false): ");
        double timeToMetroByTransport = promptNonNegativeDouble("Enter time to metro by transport (non-negative double): ");
        System.out.println("Available views: " + java.util.Arrays.toString(View.values()));
        View view = promptView("Enter view: ");

        System.out.println("\nEnter house details:");
        String houseName = prompt("House name: ", false);
        int houseYear = promptPositiveInt("House year (positive integer): ");
        int houseNumberOfFlats = promptPositiveInt("Number of flats in house (positive integer): ");

        Coordinates coordinates = new Coordinates(x, y);
        House house = new House(houseName, houseYear, houseNumberOfFlats);

        Flat flat = new Flat();
        flat.setName(name);
        flat.setCoordinates(coordinates);
        flat.setCreationDate(ZonedDateTime.now());
        flat.setArea(area);
        flat.setNumberOfRooms(numberOfRooms);
        flat.setNew(isNew);
        flat.setTimeToMetroByTransport(timeToMetroByTransport);
        flat.setView(view);
        flat.setHouse(house);

        return flat;
    }

    private String prompt(String message, boolean allowEmpty) {
        while (true) {
            System.out.print(message);
            try {
                String input = scanner.nextLine().trim();
                if (!allowEmpty && input.isEmpty()) {
                    System.out.println("This field cannot be empty. Please try again.");
                    continue;
                }
                return input;
            } catch (NoSuchElementException e) {
                System.out.println("\nEOF detected. Exiting program.");
                System.exit(0);
            } catch (Exception e) {
                System.out.println("Invalid input. Please try again.");
            }
        }
    }

    private int promptInt(String message) {
        while (true) {
            try {
                return Integer.parseInt(prompt(message, false));
            } catch (NumberFormatException e) {
                System.out.println("Invalid integer. Try again.");
            }
        }
    }

    private int promptPositiveInt(String message) {
        while (true) {
            int value = promptInt(message);
            if (value > 0) return value;
            System.out.println("Value must be positive. Try again.");
        }
    }

    private long promptPositiveLong(String message) {
        while (true) {
            try {
                long value = Long.parseLong(prompt(message, false));
                if (value > 0) return value;
                System.out.println("Value must be positive. Try again.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid long. Try again.");
            }
        }
    }

    private double promptNonNegativeDouble(String message) {
        while (true) {
            try {
                double value = Double.parseDouble(prompt(message, false));
                if (value >= 0) return value;
                System.out.println("Value must be non-negative. Try again.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid double. Try again.");
            }
        }
    }

    private Boolean promptBoolean(String message) {
        while (true) {
            String input = prompt(message, false).toLowerCase();
            if (input.equals("true")) return true;
            if (input.equals("false")) return false;
            System.out.println("Invalid value. Enter 'true' or 'false'.");
        }
    }

    private View promptView(String message) {
        while (true) {
            try {
                return View.valueOf(prompt(message, false).toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid view. Options: " + java.util.Arrays.toString(View.values()));
            }
        }
    }
}
