import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.JButton;

import java.util.prefs.*;


import Sweeper.Box;
import Sweeper.Coord;
import Sweeper.Game;
import Sweeper.Ranges;
import Sweeper.GameStat;
import Sweeper.*;
/**
 * Класс Matrix - отвечает за получения изображений для формы
 * @autor Фролов Аркадий
 */
// Класс для получения изображений для формы
class SkinImages{
    private String folder = "default";
    private Object owner;
	boolean isExternal = false;
	int externalId = -1;
	String externalNames[] = null;
	HashMap<String, Integer> namesMap;
    // При создании передается объект класса игры и название папки с изображениями
    public SkinImages(Object own, String sk) {
        owner = own;
        if(sk != null)
            folder = sk;
		switch(folder){
			case "default":
			case "skin1":
				isExternal = false;
				break;
			default:
				isExternal = true;
				externalId = SkinManager.findSkinName(folder);
				externalNames = SkinManager.loadSkin(folder);
				break;
		}
		namesMap = SkinManager.getNamesHashMap();		
    }

	/**
	 * Функция для получения картинок
	 * @param name - название скина
	 * @return - возврат картинок
	 */
    public Image getImage (String name){
        String filename = folder + "/" + name + ".png";
        if (isExternal == false){
			ImageIcon icon = new ImageIcon(owner.getClass().getResource(filename)); //использование ресурсов (подключить папку с ресурсами)
			return icon.getImage();
		}else{
			int id = namesMap.get(name).intValue();
			filename = externalNames[id];
			return SkinManager.loadImage(filename);
		}
    }
}

// основная игра
public class Sweeper extends JFrame
{
    private Game game = null;
	GameEngine infEngine = null;
	Cell[][] visualCells;
	String elapsedTime = null;
	int timeLimit=-1;//-1: время без лимита
	private Timer timer;
    private long startTime;
	private JPanel buttonsPanel;
	int debugCellMode = 0;

    private JPanel panel;
    private JLabel label;

    private int COLS; // столбцы
    private int ROWS; // строки
    private int BOMBS; // количество бомб
    private final int IMAGE_SIZE = 30; // размер картинки одинаковый по x и по y
    // путь к папке с изображениями
    SkinImages skin;

    // для хранения записи рекордов, формат имя - число
    HashMap<String, Integer> states = new HashMap<>();

