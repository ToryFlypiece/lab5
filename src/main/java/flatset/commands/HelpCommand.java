package flatset.commands;

import java.util.HashSet;
import flatset.Flat;

/**
 * Команда для вывода справки по доступным командам.
 */
public class HelpCommand implements Command {

    /**
     * Выполняет команду вывода справочной информации о доступных командах программы.
     *
     * @param flatSet Коллекция квартир (не используется в данной команде, но требуется по интерфейсу).
     * @param argument Не используется.
     */
    @Override
    public void execute(HashSet<Flat> flatSet, String argument) {
        System.out.println("\n=== Available Commands ===");
        System.out.println("  help                              - Вывести список доступных команд");
        System.out.println("  info                              - Вывести информацию о коллекции");
        System.out.println("  show                              - Вывести список квартир в коллекции");
        System.out.println("  add {name,x,y,area,rooms,new,transport,view} - Добавить квартиру");
        System.out.println("  remove_by_id id                   - Удалить квартиру по ID");
        System.out.println("  clear                             - Очистить коллекцию");
        System.out.println("  save [filename]                   - Сохранить коллекцию в файл (по умолчанию: flats.json)");
        System.out.println("  execute_script filename           - Выполнить список команд из файла");

        System.out.println("\n=== Conditional Commands ===");
        System.out.println("  add_if_min {flat_data}            - Добавить квартиру если её значение меньше всех имеющихся в коллекции");
        System.out.println("  add_if_max {flat_data}            - Добавить квартиру если её значение больше всех имеющихся в коллекции");
        System.out.println("  remove_greater {flat_data}        - Удалить квартиры со значениями больше аргумента");

        System.out.println("\n=== Update Commands ===");
        System.out.println("  update id {flat_data}             - Обновить все данные квартиры по ID");
        System.out.println("  update_by_id id {json_data}       - Обновить часть данных квартиры по ID");

        System.out.println("\n=== Field Display Commands ===");
        System.out.println("  print_unique_house                - Вывести уникальные дома");
        System.out.println("  print_field_ascending_number_of_rooms - Вывести квартиры в порядке уменьшения кол-ва комнат");
        System.out.println("  print_field_descending_house      - Вывести квартиры в порядке уменьшения номера дома");

        System.out.println("\n=== System Commands ===");
        System.out.println("  exit                              - Выйти из программы");

        System.out.println("\n=== Notes ===");
        System.out.println("* {flat_data} format: {name,x,y,area,numberOfRooms,isNew,timeToMetroByTransport,view}");
        System.out.println("* {json_data} format: {\"field\":value} pairs (e.g., {\"name\":\"New Name\",\"area\":100})");
        System.out.println("* View options: STREET, PARK, BAD, GOOD, TERRIBLE");
    }
}
