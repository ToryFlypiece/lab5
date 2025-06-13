package flatset.commands;

import flatset.Flat;
import flatset.auth.User;
import java.util.HashSet;

/**
 * Интерфейс, представляющий команду для работы с коллекцией квартир.
 * Все конкретные команды должны реализовывать этот интерфейс.
 */
public interface Command {
    /**
     * Выполняет действие команды над указанной коллекцией квартир.
     *
     * @param flatSet коллекция квартир, над которой выполняется команда
     * @param argument аргумент команды (может быть пустой строкой, если команда не требует аргументов)
     * @param currentUser пользователь, выполняющий команду (используется для прав доступа и owner_id)
     * @throws IllegalArgumentException если аргумент команды некорректен
     * @throws IllegalStateException если состояние коллекции не позволяет выполнить команду
     */
    void execute(HashSet<Flat> flatSet, String argument, User currentUser);
}
