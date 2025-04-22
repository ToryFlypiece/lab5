package flatset.commands;

import flatset.Coordinates;
import flatset.Flat;
import flatset.House;
import flatset.View;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Scanner;

/**
 * Команда для добавления нового элемента в коллекцию квартир.
 * Поддерживает два режима:
 * 1. Аргументный режим: все параметры передаются одной строкой
 * 2. Интерактивный режим: запрашивает параметры по одному
 */

public class UpdateCommand implements Command {

    private final Scanner scanner;

    public UpdateCommand() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void execute(HashSet<Flat> flatSet, String argument) {
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

            Optional<Flat> existing = flatSet.stream()
                    .filter(f -> f.getId() == id)
                    .findFirst();

            if (existing.isPresent()) {
                updatedFlat.setId(id);
                flatSet.remove(existing.get());
                flatSet.add(updatedFlat);
                System.out.println("Updated apartment with ID " + id);
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
