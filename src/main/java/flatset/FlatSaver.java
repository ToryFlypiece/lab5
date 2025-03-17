package flatset;

import javax.json.*;
import java.io.FileOutputStream;
import java.util.HashSet;
/**
 * Класс FlatSaver отвечает за сохранение коллекции квартир в JSON-файл.
 */
public class FlatSaver {
    /**
     * Сохраняет коллекцию квартир в JSON-файл.
     *
     * @param flatSet Коллекция квартир, которую необходимо сохранить.
     * @param filePath Путь к файлу, в который будут сохранены данные.
     */

    public static void saveCollection(HashSet<Flat> flatSet, String filePath) {
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
}