package flatset.commands;

import flatset.Flat;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import java.io.FileOutputStream;
import java.util.HashSet;

public class SaveCommand implements Command {
    private static final String DEFAULT_FILENAME = "flats.json";

    @Override
    public void execute(HashSet<Flat> flatSet, String argument) {
        String filename = argument.isEmpty() ? DEFAULT_FILENAME : argument.trim();

        try (FileOutputStream fos = new FileOutputStream(filename);
             JsonWriter jsonWriter = Json.createWriter(fos)) {

            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

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

                // Add coordinates
                objectBuilder.add("coordinates", Json.createObjectBuilder()
                        .add("x", flat.getCoordinates().getX())
                        .add("y", flat.getCoordinates().getY()));

                // Add house if present
                if (flat.getHouse() != null) {
                    objectBuilder.add("house", Json.createObjectBuilder()
                            .add("name", flat.getHouse().getName())
                            .add("year", flat.getHouse().getYear())
                            .add("numberOfFlatsOnFloor", flat.getHouse().getNumberOfFlatsOnFloor()));
                }

                arrayBuilder.add(objectBuilder);
            }

            jsonWriter.writeArray(arrayBuilder.build());
            System.out.println("Successfully saved " + flatSet.size() + " flats to " + filename);

        } catch (Exception e) {
            System.err.println("Failed to save data: " + e.getMessage());
        }
    }
}