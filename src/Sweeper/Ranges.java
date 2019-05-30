package Sweeper;
/**
 * Класс Ranges - отвечает за размеры поля и работу с координатами.
 * @autor Фролов Аркадий
 */

import java.util.ArrayList;
import java.util.Random;

public class Ranges
{
    private static Coord size; //размеры поля по x и y
    private static ArrayList<Coord> allCoords; //список всех координат (перебираем все координаты в списке)
    private static Random random = new Random(); //рандом

    /**
     * Функция устанавливающая размер поля {@link Game#}
     * @param _size
     */
    public static void setSize (Coord _size)  //установка размера
    {
        size = _size;
        allCoords = new ArrayList<Coord>();
        for (int y = 0; y < size.y; y++) //заполняем координаты
            for (int x = 0; x < size.x; x++)
                allCoords.add(new Coord(x,y));
    }
    /**
     * Функция получающая размер поля {@link Ranges#size}
     * @return - возвращает размер поля
     */
    public static Coord getSize()
    {
        return size;
    }

    /**
     * Функция получающая список всех координат {@link Ranges#allCoords}
     * @return - возвращает все координата
     */
    public static ArrayList<Coord> getAllCoords ()
    {
        return allCoords;
    } //получение списка всех координат

    /**
     * Функция проверяющая находится ли клетка в пределах поля
     * @param coord - координата
     * @return - провека координат
     */
    static boolean inRange (Coord coord) //проверяем какая-либо клетка находится в пределах нашего поля или нет (вспомогательная функци, которая помогает определить какая либо клетка находится в пределах нашего поля или нет)
    {
        return coord.x >= 0 && coord.x < size.x &&
                coord.y >= 0 && coord.y < size.y;
    }
    /**
     * Функция получающая случайные координты {@link Ranges#random}
     * @return - возвращает новую случайные координаты
     */
    static Coord getRandomCoord () { //получение случайной координаты
        return new Coord(random.nextInt(size.x), random.nextInt(size.y));
    }

    /**
     * Функция перебирает все клетки вокруг заданной (получение координат вокруг какой либо клетки) {@link Game#}
     * @param coord
     * @return - возращает список координат
     */
    static ArrayList<Coord> getCoordsArround (Coord coord) //Получение координат вокруг какой либо клетки
    {
        Coord around;
        ArrayList<Coord> list = new ArrayList<Coord>(); //список всех координат, которые будут вокруг
        for (int x = coord.x - 1; x <= coord.x + 1; x++)  //перебираем все перемененные начиная на 1 левее, заканчивая на 1 правее (по х)
            for (int y = coord.y - 1; y <= coord.y + 1; y++)
                if (inRange(around = new Coord(x, y))) //проверка есть ли такая координата
                    if (!around.equals(coord))
                        list.add(around);
        return list;
    }

}