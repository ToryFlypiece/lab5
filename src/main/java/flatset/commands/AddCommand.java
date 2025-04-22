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
                // Интерактивный режим
                addInteractive(flatSet);
            } else {
                // Режим с аргументами
                addFromArgument(flatSet, argument);
            }
        } catch (Exception e) {
            System.err.println("Error adding an element: " + e.getMessage());
        }
    }

    private void addFromArgument(HashSet<Flat> flatSet, String argument) {
        // Ваш существующий код для обработки аргументов
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
        long numberOfRooms = Long.parseLong(flatArgs[4].trim());
        Boolean isNew = Boolean.parseBoolean(flatArgs[5].trim());
        double timeToMetroByTransport = Double.parseDouble(flatArgs[6].trim());
        View view = View.valueOf(flatArgs[7].trim().toUpperCase());

        String[] houseArgs = houseParams.substring(1, houseParams.length() - 1).split(",");
        if (houseArgs.length != 3) {
            throw new IllegalArgumentException("Invalid format for house. Required: houseName,houseYear,houseNumberOfFlats");
        }

        String houseName = houseArgs[0].trim();
        int houseYear = Integer.parseInt(houseArgs[1].trim());
        int houseNumberOfFlats = Integer.parseInt(houseArgs[2].trim());

        House house = new House(houseName, houseYear, houseNumberOfFlats);
        addFlat(flatSet, name, x, y, area, numberOfRooms, isNew, timeToMetroByTransport, view, house);
    }

    private void addInteractive(HashSet<Flat> flatSet) {
        System.out.println("\nAdding new flat (interactive mode)");
        System.out.println("---------------------------------");

        // Основные параметры квартиры
        String name = prompt("Enter flat name: ", false);
        int x = Integer.parseInt(prompt("Enter coordinate X (integer): ", false));
        int y = Integer.parseInt(prompt("Enter coordinate Y (integer): ", false));
        long area = Long.parseLong(prompt("Enter area (long): ", false));
        long numberOfRooms = Long.parseLong(prompt("Enter number of rooms (long): ", false));
        Boolean isNew = Boolean.parseBoolean(prompt("Is it new? (true/false): ", false));
        double timeToMetroByTransport = Double.parseDouble(prompt("Enter time to metro by transport (double): ", false));

        // Параметры вида
        System.out.println("Available views: " + java.util.Arrays.toString(View.values()));
        View view = View.valueOf(prompt("Enter view: ", false).toUpperCase());

        // Параметры дома
        System.out.println("\nEnter house details:");
        String houseName = prompt("House name: ", false);
        int houseYear = Integer.parseInt(prompt("House year (integer): ", false));
        int houseNumberOfFlats = Integer.parseInt(prompt("Number of flats in house (integer): ", false));

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