package flatset.commands;

import flatset.Flat;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class PrintFieldAscendingNumberOfRoomsCommand implements Command {
    @Override
    public void execute(HashSet<Flat> flatSet, String argument) {
        List<Long> roomsList = flatSet.stream()
                .map(Flat::getNumberOfRooms)
                .sorted()
                .collect(Collectors.toList());

        if (roomsList.isEmpty()) {
            System.out.println("No flats found in the collection.");
            return;
        }

        System.out.println("=== Number of Rooms (Ascending) ===");
        roomsList.forEach(rooms ->
                System.out.println("- " + rooms + " room" + (rooms != 1 ? "s" : ""))
        );
    }
}