	/**
	 * Функция читает список рекордов из фалйа
	 */
    // функция читает список рекордов из файла
	@SuppressWarnings("unchecked")
    private void DeserializeRecords()
    {
        try
        {
            FileInputStream fis = new FileInputStream("records.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
			Object obj = ois.readObject();
			if (obj instanceof HashMap){
				HashMap map = (HashMap) obj;

				states = (HashMap<String, Integer>) map;
			}

			ois.close();
            fis.close();
        }
        catch(IOException ioe)
        {

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
	/**
	 * Функция записывает рекорды в файл
	 */
    // записываем список рккодов в файл
    private void SerializeRecords()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream("records.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(states);
            oos.close();
            fos.close();
        }catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
	/**
	 * Функция задающая лимит для таймера {@link Sweeper#timeLimit}
	 * @param timeLimit - ограничение по времени
	 */
	public void setTimeLimit(int timeLimit){
		this.timeLimit = timeLimit;
	}
	/**
	 * Функция для конвертирования словаря с записями рекордом в строку для вывода
	 * @param map - количество игроков в статистике
	 * @return - возвращает все имена и количество очков
	 */
    // конвертирует словарь с записями рекордов в строку для вывода
    public String convertWithStream(Map<String, Integer> map) {
        String mapAsString = map.keySet().stream()
                .map(key -> key + "                                                    " + map.get(key))
                .collect(Collectors.joining("\n", "", ""));
        return mapAsString;
    }
	/**
	 * Функция для запуска приложения
	 */
    public static void main(String[] args)
    {
		int w = 9, h=9;
		int bombs=10;
        if (args.length >= 2){
			w=Integer.parseInt(args[0]);
			h= Integer.parseInt(args[1]);
			if (args.length > 2) bombs = Integer.parseInt(args[2]);
			new Sweeper(w, h, bombs, "default", null, true);

		}else{
			new GameModeDialog();
			//new Sweeper(9, 9, 10, "default", true);
		}
    }

	/**
	 * Конструктор - требуется передать ширину и высоту поля, число бомб, и путь к папке с изображениями
	 * @param name - название
	 * @param bombs - количество бомб
	 * @param cols - количество столбцовэ
	 * @param rows - количество строк
	 * @param fold - скин
	 * @see  Sweeper#Sweeper(int, int, int, String, String, boolean)
	 */
    // конструктор класса Sweeper, требуется передать ширину и высоту поля, число бомб, и путь к папке с изображениями
    public Sweeper (int cols, int rows, int bombs, String fold, String name, boolean infinityMap)
    {
        if (cols==0 && rows ==0) {
            JOptionPane.showMessageDialog(null, "Такой размер поля недопустим в игре");
            cols=1;
            rows=1;
        }
		if (cols<1) {
		JOptionPane.showMessageDialog(null, "Такой размер поля недопустим в игре");
		cols=1;
		rows=1;
	}
		if (rows<1) {
			JOptionPane.showMessageDialog(null, "Такой размер поля недопустим в игре");
			cols=1;
			rows=1;
		}
        if (cols >100) {
        JOptionPane.showMessageDialog(null, "Такой размер поля недопустим в игре");
        cols=99;
        rows=99;
    }
        if (rows >100) {
            JOptionPane.showMessageDialog(null, "Такой размер поля недопустим в игре");
            cols=99;
            rows=99;
        }
        // в начале игры предлагаем ввести имя
        String s = null;
		if(name ==null){
			if (Records.isFirst) {
				name = JOptionPane.showInputDialog("Введите имя:");
			}else{
				name = Records.gamer;
			}
		}
        s = name;
			//JOptionPane.showInputDialog("Введите имя:");
            // удаляем пробелы
            if(s != null && s.length() > 0){
                s = s.trim();
                Records.gamer = s;
            } else {
            	// если имя не введено, то считаем, что имя игрока "Анон"
                Records.gamer = "Анон";
            }
            Records.isFirst = false;

        // считываем список рекордов
        DeserializeRecords();

        // если еще нет такого игрока то делаем запись
        if(!states.containsKey(Records.gamer)){
            states.put(Records.gamer, 0);
        }

        skin = new SkinImages(this, fold);
        COLS = cols;
        ROWS = rows;
        BOMBS = bombs;

		if (infinityMap == false){
			game = new Game(COLS, ROWS, BOMBS);
			game.start();

		}else{
			Ranges.setSize(new Coord(cols, rows));
			visualCells = new Cell[cols][rows];
			infEngine = new GameEngine(cols, rows);
			infEngine.start();
			infEngine.map.setMinesPercent((bombs*100)/(cols*rows));
		}
        setImages();
        initLabel();
        initPanel();
        initFrame();
        initJmenuBar();
    }
	/**
	 * Функция начала игры (запись в статистику, установка подписей, установка таймера)
	 */
	public void startGame(){
			SerializeRecords();
            initLabel ();
			if (infEngine == null)
			{
				game.start ();
            }else{
				infEngine.start();
			}

			panel.repaint();
			if (timeLimit > 0){
				startTimer();
			}
	}

	/**
	 * Функция создания меню на игровом поле
	 */
    // тут создается меню
    private void initJmenuBar() {

        JMenuBar menuBar = new JMenuBar();
        JMenu menu1 = new JMenu("Файл");

        JMenuItem newGameItem = new JMenuItem("Новая игра");
        newGameItem.addActionListener(ev -> {
           startGame();
        });
        menu1.add(newGameItem);

		JMenuItem modOptionsItem = new JMenuItem("Изменить настройки...");
		menu1.add(modOptionsItem);
		modOptionsItem.addActionListener( e -> {
			new GameModeDialog();
			dispose();
		});

        JMenuItem exitItem = new JMenuItem("Выход");
        exitItem.addActionListener(ev -> System.exit(0));
        menu1.add(exitItem);

        menuBar.add(menu1);

        JMenu menu3 = new JMenu("Рекоды");
        JMenuItem recordItem = new JMenuItem("Показать список рекодсменов");
        recordItem.addActionListener(ev -> {


            // выводим список рекордсменов
            JOptionPane.showMessageDialog(null,
                    convertWithStream(states),
                    "Список рекордсменов",
                    JOptionPane.PLAIN_MESSAGE);
        });
        menu3.add(recordItem);
        menuBar.add(menu3);

        setJMenuBar(menuBar);
        setVisible(true);
    }

	/**
	 * Функция вывода подписей к игре
	 */
    private void initLabel ()
    {
        if(label == null){
            label = new JLabel("Найди все бомбы!!!");
            add (label, BorderLayout.SOUTH);
        }
        else {
            label.setText("Найди все бомбы!!!");
        }
    }
	/**
	 * Функция получающая id картинок {@link Game#}
	 * @param id
	 * @return - возвращает картинки
	 */
	public Image getImageByCellId(int id){
		//Image image = null;
		switch(id){
			case Cell.CLOSED_ID:
				return (Image)Box.closed.image;
			case Cell.FLAG_ID:
				return (Image)Box.flaged.image;
			case Cell.MINE_ID:
				return (Image)Box.bomb.image;
			case Cell.BOMBED_ID:
				return (Image)Box.bombed.image;
			case Cell.SPACE_ID:
				return (Image)Box.opened.image;
			case 0:
				return (Image)Box.zero.image;
			case 1:
				return (Image)Box.num1.image;
			case 2:
				return (Image)Box.num2.image;
			case 3:
				return (Image)Box.num3.image;
			case 4:
				return (Image)Box.num4.image;
			case 5:
				return (Image)Box.num5.image;
			case 6:
				return (Image)Box.num6.image;
			case 7:
				return (Image)Box.num7.image;
			case 8:
				return (Image)Box.num8.image;
		}
		return null;
	}
	/**
	 * УБРАТЬ ИЗ КОДА
	 */
	void paintDebug1(Graphics g, int gridW, int gridH)
	{
		int shiftX = 0;
		int shiftY = 0;
		for (int x=0; x < gridW; x++){
			for (int y=0;y < gridH; y++){
				Cell cell = visualCells[x][y];
				int id = cell.getId();
				g.drawString("" + id, shiftX + x * IMAGE_SIZE, shiftY + y * IMAGE_SIZE +20);
			}
		}
	}
	/**
	 * Создания игровой панели
	 */
    private JPanel createCellsPanel(){

		//JPanel panel =
		panel = new JPanel() // при инициализации выводим картинки
        {
            @Override
            protected void paintComponent(Graphics g)
            {

                super.paintComponent(g);
				if (infEngine == null)
				{
					for (Coord coord : Ranges.getAllCoords())
						//g.drawImage((Image) game.getBox(coord).image, coord.x * IMAGE_SIZE, coord.y * IMAGE_SIZE, this); //приведение типа к Image
						g.drawImage((Image) game.getBox(coord).image, coord.x * IMAGE_SIZE, coord.y * IMAGE_SIZE, IMAGE_SIZE, IMAGE_SIZE, this); //приведение типа к Image
                }else{
					int gridW, gridH;
					gridW = infEngine.map.getGridWidth();
					gridH = infEngine.map.getGridHeight();
							int shiftX = 0;
							int shiftY = 0;
					boolean visibleAll = getGameState() == GameStat.BOMBED;
					if (infEngine.map.refreshVisualWindow(visualCells, visibleAll) == false) return;
					if (debugCellMode == 1){
						paintDebug1(g, gridW, gridH);
						return;
					}
					for (int x=0; x < gridW; x++){
						for (int y=0;y < gridH; y++){
							Cell cell = visualCells[x][y];
							int imgId = cell.getImageId(infEngine.map);
							Image image = getImageByCellId(imgId);

							g.drawImage(image, shiftX + x * IMAGE_SIZE, shiftY + y * IMAGE_SIZE, IMAGE_SIZE, IMAGE_SIZE, this);
						}
					}
					if (debugCellMode == 2){
						paintDebug1(g, gridW, gridH);
					}
				}
            }
        };


        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX() / IMAGE_SIZE;
                int y = e.getY() / IMAGE_SIZE;
                Coord coord = new Coord(x, y);
				int buttonId = e.getButton();
                do {
					//if (GameStat.PLAYED != getGameState()) return;
					if ( buttonId == MouseEvent.BUTTON1){ // левая кнопка мыши
						if (GameStat.PLAYED != getGameState()) break;
						if (infEngine == null){

							game.pressLeftButton (coord);
						}else{
							if (debugCellMode > 0){
								Cell cell = infEngine.map.getRelativeCell(x, y);
								if (debugCellMode == 2) infEngine.openCell(x, y);
							}else{
								infEngine.openCell(x, y);
							}
						}
						break;
					}
					if (e.getButton() == MouseEvent.BUTTON3){ // правая кнока мыши
						if (GameStat.PLAYED != getGameState()) break;
						if (infEngine == null){
							game.pressRightButton (coord);
						}else{
							infEngine.toggleFlag(x, y);
						}
						break;
					}
				}while(false);
                // если победа, то добавляем нужное число очков игроку и сохраняем в файл
				updateGameStateInfo();
                panel.repaint(); // после каждого действия мыши перерисовываем панель игры
            }
        });

        panel.setPreferredSize(new Dimension(Ranges.getSize().x * IMAGE_SIZE, Ranges.getSize().y * IMAGE_SIZE + 20));

		return panel;
	}

