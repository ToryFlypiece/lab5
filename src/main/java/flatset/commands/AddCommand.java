package flatset.commands;

import flatset.Coordinates;
import flatset.Flat;
import flatset.View;
import flatset.House;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Команда для добавления нового элемента в коллекцию квартир.
 * Поддерживает два режима:
 * 1. Аргументный режим: все параметры передаются одной строкой
 * 2. Интерактивный режим: запрашивает параметры по одному
 */
public class AddCommand implements Command {
    private final Scanner scanner;

    public AddCommand() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void execute(HashSet<Flat> flatSet, String argument) {
        try {
            if (argument == null || argument.trim().isEmpty()) {
                addInteractive(flatSet);
            } else {
                addFromArgument(flatSet, argument);
            }
        } catch (Exception e) {
            System.err.println("Error adding an element: " + e.getMessage());
        }
    }

    private void addFromArgument(HashSet<Flat> flatSet, String argument) {
        if (!argument.startsWith("{") || !argument.endsWith("}")) {
            throw new IllegalArgumentException("Invalid format. Parameters must be in curly brackets");
        }

        String content = argument.substring(1, argument.length() - 1);
        int houseStartIndex = content.lastIndexOf("{");
        int houseEndIndex = content.lastIndexOf("}");

        if (houseStartIndex == -1 || houseEndIndex == -1) {
            throw new IllegalArgumentException("Invalid format. Should include house information.");
        }

        String flatParams = content.substring(0, houseStartIndex).trim();
        String houseParams = content.substring(houseStartIndex, houseEndIndex + 1).trim();

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
        addFlat(flatSet, name, x, y, area, numberOfRooms, isNew, timeToMetroByTransport, view, house);
    }

    private void addInteractive(HashSet<Flat> flatSet) {
        System.out.println("\nAdding new flat (interactive mode)");
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

        House house = new House(houseName, houseYear, houseNumberOfFlats);
        addFlat(flatSet, name, x, y, area, numberOfRooms, isNew, timeToMetroByTransport, view, house);
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

    private void addFlat(HashSet<Flat> flatSet, String name, int x, int y, long area,
                         long numberOfRooms, Boolean isNew, double timeToMetroByTransport,
                         View view, House house) {
        long id = generateAutoIncrementId(flatSet);
        ZonedDateTime creationDate = ZonedDateTime.now();
        Coordinates coordinates = new Coordinates(x, y);

        Flat flat = new Flat();
        flat.setId(id);
        flat.setName(name);
        flat.setCoordinates(coordinates);
        flat.setCreationDate(creationDate);
        flat.setArea(area);
        flat.setNumberOfRooms(numberOfRooms);
        flat.setNew(isNew);
        flat.setTimeToMetroByTransport(timeToMetroByTransport);
        flat.setView(view);
        flat.setHouse(house);

        flatSet.add(flat);
        System.out.println("\nElement added successfully: " + flat);
    }

    private long generateAutoIncrementId(HashSet<Flat> flatSet) {
        long maxId = flatSet.stream()
                .mapToLong(Flat::getId)
                .max()
                .orElse(0);
        return maxId + 1;
    }
}
