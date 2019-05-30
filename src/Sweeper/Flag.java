package Sweeper;
/**
 * Класс Flag - отвечает за установку флагов, а также за верхний и нижний слой игры
 * @autor Фролов Аркадий
 */
public class Flag
{
    private Matrix flagMap; //матрица (масс) флагов
    private int countOfclosedBoxes; //количество закрытых боксов

    /**
     * Функция запуска поля с закрытыми ячейками
     */
    void start ()
    {
        flagMap = new Matrix(Box.closed);
        countOfclosedBoxes = Ranges.getSize().x * Ranges.getSize().y;
    }
    //получение той или иной картинки
    Box get (Coord coord)
    {
        return flagMap.get(coord);
    }

    /**
     * Функция установки открытого бокса (картинки)
     * @param coord - координата
     */
    public void setOpenedToBox(Coord coord) //установка открытого флага
    {
        flagMap.set(coord, Box.opened);
        countOfclosedBoxes--;
    }
    /**
     * Функция позволяющая переключить флаги (поставить, убрать флаг)
     * @param coord - координата
     */
    void toggleFlagedToBox (Coord coord)
    {
        switch (flagMap.get(coord))
        {
            case flaged : setClosedToBox (coord); break;
            case closed : setFlagedToBomb(coord); break;
        }
    }

    private void setClosedToBox(Coord coord)
    {
        flagMap.set(coord, Box.closed);
    }

    /**
     * Функция открытия (установки) флага
     * @param coord - координата
     */
    public void setFlagedToBomb(Coord coord)
    {
        flagMap.set(coord, Box.flaged);
    }

    /**
     * Функция подсчета сколько всего закрытых клеток {@link Flag#countOfclosedBoxes}
     * @return возврат количества
     */
    int getCountClosedBoxes()
    {
        return countOfclosedBoxes;
    }

    /**
     * Функция установки взорванной бомбы
     * @param coord - координата
     */
    void setBombedToBox(Coord coord)
    {
        flagMap.set(coord, Box.bombed);
    } //установка взовранной мины, когда проиграл

    /**
     * Функция открывающая не закрытые бомбовые боксы
     * @param coord - координата
     */
    void setOpenedToClosedBombBox(Coord coord)
    {
        if (flagMap.get(coord) == Box.closed)
            flagMap.set(coord, Box.opened);
    }

    /**
     * Функция установки спецаильной картинки если флаг был поставлен не правильно
     * @param coord - координата
     */
    void setNobombToFlagedSafeBox(Coord coord)
    {
        if (flagMap.get(coord) == Box.flaged)
            flagMap.set(coord, Box.nobomb);
    }

    /**
     * Функция открытия незакрытых бомб, помеченных бомб (при поражении)
     * @param coord - координата
     * @return - возврат количетсва
     */
    int getCountOfFlagedBoxesAround (Coord coord) //открыть незакрытые бомбы
    {
        int count = 0;
        for (Coord around : Ranges.getCoordsArround(coord))
            if (flagMap.get(around) == Box.flaged)
                count++;
        return count;
    }
}