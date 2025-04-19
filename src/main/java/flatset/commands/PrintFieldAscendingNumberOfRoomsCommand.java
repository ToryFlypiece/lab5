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
        // Создаём Map, где ключ - это количество комнат, а значение - список квартир с этим количеством комнат
        Map<Long, List<Flat>> flatsByRooms = flatSet.stream()
                .collect(Collectors.groupingBy(Flat::getNumberOfRooms));

        if (flatsByRooms.isEmpty()) {
            System.out.println("В коллекции нет ни одной квартиры.");
            return;
        }

        System.out.println("=== Кол-во комнат (В порядке возрастания) ===");

        // Сортируем по количеству комнат
        flatsByRooms.keySet().stream()
                .sorted()
                .forEach(rooms -> {
                    // Для каждого количества комнат выводим соответствующие ID квартир
                    System.out.print("- " + rooms + " комнат" +
                            (rooms % 10 == 1 && rooms % 100 != 11 ? "а" :
                                    (rooms % 10 >= 2 && rooms % 10 <= 4 && (rooms % 100 < 10 || rooms % 100 >= 20) ? "ы" : "")));

                    // Выводим ID квартир с данным количеством комнат
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
