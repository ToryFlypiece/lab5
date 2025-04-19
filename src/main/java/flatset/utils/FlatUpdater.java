package flatset.utils;

import flatset.Coordinates;
import flatset.Flat;
import flatset.House;
import flatset.View;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;

/**
 * Класс для обновления полей объекта Flat из JSON-данных.
 * Предоставляет функциональность для частичного или полного обновления информации о квартире.
 */
public class FlatUpdater {

    /**
     * Обновляет поля объекта Flat на основе данных в JSON-формате.
     * Поддерживает обновление как основных полей квартиры, так и вложенных объектов (координат и дома).
     *
     * @param flat объект Flat для обновления
     * @param jsonData строка с данными в JSON-формате для обновления
     * @throws IllegalArgumentException если переданные данные содержат неизвестные поля
     *                                  или имеют некорректный формат
     *
     * @example Пример JSON-данных:
     * {
     *     "name": "Новое название",
     *     "area": 75,
     *     "coordinates": {"x": 10, "y": 20},
     *     "house": {"name": "Дом", "year": 2020}
     * }
     */
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

    /**
     * Внутренний метод для обновления координат квартиры.
     *
     * @param coords объект Coordinates для обновления
     * @param json JSON-объект с новыми значениями координат
     */
    private static void updateCoordinates(Coordinates coords, JsonObject json) {
        if (json.containsKey("x")) {
            coords.setX(json.getInt("x"));
        }
        if (json.containsKey("y")) {
            coords.setY(json.getInt("y"));
        }
    }

    /**
     * Внутренний метод для обновления информации о доме.
     * Если объект House не существует, создает новый.
     *
     * @param house объект House для обновления (может быть null)
     * @param json JSON-объект с новыми значениями характеристик дома
     */
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
