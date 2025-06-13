package flatset.commands;

import flatset.Flat;
import flatset.House;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import flatset.auth.User;


/**
 * Команда для вывода уникальных домов, указанных в квартиках коллекции.
 */
public class PrintUniqueHouseCommand implements Command {

    /**
     * Выполняет команду, которая отображает уникальные дома,
     * связанные с квартирами в коллекции. Повторяющиеся дома исключаются.
     *
     * @param flatSet Коллекция квартир, из которой извлекаются дома.
     * @param argument Не используется в данной команде.
     */
    @Override
    public void execute(HashSet<Flat> flatSet, String argument, User currentUser) {
        Set<House> uniqueHouses = flatSet.stream()
                .map(Flat::getHouse)
                .filter(house -> house != null)
                .collect(Collectors.toSet());

        if (uniqueHouses.isEmpty()) {
            System.out.println("There are no houses in the collection.");
            return;
        }

        System.out.println("=== Unique Houses ===");
        uniqueHouses.forEach(house ->
                System.out.printf("- %s (Year: %d, Flats/Floors: %d)%n",
                        house.getName(),
                        house.getYear(),
                        house.getNumberOfFlatsOnFloor())
        );
    }
}
