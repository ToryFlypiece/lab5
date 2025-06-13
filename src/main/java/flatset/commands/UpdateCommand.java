package flatset.commands;

import flatset.*;
import flatset.auth.User;

import java.sql.*;
import java.time.ZonedDateTime;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

public class UpdateCommand implements InteractiveCommand {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/flatset";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "admin";

    @Override
    public void execute(Set<Flat> flatSet, String argument, User currentUser, Scanner scanner) {
        try {
            if (argument == null || argument.trim().isEmpty()) {
                updateInteractive(flatSet, currentUser, scanner);
            } else {
                updateFromArgument(flatSet, argument, currentUser);
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Input error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error updating an element: " + e.getMessage());
        }
    }

    @Override
    public void execute(Set<Flat> flatSet, String argument, User currentUser) {
        throw new UnsupportedOperationException("Use execute with Scanner");
    }

    private void updateFromArgument(Set<Flat> flatSet, String argument, User currentUser) throws SQLException {
        // Аргумент должен содержать id и параметры, формат примерно:
        // {id, name,x,y,area,numberOfRooms,isNew,timeToMetroByTransport,view, {houseName,houseYear,houseNumberOfFlats}}

        if (!argument.startsWith("{") || !argument.endsWith("}")) {
            throw new IllegalArgumentException("Invalid format. Parameters must be in curly brackets");
        }

        String content = argument.substring(1, argument.length() - 1);
        int firstComma = content.indexOf(',');
        if (firstComma == -1) {
            throw new IllegalArgumentException("Must specify ID first");
        }

        // Получаем id
        String idStr = content.substring(0, firstComma).trim();
        long id = Long.parseLong(idStr);

        String rest = content.substring(firstComma + 1).trim();

        int houseStartIndex = rest.lastIndexOf("{");
        int houseEndIndex = rest.lastIndexOf("}");

        if (houseStartIndex == -1 || houseEndIndex == -1) {
            throw new IllegalArgumentException("Invalid format. Should include house information.");
        }

        String flatParams = rest.substring(0, houseStartIndex).trim();
        String houseParams = rest.substring(houseStartIndex, houseEndIndex + 1).trim();

        String[] flatArgs = flatParams.split(",");
        if (flatArgs.length != 8) {
            throw new IllegalArgumentException("Invalid flat format. Required: name,x,y,area,numberOfRooms,isNew,timeToMetroByTransport,view");
        }

        String name = flatArgs[0].trim();
        int x = Integer.parseInt(flatArgs[1].trim());
        int y = Integer.parseInt(flatArgs[2].trim());

        long area = Long.parseLong(flatArgs[3].trim());
        if (area <= 0) throw new IllegalArgumentException("Area must be a positive number");

        long numberOfRooms = Long.parseLong(flatArgs[4].trim());
        if (numberOfRooms <= 0) throw new IllegalArgumentException("Number of rooms must be a positive number");

        String isNewStr = flatArgs[5].trim().toLowerCase();
        if (!isNewStr.equals("true") && !isNewStr.equals("false")) {
            throw new IllegalArgumentException("isNew must be true or false");
        }
        Boolean isNew = Boolean.parseBoolean(isNewStr);

        double timeToMetroByTransport = Double.parseDouble(flatArgs[6].trim());
        if (timeToMetroByTransport < 0) {
            throw new IllegalArgumentException("Time to metro must be non-negative");
        }

        View view;
        try {
            view = View.valueOf(flatArgs[7].trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid view type");
        }

        String[] houseArgs = houseParams.substring(1, houseParams.length() - 1).split(",");
        if (houseArgs.length != 3) {
            throw new IllegalArgumentException("Invalid format for house. Required: houseName,houseYear,houseNumberOfFlats");
        }

        String houseName = houseArgs[0].trim();
        int houseYear = Integer.parseInt(houseArgs[1].trim());
        if (houseYear <= 0) throw new IllegalArgumentException("House year must be a positive integer");

        int houseNumberOfFlats = Integer.parseInt(houseArgs[2].trim());
        if (houseNumberOfFlats <= 0) throw new IllegalArgumentException("Number of flats in house must be positive");

        House house = new House(houseName, houseYear, houseNumberOfFlats);

        updateFlatInDbAndMemory(flatSet, id, name, x, y, area, numberOfRooms, isNew, timeToMetroByTransport, view, house, currentUser);
    }

    private void updateInteractive(Set<Flat> flatSet, User currentUser, Scanner scanner) throws SQLException {
        System.out.println("\nUpdating flat (interactive mode)");
        System.out.println("--------------------------------");

        long id = promptLong(scanner, "Enter ID of flat to update: ");

        // Найдем flat в памяти, проверим права
        Flat flatToUpdate = flatSet.stream()
                .filter(f -> f.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Flat with ID " + id + " not found"));

        if (flatToUpdate.getOwnerId() != currentUser.getId()) {
            System.err.println("You do not have permission to update this flat.");
            return;
        }

        System.out.println("Leave input empty to keep current value.");

        String name = promptOrDefault(scanner, "Enter flat name (" + flatToUpdate.getName() + "): ", flatToUpdate.getName());
        int x = promptIntOrDefault(scanner, "Enter coordinate X (" + flatToUpdate.getCoordinates().getX() + "): ", flatToUpdate.getCoordinates().getX());
        int y = promptIntOrDefault(scanner, "Enter coordinate Y (" + flatToUpdate.getCoordinates().getY() + "): ", flatToUpdate.getCoordinates().getY());

        long area = promptLongOrDefault(scanner, "Enter area (" + flatToUpdate.getArea() + "): ", flatToUpdate.getArea());
        long numberOfRooms = promptLongOrDefault(scanner, "Enter number of rooms (" + flatToUpdate.getNumberOfRooms() + "): ", flatToUpdate.getNumberOfRooms());
        Boolean isNew = promptBooleanOrDefault(scanner, "Is it new? (" + flatToUpdate.isNew() + "): ", flatToUpdate.isNew());
        double timeToMetroByTransport = promptDoubleOrDefault(scanner, "Enter time to metro by transport (" + flatToUpdate.getTimeToMetroByTransport() + "): ", flatToUpdate.getTimeToMetroByTransport());

        System.out.println("Available views: " + java.util.Arrays.toString(View.values()));
        View view = promptViewOrDefault(scanner, "Enter view (" + flatToUpdate.getView() + "): ", flatToUpdate.getView());

        System.out.println("\nEnter house details:");
        House house = flatToUpdate.getHouse();

        String houseName = promptOrDefault(scanner, "House name (" + house.getName() + "): ", house.getName());
        int houseYear = promptIntOrDefault(scanner, "House year (" + house.getYear() + "): ", house.getYear());
        int houseNumberOfFlats = promptIntOrDefault(scanner, "Number of flats in house (" + house.getNumberOfFlatsOnFloor() + "): ", house.getNumberOfFlatsOnFloor());

        House newHouse = new House(houseName, houseYear, houseNumberOfFlats);

        updateFlatInDbAndMemory(flatSet, id, name, x, y, area, numberOfRooms, isNew, timeToMetroByTransport, view, newHouse, currentUser);
    }

    private void updateFlatInDbAndMemory(Set<Flat> flatSet, long id, String name, int x, int y, long area,
                                         long numberOfRooms, Boolean isNew, double timeToMetroByTransport,
                                         View view, House house, User currentUser) throws SQLException {
        Coordinates coordinates = new Coordinates(x, y);

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            conn.setAutoCommit(false);

            // Проверяем, что квартира существует и принадлежит текущему пользователю
            String checkSql = "SELECT owner_id, house_id FROM flats WHERE id = ?";
            Integer existingOwnerId = null;
            Integer existingHouseId = null;
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setLong(1, id);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        existingOwnerId = rs.getInt("owner_id");
                        existingHouseId = rs.getObject("house_id") != null ? rs.getInt("house_id") : null;
                    } else {
                        throw new IllegalArgumentException("Flat with ID " + id + " does not exist");
                    }
                }
            }

            if (existingOwnerId == null || existingOwnerId != currentUser.getId()) {
                throw new IllegalArgumentException("You do not have permission to update this flat.");
            }

            // Обновим или вставим дом, если он изменился
            Integer houseId = existingHouseId;
            if (house != null) {
                String houseSQL;
                if (houseId == null) {
                    // Вставка нового дома
                    houseSQL = "INSERT INTO houses (name, year, number_of_flats_on_floor) VALUES (?, ?, ?) RETURNING id";
                    try (PreparedStatement houseStmt = conn.prepareStatement(houseSQL)) {
                        houseStmt.setString(1, house.getName());
                        houseStmt.setInt(2, house.getYear());
                        houseStmt.setInt(3, house.getNumberOfFlatsOnFloor());
                        try (ResultSet rs = houseStmt.executeQuery()) {
                            if (rs.next()) {
                                houseId = rs.getInt("id");
                            }
                        }
                    }
                } else {
                    // Обновление существующего дома
                    houseSQL = "UPDATE houses SET name = ?, year = ?, number_of_flats_on_floor = ? WHERE id = ?";
                    try (PreparedStatement houseStmt = conn.prepareStatement(houseSQL)) {
                        houseStmt.setString(1, house.getName());
                        houseStmt.setInt(2, house.getYear());
                        houseStmt.setInt(3, house.getNumberOfFlatsOnFloor());
                        houseStmt.setInt(4, houseId);
                        houseStmt.executeUpdate();
                    }
                }
            }

            String flatSQL = "UPDATE flats SET name = ?, x = ?, y = ?, area = ?, number_of_rooms = ?, is_new = ?, " +
                    "time_to_metro_by_transport = ?, view = ?::flat_view_enum, house_id = ? WHERE id = ?";

            try (PreparedStatement flatStmt = conn.prepareStatement(flatSQL)) {
                flatStmt.setString(1, name);
                flatStmt.setInt(2, coordinates.getX());
                flatStmt.setInt(3, coordinates.getY());
                flatStmt.setLong(4, area);
                flatStmt.setLong(5, numberOfRooms);
                flatStmt.setObject(6, isNew);
                flatStmt.setDouble(7, timeToMetroByTransport);
                flatStmt.setString(8, view.toString());
                if (houseId != null) {
                    flatStmt.setInt(9, houseId);
                } else {
                    flatStmt.setNull(9, Types.INTEGER);
                }
                flatStmt.setLong(10, id);

                int updatedRows = flatStmt.executeUpdate();
                if (updatedRows == 0) {
                    conn.rollback();
                    throw new SQLException("Failed to update flat in database");
                }
            }

            conn.commit();

            flatSet.removeIf(f -> f.getId() == id);

            Flat updatedFlat = new Flat();
            updatedFlat.setId(id);
            updatedFlat.setName(name);
            updatedFlat.setCoordinates(coordinates);
            updatedFlat.setCreationDate(ZonedDateTime.now()); // Можно сохранить дату создания из БД, если нужно
            updatedFlat.setArea(area);
            updatedFlat.setNumberOfRooms(numberOfRooms);
            updatedFlat.setNew(isNew);
            updatedFlat.setTimeToMetroByTransport(timeToMetroByTransport);
            updatedFlat.setView(view);
            updatedFlat.setHouse(house);
            updatedFlat.setOwnerId(currentUser.getId());

            flatSet.add(updatedFlat);

            System.out.println("\nElement updated successfully: " + updatedFlat);

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            throw e;
        }
    }

    private String promptOrDefault(Scanner scanner, String message, String defaultValue) {
        System.out.print(message);
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) return defaultValue;
        return input;
    }

    private int promptIntOrDefault(Scanner scanner, String message, int defaultValue) {
        while (true) {
            String input = promptOrDefault(scanner, message, Integer.toString(defaultValue));
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid integer. Try again.");
            }
        }
    }

    private long promptLongOrDefault(Scanner scanner, String message, long defaultValue) {
        while (true) {
            String input = promptOrDefault(scanner, message, Long.toString(defaultValue));
            try {
                return Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid long integer. Try again.");
            }
        }
    }

    private double promptDoubleOrDefault(Scanner scanner, String message, double defaultValue) {
        while (true) {
            String input = promptOrDefault(scanner, message, Double.toString(defaultValue));
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }

    private Boolean promptBooleanOrDefault(Scanner scanner, String message, Boolean defaultValue) {
        while (true) {
            String input = promptOrDefault(scanner, message, defaultValue.toString()).toLowerCase();
            if (input.equals("true")) return true;
            if (input.equals("false")) return false;
            System.out.println("Input must be true or false. Try again.");
        }
    }

    private View promptViewOrDefault(Scanner scanner, String message, View defaultValue) {
        while (true) {
            String input = promptOrDefault(scanner, message, defaultValue.toString()).toUpperCase();
            try {
                return View.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid view. Available options: " + java.util.Arrays.toString(View.values()));
            }
        }
    }

    private long promptLong(Scanner scanner, String message) {
        while (true) {
            try {
                String input = prompt(scanner, message, false);
                return Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid long integer. Try again.");
            }
        }
    }

    private String prompt(Scanner scanner, String message, boolean allowEmpty) {
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
}
