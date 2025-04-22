package flatset.commands;

import flatset.Flat;
import flatset.utils.FlatUpdater;
import java.util.HashSet;
import java.util.Optional;

/**
 * Команда для обновления данных квартиры по ID.
 */
public class UpdateByIdCommand implements Command {

    /**
     * Выполняет команду обновления данных квартиры по ID.
     * Для этого извлекаются данные поля, которые нужно обновить, в формате {field:value}.
     * Если квартира с заданным ID найдена, ее поля обновляются.
     * В случае ошибки или некорректного ввода выводится соответствующее сообщение.
     *
     * @param flatSet Коллекция квартир, в которой необходимо обновить данные.
     * @param argument Аргумент команды, содержащий ID квартиры и строку с полями для обновления в формате {field:value}.
     */
    @Override
    public void execute(HashSet<Flat> flatSet, String argument) {
        try {
            String[] parts = argument.split(" ", 2);
            if (parts.length != 2) {
                throw new IllegalArgumentException("Usage: update_by_id id {field:value}");
            }

            long id = Long.parseLong(parts[0]);
            String jsonData = parts[1];

            // Ищем квартиру по ID
            Optional<Flat> toUpdate = flatSet.stream()
                    .filter(f -> f.getId() == id)
                    .findFirst();

            // Если квартира найдена, обновляем ее поля
            if (toUpdate.isPresent()) {
                FlatUpdater.updateFields(toUpdate.get(), jsonData);
                System.out.println("Updated fields of apartment with ID " + id);
            } else {
                System.out.println("Apartment with ID " + id + " not found.");
            }
        } catch (Exception e) {
            // Выводим ошибку, если что-то пошло не так
            System.err.println("Error updating: " + e.getMessage());
        }
    }
}
