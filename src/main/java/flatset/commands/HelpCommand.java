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
        System.out.println("  help                              - Output the list of commands");
        System.out.println("  info                              - Output the collection info");
        System.out.println("  show                              - Display the list of flats in the collection");
        System.out.println("  add {name,x,y,area,rooms,new,transport,view} - Add a flat");
        System.out.println("  remove_by_id id                   - Remove a flat by ID");
        System.out.println("  clear                             - Clear the collection");
        System.out.println("  save [filename]                   - Save the collection to a file (default: flats.json)");
        System.out.println("  execute_script filename           - Execute a list of commands from a file");

        System.out.println("\n=== Conditional Commands ===");
        System.out.println("  add_if_min {flat_data}            - Add a flat if its value is less than all others in the collection");
        System.out.println("  add_if_max {flat_data}            - Add a flat if its value is greater than all others in the collection");
        System.out.println("  remove_greater {flat_data}        - Remove flats with values greater than the argument");

        System.out.println("\n=== Update Commands ===");
        System.out.println("  update id {flat_data}             - Update all data of a flat by ID");
        System.out.println("  update_by_id id {json_data}       - Update part of a flat's data by ID");

        System.out.println("\n=== Field Display Commands ===");
        System.out.println("  print_unique_house                - Display unique houses");
        System.out.println("  print_field_ascending_number_of_rooms - Display flats in ascending order of the number of rooms");
        System.out.println("  print_field_descending_house      - Display flats in descending order of house number");

        System.out.println("\n=== System Commands ===");
        System.out.println("  exit                              - Exit the program");

        System.out.println("\n=== Notes ===");
        System.out.println("* {flat_data} format: {name,x,y,area,numberOfRooms,isNew,timeToMetroByTransport,view}");
        System.out.println("* {json_data} format: {\"field\":value} pairs (e.g., {\"name\":\"New Name\",\"area\":100})");
        System.out.println("* View options: STREET, PARK, BAD, GOOD, TERRIBLE");
    }
}
