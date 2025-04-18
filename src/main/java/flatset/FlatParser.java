package flatset;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import java.time.ZonedDateTime;

public class FlatParser {
    public static Flat parseFlat(String input) throws IllegalArgumentException {
        try {
            // Remove curly braces if present
            String trimmedInput = input.trim();
            if (trimmedInput.startsWith("{") && trimmedInput.endsWith("}")) {
                trimmedInput = trimmedInput.substring(1, trimmedInput.length() - 1);
            }

            String[] parts = trimmedInput.split(",");
            if (parts.length < 8) {
                throw new IllegalArgumentException("Invalid format. Expected at least 8 comma-separated values");
            }

            Flat flat = new Flat();

            // Parse basic fields
            flat.setName(parts[0].trim());
            flat.setCoordinates(new Coordinates(
                    Integer.parseInt(parts[1].trim()),
                    Integer.parseInt(parts[2].trim())
            ));
            flat.setArea(Long.parseLong(parts[3].trim()));
            flat.setNumberOfRooms(Long.parseLong(parts[4].trim()));
            flat.setNew(Boolean.parseBoolean(parts[5].trim()));
            flat.setTimeToMetroByTransport(Double.parseDouble(parts[6].trim()));
            flat.setView(View.valueOf(parts[7].trim().toUpperCase()));

            // Set automatic fields
            flat.setCreationDate(ZonedDateTime.now());

            // Parse house if available
            if (parts.length > 8) {
                House house = new House();
                house.setName(parts[8].trim());
                if (parts.length > 9) house.setYear(Integer.parseInt(parts[9].trim()));
                if (parts.length > 10) house.setNumberOfFlatsOnFloor(Integer.parseInt(parts[10].trim()));
                flat.setHouse(house);
            }

            return flat;
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to parse flat: " + e.getMessage(), e);
        }
    }

    public static Flat parseJsonFlat(String jsonInput) {
        try (JsonReader reader = Json.createReader(new StringReader(jsonInput))) {
            JsonObject jsonObject = reader.readObject();
            Flat flat = new Flat();

            if (jsonObject.containsKey("name")) {
                flat.setName(jsonObject.getString("name"));
            }
            if (jsonObject.containsKey("coordinates")) {
                JsonObject coords = jsonObject.getJsonObject("coordinates");
                flat.setCoordinates(new Coordinates(
                        coords.getInt("x"),
                        coords.getInt("y")
                ));
            }
            // Add other field parsings...

            return flat;
        }
    }
}