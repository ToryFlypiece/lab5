package flatset.commands;

import flatset.Flat;
import flatset.House;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class PrintUniqueHouseCommand implements Command {
    @Override
    public void execute(HashSet<Flat> flatSet, String argument) {
        Set<House> uniqueHouses = flatSet.stream()
                .map(Flat::getHouse)
                .filter(house -> house != null)
                .collect(Collectors.toSet());

        if (uniqueHouses.isEmpty()) {
            System.out.println("No houses found in the collection.");
            return;
        }

        System.out.println("=== Unique Houses ===");
        uniqueHouses.forEach(house ->
                System.out.printf("- %s (Year: %d, Flats/floor: %d)%n",
                        house.getName(),
                        house.getYear(),
                        house.getNumberOfFlatsOnFloor())
        );
    }
}