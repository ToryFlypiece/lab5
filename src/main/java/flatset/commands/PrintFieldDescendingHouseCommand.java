package flatset.commands;

import flatset.Flat;
import flatset.House;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Команда для вывода информации о домах, связанных с квартирами,
 * в порядке убывания (по убыванию их сравнимости).
 */
public class PrintFieldDescendingHouseCommand implements Command {

    /**
     * Выполняет команду, которая отображает список домов, связанных с квартирами,
     * отсортированных в порядке убывания. Пропускает квартиры без указанных домов.
     *
     * @param flatSet Коллекция квартир, содержащих дома.
     * @param argument Не используется в данной команде.
     */
    @Override
    public void execute(HashSet<Flat> flatSet, String argument) {
        // Извлекаем дома, сортируем в порядке убывания и собираем в список
        List<House> houses = flatSet.stream()
                .map(Flat::getHouse)
                .filter(house -> house != null)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        if (houses.isEmpty()) {
            System.out.println("В коллекции нет домов.");
            return;
        }

        System.out.println("=== Дома (в порядке убывания) ===");
        houses.forEach(house ->
                System.out.printf("- %s (Год: %d, Квартир/этажей: %d)%n",
                        house.getName(),
                        house.getYear(),
                        house.getNumberOfFlatsOnFloor())
        );
    }
}
