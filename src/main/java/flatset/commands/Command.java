package flatset.commands;

import flatset.Flat;
import java.util.HashSet;

public interface Command {
    void execute(HashSet<Flat> flatSet, String argument);
}