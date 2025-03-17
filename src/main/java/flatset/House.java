package flatset;

import java.util.Objects;
import java.util.Comparator;
/**
 * Класс House представляет дом с названием, годом постройки и количеством квартир на этаже.
 * Реализует интерфейс Comparable для сравнения объектов House по имени, году постройки и количеству квартир на этаже.
 * Переопределяет методы equals, hashCode и toString для корректной работы с коллекциями.
 */

public class House implements Comparable<House> {
    private String name;
    private int year;
    private int numberOfFlatsOnFloor;
    /**
     * Конструктор для создания объекта House с указанными параметрами.
     */

    public House(String name_p, int year_p, int numOfFlats_p)
    {
        this.name = name_p;
        this.year = year_p;
        this.numberOfFlatsOnFloor = numOfFlats_p;
    }
    /**
     * Конструктор по умолчанию.
     */
    public House() {}

    // геттеры и сеттеры
    public String getName() {return name;}


    /**
     * Устанавливает название дома.
     */
    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    /**
     * Устанавливает год постройки дома.
     */
    public void setYear(int year) {
        this.year = year;
    }

    public int getNumberOfFlatsOnFloor() {
        return numberOfFlatsOnFloor;
    }
    /**
     * Устанавливает количество квартир на этаже.
     */
    public void setNumberOfFlatsOnFloor(int numberOfFlatsOnFloor) {
        this.numberOfFlatsOnFloor = numberOfFlatsOnFloor;
    }
    /**
     * Возвращает строковое представление объекта House.
     */
    @Override
    public String toString() {
        return "House{" +
                "name='" + name + '\'' +
                ", year=" + year +
                ", numberOfFlatsOnFloor=" + numberOfFlatsOnFloor +
                '}';
    }
    /**
     * Сравнивает текущий объект House с другим объектом House.
     * Сравнение выполняется по названию, году постройки и количеству квартир на этаже.
     */
    @Override
    public int compareTo(House other) {
        return Comparator.comparing(House::getName)
                .thenComparingInt(House::getYear)
                .thenComparingInt(House::getNumberOfFlatsOnFloor)
                .compare(this, other);
    }
    /**
     * Проверяет, равен ли текущий объект House другому объекту.
     * Сравнение выполняется по названию, году постройки и количеству квартир на этаже.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        House house = (House) o;
        return year == house.year &&
                numberOfFlatsOnFloor == house.numberOfFlatsOnFloor &&
                Objects.equals(name, house.name);
    }
    /**
     * Возвращает хэш-код объекта House.
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, year, numberOfFlatsOnFloor);
    }
}
