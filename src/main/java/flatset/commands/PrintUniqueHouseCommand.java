package flatset.commands;

import flatset.Flat;
import flatset.House;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Команда для вывода уникальных домов, указанных в квартирах коллекции.
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
    public void execute(HashSet<Flat> flatSet, String argument) {
        // Сбор уникальных домов из коллекции квартир
        Set<House> uniqueHouses = flatSet.stream()
                .map(Flat::getHouse)
                .filter(house -> house != null)
                .collect(Collectors.toSet());

        if (uniqueHouses.isEmpty()) {
            System.out.println("В коллекции нет домов.");
            return;
        }

        System.out.println("=== Уникальные дома ===");
        uniqueHouses.forEach(house ->
                System.out.printf("- %s (Год: %d, Квартир/этажей: %d)%n",
                        house.getName(),
                        house.getYear(),
                        house.getNumberOfFlatsOnFloor())
        );
    }
}
