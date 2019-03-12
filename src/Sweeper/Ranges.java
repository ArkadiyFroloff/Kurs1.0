package Sweeper;

import java.util.ArrayList;
import java.util.Random;


public class Ranges {
    private static Coordinat size; //размер экрана
    private  static ArrayList<Coordinat> allCoordinat;
    private static Random random = new Random();


    static void setSize(Coordinat _size){

        size = _size;
        allCoordinat = new ArrayList<Coordinat>();
        for (int y = 0; y < size.y; y ++)
            for (int x = 0; x < size.x; x ++)
                allCoordinat.add(new Coordinat(x, y));
    }


    public static Coordinat getSize() {
        return size;
    }

    public static ArrayList<Coordinat> getAllCoordinat()  //получаем все коордиаты
    {
        return allCoordinat;
    }

    static boolean inRange (Coordinat coordinat)
    {
        return coordinat.x >=0 && coordinat.x < size.x &&
                coordinat.y >=0 &&coordinat.y < size.y;
    }

    static Coordinat getRandomCoordinat(){
return new Coordinat(random.nextInt(size.x),
                     random.nextInt(size.y));
    }

}
