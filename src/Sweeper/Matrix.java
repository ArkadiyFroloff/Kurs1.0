package Sweeper;

 class Matrix {
 private Box [] [] matrix;

 Matrix(Box defaultBox)
 {
     matrix = new Box [Ranges.getSize().x][Ranges.getSize().y];
     for (Coordinat coordinat : Ranges.getAllCoordinat())
         matrix [coordinat.x] [coordinat.y] = defaultBox;
 }

 Box get (Coordinat coordinat)
 {
     if (Ranges.inRange (coordinat))  //проверяем находится ли координата в пределах нашего экрана
     return matrix [coordinat.x] [coordinat.y];
     return null;
 }

 void  set (Coordinat coordinat, Box box)
 {
     if (Ranges.inRange (coordinat))
     matrix [coordinat.x] [coordinat.y] = box;
 }
}
