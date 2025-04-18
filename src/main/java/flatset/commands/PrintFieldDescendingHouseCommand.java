package flatset.commands;

import flatset.Flat;
import flatset.House;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class PrintFieldDescendingHouseCommand implements Command {
    @Override
    public void execute(HashSet<Flat> flatSet, String argument) {
        List<House> houses = flatSet.stream()
                .map(Flat::getHouse)
                .filter(house -> house != null)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        if (houses.isEmpty()) {
            System.out.println("No houses found in the collection.");
            return;
        }

        System.out.println("=== Houses (Descending Order) ===");
        houses.forEach(house ->
                System.out.printf("- %s (Year: %d, Flats/floor: %d)%n",
                        house.getName(),
                        house.getYear(),
                        house.getNumberOfFlatsOnFloor())
        );
    }
}