package flatset;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.Set;
import java.io.IOException;
import java.io.FileNotFoundException;
import javax.json.*;
import java.util.*;
import java.time.ZonedDateTime;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.io.StringReader;

public class Main
{
    public static void main(String[] args)
    {
        //String filePath = "list.json";
        String filePath = System.getenv("FLAT_LIST");
        HashSet<Flat> flatSet = loadDataFromFile(filePath);
        startConsoleApp(flatSet);
    }
/**
 * Запись данных из файла в HashSet
 *
 * @param filePath_p параметр в котором записан путь к файлу   */
    public static HashSet<Flat> loadDataFromFile(String filePath_p)
    {
        HashSet<Flat> flatSet = new HashSet<>();

        try (FileInputStream dataStream = new FileInputStream(filePath_p))
        {
            System.out.println("File opened successfully. File input stream was created :).");
            BufferedInputStream bufferedStream = new BufferedInputStream(dataStream);

            JsonReader jsonReader = Json.createReader(bufferedStream);
            JsonArray jsonArray = jsonReader.readArray();

            for (JsonObject jsonObject : jsonArray.getValuesAs(JsonObject.class))
            {
                Flat flat = new Flat();
                flat.setId(jsonObject.getInt("id"));
                flat.setName(jsonObject.getString("name"));

                JsonObject coordinatesJson = jsonObject.getJsonObject("coordinates");
                Coordinates coordinates = new Coordinates(coordinatesJson.getInt("x"), coordinatesJson.getInt("y"));
                flat.setCoordinates(coordinates);

                flat.setCreationDate(ZonedDateTime.parse(jsonObject.getString("creationDate")));

                flat.setArea(jsonObject.getInt("area"));
                flat.setNumberOfRooms(jsonObject.getInt("numberOfRooms"));

                if (jsonObject.containsKey("new") && !jsonObject.isNull("new")) {
                    boolean isNew = jsonObject.getBoolean("new");
                }
                else
                {
                    boolean isNew = false;
                }
                flat.setTimeToMetroByTransport(jsonObject.getJsonNumber("timeToMetroByTransport").doubleValue());


                flat.setView(View.valueOf(jsonObject.getString("view")));


                if (!jsonObject.isNull("house")) {
                    JsonObject houseJson = jsonObject.getJsonObject("house");
                    House house = new House();
                    house.setName(houseJson.getString("name"));
                    house.setYear(houseJson.getInt("year"));
                    house.setNumberOfFlatsOnFloor(houseJson.getInt("numberOfFlatsOnFloor"));
                    flat.setHouse(house);
                }


                flatSet.add(flat);
            }
        }
        catch (FileNotFoundException e)
        {
            System.err.println("Error: File not found - " + e.getMessage());
        }
        catch (IOException e)
        {
            System.err.println("Error: IO Exception - " + e.getMessage());
        }
        return flatSet;
    }


    /**
     *Запуск консольного приложения и обработка пользовательских команд
     *
     * @param flatSet коллекция объектов Flat
     */
    private static void startConsoleApp(HashSet<Flat> flatSet)
    {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        LocalDateTime initializationTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        System.out.println("Console application started. Enter commands (type 'help' for a list of commands).");

        while (running)
        {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            String[] parts = input.split(" ", 2); // Split into command and argument
            String command = parts[0].toLowerCase();
            String argument = parts.length > 1 ? parts[1] : "";

            switch (command)
            {
                case "show":
                    showCollection(flatSet);
                    break;
                case "info":
                    showCollectionInfo(flatSet, initializationTime, formatter);
                    break;
                case "help":
                    showHelp();
                    break;
                case "add":
                    addFlat(flatSet, argument);
                    break;
                case "remove_by_id":
                    removeById(flatSet, argument);
                    break;
                case "clear":
                    clearCollection(flatSet);
                    break;
                case "save":
                    saveCollection(flatSet, "data.json");
                    break;
                case "print_unique_house":
                    printUniqueHouse(flatSet);
                    break;
                case "print_field_ascending_number_of_rooms":
                    printFieldAscendingNumberOfRooms(flatSet);
                    break;
                case "print_field_descending_house":
                    printFieldDescendingHouse(flatSet);
                    break;
                case "add_if_max":
                    addIfMax(flatSet, argument);
                    break;
                case "add_if_min":
                    addIfMin(flatSet, argument);
                    break;
                case "update":
                    updateFlat(flatSet, argument);
                    break;
                case "update_by_id":
                    updateById(flatSet, argument);
                    break;
                case "remove_greater":
                    removeGreater(flatSet, argument);
                    break;
                case "execute_script":
                    executeScript(flatSet, argument);
                    break;
                case "exit":
                    running = false;
                    System.out.println("Exiting the application...");
                    break;
                default:
                    System.out.println("Unknown command. Type 'help' for a list of valid commands.");
                    break;
            }
        }

        scanner.close();
    }

