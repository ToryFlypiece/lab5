package flatset.commands;

import flatset.Flat;
import flatset.FlatParser;

import java.util.HashSet;
import java.util.Optional;

public class AddIfMinCommand implements Command
{
    @Override
    public void execute(HashSet<Flat> flatSet, String argument) {
        try {
            Flat newFlat = FlatParser.parseFlat(argument);
            Optional<Flat> minFlat = flatSet.stream().min(Flat::compareTo);

            if (!minFlat.isPresent() || newFlat.compareTo(minFlat.get()) < 0) {
                flatSet.add(newFlat);
                System.out.println("Added new minimum flat: " + newFlat);
            } else {
                System.out.println("Flat is not smaller than the current minimum.");
            }
        } catch (Exception e) {
            System.err.println("Error adding flat: " + e.getMessage());
        }
    }
}