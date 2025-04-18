package flatset.commands;

import flatset.Flat;
import flatset.FlatParser;
import java.util.HashSet;

public class RemoveGreaterCommand implements Command
{
    @Override
    public void execute(HashSet<Flat> flatSet, String argument) {
        try {
            Flat comparisonFlat = FlatParser.parseFlat(argument);
            int initialSize = flatSet.size();

            flatSet.removeIf(flat -> flat.compareTo(comparisonFlat) > 0);

            int removedCount = initialSize - flatSet.size();
            System.out.println("Removed " + removedCount + " flats greater than the specified one.");
        } catch (Exception e) {
            System.err.println("Error removing flats: " + e.getMessage());
        }
    }
}