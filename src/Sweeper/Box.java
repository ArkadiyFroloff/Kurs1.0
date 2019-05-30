package Sweeper;

public enum Box {
    zero,//0
    num1,
    num2,
    num3,
    num4,
    num5,
    num6,
    num7,
    num8,
    bomb,//9
    opened,//10 space
    closed,//11
    flaged,//12
    bombed,
    nobomb;

    public Object image;
    /**
     * Функция возвращающая следующее число, если нет бомбы (переход из одного значения в другое)
     * @return возвраает последующий номер
     */
    Box getNextNumberBox ()
    {
        return Box.values() [this.ordinal() + 1];
    }

    /**
     * Функция позволяющая узнать, находится ли цифра и если да, то какая, и сколько клеток вокруг
     * @return возвраает цифры
     */
    int getNumber ()
    {
        return this.ordinal();
    }
}


