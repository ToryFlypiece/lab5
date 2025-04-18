package flatset.commands;

import java.util.HashSet;
import flatset.Flat;

public class HelpCommand implements Command {
    @Override
    public void execute(HashSet<Flat> flatSet, String argument) {
        System.out.println("Available commands:");
        System.out.println("  show                              - Display all elements in the collection.");
        System.out.println("  info                              - Display information about the collection.");
        System.out.println("  add                               - Add a new element to the collection. Usage: add {id,name,isNew}");
        System.out.println("  remove_by_id                      - Remove an element by its ID. Usage: remove_by_id {id}");
        System.out.println("  clear                             - Clear the entire collection.");
        System.out.println("  save                              - Save the collection to a file.");
        System.out.println("  print_unique_house                - Print unique values of the house field.");
        System.out.println("  print_field_ascending_number_of_rooms - Print numberOfRooms values in ascending order.");
        System.out.println("  print_field_descending_house      - Print house values in descending order.");
        System.out.println("  help                              - Display this help message.");
        System.out.println("  exit                              - Exit the application.");
    }
}