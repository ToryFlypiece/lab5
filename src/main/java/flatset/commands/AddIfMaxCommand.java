package flatset.commands;

import flatset.Flat;
import flatset.auth.User;
import flatset.utils.FlatParser;
import java.util.HashSet;
import java.util.Optional;

/**
 * Команда добавления квартиры в коллекцию, если она больше текущей максимальной.
 */
public class AddIfMaxCommand implements Command {

    /**
     * Выполняет команду добавления квартиры, если её значение больше максимального в коллекции.
     *
     * @param flatSet Коллекция квартир, в которую потенциально добавляется новый элемент.
     * @param argument Строка, содержащая параметры новой квартиры в ожидаемом формате для {@link FlatParser}.
     */
    @Override
    public void execute(HashSet<Flat> flatSet, String argument, User currentUser) {
        try {
            Flat newFlat = FlatParser.parseFlat(argument);

            Optional<Flat> maxFlat = flatSet.stream().max(Flat::compareTo);

            if (!maxFlat.isPresent() || newFlat.compareTo(maxFlat.get()) > 0) {
                flatSet.add(newFlat);
                System.out.println("Added new flat: " + newFlat);
            } else {
                System.out.println("Flat value is lower than the maximum in the collection.");
            }
        } catch (Exception e) {
            System.err.println("Error adding a flat: " + e.getMessage());
        }
    }
}
