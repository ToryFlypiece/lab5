package flatset.commands;

import flatset.Flat;
import flatset.auth.User;

import java.util.Scanner;
import java.util.Set;

/**
 * Интерфейс для интерактивных команд, которым нужен Scanner для ввода.
 */
public interface InteractiveCommand extends Command {
    /**
     * Выполняет действие команды с доступом к Scanner для интерактивного ввода.
     *
     * @param flatSet коллекция квартир
     * @param argument аргумент команды
     * @param currentUser текущий пользователь
     * @param scanner Scanner для интерактивного ввода
     */
    void execute(Set<Flat> flatSet, String argument, User currentUser, Scanner scanner);
}
