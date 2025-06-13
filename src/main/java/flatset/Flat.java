package flatset;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.Objects;

/**
 * Класс, представляющий квартиру.
 * Содержит информацию о квартире, такую как идентификатор, название, координаты, дата создания,
 * площадь, количество комнат, статус новизны, время до метро на транспорте, вид из окна и дом.
 */
public class Flat implements Comparable<Flat> {
    /**
     * Уникальный идентификатор квартиры. Значение должно быть больше 0.
     */
    private long id; // Значение поля должно быть больше 0, уникальное, генерируется автоматически
    /**
     * Название квартиры. Не может быть null или пустой строкой.
     */
    private String name; // Поле не может быть null, строка не может быть пустой
    /**
     * Координаты квартиры. Не может быть null.
     */
    private Coordinates coordinates; // Поле не может быть null
    /**
     * Дата создания записи о квартире. Не может быть null, генерируется автоматически.
     */
    private ZonedDateTime creationDate; // Поле не может быть null, генерируется автоматически
    /**
     * Площадь квартиры. Значение должно быть больше 0.
     */
    private long area; // Значение поля должно быть больше 0
    /**
     * Количество комнат в квартире. Значение должно быть больше 0.
     */
    private long numberOfRooms; // Значение поля должно быть больше 0
    /**
     * Статус новизны квартиры. Может быть null.
     */
    private Boolean isNew; // Поле может быть null
    /**
     * Время до метро на транспорте. Значение должно быть больше 0.
     */
    private double timeToMetroByTransport; // Значение поля должно быть больше 0
    /**
     * Вид из окна квартиры. Не может быть null.
     */
    private View view; // Поле не может быть null
    /**
     * Дом, в котором находится квартира. Может быть null.
     */
    private House house; // Поле может быть null
    /**
     * Идентификатор владельца квартиры (пользователя). Не может быть null.
     */
    private Integer ownerId; // Поле не может быть null

    /**
     * Конструктор по умолчанию. Инициализирует дату создания текущей датой и временем.
     */
    public Flat() {
        this.creationDate = ZonedDateTime.now();
    }

    /**
     * Конструктор с параметрами. Инициализирует все поля класса.
     *
     * @param id Уникальный идентификатор квартиры.
     * @param name Название квартиры.
     * @param coordinates Координаты квартиры.
     * @param creationDate Дата создания записи о квартире.
     * @param area Площадь квартиры.
     * @param numberOfRooms Количество комнат в квартире.
     * @param isNew Статус новизны квартиры.
     * @param timeToMetroByTransport Время до метро на транспорте.
     * @param view Вид из окна квартиры.
     * @param house Дом, в котором находится квартира.
     */
    public Flat(long id, String name, Coordinates coordinates, ZonedDateTime creationDate, long area,
                long numberOfRooms, boolean isNew, double timeToMetroByTransport, View view, House house) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.area = area;
        this.numberOfRooms = numberOfRooms;
        this.isNew = isNew;
        this.timeToMetroByTransport = timeToMetroByTransport;
        this.view = view;
        this.house = house;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public long getArea() {
        return area;
    }

    public void setArea(long area) {
        this.area = area;
    }

    public long getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(long numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public Boolean isNew() {
        return isNew;
    }

    public void setNew(Boolean isNew) {
        this.isNew = isNew;
    }

    public double getTimeToMetroByTransport() {
        return timeToMetroByTransport;
    }

    public void setTimeToMetroByTransport(double timeToMetroByTransport) {
        this.timeToMetroByTransport = timeToMetroByTransport;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    /**
     * Возвращает идентификатор владельца квартиры.
     *
     * @return Идентификатор владельца квартиры.
     */
    public Integer getOwnerId() {
        return ownerId;
    }

    /**
     * Устанавливает идентификатор владельца квартиры.
     *
     * @param ownerId Идентификатор владельца квартиры.
     */
    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public String toString() {
        return "Flat{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", area=" + area +
                ", numberOfRooms=" + numberOfRooms +
                ", isNew=" + isNew +
                ", timeToMetroByTransport=" + timeToMetroByTransport +
                ", view=" + view +
                ", house=" + house +
                ", ownerId=" + ownerId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flat flat = (Flat) o;
        return id == flat.id &&
                area == flat.area &&
                numberOfRooms == flat.numberOfRooms &&
                Double.compare(flat.timeToMetroByTransport, timeToMetroByTransport) == 0 &&
                Objects.equals(name, flat.name) &&
                Objects.equals(coordinates, flat.coordinates) &&
                Objects.equals(creationDate, flat.creationDate) &&
                Objects.equals(isNew, flat.isNew) &&
                view == flat.view &&
                Objects.equals(house, flat.house) &&
                Objects.equals(ownerId, flat.ownerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, area, numberOfRooms,
                isNew, timeToMetroByTransport, view, house, ownerId);
    }

    @Override
    public int compareTo(Flat other) {
        return Comparator.comparingLong(Flat::getId)
                .thenComparing(Flat::getName)
                .thenComparing(Flat::getCoordinates, Comparator.nullsFirst(Coordinates::compareTo))
                .thenComparing(Flat::getCreationDate, Comparator.nullsFirst(ZonedDateTime::compareTo))
                .thenComparingLong(Flat::getArea)
                .thenComparingLong(Flat::getNumberOfRooms)
                .thenComparing(Flat::isNew, Comparator.nullsFirst(Boolean::compareTo))
                .thenComparingDouble(Flat::getTimeToMetroByTransport)
                .thenComparing(Flat::getView, Comparator.nullsFirst(View::compareTo))
                .thenComparing(Flat::getHouse, Comparator.nullsFirst(House::compareTo))
                .thenComparing(Flat::getOwnerId, Comparator.nullsFirst(Integer::compareTo))
                .compare(this, other);
    }
}