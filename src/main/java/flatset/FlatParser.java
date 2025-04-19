package flatset;

import flatset.Flat;
import flatset.Coordinates;
import flatset.House;
import flatset.View;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

public class FlatParser {
    private static final int MIN_FIELDS = 8; // Минимальное количество обязательных полей для парсинга строки
    private static final String FIELD_SEPARATOR = ",";

    /**
     * Парсит данные о квартире из строки в формате командной строки
     * Формат: {name,x,y,area,numberOfRooms,isNew,timeToMetroByTransport,view[,houseName,houseYear,houseFlats]}
     */
    public static Flat parseFlat(String input) throws IllegalArgumentException {
        try {
            // Очистка и валидация входных данных
            String trimmedInput = input.trim();
            if (trimmedInput.startsWith("{") && trimmedInput.endsWith("}")) {
                trimmedInput = trimmedInput.substring(1, trimmedInput.length() - 1).trim();
            }

            String[] parts = trimmedInput.split(FIELD_SEPARATOR, -1); // Сохраняем пустые строки в конце
            if (parts.length < MIN_FIELDS) {
                throw new IllegalArgumentException(
                        String.format("Ожидается не менее %d значений, разделенных запятыми, получено %d",
                                MIN_FIELDS, parts.length));
            }

            // Создание и заполнение объекта квартиры
            Flat flat = new Flat();
            int index = 0;

            // Обязательные поля
            flat.setName(parseString(parts[index++], "название"));
            flat.setCoordinates(new Coordinates(
                    parseInt(parts[index++], "координата x"),
                    parseInt(parts[index++], "координата y")
            ));
            flat.setArea(parseLong(parts[index++], "площадь"));
            flat.setNumberOfRooms(parseLong(parts[index++], "количество комнат"));
            flat.setNew(parseBoolean(parts[index++], "признак новизны"));
            flat.setTimeToMetroByTransport(parseDouble(parts[index++], "время до метро"));
            flat.setView(parseView(parts[index++]));

            // Автоматически заполняемые поля
            flat.setCreationDate(ZonedDateTime.now());

            // Опциональные поля дома
            if (parts.length > index) {
                House house = new House();
                house.setName(parseString(parts[index++], "название дома"));

                if (parts.length > index) {
                    house.setYear(parseInt(parts[index++], "год постройки дома"));
                }
                if (parts.length > index) {
                    house.setNumberOfFlatsOnFloor(parseInt(parts[index], "квартир на этаже"));
                }

                flat.setHouse(house);
            }

            return flat;
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка парсинга квартиры: " + e.getMessage(), e);
        }
    }

    /**
     * Парсит данные о квартире из JSON-строки
     * Формат: пары {"поле":"значение"}
     */
    public static Flat parseJson(String jsonInput) throws IllegalArgumentException {
        try (JsonReader reader = Json.createReader(new StringReader(jsonInput))) {
            JsonObject jsonObject = reader.readObject();
            return parseJsonObject(jsonObject);
        } catch (Exception e) {
            throw new IllegalArgumentException("Неверный формат JSON: " + e.getMessage(), e);
        }
    }

    /**
     * Парсит данные о квартире из JsonObject
     */
    public static Flat parseJsonObject(JsonObject jsonObject) throws IllegalArgumentException {
        Flat flat = new Flat();

        // Обязательные поля
        flat.setName(getJsonString(jsonObject, "name"));
        flat.setCoordinates(new Coordinates(
                getJsonInt(jsonObject.getJsonObject("coordinates"), "x"),
                getJsonInt(jsonObject.getJsonObject("coordinates"), "y")
        ));
        flat.setArea(getJsonLong(jsonObject, "area"));
        flat.setNumberOfRooms(getJsonLong(jsonObject, "numberOfRooms"));

        // Опциональные поля
        if (jsonObject.containsKey("new")) {
            flat.setNew(jsonObject.getBoolean("new"));
        }

        flat.setTimeToMetroByTransport(jsonObject.getJsonNumber("timeToMetroByTransport").doubleValue());
        flat.setView(View.valueOf(getJsonString(jsonObject, "view").toUpperCase()));
        flat.setCreationDate(ZonedDateTime.parse(getJsonString(jsonObject, "creationDate")));

        // Данные о доме
        if (jsonObject.containsKey("house") && !jsonObject.isNull("house")) {
            JsonObject houseJson = jsonObject.getJsonObject("house");
            House house = new House();
            house.setName(getJsonString(houseJson, "name"));
            house.setYear(getJsonInt(houseJson, "year"));
            house.setNumberOfFlatsOnFloor(getJsonInt(houseJson, "numberOfFlatsOnFloor"));
            flat.setHouse(house);
        }

        return flat;
    }

    // Вспомогательные методы для парсинга строк
    private static String parseString(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " не может быть пустым");
        }
        return value.trim();
    }

    private static int parseInt(String value, String fieldName) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Некорректное значение " + fieldName + ": " + value);
        }
    }

    private static long parseLong(String value, String fieldName) {
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Некорректное значение " + fieldName + ": " + value);
        }
    }

    private static double parseDouble(String value, String fieldName) {
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Некорректное значение " + fieldName + ": " + value);
        }
    }

    private static boolean parseBoolean(String value, String fieldName) {
        if (!value.trim().equalsIgnoreCase("true") && !value.trim().equalsIgnoreCase("false")) {
            throw new IllegalArgumentException("Некорректное значение " + fieldName + ": должно быть 'true' или 'false'");
        }
        return Boolean.parseBoolean(value.trim());
    }

    private static View parseView(String value) {
        try {
            return View.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Некорректный вид: " + value +
                    ". Допустимые варианты: " + Arrays.toString(View.values()));
        }
    }

    // Вспомогательные методы для парсинга JSON
    private static String getJsonString(JsonObject json, String field) {
        if (!json.containsKey(field)) {
            throw new IllegalArgumentException("Отсутствует обязательное поле: " + field);
        }
        return json.getString(field);
    }

    private static int getJsonInt(JsonObject json, String field) {
        if (!json.containsKey(field)) {
            throw new IllegalArgumentException("Отсутствует обязательное поле: " + field);
        }
        return json.getInt(field);
    }

    private static long getJsonLong(JsonObject json, String field) {
        if (!json.containsKey(field)) {
            throw new IllegalArgumentException("Отсутствует обязательное поле: " + field);
        }
        return json.getInt(field);
    }
}