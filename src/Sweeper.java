import javax.swing.*;
import java.awt.*;
import Sweeper.Box;
import Sweeper.Coordinat;
import Sweeper.Ranges;
import Sweeper.Game;


public class Sweeper extends JFrame
{
    private Game game;

    private JPanel panel;
    private final  int COLS = 9;  //размер столбцов
    private final int ROWS = 9;   //размер строк
    private  final int BOMBS = 10; //колво бомб
    private  final int IMAGE_SIZE = 50; //размер картинки 50х50

    public static void main(String[] args)
    {
        new Sweeper();
    }

    private  Sweeper()
    {
        game = new Game (COLS, ROWS, BOMBS);
        game.start();
        setImages();
        initPanel();
        initFrame();
    }

    private void initPanel (){
        panel = new JPanel() // при инициализации выводим картинки
        {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (Coordinat coordinat : Ranges.getAllCoordinat())
                    g.drawImage((Image) game.getBox(coordinat).image,
                            coordinat.x * IMAGE_SIZE, coordinat.y * IMAGE_SIZE, this);
            }
        };

        panel.setPreferredSize(new Dimension(
                Ranges.getSize().x * IMAGE_SIZE,
                Ranges.getSize().y * IMAGE_SIZE));   //задаем размер панели,поля
        add (panel);
    }

        private void initFrame()  //подготовка заготовок
        {

            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //для закрытия программы чтобы не висела в фоне
            setTitle("Java Sweeper");
            setLocationRelativeTo(null);
            setResizable(false);  // - нельзя изменять размер окна
            setVisible(true);
            setIconImage(getImage("icon"));
            pack();

        }

        private void setImages()  //установка всех картинок
        {
            for (Box box: Box.values())
                box.image = getImage(box.name().toLowerCase());
        }

        private  Image getImage (String name) //получение картинок
        {
            String filename = "img/" + name + ".png";
            ImageIcon icon = new ImageIcon (getClass().getResource(filename));
            return icon.getImage();
        }

}
