package flatset.commands;

import flatset.Flat;
import flatset.House;
import flatset.auth.User;
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
    public void execute(HashSet<Flat> flatSet, String argument, User currentUser) {
        List<House> houses = flatSet.stream()
                .map(Flat::getHouse)
                .filter(house -> house != null)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        if (houses.isEmpty()) {
            System.out.println("There are no houses in the collection.");
            return;
        }

        System.out.println("=== Houses (in descending order) ===");
        houses.forEach(house ->
                System.out.printf("- %s (Year: %d, Flats/Floors: %d)%n",
                        house.getName(),
                        house.getYear(),
                        house.getNumberOfFlatsOnFloor())
        );
    }
}
