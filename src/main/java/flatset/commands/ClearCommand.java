package flatset.commands;

import flatset.Flat;
import java.util.HashSet;

public class ClearCommand implements Command {
    @Override
    public void execute(HashSet<Flat> flatSet, String argument) {
        try {
            int sizeBefore = flatSet.size();
            flatSet.clear();

            if (sizeBefore > 0) {
                System.out.println("Successfully cleared all " + sizeBefore + " flats from the collection.");
            } else {
                System.out.println("Collection was already empty.");
            }
        } catch (Exception e) {
            System.err.println("Error clearing collection: " + e.getMessage());
        }
    }
}