    /**
     * Output all elements of the collection to the console.
     *
     * @param flatSet The collection of Flat objects.
     */
    private static void showCollection(HashSet<Flat> flatSet)
    {
        if (flatSet.isEmpty()) {
            System.out.println("The collection is empty.");
        } else {
            System.out.println("Collection elements:");
            for (Flat flat : flatSet) {
                System.out.println(flat);
            }
        }
    }

    /**
     * Display information about the collection.
     *
     * @param flatSet            The collection of Flat objects.
     * @param initializationTime The time when the collection was initialized.
     * @param formatter          The formatter for displaying the date and time.
     */
    private static void showCollectionInfo(HashSet<Flat> flatSet, LocalDateTime initializationTime, DateTimeFormatter formatter)
    {
        System.out.println("Collection Information:");
        System.out.println("Type: " + flatSet.getClass().getSimpleName());
        System.out.println("Initialization Date: " + initializationTime.format(formatter));
        System.out.println("Number of Elements: " + flatSet.size());
    }

    /**
     * Add a new Flat object to the collection.
     *
     * @param flatSet  The collection of Flat objects.
     * @param argument The string representation of the element to add.
     *                Format: "{name,x,y,area,numberOfRooms,isNew,timeToMetroByTransport,view}"
     *                Example: "{Flat 1,10,-100,75,3,true,15.5,PARK}"
     */
    private static void addFlat(HashSet<Flat> flatSet, String argument) {
        try {
            // Check if the input is enclosed in curly brackets
            if (!argument.startsWith("{") || !argument.endsWith("}")) {
                throw new IllegalArgumentException("Invalid format. Parameters must be enclosed in curly brackets.");
            }

            // Remove the curly brackets and split the parameters
            String[] params = argument.substring(1, argument.length() - 1).split(",");
            if (params.length != 8) {
                throw new IllegalArgumentException("Invalid format. Expected: name,x,y,area,numberOfRooms,isNew,timeToMetroByTransport,view");
            }

            // Parse the parameters
            String name = params[0].trim();
            int x = Integer.parseInt(params[1].trim());
            int y = Integer.parseInt(params[2].trim());
            long area = Long.parseLong(params[3].trim());
            long numberOfRooms = Long.parseLong(params[4].trim());
            Boolean isNew = Boolean.parseBoolean(params[5].trim());
            double timeToMetroByTransport = Double.parseDouble(params[6].trim());
            View view = View.valueOf(params[7].trim().toUpperCase());

            // Generate id and creationDate automatically
            long id = generateAutoIncrementId(flatSet); // Generate an auto-incremented ID
            ZonedDateTime creationDate = ZonedDateTime.now(); // Use the current date and time

            // Create Coordinates object
            Coordinates coordinates = new Coordinates(x, y);

            // Create a new Flat object
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

            // Add the new Flat object to the collection
            flatSet.add(flat);
            System.out.println("Element added successfully: " + flat);
        } catch (Exception e) {
            System.err.println("Error adding element: " + e.getMessage());
        }
    }

    /**
     * Generate an auto-incremented ID for a new Flat object.
     *
     * @param flatSet The collection of Flat objects.
     * @return A new ID that is one greater than the maximum ID in the collection.
     */
    private static long generateAutoIncrementId(HashSet<Flat> flatSet) {
        long maxId = flatSet.stream()
                .mapToLong(Flat::getId)
                .max()
                .orElse(0); // If the collection is empty, start from 1
        return maxId + 1; // Increment the maximum ID by 1
    }


    private static void removeById(HashSet<Flat> flatSet, String argument)
    {
        try {
            long id = Long.parseLong(argument.trim());


            boolean removed = flatSet.removeIf(flat -> flat.getId() == id);

            if (removed) {
                System.out.println("Element with ID " + id + " removed successfully.");
            } else {
                System.out.println("No element found with ID " + id + ".");
            }
        } catch (Exception e) {
            System.err.println("Error removing element: " + e.getMessage());
        }
    }

    /**
     * Clear the entire collection.
     *
     * @param flatSet The collection of Flat objects.
     */
    private static void clearCollection(HashSet<Flat> flatSet)
    {
        flatSet.clear();
        System.out.println("Collection cleared successfully.");
    }


