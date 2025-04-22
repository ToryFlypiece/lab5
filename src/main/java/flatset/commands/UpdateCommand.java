package flatset.commands;

import flatset.Coordinates;
import flatset.Flat;
import flatset.House;
import flatset.View;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Scanner;

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
                // Interactive mode - ask for ID first
                System.out.print("Enter ID of the flat to update: ");
                String idInput = scanner.nextLine().trim();
                id = Long.parseLong(idInput);
                updatedFlat = promptForFlatData();
            } else {
                // update <id> {flat_data}
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

        String name = prompt("Enter flat name: ");
        int x = Integer.parseInt(prompt("Enter coordinate X (integer): "));
        int y = Integer.parseInt(prompt("Enter coordinate Y (integer): "));
        long area = Long.parseLong(prompt("Enter area (long): "));
        long numberOfRooms = Long.parseLong(prompt("Enter number of rooms (long): "));
        Boolean isNew = Boolean.parseBoolean(prompt("Is it new? (true/false): "));
        double timeToMetroByTransport = Double.parseDouble(prompt("Enter time to metro by transport (double): "));
        System.out.println("Available views: " + java.util.Arrays.toString(View.values()));
        View view = View.valueOf(prompt("Enter view: ").toUpperCase());

        System.out.println("\nEnter house details:");
        String houseName = prompt("House name: ");
        int houseYear = Integer.parseInt(prompt("House year (integer): "));
        int houseNumberOfFlats = Integer.parseInt(prompt("Number of flats in house (integer): "));

        House house = new House(houseName, houseYear, houseNumberOfFlats);
        Coordinates coordinates = new Coordinates(x, y);

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

    private String prompt(String message) {
        while (true) {
            System.out.print(message);
            try {
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    System.out.println("This field cannot be empty.");
                    continue;
                }
                return input;
            } catch (Exception e) {
                System.out.println("Invalid input. Try again.");
            }
        }
    }
}
