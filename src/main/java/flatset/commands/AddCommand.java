package flatset.commands;

import flatset.Coordinates;
import flatset.Flat;
import flatset.View;
import flatset.House;
import java.time.ZonedDateTime;
import java.util.HashSet;

/**
 * Команда для добавления нового элемента в коллекцию квартир.
 */
public class AddCommand implements Command {

    /**
     * Выполняет команду добавления элемента в коллекцию.
     *
     * @param flatSet Коллекция квартир, в которую добавляется новый элемент.
     * @param argument Строка аргументов в формате:
     *                 {name,x,y,area,numberOfRooms,isNew,timeToMetroByTransport,view,{houseName,houseYear,houseNumberOfFlats}}
     *                 Все параметры разделяются запятыми и заключены в фигурные скобки.
     */
    @Override
    public void execute(HashSet<Flat> flatSet, String argument) {
        try {
            if (!argument.startsWith("{") || !argument.endsWith("}")) {
                throw new IllegalArgumentException("Invalid format. Parameters must be in curly brackets");
            }

            // Убираем внешние скобки
            String content = argument.substring(1, argument.length() - 1);

            // Находим место, где начинается вложенная часть с домом
            int houseStartIndex = content.lastIndexOf("{");
            int houseEndIndex = content.lastIndexOf("}");

            if (houseStartIndex == -1 || houseEndIndex == -1) {
                throw new IllegalArgumentException("Invalid format. Should include house information.");
            }

            // Разделяем аргументы квартиры и дома
            String flatParams = content.substring(0, houseStartIndex).trim();
            String houseParams = content.substring(houseStartIndex, houseEndIndex + 1).trim();

            // Разбираем параметры квартиры
            String[] flatArgs = flatParams.split(",");
            if (flatArgs.length != 8) {
                throw new IllegalArgumentException("Invalid flat format. Required: name,x,y,area,numberOfRooms,isNew,timeToMetroByTransport,view");
            }

            String name = flatArgs[0].trim();
            int x = Integer.parseInt(flatArgs[1].trim());
            int y = Integer.parseInt(flatArgs[2].trim());
            long area = Long.parseLong(flatArgs[3].trim());
            long numberOfRooms = Long.parseLong(flatArgs[4].trim());
            Boolean isNew = Boolean.parseBoolean(flatArgs[5].trim());
            double timeToMetroByTransport = Double.parseDouble(flatArgs[6].trim());
            View view = View.valueOf(flatArgs[7].trim().toUpperCase());

            // Разбираем параметры дома
            String[] houseArgs = houseParams.substring(1, houseParams.length() - 1).split(",");
            if (houseArgs.length != 3) {
                throw new IllegalArgumentException("Invalid format for house. Required: houseName,houseYear,houseNumberOfFlats");
            }

            String houseName = houseArgs[0].trim();
            int houseYear = Integer.parseInt(houseArgs[1].trim());
            int houseNumberOfFlats = Integer.parseInt(houseArgs[2].trim());

            House house = new House(houseName, houseYear, houseNumberOfFlats);

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
            flat.setHouse(house);  // Устанавливаем дом для квартиры

            flatSet.add(flat);
            System.out.println("Element added succesfully: " + flat);
        } catch (Exception e) {
            System.err.println("Error adding an element: " + e.getMessage());
        }
    }

    /**
     * Генерирует уникальный идентификатор, увеличивая максимальный существующий ID в коллекции на 1.
     *
     * @param flatSet Коллекция квартир, в которой ищется максимальный ID.
     * @return Новый уникальный ID.
     */
    private long generateAutoIncrementId(HashSet<Flat> flatSet) {
        long maxId = flatSet.stream()
                .mapToLong(Flat::getId)
                .max()
                .orElse(0);
        return maxId + 1;
    }
}
