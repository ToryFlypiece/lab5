package flatset;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;

public class FlatUpdater {
    public static void updateFields(Flat flat, String jsonData) throws IllegalArgumentException {
        try (JsonReader reader = Json.createReader(new StringReader(jsonData))) {
            JsonObject jsonObject = reader.readObject();

            for (String key : jsonObject.keySet()) {
                switch (key.toLowerCase()) {
                    case "name":
                        flat.setName(jsonObject.getString(key));
                        break;
                    case "area":
                        flat.setArea(jsonObject.getInt(key));
                        break;
                    case "numberofrooms":
                        flat.setNumberOfRooms(jsonObject.getInt(key));
                        break;
                    case "new":
                        flat.setNew(jsonObject.getBoolean(key));
                        break;
                    case "timetometrobytransport":
                        flat.setTimeToMetroByTransport(jsonObject.getJsonNumber(key).doubleValue());
                        break;
                    case "view":
                        flat.setView(View.valueOf(jsonObject.getString(key).toUpperCase()));
                        break;
                    case "coordinates":
                        updateCoordinates(flat.getCoordinates(), jsonObject.getJsonObject(key));
                        break;
                    case "house":
                        updateHouse(flat.getHouse(), jsonObject.getJsonObject(key));
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown field: " + key);
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to update flat: " + e.getMessage(), e);
        }
    }

    private static void updateCoordinates(Coordinates coords, JsonObject json) {
        if (json.containsKey("x")) {
            coords.setX(json.getInt("x"));
        }
        if (json.containsKey("y")) {
            coords.setY(json.getInt("y"));
        }
    }

    private static void updateHouse(House house, JsonObject json) {
        if (house == null) {
            house = new House();
        }
        if (json.containsKey("name")) {
            house.setName(json.getString("name"));
        }
        if (json.containsKey("year")) {
            house.setYear(json.getInt("year"));
        }
        if (json.containsKey("numberofflatsonfloor")) {
            house.setNumberOfFlatsOnFloor(json.getInt("numberofflatsonfloor"));
        }
    }
}