    /**
     * Save the collection to a JSON file.
     *
     * @param flatSet  The collection of Flat objects.
     * @param filePath The path to the file where the collection will be saved.
     */
    private static void saveCollection(HashSet<Flat> flatSet, String filePath)
    {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             JsonWriter jsonWriter = Json.createWriter(fos)) {


            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();


            for (Flat flat : flatSet) {
                JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder()
                        .add("id", flat.getId())
                        .add("name", flat.getName())
                        .add("new", flat.isNew());
                jsonArrayBuilder.add(jsonObjectBuilder);
            }


            JsonArray jsonArray = jsonArrayBuilder.build();
            jsonWriter.writeArray(jsonArray);

            System.out.println("Collection saved successfully to " + filePath);
        } catch (Exception e) {
            System.err.println("Error saving collection: " + e.getMessage());
        }
    }

    /**
     * Print unique values of the house field.
     *
     * @param flatSet The collection of Flat objects.
     */
    private static void printUniqueHouse(HashSet<Flat> flatSet) {
        Set<House> uniqueHouses = flatSet.stream()
                .map(Flat::getHouse)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if (uniqueHouses.isEmpty()) {
            System.out.println("No houses found in the collection.");
        } else {
            System.out.println("Unique houses:");
            for (House house : uniqueHouses) {
                System.out.println(house);
            }
        }
    }

    /**
     * Print values of the numberOfRooms field in ascending order.
     *
     * @param flatSet The collection of Flat objects.
     */
    private static void printFieldAscendingNumberOfRooms(HashSet<Flat> flatSet) {
        List<Long> numberOfRooms = flatSet.stream()
                .map(Flat::getNumberOfRooms)
                .sorted()
                .collect(Collectors.toList());

        if (numberOfRooms.isEmpty()) {
            System.out.println("No numberOfRooms found in the collection.");
        } else {
            System.out.println("numberOfRooms in ascending order:");
            for (Long rooms : numberOfRooms) {
                System.out.println(rooms);
            }
        }
    }

    /**
     * Print values of the house field in descending order.
     *
     * @param flatSet The collection of Flat objects.
     */
    private static void printFieldDescendingHouse(HashSet<Flat> flatSet)
    {
        List<House> houses = flatSet.stream()
                .map(Flat::getHouse)
                .filter(Objects::nonNull)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        if (houses.isEmpty()) {
            System.out.println("No houses found in the collection.");
        } else {
            System.out.println("Houses in descending order:");
            for (House house : houses) {
                System.out.println(house);
            }
        }
    }

    /**
     * Add a new element if it is greater than the maximum element in the collection.
     *
     * @param flatSet  The collection of Flat objects.
     * @param argument The string representation of the element to add.
     */
    private static void addIfMax(HashSet<Flat> flatSet, String argument)
    {
        try {
            Flat newFlat = parseFlat(argument);
            Flat maxFlat = flatSet.stream().max(Flat::compareTo).orElse(null);

            if (maxFlat == null || newFlat.compareTo(maxFlat) > 0) {
                flatSet.add(newFlat);
                System.out.println("Element added successfully: " + newFlat);
            } else {
                System.out.println("Element is not greater than the maximum element in the collection.");
            }
        } catch (Exception e) {
            System.err.println("Error adding element: " + e.getMessage());
        }
    }

    /**
     * Add a new element if it is smaller than the minimum element in the collection.
     *
     * @param flatSet  The collection of Flat objects.
     * @param argument The string representation of the element to add.
     */
    private static void addIfMin(HashSet<Flat> flatSet, String argument)
    {
        try {
            Flat newFlat = parseFlat(argument);
            Flat minFlat = flatSet.stream().min(Flat::compareTo).orElse(null);

            if (minFlat == null || newFlat.compareTo(minFlat) < 0) {
                flatSet.add(newFlat);
                System.out.println("Element added successfully: " + newFlat);
            } else {
                System.out.println("Element is not smaller than the minimum element in the collection.");
            }
        } catch (Exception e) {
            System.err.println("Error adding element: " + e.getMessage());
        }
    }

    /**
     * Update an existing Flat object in the collection by its ID.
     *
     * @param flatSet  The collection of Flat objects.
     * @param argument The string representation of the update command.
     *                Format: "id {name,x,y,area,numberOfRooms,isNew,timeToMetroByTransport,view}"
     *                Example: "1 {Flat 1,10,-100,75,3,true,15.5,PARK}"
     */
    private static void updateFlat(HashSet<Flat> flatSet, String argument) {
        try {
            // Split the input string into id and parameters
            String[] parts = argument.split(" ", 2);
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid format. Expected: id {name,x,y,area,numberOfRooms,isNew,timeToMetroByTransport,view}");
            }

            // Parse the ID
            long id = Long.parseLong(parts[0].trim());

            String paramsString = parts[1].trim();
            if (!paramsString.startsWith("{") || !paramsString.endsWith("}")) {
                throw new IllegalArgumentException("Invalid format. Parameters must be enclosed in curly brackets.");
            }

            String[] params = paramsString.substring(1, paramsString.length() - 1).split(",");
            if (params.length != 8) {
                throw new IllegalArgumentException("Invalid format. Expected: name,x,y,area,numberOfRooms,isNew,timeToMetroByTransport,view");
            }

            // Parse the parameters
            String name = params[0].trim();
            int x = Integer.parseInt(params[1].trim());
            int y = Integer.parseInt(params[2].trim());
            long area = Long.parseLong(params[3].trim());
            long numberOfRooms = Long.parseLong(params[4].trim());
            Boolean isNew = Boolean.parseBoolean(params[5].trim());
            double timeToMetroByTransport = Double.parseDouble(params[6].trim());
            View view = View.valueOf(params[7].trim().toUpperCase());

            // Find the Flat object with the specified ID
            Optional<Flat> existingFlat = flatSet.stream()
                    .filter(flat -> flat.getId() == id)
                    .findFirst();

            if (existingFlat.isPresent()) {
                // Update the existing Flat object
                Flat flat = existingFlat.get();
                flat.setName(name);

                Coordinates coordinates = new Coordinates(x, y);

                flat.setArea(area);
                flat.setNumberOfRooms(numberOfRooms);
                flat.setNew(isNew);
                flat.setTimeToMetroByTransport(timeToMetroByTransport);
                flat.setView(view);

                System.out.println("Element updated successfully: " + flat);
            } else {
                System.out.println("No element found with ID " + id + ".");
            }
        } catch (Exception e) {
            System.err.println("Error updating element: " + e.getMessage());
        }
    }

    /**
     * Remove all elements greater than the specified element.
     *
     * @param flatSet  The collection of Flat objects.
     * @param argument The string representation of the element to compare.
     */
    private static void removeGreater(HashSet<Flat> flatSet, String argument)
    {
        try {
            Flat compareFlat = parseFlat(argument);
            flatSet.removeIf(flat -> flat.compareTo(compareFlat) > 0);
            System.out.println("Elements greater than " + compareFlat + " removed successfully.");
        } catch (Exception e) {
            System.err.println("Error removing elements: " + e.getMessage());
        }
    }


    private static void processCommand(HashSet<Flat> flatSet, String command) {
        String[] parts = command.split(" ", 2);
        String cmd = parts[0].toLowerCase();
        String argument = parts.length > 1 ? parts[1].trim() : "";

        switch (cmd) {
            case "add":
                addFlat(flatSet, argument);
                break;
            case "remove_by_id":
                removeById(flatSet, argument);
                break;
            case "clear":
                clearCollection(flatSet);
                break;
            case "save":
                saveCollection(flatSet, "data.json");
                break;
            case "print_unique_house":
                printUniqueHouse(flatSet);
                break;
            case "print_field_ascending_number_of_rooms":
                printFieldAscendingNumberOfRooms(flatSet);
                break;
            case "print_field_descending_house":
                printFieldDescendingHouse(flatSet);
                break;
            case "add_if_max":
                addIfMax(flatSet, argument);
                break;
            case "add_if_min":
                addIfMin(flatSet, argument);
                break;
            case "update":
                updateFlat(flatSet, argument);
                break;
            case "update_by_id":
                updateById(flatSet, argument);
                break;
            case "remove_greater":
                removeGreater(flatSet, argument);
                break;
            case "execute_script":
                System.err.println("Error: Recursive script execution is not allowed.");
                break;
            case "help":
                showHelp();
                break;
            case "exit":
                System.out.println("Exiting script execution.");
                break;
            default:
                System.out.println("Unknown command: " + cmd);
                break;
        }
    }


    /**
     * Execute commands from a script file.
     *
     * @param flatSet  The collection of Flat objects.
     * @param filePath The path to the script file.
     */
    private static void executeScript(HashSet<Flat> flatSet, String filePath)
    {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (String line : lines) {
                System.out.println("Executing: " + line);
                processCommand(flatSet, line); // Reuse the command processing logic
            }
        } catch (Exception e) {
            System.err.println("Error executing script: " + e.getMessage());
        }
    }

    /**
     * Display a list of valid commands.
     */
    private static void showHelp() {
        System.out.println("Available commands:");
        System.out.println("  show                              - Display all elements in the collection.");
        System.out.println("  info                              - Display information about the collection.");
        System.out.println("  add                               - Add a new element to the collection. Usage: add {id,name,isNew}");
        System.out.println("  remove_by_id                      - Remove an element by its ID. Usage: remove_by_id {id}");
        System.out.println("  clear                             - Clear the entire collection.");
        System.out.println("  save                              - Save the collection to a file.");
        System.out.println("  print_unique_house                - Print unique values of the house field.");
        System.out.println("  print_field_ascending_number_of_rooms - Print numberOfRooms values in ascending order.");
        System.out.println("  print_field_descending_house      - Print house values in descending order.");
        System.out.println("  help                              - Display this help message.");
        System.out.println("  exit                              - Exit the application.");
    }

    /**
     * Parse a string representation of a Flat object.
     *
     * @param argument The string representation of the Flat object.
     * @return A new Flat object.
     */
    private static Flat parseFlat(String argument)
    {
        String[] parts = argument.split(",");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid format.");
        }

        long id = Long.parseLong(parts[0].trim());
        String name = parts[1].trim();
        Boolean isNew = Boolean.parseBoolean(parts[2].trim());

        Flat flat = new Flat();
        flat.setId(id);
        flat.setName(name);
        flat.setNew(isNew);

        return flat;
    }

    /**
     * Update a specific field of a Flat object by ID using a key-value pair.
     * Command format: update_by_id id {"key": "value"}
     * Example: update_by_id 1 {"name": "NewFlatName"}
     */
    private static void updateById(HashSet<Flat> flatSet, String argument) {
        try {
            // Split the argument into ID and JSON parts
            String[] parts = argument.split(" ", 2);
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid format. Expected: id {json}");
            }

            long id = Long.parseLong(parts[0].trim());
            String jsonInput = parts[1].trim();

            // Parse JSON input using javax.json
            JsonReader jsonReader = Json.createReader(new StringReader(jsonInput));
            JsonObject jsonObject = jsonReader.readObject();

            // Find the Flat object
            Optional<Flat> flatOptional = flatSet.stream()
                    .filter(f -> f.getId() == id)
                    .findFirst();

            if (!flatOptional.isPresent()) {
                System.out.println("No element found with ID " + id);
                return;
            }

            Flat flat = flatOptional.get();

            // Update fields based on JSON key-value pairs
            for (String field : jsonObject.keySet()) {
                switch (field.toLowerCase()) {
                    case "name":
                        flat.setName(jsonObject.getString(field));
                        break;
                    case "coordinates":
                        updateCoordinates(flat.getCoordinates(), jsonObject.getJsonObject(field));
                        break;
                    case "area":
                        flat.setArea(jsonObject.getJsonNumber(field).longValue());
                        break;
                    case "numberofrooms":
                        flat.setNumberOfRooms(jsonObject.getJsonNumber(field).longValue());
                        break;
                    case "isnew":
                        flat.setNew(jsonObject.getBoolean(field));
                        break;
                    case "timetometrobytransport":
                        flat.setTimeToMetroByTransport(jsonObject.getJsonNumber(field).doubleValue());
                        break;
                    case "view":
                        flat.setView(View.valueOf(jsonObject.getString(field).toUpperCase()));
                        break;
                    case "house":
                        updateHouse(flat.getHouse(), jsonObject.getJsonObject(field));
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown field: " + field);
                }
            }

            System.out.println("Element with ID " + id + " updated successfully");
        } catch (Exception e) {
            System.err.println("Error updating element: " + e.getMessage());
        }
    }

    // Helper method to update Coordinates
    private static void updateCoordinates(Coordinates coordinates, JsonObject jsonObject) {
        if (jsonObject.containsKey("x")) {
            coordinates.setX(jsonObject.getInt("x"));
        }
        if (jsonObject.containsKey("y")) {
            coordinates.setY(jsonObject.getInt("y"));
        }
    }

    // Helper method to update House
    private static void updateHouse(House house, JsonObject jsonObject) {
        if (house == null) {
            house = new House();
        }

        if (jsonObject.containsKey("name")) {
            house.setName(jsonObject.getString("name"));
        }
        if (jsonObject.containsKey("year")) {
            house.setYear(jsonObject.getInt("year"));
        }
        if (jsonObject.containsKey("numberofflatsonfloor")) {
            house.setNumberOfFlatsOnFloor(jsonObject.getInt("numberofflatsonfloor"));
        }
    }
}