	/**
	 * Перемещение по полю (кнопки и скроллбар)
	 */
	void addArrowButton()//JPanel cellsPanel)
	{
	    //panel = cellsPanel;
		buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BorderLayout());
		JButton button1;

		button1 = new JButton("Влево");
		button1.addActionListener(ev -> {
			infEngine.scroll(Direction.LEFT);
			panel.repaint();
		});
		buttonsPanel.add(button1, BorderLayout.WEST);

		button1 = new JButton("Вправо");
		button1.addActionListener(ev -> {
			infEngine.scroll(Direction.RIGHT);
			panel.repaint();
		});
		buttonsPanel.add(button1, BorderLayout.EAST);

		button1 = new JButton("Вверх");
		button1.addActionListener(ev -> {
			infEngine.scroll(Direction.TOP);
			panel.repaint();
		});
		buttonsPanel.add(button1, BorderLayout.NORTH);

		button1 = new JButton("Вниз");
		button1.addActionListener(ev -> {
			infEngine.scroll(Direction.BOTTOM);
			panel.repaint();
		});
		buttonsPanel.add(button1, BorderLayout.SOUTH);

		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int screenWidth = gd.getDisplayMode().getWidth();
		int screenHeight = gd.getDisplayMode().getHeight();
		int needWidth, needHeight;
		needWidth = Ranges.getSize().x * IMAGE_SIZE + 160;
		needHeight = Ranges.getSize().y * IMAGE_SIZE + 160;

