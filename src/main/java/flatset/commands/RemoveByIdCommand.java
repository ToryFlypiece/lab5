package flatset.commands;

import flatset.Flat;
import java.util.HashSet;
import flatset.auth.User;

/**
 * Команда для удаления квартиры по её ID.
 */
public class RemoveByIdCommand implements Command {

    /**
     * Выполняет команду удаления квартиры из коллекции по её ID.
     * Если квартира с таким ID не найдена, выводится соответствующее сообщение.
     *
     * @param flatSet Коллекция квартир, из которой удаляется квартира.
     * @param argument Строка, содержащая ID квартиры для удаления.
     */
    @Override
    public void execute(HashSet<Flat> flatSet, String argument, User currentUser) {
        try {
            if (argument == null || argument.trim().isEmpty()) {
                System.out.println("Error: ID is not defined. Usage: remove_by_id <id>");
                return;
            }

            long id = Long.parseLong(argument.trim());
            boolean removed = flatSet.removeIf(flat -> flat.getId() == id);

            if (removed) {
                System.out.println("Apartment with ID " + id + " has been successfully removed.");
            } else {
                System.out.println("No apartment found with ID " + id + ".");
            }
        } catch (NumberFormatException e) {
            System.err.println("Error: Invalid ID format. ID must be a number.");
        } catch (Exception e) {
            System.err.println("Error deleting apartment: " + e.getMessage());
        }
    }
}
