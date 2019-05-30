package Sweeper;
/**
 * Класс Coord - отвечает за хранение координат клетки и работу с ней.
 * @autor Фролов Аркадий
 */
public class Coord
{
    public int x;
    public int y;

    /**
     * Конструктор - для получения координат и их сохранения
     * @param x - "ширина"
     * @param y - "высота"
     * @see Coord#Coord(int, int)
     */
    public Coord (int x, int y) //Конструктор для получения координат и их сохранения (который получает 2 координаты и их сохраняет
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Функция для сравнения координат (совпадение)
     * @param o
     */
    //Функция для сравнения координат (совпадают или нет)
    @Override
    public boolean equals(Object o)
    {
        if (o instanceof Coord)  //если передали координату
        {
            Coord to = (Coord)o;
            return to.x == x && to.y == y; //проверяем на совпадение координаты
        }
        return super.equals(o);
    }
}