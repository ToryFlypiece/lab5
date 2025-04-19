package flatset.commands;

import flatset.Flat;
import java.util.HashSet;

/**
 * Команда для очистки коллекции квартир.
 */
public class ClearCommand implements Command {

    /**
     * Выполняет команду очистки коллекции, удаляя все элементы из неё.
     *
     * @param flatSet Коллекция квартир, подлежащая очистке.
     * @param argument Не используется в данной команде.
     */
    @Override
    public void execute(HashSet<Flat> flatSet, String argument) {
        try {
            int sizeBefore = flatSet.size();
            flatSet.clear();

            if (sizeBefore > 0) {
                System.out.println("All " + sizeBefore + " flats have been succesfully removed.");
            } else {
                System.out.println("Collection is already empty.");
            }
        } catch (Exception e) {
            System.err.println("Error clearing the collection: " + e.getMessage());
        }
    }
}