		if (needWidth > screenWidth || needHeight > screenHeight)
		{

			if (infEngine == null){
				JScrollPane pane = new JScrollPane(panel);
				pane.setPreferredSize(new Dimension(screenWidth -160, screenHeight-160));
				add (pane);
			}
			else{
				buttonsPanel.add(panel, BorderLayout.CENTER);
				JScrollPane pane = new JScrollPane(buttonsPanel);
				pane.setPreferredSize(new Dimension(screenWidth -160, screenHeight-160));
				add(pane);
			}
		}else{
			//panel = cellsPanel;
			if (infEngine == null){
				add (panel);
			}
			else{
				buttonsPanel.add(panel, BorderLayout.CENTER);
				add(buttonsPanel);
			}
		}


		//
	}

	private void initPanel ()
	{
		createCellsPanel();
		addArrowButton();//cellsPanel);
	}

	/**
	 * Функция для расчета статистики
	 */
	void updateGameStateInfo(){
			GameStat state = getGameState();
                if( state == GameStat.WINNER){
                    int n = 0;
                    if(COLS == 9 && ROWS ==9 && BOMBS ==10)
                        n = 1;
                    else if(COLS == 16 && ROWS ==16 && BOMBS==40)
                        n = 3;
                    else if(COLS == 30 && ROWS==16 && BOMBS==99)
                        n = 5;
                    else if(COLS == 8 && ROWS==8 && BOMBS==1)
                        n = 1;
                    int score = states.get(Records.gamer);
                    states.replace(Records.gamer, score + n);
                    SerializeRecords();
                }else{
					if (state == state.BOMBED){

					}
				}
                String msg = getMessage ();
				if (elapsedTime != null && elapsedTime.length() > 0)
				{
					msg += " | " +elapsedTime;
				}
				label.setText(msg);
	}

	/**
	 * Функция получающая состояние игры {@link Game#}
	 * @return - возвращает состояние
	 */
	public GameStat getGameState(){
		GameStat gameState;
		if (infEngine == null){
			gameState = game.getState();
		}else{
			gameState = infEngine.getGameState();
		}
		return gameState;
	}

	public void setBombed(){
		if (infEngine == null){
			game.setBombed();
		}else{
			infEngine.setBombed();
		}
		panel.repaint();
	}

	/**
	 * функция вывода состояния игры в сообщении
	 */
    private String getMessage()
    {
        switch (getGameState())
        {
            case PLAYED: return "Еще есть бомбы";
            case BOMBED:return "Проиграл";
            case WINNER: return "Победа!!!";
            default: return "Welcome";
        }
    }
	/**
	 * функция для инициализации Frame (установка названия для игры, отображение иконки и т.д)
	 */
    private void initFrame ()
    {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Сапер");
        setResizable(false);
        setVisible(true);
        pack(); //метод из класса JFrame устанавливает размер окна достаточный для отображения
        setIconImage(skin.getImage("icon"));
        setLocationRelativeTo(null);
    }

    private void setImages ()
    {
        for (Box box : Box.values())
            box.image = skin.getImage(box.name().toLowerCase());
    }

	    // функция таймера
    private final Timer getTimer() {
        if (timer == null) {
            int delay = 1000; // milliseconds
            ActionListener taskPerformer = new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    taskPerformed();
                }
            };
            //
            timer = new Timer(delay, taskPerformer);
            timer.setInitialDelay(0);
        }
        return timer;
    }
	void onGameOver(){

	}
	void stop(boolean win){
        if (infEngine == null){
			if (win){
				game.setEnd();
			}else{
				game.setBombed();
			}
		}else{
				infEngine.stop(win);
		   }
		updateGameStateInfo();
        panel.repaint();
	}

    // функция вызывается при каждом тике таймера
    private final void taskPerformed() {
        GameStat stat = getGameState();
		if (stat != GameStat.PLAYED){
			stopTimer();
			return;
		}
		long time = System.currentTimeMillis() - startTime; // milliseconds
	   int elapsed = timeLimit - (int)time/1000;
	   if (elapsed < 0) elapsed = 0;
	   elapsedTime = String.valueOf(elapsed);
	   label.setText(elapsedTime);
       // если прошло больше 20000 миллисекунд, то останавливаем игру
       if(time > timeLimit*1000)
		{
			stop(false);
		   String s = "Время вышло";
		   elapsedTime = s;
           //label.setText(s);
		   //panel.repaint();
           stopTimer();
       }
    }
	/**
	 * установка таймера
	 */
    public final synchronized void startTimer() {
        startTime = System.currentTimeMillis();
        getTimer().start();
    }

	/**
	 * остановка таймера
	 */
    public final synchronized void stopTimer() {
        getTimer().stop();
		timer = null;
		//elapsedTime = "";
    }
}

/**
 * Класс Matrix - отвечает за хранение имени игрока
 * @autor Фролов Аркадий
 */
// класс для хранения имени игрока и проверки нужно ли спрашивать имя игрока
class Records{
    public static boolean isFirst = true;
    public static String gamer = null;
}

