package flatset.commands;

import flatset.Flat;
import flatset.utils.FlatParser;
import java.util.HashSet;

/**
 * Команда для удаления всех квартир, значение которых больше заданной квартиры.
 */
public class RemoveGreaterCommand implements Command {

    /**
     * Выполняет команду удаления всех квартир, чьи значения больше значения заданной квартиры.
     * Значение квартиры сравнивается с помощью метода `compareTo`.
     *
     * @param flatSet Коллекция квартир, из которой удаляются квартиры.
     * @param argument Строка, содержащая данные квартиры для сравнения.
     */
    @Override
    public void execute(HashSet<Flat> flatSet, String argument) {
        try {
            Flat comparisonFlat = FlatParser.parseFlat(argument);
            int initialSize = flatSet.size();

            flatSet.removeIf(flat -> flat.compareTo(comparisonFlat) > 0);

            int removedCount = initialSize - flatSet.size();
            System.out.println("Removed " + removedCount + " apartments with values greater than the given one.");
        } catch (Exception e) {
            System.err.println("Error deleting apartments: " + e.getMessage());
        }
    }
}
