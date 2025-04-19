package flatset.commands;

import flatset.Flat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;

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
    public void execute(HashSet<Flat> flatSet, String argument) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        System.out.println("=== Информация о коллекции ===");
        System.out.println("Тип: " + flatSet.getClass().getSimpleName());
        System.out.println("Время инициализации: " + LocalDateTime.now().format(formatter));
        System.out.println("Количество элементов: " + flatSet.size());
        System.out.println("Размер в памяти: ~" + (flatSet.size() * 128) + " bytes");
    }
}
