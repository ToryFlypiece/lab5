package flatset.commands;

import flatset.Coordinates;
import flatset.Flat;
import flatset.View;
import java.time.ZonedDateTime;
import java.util.HashSet;

public class AddCommand implements Command {
    @Override
    public void execute(HashSet<Flat> flatSet, String argument) {
        try {
            if (!argument.startsWith("{") || !argument.endsWith("}")) {
                throw new IllegalArgumentException("Invalid format. Parameters must be enclosed in curly brackets.");
            }

            String[] params = argument.substring(1, argument.length() - 1).split(",");
            if (params.length != 8) {
                throw new IllegalArgumentException("Invalid format. Expected: name,x,y,area,numberOfRooms,isNew,timeToMetroByTransport,view");
            }

            String name = params[0].trim();
            int x = Integer.parseInt(params[1].trim());
            int y = Integer.parseInt(params[2].trim());
            long area = Long.parseLong(params[3].trim());
            long numberOfRooms = Long.parseLong(params[4].trim());
            Boolean isNew = Boolean.parseBoolean(params[5].trim());
            double timeToMetroByTransport = Double.parseDouble(params[6].trim());
            View view = View.valueOf(params[7].trim().toUpperCase());

            long id = generateAutoIncrementId(flatSet);
            ZonedDateTime creationDate = ZonedDateTime.now();
            Coordinates coordinates = new Coordinates(x, y);

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

            flatSet.add(flat);
            System.out.println("Element added successfully: " + flat);
        } catch (Exception e) {
            System.err.println("Error adding element: " + e.getMessage());
        }
    }

    private long generateAutoIncrementId(HashSet<Flat> flatSet) {
        long maxId = flatSet.stream()
                .mapToLong(Flat::getId)
                .max()
                .orElse(0);
        return maxId + 1;
    }
}