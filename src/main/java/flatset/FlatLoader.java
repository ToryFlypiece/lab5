package flatset;

import javax.json.*;
import java.io.*;
import java.util.HashSet;
import java.time.ZonedDateTime;
/**
 * Класс FlatLoader отвечает за загрузку данных о квартирах из JSON-файла.
 * Данные загружаются в коллекцию HashSet<Flat>.
 */
public class FlatLoader {
    /**
     * Загружает данные о квартирах из JSON-файла.
     *
     * @param filePath Путь к JSON-файлу, из которого загружаются данные.
     * @return Коллекция HashSet<Flat>, содержащая загруженные данные.
     */
    public static HashSet<Flat> loadDataFromFile(String filePath) {
        HashSet<Flat> flatSet = new HashSet<>();

        try (FileInputStream dataStream = new FileInputStream(filePath);
             BufferedInputStream bufferedStream = new BufferedInputStream(dataStream);
             JsonReader jsonReader = Json.createReader(bufferedStream)) {

            JsonArray jsonArray = jsonReader.readArray();
            for (JsonObject jsonObject : jsonArray.getValuesAs(JsonObject.class)) {
                Flat flat = parseFlatFromJson(jsonObject);
                flatSet.add(flat);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found - " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error: IO Exception - " + e.getMessage());
        }

        return flatSet;
    }
    /**
     * Преобразует JSON-объект в объект класса Flat.
     *
     * @param jsonObject JSON-объект, содержащий данные о квартире.
     * @return Объект Flat, созданный на основе данных из JSON.
     */

    private static Flat parseFlatFromJson(JsonObject jsonObject) {
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
            flat.setNew(jsonObject.getBoolean("new"));
        } else {
            flat.setNew(false);
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

        return flat;
    }
}