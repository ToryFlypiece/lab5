package flatset.commands;

import flatset.Flat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;

public class InfoCommand implements Command {
    @Override
    public void execute(HashSet<Flat> flatSet, String argument) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        System.out.println("=== Collection Information ===");
        System.out.println("Type: " + flatSet.getClass().getSimpleName());
        System.out.println("Initialization time: " + LocalDateTime.now().format(formatter));
        System.out.println("Number of elements: " + flatSet.size());
        System.out.println("Estimated memory: ~" + (flatSet.size() * 128) + " bytes");
    }
}