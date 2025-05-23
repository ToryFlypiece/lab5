package flatset.commands;

import flatset.Flat;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Команда для вывода количества комнат во всех квартирах в порядке возрастания.
 * Включает в вывод также ID квартир с соответствующим количеством комнат.
 */
public class PrintFieldAscendingNumberOfRoomsCommand implements Command {

    /**
     * Выполняет команду, которая отображает количество комнат в каждой квартире
     * в порядке возрастания, а также выводит ID квартир с соответствующим количеством комнат.
     * Значения форматируются с правильными окончаниями слов.
     *
     * @param flatSet Коллекция квартир, из которой извлекаются значения количества комнат.
     * @param argument Не используется в данной команде.
     */
    @Override
    public void execute(HashSet<Flat> flatSet, String argument) {
        Map<Long, List<Flat>> flatsByRooms = flatSet.stream()
                .collect(Collectors.groupingBy(Flat::getNumberOfRooms));

        if (flatsByRooms.isEmpty()) {
            System.out.println("The collection contains no flats.");
            return;
        }

        System.out.println("=== Number of Rooms (In Ascending Order) ===");

        flatsByRooms.keySet().stream()
                .sorted()
                .forEach(rooms -> {
                    System.out.print("- " + rooms + " room" +
                            (rooms % 10 == 1 && rooms % 100 != 11 ? "s" :
                                    (rooms % 10 >= 2 && rooms % 10 <= 4 && (rooms % 100 < 10 || rooms % 100 >= 20) ? "s" : "")));

                    List<Flat> flats = flatsByRooms.get(rooms);
                    System.out.print(": ");
                    for (int i = 0; i < flats.size(); i++) {
                        System.out.print(flats.get(i).getId());
                        if (i < flats.size() - 1) {
                            System.out.print(", ");
                        }
                    }

                    System.out.println();
                });
    }
}
