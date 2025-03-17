package flatset;

import java.util.HashSet;
import java.util.Optional;
/**
 * Класс FlatManager управляет коллекцией квартир.
 * Предоставляет методы для добавления, удаления, поиска и очистки коллекции.
 */
public class FlatManager {

    /**
     * Конструктор класса FlatManager.
     *
     * @param flatSet Коллекция квартир, которой будет управлять FlatManager.
     */
    private HashSet<Flat> flatSet;

    public FlatManager(HashSet<Flat> flatSet) {
        this.flatSet = flatSet;
    }

    public void addFlat(Flat flat) {
        flatSet.add(flat);
    }
    /**
     * Удаляет квартиру из коллекции по её ID.
     *
     * @param id ID квартиры, которую необходимо удалить.
     */
    public void removeById(long id) {
        flatSet.removeIf(flat -> flat.getId() == id);
    }
    /**
     * Очищает коллекцию квартир.
     */
    public void clearCollection() {
        flatSet.clear();
    }
    /**
     * Ищет квартиру в коллекции по её ID.
     *
     * @param id ID квартиры, которую необходимо найти.
     * @return Optional<Flat>, содержащий найденную квартиру, если она существует.
     */
    public Optional<Flat> findById(long id) {
        return flatSet.stream()
                .filter(flat -> flat.getId() == id)
                .findFirst();
    }

    public HashSet<Flat> getFlatSet() {
        return flatSet;
    }
}