package flatset.commands;

import flatset.Flat;
import flatset.FlatParser;
import java.util.HashSet;
import java.util.Optional;

/**
 * Команда добавления квартиры в коллекцию, если она меньше текущей минимальной.
 */
public class AddIfMinCommand implements Command {

    /**
     * Выполняет команду добавления квартиры, если её значение меньше минимального в коллекции.
     *
     * @param flatSet Коллекция квартир, в которую потенциально добавляется новый элемент.
     * @param argument Строка, содержащая параметры новой квартиры в ожидаемом формате для {@link FlatParser}.
     */
    @Override
    public void execute(HashSet<Flat> flatSet, String argument) {
        try {
            // Парсинг новой квартиры из аргументов
            Flat newFlat = FlatParser.parseFlat(argument);

            // Поиск квартиры с минимальным значением
            Optional<Flat> minFlat = flatSet.stream().min(Flat::compareTo);

            // Добавление квартиры, если она меньше минимальной
            if (!minFlat.isPresent() || newFlat.compareTo(minFlat.get()) < 0) {
                flatSet.add(newFlat);
                System.out.println("Добавлена новая квартира: " + newFlat);
            } else {
                System.out.println("Значение квартиры не меньше минимального в коллекции.");
            }
        } catch (Exception e) {
            System.err.println("Ошибка добавления квартиры: " + e.getMessage());
        }
    }
}
