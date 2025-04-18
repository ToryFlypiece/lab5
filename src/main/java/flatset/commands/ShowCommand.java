package flatset.commands;

import flatset.Flat;
import java.util.HashSet;

public class ShowCommand implements Command {
    @Override
    public void execute(HashSet<Flat> flatSet, String argument) {
        if (flatSet.isEmpty()) {
            System.out.println("The collection is empty.");
        } else {
            System.out.println("Collection elements:");
            for (Flat flat : flatSet) {
                System.out.println(flat);
            }
        }
    }
}