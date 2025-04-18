package flatset.commands;

import flatset.Flat;
import flatset.FlatParser;
import java.util.HashSet;
import java.util.Optional;

public class AddIfMaxCommand implements Command {
    @Override
    public void execute(HashSet<Flat> flatSet, String argument) {
        try {
            Flat newFlat = FlatParser.parseFlat(argument);
            Optional<Flat> maxFlat = flatSet.stream().max(Flat::compareTo);

            if (!maxFlat.isPresent() || newFlat.compareTo(maxFlat.get()) > 0) {
                flatSet.add(newFlat);
                System.out.println("Added new maximum flat: " + newFlat);
            } else {
                System.out.println("Flat is not larger than the current maximum.");
            }
        } catch (Exception e) {
            System.err.println("Error adding flat: " + e.getMessage());
        }
    }
}