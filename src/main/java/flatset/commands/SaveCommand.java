package flatset.commands;

import flatset.Flat;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import java.io.FileOutputStream;
import java.util.HashSet;

/**
 * Команда для сохранения данных о квартирах в файл в формате JSON.
 */
public class SaveCommand implements Command {
    private static final String DEFAULT_FILENAME = "flats.json";

    /**
     * Выполняет команду сохранения коллекции квартир в файл в формате JSON.
     * Если имя файла не задано, используется имя файла по умолчанию.
     *
     * @param flatSet Коллекция квартир, которые необходимо сохранить.
     * @param argument Имя файла для сохранения данных. Если пустое, используется значение по умолчанию.
     */
    @Override
    public void execute(HashSet<Flat> flatSet, String argument) {
        // Определение имени файла для сохранения данных
        String filename = argument.isEmpty() ? DEFAULT_FILENAME : argument.trim();

        try (FileOutputStream fos = new FileOutputStream(filename);
             JsonWriter jsonWriter = Json.createWriter(fos)) {

            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

            // Процесс формирования JSON для каждой квартиры
            for (Flat flat : flatSet) {
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder()
                        .add("id", flat.getId())
                        .add("name", flat.getName())
                        .add("area", flat.getArea())
                        .add("numberOfRooms", flat.getNumberOfRooms())
                        .add("new", flat.isNew())
                        .add("timeToMetroByTransport", flat.getTimeToMetroByTransport())
                        .add("view", flat.getView().toString())
                        .add("creationDate", flat.getCreationDate().toString());

                // Добавление координат квартиры
                objectBuilder.add("coordinates", Json.createObjectBuilder()
                        .add("x", flat.getCoordinates().getX())
                        .add("y", flat.getCoordinates().getY()));

                // Добавление информации о доме, если он существует
                if (flat.getHouse() != null) {
                    objectBuilder.add("house", Json.createObjectBuilder()
                            .add("name", flat.getHouse().getName())
                            .add("year", flat.getHouse().getYear())
                            .add("numberOfFlatsOnFloor", flat.getHouse().getNumberOfFlatsOnFloor()));
                }

                arrayBuilder.add(objectBuilder);
            }

            // Запись собранного JSON в файл
            jsonWriter.writeArray(arrayBuilder.build());
            System.out.println("Successfully saved " + flatSet.size() + " apartment(s) to " + filename);

        } catch (Exception e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }
}
