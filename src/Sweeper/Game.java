package Sweeper;
/**
 * Класс Game - отвечает за управление всеми процессами игры
 * @autor Фролов Аркадий
 */
public class Game
{
    private Bomb bomb; //матрица где находится бомба
    private Flag flag; //флаги
    private GameStat state; //состояние победил или нет

    /**
     * Функция получения состония игры {@link Game#state}
     * @return возвращает название состояния
     */
    public GameStat getState()
    {
        return state;
    } //получение состояния игры

    /**
     * Конструктор - для получение данных перед началом игры
     * @param cols - столбец
     * @param rows - строка
     * @param bombs - бомбы
     * @see Game()#Game(int, int, int)
     */
    public Game (int cols, int rows, int bombs) //Конструкт. сюда передаем данные об игре
    {
        Ranges.setSize(new Coord(cols, rows));
        bomb = new Bomb(bombs);
        flag = new Flag();
    }

    /**
     * Функция запуска игры
     *
     */
    public void start () //Запуск игры
    {
        bomb.start();
        flag.start();
        state = GameStat.PLAYED;
    }

    /**
     * Функция получения картинок (Box) в координатах
     * @param coord - координата
     */
    public Box getBox (Coord coord) //получаем, какой Box в указанных координатах находится
    {
        if (flag.get(coord) == Box.opened)
            return bomb.get(coord);
        else
            return flag.get(coord);
    }

    /**
     * Функция отвечающаяя за нажатие левой клавиши мыши
     * @param coord
     */
    public void pressLeftButton (Coord coord) //метод нажатия левой кнопки
    {
        if (gameOver ()) return;
        openBox (coord);
        checkWinner();
    }

    /**
     * Функция отвечающаяя за проверку игры на победу
     */
    private void checkWinner ()
    {
        if (state == GameStat.PLAYED)
            if (flag.getCountClosedBoxes() == bomb.getTotalBombs())
                state = GameStat.WINNER;
    }

    /**
     * Функция устанавливает статус игры - победа {@link Game#state}
     */
    public void setEnd ()
    {
        state = GameStat.WINNER;
    }

    /**
     * Функция устанавливает статуст игры - проигрыш  {@link Game#state}
     */
	public void setBombed ()
    {
        state = GameStat.BOMBED;
    }

    /**
     * Функция отвечающаяя за открытие картинок (Box)
     * @param coord
     */
    private void openBox(Coord coord)
    {
        switch (flag.get(coord))
        {
            case opened : setOpenedToClosedBoxesAroundNumber(coord);return;
            case flaged : return;
            case closed :
                switch (bomb.get(coord))
                {
                    case zero: openBoxesAround (coord); return;
                    case bomb: openBombs (coord); return;
                    default: flag.setOpenedToBox(coord);  return;
                }

        }
    }
    /**
     * Функция отвечающаяя за открытие всех бомб
     * @param bombed
     */
    private void openBombs(Coord bombed)
    {
        state = GameStat.BOMBED;
        flag.setBombedToBox(bombed);
        for (Coord coord : Ranges.getAllCoords())
            if (bomb.get(coord) == Box.bomb)
                flag.setOpenedToClosedBombBox (coord);
            else
                flag.setNobombToFlagedSafeBox (coord);
    }
    /**
     * Функция открытия клеток вокруг текущей координаты если нажатие было на пустую клетку
     * @param coord - координата
     */
    private void openBoxesAround(Coord coord)
    {
        flag.setOpenedToBox(coord);  //открывает текущую
        for (Coord around : Ranges.getCoordsArround(coord)) //перебираем все клетку вокруг
            openBox(around); //вызываем опенбокс (открытие)
    }

    /**
     * Функция отвечающаяя за нажатие правой клавиши мыши
     * @param coord
     */
    public void pressRightButton(Coord coord)  //правая кнопка мыши
    {
        if (gameOver ()) return;
        flag.toggleFlagedToBox (coord);
    }

    /**
     * Функция конец игры
     * @return - проверка окончена ли игра
     */
    private boolean gameOver()
    {
        if (state == GameStat.PLAYED)
            return false;
        //start();
        return true;
    }

        //устанвка открытого бокс на закрытой бомбе
    private void setOpenedToClosedBoxesAroundNumber (Coord coord)
    {
        if (bomb.get(coord) != Box.bomb)
            if (flag.getCountOfFlagedBoxesAround(coord) == bomb.get(coord).getNumber())
                for (Coord around : Ranges.getCoordsArround(coord))
                    if (flag.get(around) == Box.closed)
                        openBox(around);
    }

}