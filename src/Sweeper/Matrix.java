package Sweeper;
/**
 * Класс Matrix - отвечает за хранение элементов в клетках
 * @autor Фролов Аркадий
 */
public class Matrix
{
    private Box [] [] matrix; //массив где хранятся все box
    /**
     * Конструктор - для заполнения поля указанными картинками (box)
     * @param defaultBox
     * @see  Matrix#Matrix(Box)
     */
    Matrix (Box defaultBox)  //хаполняем все поле указанным полем box
    {
        matrix = new Box [Ranges.getSize().x] [Ranges.getSize().y];
        for (Coord coord : Ranges.getAllCoords())  //Заполняем значениями
            matrix [coord.x] [coord.y] = defaultBox;
    }
    /**
     * Функция позволяющая получать, что находится в заданных координатах
     * @param coord - координата
     * @return - возвращаем, что находится в координате
     */
    Box get (Coord coord)  //получение что находится
    {
        if (Ranges.inRange (coord))  //проверяем находится ли координата в пределах нашего экрана
            return matrix [coord.x] [coord.y];
        return null;
    }
    /**
     * Функция позволяющая устанавливать нужное значение в указанную клетку
     * @param coord - координата
     * @param box - картинка
     */
    void set (Coord coord, Box box)
    {
        if (Ranges.inRange (coord))
            matrix [coord.x] [coord.y] = box;
    }

}