package flatset.commands;

import flatset.Flat;
import flatset.utils.FlatParser;
import java.util.HashSet;
import java.util.Optional;

/**
 * Команда для обновления данных квартиры по ID.
 */
public class UpdateCommand implements Command {

    /**
     * Выполняет команду обновления данных квартиры по ID.
     * Для этого извлекаются новые данные квартиры, а затем старая квартира заменяется на обновленную.
     * В случае ошибки или некорректного ввода выводится соответствующее сообщение.
     *
     * @param flatSet Коллекция квартир, в которой необходимо обновить данные.
     * @param argument Аргумент команды, содержащий ID квартиры и строку с новыми данными для обновления.
     */
    @Override
    public void execute(HashSet<Flat> flatSet, String argument) {
        try {
            // Разделяем аргумент на ID и новые данные для квартиры
            String[] parts = argument.split(" ", 2);
            if (parts.length != 2) {
                throw new IllegalArgumentException("Usage: update id {flat_data}");
            }

            long id = Long.parseLong(parts[0]);
            Flat updatedFlat = FlatParser.parseFlat(parts[1]);
            updatedFlat.setId(id);

            // Ищем квартиру по ID
            Optional<Flat> existing = flatSet.stream()
                    .filter(f -> f.getId() == id)
                    .findFirst();

            // Если квартира найдена, заменяем ее на обновленную
            if (existing.isPresent()) {
                flatSet.remove(existing.get());
                flatSet.add(updatedFlat);
                System.out.println("Updated apartment with ID " + id);
            } else {
                System.out.println("Apartment with ID " + id + " not found.");
            }
        } catch (Exception e) {
            // Выводим ошибку, если что-то пошло не так
            System.err.println("Error updating: " + e.getMessage());
        }
    }
}
