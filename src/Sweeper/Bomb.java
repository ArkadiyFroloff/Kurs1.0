package Sweeper;

/**
 * Класс Bomb - отвечает за работу с бомбами.
 * @autor Фролов Аркадий
 */
public class Bomb
{
    private Matrix bombMap; //матрица мссив с бомбами
    private int totalBombs; //общее количество бомб

    /**
     * Конструктор - принимает и сохраняет количество бомб (для установка нужного количества бомб в игре)
     * @param totalBombs - общее количетво бомб
     * @see Bomb#Bomb(int)
     */
    Bomb (int totalBombs)
    {
        this.totalBombs = totalBombs;
        fixBombsCount();
    }
    /**
     * Функция раставляющая бомбы на поле
     */
    void start()
    {
        bombMap = new Matrix(Box.zero);
        for (int j = 0; j < totalBombs; j++)  //Вызываем несколько раз для размещения нужного количества
            placeBomb ();
    }

    /**
     * Функция позволяющая возвращать, что находится в заданной координате {@link Bomb#bombMap}
     * @param coord - координата
     * @return - получаем
     */
    Box get (Coord coord)
    {
        return bombMap.get(coord);
    }

    //фиксируем количество бомб
    /**
     * Функция фиксирующая количество бомб на поле
     */
    private void fixBombsCount ()
    {
        //int maxBombs = Ranges.getSize().x * Ranges.getSize().y /2;
        int maxBombs = Ranges.getSize().x * Ranges.getSize().y;
        if (totalBombs > maxBombs)
            totalBombs = maxBombs;
    }

    /**
     * Функция для размещения бомб (расстановка бомб)
     */
    private void placeBomb ()
    {
        while (true)
        {
            Coord coord = Ranges.getRandomCoord(); //находим случайную координуту далее размещаем ее
            if (Box.bomb == bombMap.get(coord))  //перебираем все клетки и в каждую устанавливаем  (УБРАТЬ)
                continue;
            bombMap.set(new Coord(coord.x, coord.y), Box.bomb);
            incNumbersAroundBomb(coord);
            break;
        }

    }
    /**
     * Функция увелечения чисел вокруг бомб
     * @param coord - координата
     */
    private void incNumbersAroundBomb (Coord coord)
    {
        for (Coord around : Ranges.getCoordsArround(coord))
            if (Box.bomb != bombMap.get(around))
                bombMap.set(around, bombMap.get(around).getNextNumberBox());
    }

    /**
     * Функция получающая общее количество бомб, {@link Bomb#totalBombs}
     * @return - общее количество бомб
     */
    int getTotalBombs()
    {
        return totalBombs;
    }
}