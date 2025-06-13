package flatset.commands;

import flatset.Flat;
import flatset.auth.User;
import java.util.HashSet;

/**
 * Команда для отображения всех квартир в коллекции.
 */
public class ShowCommand implements Command {

    /**
     * Выполняет команду отображения всех элементов коллекции.
     * Если коллекция пуста, выводится сообщение об этом.
     *
     * @param flatSet Коллекция квартир, элементы которой необходимо отобразить.
     * @param argument Аргумент команды (не используется в данном случае).
     */
    @Override
    public void execute(HashSet<Flat> flatSet, String argument, User currentUser) {
        if (flatSet.isEmpty()) {
            System.out.println("Collection is empty.");
        } else {
            System.out.println("Elements of the collection:");
            for (Flat flat : flatSet) {
                System.out.println(flat);
            }
        }
    }
}
