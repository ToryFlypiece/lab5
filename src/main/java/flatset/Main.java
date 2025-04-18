package flatset;

import flatset.commands.CommandManager;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class Main {
    public static void main(String[] args) {
        // Load data from file
        String filePath = "list.json";
        HashSet<Flat> flatSet = loadDataFromFile(filePath);

        // Initialize command manager with the loaded data
        CommandManager commandManager = new CommandManager(flatSet);
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Flat Management System ===");
        System.out.println("Type 'help' to see available commands");
        System.out.println("==============================");

        // Main command loop
        while (commandManager.isRunning()) {
            try {
                System.out.print("\n> ");
                String input = scanner.nextLine().trim();

                if (!input.isEmpty()) {
                    commandManager.executeCommand(input);
                }
            } catch (Exception e) {
                System.err.println("Error processing command: " + e.getMessage());
            }
        }

        // Cleanup
        scanner.close();
        System.out.println("System shutdown completed.");
    }

    public static HashSet<Flat> loadDataFromFile(String filePath) {
        HashSet<Flat> flatSet = new HashSet<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             BufferedInputStream bis = new BufferedInputStream(fis);
             JsonReader jsonReader = Json.createReader(bis)) {

            JsonArray jsonArray = jsonReader.readArray();
            for (JsonObject jsonObject : jsonArray.getValuesAs(JsonObject.class)) {
                Flat flat = parseFlatFromJson(jsonObject);
                flatSet.add(flat);
            }

            System.out.println("Successfully loaded " + flatSet.size() + " flats from " + filePath);
        } catch (FileNotFoundException e) {
            System.out.println("No existing data file found. Starting with empty collection.");
        } catch (Exception e) {
            System.err.println("Error loading data: " + e.getMessage());
        }

        return flatSet;
    }

    private static Flat parseFlatFromJson(JsonObject jsonObject) {
        // Implementation of JSON to Flat parsing
        Flat flat = new Flat();
        // ... (your existing parsing logic)
        return flat;
    }
}