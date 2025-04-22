package flatset;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.Objects;
/**
 * Класс, представляющий квартиру.
 * Содержит информацию о квартире, такую как идентификатор, название, координаты, дата создания,
 * площадь, количество комнат, статус новизны, время до метро на транспорте, вид из окна и дом.
 */
public class Flat implements Comparable<Flat>
{
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
     * Конструктор по умолчанию. Инициализирует дату создания текущей датой и временем.
     */

    public Flat()
    {
        this.creationDate = ZonedDateTime.now();
    };
    /**
     * Конструктор с параметрами. Инициализирует все поля класса.

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

    /**
     * Возвращает уникальный идентификатор квартиры.

     * @return Уникальный идентификатор квартиры.
     */
    public long getId()
    {
        return id;
    }
    /**
     * Устанавливает уникальный идентификатор квартиры.

     * @param id Уникальный идентификатор квартиры.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Возвращает название квартиры.

     * @return Название квартиры.
     */
    public String getName() {
        return name;
    }

    /**
     * Устанавливает название квартиры.

     * @param name Название квартиры.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Возвращает координаты квартиры.

     * @return Координаты квартиры.
     */

    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Устанавливает координаты квартиры.

     * @param coordinates Координаты квартиры.
     */

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Возвращает дату создания записи о квартире.

     * @return Дата создания записи о квартире.
     */
    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Устанавливает дату создания записи о квартире.

     * @param creationDate Дата создания записи о квартире.
     */
    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }
    /**
     * Возвращает площадь квартиры.

     * @return Площадь квартиры.
     */

    public long getArea() {
        return area;
    }
    /**
     * Устанавливает площадь квартиры.

     * @param area Площадь квартиры.
     */

    public void setArea(long area) {
        this.area = area;
    }
    /**
     * Возвращает количество комнат в квартире.

     * @return Количество комнат в квартире.
     */

    public long getNumberOfRooms() {
        return numberOfRooms;
    }
    /**
     * Устанавливает количество комнат в квартире.

     * @param numberOfRooms Количество комнат в квартире.
     */

    public void setNumberOfRooms(long numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }
    /**
     * Возвращает статус новизны квартиры.

     * @return Статус новизны квартиры.
     */

    public Boolean isNew() {
        return isNew;
    }
    /**
     * Устанавливает статус новизны квартиры.

     * @param isNew Статус новизны квартиры.
     */

    public void setNew(Boolean isNew) {
        this.isNew = isNew;
    }
    /**
     * Возвращает время до метро на транспорте.

     * @return Время до метро на транспорте.
     */

    public double getTimeToMetroByTransport() {
        return timeToMetroByTransport;
    }
    /**
     * Устанавливает время до метро на транспорте.

     * @param timeToMetroByTransport Время до метро на транспорте.
     */
    public void setTimeToMetroByTransport(double timeToMetroByTransport) {
        this.timeToMetroByTransport = timeToMetroByTransport;
    }
    /**
     * Возвращает вид из окна квартиры.

     * @return Вид из окна квартиры.
     */
    public View getView() {
        return view;
    }
    /**
     * Устанавливает вид из окна квартиры.

     * @param view Вид из окна квартиры.
     */
    public void setView(View view) {
        this.view = view;
    }
    /**
     * Возвращает дом, в котором находится квартира.

     * @return Дом, в котором находится квартира.
     */
    public House getHouse() {
        return house;
    }
    /**
     * Устанавливает дом, в котором находится квартира.

     * @param house Дом, в котором находится квартира.
     */
    public void setHouse(House house) {
        this.house = house;
    }
    /**
     * Возвращает строковое представление объекта квартиры.

     * @return Строковое представление объекта квартиры.
     */
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
                '}';
    }
    /**
     * Сравнивает объект квартиры с другим объектом на равенство.

     * @param o Объект для сравнения.
     * @return true, если объекты равны, иначе false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flat flat = (Flat) o; //преобразование класса для сравнения
        return id == flat.id &&
                area == flat.area &&
                numberOfRooms == flat.numberOfRooms &&
                Double.compare(flat.timeToMetroByTransport, timeToMetroByTransport) == 0 &&
                Objects.equals(name, flat.name) &&
                Objects.equals(coordinates, flat.coordinates) &&
                Objects.equals(creationDate, flat.creationDate) &&
                Objects.equals(isNew, flat.isNew) &&
                view == flat.view &&
                Objects.equals(house, flat.house);
    }
    /**
     * Возвращает хэш-код объекта квартиры.

     * @return Хэш-код объекта квартиры.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, area, numberOfRooms, isNew, timeToMetroByTransport, view, house);
    }
    /**
     * Сравнивает текущую квартиру с другой квартирой для упорядочивания.
     * Сравнение производится по идентификатору, названию, координатам, дате создания,
     * площади, количеству комнат, статусу новизны, времени до метро, виду из окна и дому.

     * @param other Другая квартира для сравнения.
     * @return Отрицательное число, если текущая квартира меньше другой,
     *         положительное число, если больше, и 0, если они равны.
     */
    @Override
    public int compareTo(Flat other)
    {

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
                .compare(this, other);
    }
}
