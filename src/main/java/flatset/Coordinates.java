package flatset;

import java.util.Comparator;
import java.util.Objects;
/**
 * Класс, представляющий координаты.
 * Содержит информацию о координатах, таких как x и y.
 */
public class Coordinates implements Comparable<Coordinates>
{
    /**
     * Координата x. Не может быть null.
     */
    private int x; // Поле не может быть null
    /**
     * Координата y. Не может быть null, значение должно быть больше -318.
     */
    private int y; // Значение поля должно быть больше -318, Поле не может быть null
    /**
     * Конструктор с параметрами. Инициализирует координаты x и y.
     *
     * @param x Координата x.
     * @param y Координата y.
     */
    public Coordinates(int x, int y)
    {
        this.setX(x);
        this.setY(y);
    }
    /**
     * Возвращает координату x.
     *
     * @return Координата x.
     */
    // Геттеры и сеттеры
    public Integer getX() {
        return x;
    }
    /**
     * Устанавливает координату x.
     *
     * @param x Координата x.
     */

    public void setX(Integer x) {
        this.x = x;
    }
    /**
 * Возвращает координату y.
 *
 * @return Координата y.
 */

    public Integer getY() {
        return y;
    }
    /**
     * Устанавливает координату y.
     *
     * @param y Координата y
     * @throws IllegalArgumentException если значение y меньше или равно -318.
     */
    public void setY(Integer y)
    {
        if ( y <= -318)
        {
            throw new IllegalArgumentException("Coordinate y must be greater than -318.");
        }
        else
        {
            this.y = y;
        }
    }
    /**
     * Возвращает строковое представление объекта координат.
     *
     * @return Строковое представление объекта координат.
     */
    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
    /**
     * Сравнивает объект координат с другим объектом на равенство.
     *
     * @param o Объект для сравнения.
     * @return true, если объекты равны, иначе false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return Objects.equals(x, that.x) && Objects.equals(y, that.y);
    }
    /**
     * Возвращает хэш-код объекта координат.
     *
     * @return Хэш-код объекта координат.
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
    /**
     * Сравнивает текущие координаты с другими координатами для упорядочивания.
     * Сравнение производится сначала по координате x, затем по координате y.
     *
     * @param other Другие координаты для сравнения.
     * @return Отрицательное число, если текущие координаты меньше других,
     *         положительное число, если больше, и 0, если они равны.
     */
    @Override
    public int compareTo(Coordinates other) {

        return Comparator.comparing(Coordinates::getX, Comparator.nullsFirst(Integer::compareTo))
                .thenComparing(Coordinates::getY, Comparator.nullsFirst(Integer::compareTo))
                .compare(this, other);
    }
}