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
                System.out.println("Все " + sizeBefore + " квартир(ы) из коллекции успешно удалены.");
            } else {
                System.out.println("Коллекция уже пуста.");
            }
        } catch (Exception e) {
            System.err.println("Ошибка очистки коллекции: " + e.getMessage());
        }
    }
}
