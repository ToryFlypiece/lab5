package flatset.commands;

import flatset.Flat;
import flatset.auth.User;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

/**
 * Команда для вывода информации о коллекции квартир.
 */
public class InfoCommand implements Command {

    /**
     * Выполняет команду отображения общей информации о коллекции:
     * тип, время запроса, количество элементов и примерный объём в памяти.
     *
     * @param flatSet Коллекция квартир, о которой выводится информация.
     * @param argument Не используется в данной команде.
     */
    @Override
    public void execute(Set<Flat> flatSet, String argument, User currentUser) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        System.out.println("=== Collection Information ===");
        System.out.println("Type: " + flatSet.getClass().getSimpleName());
        System.out.println("Initialization time: " + LocalDateTime.now().format(formatter));
        System.out.println("Number of elements: " + flatSet.size());
        System.out.println("Memory size: ~" + (flatSet.size() * 128) + " bytes");
    }
}
