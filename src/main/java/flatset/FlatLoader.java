package flatset;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.HashSet;

public class FlatLoader {
    private static final String DEFAULT_DATA_FILE = "flats.json";

    /**
     * Загружает исходные данные из файла по умолчанию
     * @return Множество загруженных квартир или пустое множество, если файл не найден
     */
    public static HashSet<Flat> loadInitialData() {
        return loadInitialData(DEFAULT_DATA_FILE);
    }

    /**
     * Загружает исходные данные из указанного файла
     * @param filePath Путь к JSON-файлу с данными о квартирах
     * @return Множество загруженных квартир или пустое множество, если файл не найден/некорректен
     */
    public static HashSet<Flat> loadInitialData(String filePath) {
        try {
            System.out.println("Loading data from " + filePath + "...");
            HashSet<Flat> flats = loadFromFile(filePath);
            System.out.println("Successfully loaded " + flats.size() + " apartments");
            return flats;
        } catch (FileNotFoundException e) {
            System.out.println("Data file not found - starting with an empty collection");
            return new HashSet<>();
        } catch (Exception e) {
            System.err.println("Warning: Failed to load data - " + e.getMessage());
            return new HashSet<>();
        }
    }

    /**
     * Непосредственно загружает квартиры из указанного JSON-файла
     * @param filePath Путь к JSON-файлу
     * @return Множество загруженных квартир
     * @throws FileNotFoundException если указанный файл не существует
     * @throws IOException при проблемах с чтением файла
     * @throws javax.json.JsonException при ошибках парсинга JSON
     */
    public static HashSet<Flat> loadFromFile(String filePath)
            throws FileNotFoundException, IOException {
        HashSet<Flat> flatSet = new HashSet<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             BufferedInputStream bis = new BufferedInputStream(fis);
             JsonReader jsonReader = Json.createReader(bis)) {

            JsonArray jsonArray = jsonReader.readArray();
            for (JsonObject jsonObject : jsonArray.getValuesAs(JsonObject.class)) {
                try {
                    Flat flat = parseJsonObject(jsonObject);
                    flatSet.add(flat);
                } catch (Exception e) {
                    System.err.println("Skipping invalid apartment record: " + e.getMessage());
                }
            }
        }
        return flatSet;
    }

    /**
     * Парсит JSON-объект в объект Flat
     * @param jsonObject JSON-объект с данными о квартире
     * @return Распарсенный объект Flat
     * @throws IllegalArgumentException если отсутствуют обязательные поля или они некорректны
     */
    private static Flat parseJsonObject(JsonObject jsonObject) {
        Flat flat = new Flat();

        // Обязательные поля с валидацией
        if (!jsonObject.containsKey("id")) {
            throw new IllegalArgumentException("Missing mandatory field: id");
        }
        flat.setId(jsonObject.getInt("id"));

        flat.setName(getStringField(jsonObject, "name"));
        flat.setCreationDate(ZonedDateTime.parse(getStringField(jsonObject, "creationDate")));

        // Парсинг координат
        if (!jsonObject.containsKey("coordinates")) {
            throw new IllegalArgumentException("Missing coordinates object");
        }
        JsonObject coords = jsonObject.getJsonObject("coordinates");
        flat.setCoordinates(new Coordinates(
                getIntField(coords, "x"),
                getIntField(coords, "y")
        ));

        // Числовые поля
        flat.setArea(getIntField(jsonObject, "area"));
        flat.setNumberOfRooms(getIntField(jsonObject, "numberOfRooms"));

        // Опциональное булево поле
        if (jsonObject.containsKey("new")) {
            flat.setNew(jsonObject.getBoolean("new"));
        }

        // Время до метро
        flat.setTimeToMetroByTransport(jsonObject.getJsonNumber("timeToMetroByTransport").doubleValue());

        // Перечисление View
        flat.setView(View.valueOf(getStringField(jsonObject, "view")));

        // Дом (опционально)
        if (jsonObject.containsKey("house") && !jsonObject.isNull("house")) {
            JsonObject houseJson = jsonObject.getJsonObject("house");
            House house = new House();
            house.setName(getStringField(houseJson, "name"));
            house.setYear(getIntField(houseJson, "year"));
            house.setNumberOfFlatsOnFloor(getIntField(houseJson, "numberOfFlatsOnFloor"));
            flat.setHouse(house);
        }

        return flat;
    }

    // Вспомогательный метод для обязательных строковых полей
    private static String getStringField(JsonObject obj, String field) {
        if (!obj.containsKey(field)) {
            throw new IllegalArgumentException("Missing mandatory field: " + field);
        }
        return obj.getString(field);
    }

    // Вспомогательный метод для обязательных целочисленных полей
    private static int getIntField(JsonObject obj, String field) {
        if (!obj.containsKey(field)) {
            throw new IllegalArgumentException("Missing mandatory field: " + field);
        }
        return obj.getInt(field);
    }
}
