package flatset;

import java.util.HashSet;
/**
 * Главный класс приложения. Загружает данные из файла, создает FlatManager и запускает CommandProcessor.
 */
public class Main {
    public static void main(String[] args) {
        String filePath = "list.json";
        HashSet<Flat> flatSet = FlatLoader.loadDataFromFile(filePath);

        FlatManager flatManager = new FlatManager(flatSet);
        CommandProcessor commandProcessor = new CommandProcessor(flatManager);

        commandProcessor.startConsoleApp();
    }
}