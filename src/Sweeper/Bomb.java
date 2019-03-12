package Sweeper;

class Bomb {

private Matrix bombMap;
private int totalBombs;  // всего

    //принимаем количсетво бомб и сохраняе
    Bomb (int totalBombs)
    {
        this.totalBombs = totalBombs;
    }

    void start() {
        bombMap = new Matrix(Box.zero);
        for (int j = 0; j < totalBombs; j ++)
        placeBomb();
    }

    Box get (Coordinat coordinat)
    {
        return bombMap.get(coordinat);
    }
    //размещаем бомбы
    private void placeBomb()
    {
        Coordinat coordinat = Ranges.getRandomCoordinat();
        bombMap.set(coordinat, Box.bomb);

    }
}
