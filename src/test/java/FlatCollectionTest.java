import flatset.Coordinates;
import flatset.Flat;
import flatset.House;
import flatset.View;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class FlatCollectionTest {

    private HashSet<Flat> flatSet;

    @BeforeEach
    void setUp() {
        flatSet = new HashSet<>();
    }

    // Test 1: Добавление элемента в коллекцию
    @Test
    void testAddFlat() {
        Flat flat = new Flat(1, "Flat 1", new Coordinates(10, -100), ZonedDateTime.now(), 75, 3, true, 15.5, View.PARK, null);
        flatSet.add(flat);
        assertEquals(1, flatSet.size());
        assertTrue(flatSet.contains(flat));
    }

    // Test 2: Удаление элемента по ID
    @Test
    void testRemoveById() {
        Flat flat = new Flat(1, "Flat 1", new Coordinates(10, -100), ZonedDateTime.now(), 75, 3, true, 15.5, View.PARK, null);
        flatSet.add(flat);
        assertTrue(flatSet.removeIf(f -> f.getId() == 1));
        assertEquals(0, flatSet.size());
    }

    // Test 3: Очистка коллекции
    @Test
    void testClearCollection() {
        Flat flat = new Flat(1, "Flat 1", new Coordinates(10, -100), ZonedDateTime.now(), 75, 3, true, 15.5, View.PARK, null);
        flatSet.add(flat);
        flatSet.clear();
        assertTrue(flatSet.isEmpty());
    }

    // Test 4: Обновление элемента
    @Test
    void testUpdateFlat() {
        Flat flat = new Flat(1, "Flat 1", new Coordinates(10, -100), ZonedDateTime.now(), 75, 3, true, 15.5, View.PARK, null);
        flatSet.add(flat);

        Flat updatedFlat = new Flat(1, "Updated Flat", new Coordinates(20, -200), ZonedDateTime.now(), 90, 4, false, 10.0, View.STREET, null);
        flatSet.remove(flat);
        flatSet.add(updatedFlat);

        assertEquals(1, flatSet.size());
        assertTrue(flatSet.contains(updatedFlat));
    }

    // Test 5: add_if_greater
    @Test
    void testAddIfMax() {
        Flat flat1 = new Flat(1, "Flat 1", new Coordinates(10, -100), ZonedDateTime.now(), 75, 3, true, 15.5, View.PARK, null);
        Flat flat2 = new Flat(2, "Flat 2", new Coordinates(20, -200), ZonedDateTime.now(), 90, 4, false, 10.0, View.STREET, null);

        flatSet.add(flat1);
        flatSet.add(flat2);

        Flat newFlat = new Flat(3, "Flat 3", new Coordinates(30, -300), ZonedDateTime.now(), 100, 5, true, 20.0, View.BAD, null);
        if (newFlat.compareTo(flatSet.stream().max(Flat::compareTo).orElse(null)) > 0) {
            flatSet.add(newFlat);
        }

        assertEquals(3, flatSet.size());
        assertTrue(flatSet.contains(newFlat));
    }

    // Test 6: add_if_min
    @Test
    void testAddIfMin() {
        Flat flat1 = new Flat(1, "Flat 1", new Coordinates(10, -100), ZonedDateTime.now(), 75, 3, true, 15.5, View.PARK, null);
        Flat flat2 = new Flat(2, "Flat 2", new Coordinates(20, -200), ZonedDateTime.now(), 90, 4, false, 10.0, View.STREET, null);

        flatSet.add(flat1);
        flatSet.add(flat2);

        Flat newFlat = new Flat(0, "Flat 0", new Coordinates(0, 0), ZonedDateTime.now(), 50, 2, false, 5.0, View.NORMAL, null);
        if (newFlat.compareTo(flatSet.stream().min(Flat::compareTo).orElse(null)) < 0) {
            flatSet.add(newFlat);
        }

        assertEquals(3, flatSet.size());
        assertTrue(flatSet.contains(newFlat));
    }

    // Test 7: remove_if_greater
    @Test
    void testRemoveGreater() {
        Flat flat1 = new Flat(1, "Flat 1", new Coordinates(10, -100), ZonedDateTime.now(), 75, 3, true, 15.5, View.PARK, null);
        Flat flat2 = new Flat(2, "Flat 2", new Coordinates(20, -200), ZonedDateTime.now(), 90, 4, false, 10.0, View.STREET, null);
        Flat flat3 = new Flat(3, "Flat 3", new Coordinates(30, -300), ZonedDateTime.now(), 100, 5, true, 20.0, View.BAD, null);

        flatSet.add(flat1);
        flatSet.add(flat2);
        flatSet.add(flat3);

        Flat compareFlat = new Flat(2, "Flat 2", new Coordinates(20, -200), ZonedDateTime.now(), 90, 4, false, 10.0, View.STREET, null);
        flatSet.removeIf(f -> f.compareTo(compareFlat) > 0);

        assertEquals(2, flatSet.size());
        assertFalse(flatSet.contains(flat3));
    }

    // Test 8: Вывод уникальных домов
    @Test
    void testPrintUniqueHouses() {
        House house1 = new House("House A", 2005, 4);
        House house2 = new House("House B", 2010, 6);

        Flat flat1 = new Flat(1, "Flat 1", new Coordinates(10, -100), ZonedDateTime.now(), 75, 3, true, 15.5, View.PARK, house1);
        Flat flat2 = new Flat(2, "Flat 2", new Coordinates(20, -200), ZonedDateTime.now(), 90, 4, false, 10.0, View.STREET, house2);
        Flat flat3 = new Flat(3, "Flat 3", new Coordinates(30, -300), ZonedDateTime.now(), 100, 5, true, 20.0, View.BAD, house1);

        flatSet.add(flat1);
        flatSet.add(flat2);
        flatSet.add(flat3);

        HashSet<House> uniqueHouses = new HashSet<>();
        flatSet.forEach(f -> uniqueHouses.add(f.getHouse()));

        assertEquals(2, uniqueHouses.size());
        assertTrue(uniqueHouses.contains(house1));
        assertTrue(uniqueHouses.contains(house2));
    }

    // Test 9: Вывод кол-ва комнат в порядке возрастания
    @Test
    void testPrintNumberOfRoomsAscending() {
        Flat flat1 = new Flat(1, "Flat 1", new Coordinates(10, -100), ZonedDateTime.now(), 75, 3, true, 15.5, View.PARK, null);
        Flat flat2 = new Flat(2, "Flat 2", new Coordinates(20, -200), ZonedDateTime.now(), 90, 4, false, 10.0, View.STREET, null);
        Flat flat3 = new Flat(3, "Flat 3", new Coordinates(30, -300), ZonedDateTime.now(), 100, 5, true, 20.0, View.BAD, null);

        flatSet.add(flat1);
        flatSet.add(flat2);
        flatSet.add(flat3);

        long[] sortedRooms = flatSet.stream()
                .mapToLong(Flat::getNumberOfRooms)
                .sorted()
                .toArray();

        assertArrayEquals(new long[]{3, 4, 5}, sortedRooms);
    }

    // Test 10: Вывод домов в порядке убывания
    @Test
    void testPrintHouseDescending() {
        House house1 = new House("House A", 2005, 4);
        House house2 = new House("House B", 2010, 6);

        Flat flat1 = new Flat(1, "Flat 1", new Coordinates(10, -100), ZonedDateTime.now(), 75, 3, true, 15.5, View.PARK, house1);
        Flat flat2 = new Flat(2, "Flat 2", new Coordinates(20, -200), ZonedDateTime.now(), 90, 4, false, 10.0, View.STREET, house2);
        Flat flat3 = new Flat(3, "Flat 3", new Coordinates(30, -300), ZonedDateTime.now(), 100, 5, true, 20.0, View.BAD, house1);

        flatSet.add(flat1);
        flatSet.add(flat2);
        flatSet.add(flat3);

        House[] sortedHouses = flatSet.stream()
                .map(Flat::getHouse)
                .sorted(Comparator.reverseOrder())
                .toArray(House[]::new);

        assertArrayEquals(new House[]{house2, house1, house1}, sortedHouses);
    }
}