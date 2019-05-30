import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.swing.JButton;

import java.util.prefs.*;


import Sweeper.*;
/**
 * Класс GameModeDialog - отвечает за создание главного меню
 * @autor Фролов Аркадий
 */
public class GameModeDialog extends JFrame implements SetNewSkinListener
{
	JPanel leftPanel, rightPanel;
	JPanel buttonsPanel;
	JPanel modePanel;
	JPanel normalGameOptionPanel;
	JPanel normalCustomGameOptionPanel;

	JTextField usernameEdit;
	ButtonGroup bg;
	JRadioButton normalGameBtn;
	JRadioButton normalCustomGameBtn;
	JRadioButton infinityGameBtn;
	JCheckBox saveMyCaseChk;

	JButton beginGameBtn;
	JButton quitBtn;

	//Regular game options
	JRadioButton juniorLevelBtn;
	JRadioButton middleLevelBtn;
	JRadioButton professionalBtn;

	JTextField gridWidthEdit;
	JTextField gridHeightEdit;
	JTextField timeLimitEdit;
	JTextField bombsEdit;
	JCheckBox bombsInPercentChk;

	Choice skinsComboBox = new Choice();

	private static final String GAMER_NAME = "gamer_name";
	private static final String SKIN = "skin";
	private static final String MODE = "mode";
	private static final String DIFFICULT_LEVEL = "difficult_level";
	private static final String NEED_SAVE_SETTINGS = "need_save_settings";
	private static final String COLUMNS = "columns";
	private static final String ROWS = "rows";
	private static final String TIME_LIMIT = "timeLimit";
	private static final String BOMBS_VALUE = "bombsValue";
	private static final String BOMBS_IN_PERCENT = "bombsInPercent";

	final int FRAME_WIDTH=525, FRAME_HEIGHT=230;
	private static final String MAKE_NEW_SKIN = "Добавить новый...";

	/**
	 * Конструктор - отображающий главное меню со всеми компонентами.
	 */
	public GameModeDialog(){
		super("Игра \"Сапер\":");
		setLayout(new BorderLayout());
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		setResizable(false);
		leftPanel = new JPanel();
		rightPanel = new JPanel();
		buttonsPanel = new JPanel();
		modePanel =new JPanel();

		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.add(new JLabel("Введите имя игрока:"));

		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

		usernameEdit = new JTextField();
		leftPanel.add(usernameEdit);

		bg = new ButtonGroup();
		normalGameBtn = new JRadioButton("Обычная игра");
		normalCustomGameBtn = new JRadioButton("Обычная игра с опциями");

		infinityGameBtn = new JRadioButton("Игра на бесконечном поле", true);

		bg.add(normalGameBtn);

		bg.add(normalCustomGameBtn);
		bg.add(infinityGameBtn);

		leftPanel.add(normalGameBtn);
		leftPanel.add(normalCustomGameBtn);
		leftPanel.add(infinityGameBtn);

		normalGameBtn.addActionListener(e -> {
			setSelectedGameMode(0);
		});

		normalCustomGameBtn.addActionListener(e -> {
			setSelectedGameMode(1);
		});
		infinityGameBtn.addActionListener(e -> {
			setSelectedGameMode(2);
		});
	//	saveMyCaseChk =new JCheckBox("Запомнить мой выбор", true);    ТУУУУУУУУУУУУУУУУУУУУУУУУУУУУУУУУУУУУУТТТТТ

		beginGameBtn = new JButton("Начать игру");
		beginGameBtn.addActionListener( e -> {
			startGameGui();
		});
		quitBtn = new JButton("Выход");
		quitBtn.addActionListener( e -> {
			System.exit(0);
		});
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
		buttonsPanel.add(beginGameBtn);
		buttonsPanel.add(quitBtn);
		leftPanel.add(new Label("Настройки интерфейса, Скин:"));
		leftPanel.add(skinsComboBox);
		leftPanel.add(buttonsPanel);

		skinsComboBox.add(MAKE_NEW_SKIN);
		skinsComboBox.add("default");
		skinsComboBox.add("skin1");
		skinsComboBox.select("default");
		int skinsCount = SkinManager.getSkinsCount();
		for (int i=0; i < skinsCount; i++){
			skinsComboBox.add(SkinManager.skinNameAt(i));
		}
		skinsComboBox.addItemListener( e -> {
			if (skinsComboBox.getSelectedIndex() == 0)
				activateSkinDialog();
		});
		// Опции обычной игры
		normalGameOptionPanel =new JPanel();
		normalGameOptionPanel.setLayout(new BoxLayout(normalGameOptionPanel, BoxLayout.Y_AXIS));
		ButtonGroup bg2 = new ButtonGroup();

		juniorLevelBtn =new JRadioButton("Новичок (поле 9х9, 10 бомб)", true);
		middleLevelBtn = new JRadioButton("Любитель (поле 16х16, 40 бомб)", false);
		professionalBtn = new JRadioButton("Профессионал (поле 30х16, 99 бомб)", false);

		bg2.add(juniorLevelBtn);
		bg2.add(middleLevelBtn);
		bg2.add(professionalBtn);



		normalCustomGameOptionPanel = new JPanel();
		normalCustomGameOptionPanel.setLayout(
					new BoxLayout(normalCustomGameOptionPanel, BoxLayout.Y_AXIS)
					);
		gridWidthEdit  = new JTextField("9");
		gridHeightEdit = new JTextField("9");;
		timeLimitEdit = new JTextField("23");
		bombsEdit = new JTextField("10");
		bombsInPercentChk = new JCheckBox("Кол-во бомб указывается в %", true);
		bombsInPercentChk.setVisible(false);

		loadSettings();

		add(leftPanel, BorderLayout.WEST);
		add(rightPanel, BorderLayout.EAST);
		setVisible(true);

	}

	/**
	 * Функция вызова окна для добавления нового скина
	 */
	public void activateSkinDialog(){
		new SkinManager(this);
	}

	/**
	 * Функция вызова нового скина
	 * @param id - номер картинки
	 * @param name - название
	 */
	public void selectNewSkin(int id, String name){
		skinsComboBox.add(name);
		skinsComboBox.select(name);
	//	SkinManager.setSkinsCount(0);  // ДЛЯ УДАЛЕНИЕ СКИНОВ
	}

	/**
	 * Функция вызова отрисовки режима "Обычная игра"
	 */
	public void setNormalGameOptionPanel(){
		rightPanel.removeAll();
		normalGameOptionPanel.removeAll();
		normalGameOptionPanel.add(juniorLevelBtn);
		normalGameOptionPanel.add(middleLevelBtn);
		normalGameOptionPanel.add(professionalBtn);
		rightPanel.add(normalGameOptionPanel);
		rightPanel.revalidate();

	}

	/**
	 * Функция вызова отрисовки режима "Обычная игра c опциями" и "Бесконечное поле"
	 */
	public void setCustomGameOptionPanel(){
		rightPanel.removeAll();

		normalCustomGameOptionPanel.removeAll();
		normalCustomGameOptionPanel.add(new JLabel("Ширина:"));
		normalCustomGameOptionPanel.add(gridWidthEdit);
		normalCustomGameOptionPanel.add(new JLabel("Высота:"));
		normalCustomGameOptionPanel.add(gridHeightEdit);
		normalCustomGameOptionPanel.add(new JLabel("Количество бомб:"));
		normalCustomGameOptionPanel.add(bombsEdit);
		normalCustomGameOptionPanel.add(new JLabel("Лимит времени:"));
		normalCustomGameOptionPanel.add(timeLimitEdit);
		normalCustomGameOptionPanel.add(bombsInPercentChk);
		rightPanel.add(normalCustomGameOptionPanel);
		rightPanel.revalidate();

	}

	/**
	 * Функция получения режима игры
	 * @return вызор режима игры
	 */
	public int getGameMode(){
		int mode = 0;
		if (normalGameBtn.isSelected())
		{
			return 0;
		}
		if (normalCustomGameBtn.isSelected())
		{
			return 1;
		}
		if (infinityGameBtn.isSelected())
		{
			return 2;
		}
		return mode;
	}
	/**
	 * Функция установки переключателей для всех режимов игры
	 * @param mode - режим
	 */
	public void setSelectedGameMode(int mode){
		rightPanel.removeAll();
		switch(mode){
			case 0:
				rightPanel.add(normalGameOptionPanel);
				//setSize(580, 160);

				normalGameBtn.setSelected(true);
				setNormalGameOptionPanel();
				break;
			case 1:
				//setSize(660, 370);
				normalCustomGameBtn.setSelected(true);
				setCustomGameOptionPanel();
				break;
			case 2:
				infinityGameBtn.setSelected(true);
				setCustomGameOptionPanel();
				break;
		}
		rightPanel.repaint();

		/**
		 * Функция получения режимов для переключателей на "Обычная игра"
		 */
	}
	public int getDifficultLevel(){
		int level =0; //default
		if(juniorLevelBtn.isSelected()){
			return 0;
		}
		if(middleLevelBtn.isSelected()){
			return 1;
		}
		if(professionalBtn.isSelected()){
			return 2;
		}
		return level;
	}

	/**
	 * Функция установки режимов на переключателях на "Обычная игра"
	 * @param l - выбранный параметр
	 */
	public void setDifficultLevel(int l){
		switch(l){
			default:
			case 0:
				juniorLevelBtn.setSelected(true);
				break;
			case 1:
				middleLevelBtn.setSelected(true);
				break;
			case 2:
				professionalBtn.setSelected(true);
				break;
		}
	}

	/**
	 * Функция загрузки параметров для режима "Обычная игра"
	 */
	public void startGameGui(){
		int gameMode = getGameMode();
		int cols = 9, rows = 9;
		int bombsValue = 10;
		int bombs = 10;
		int level = 0;
		int time = 0;
		boolean infinityGame = true;
		String username  = usernameEdit.getText();
		String skin = skinsComboBox.getSelectedItem();
		switch(gameMode){
			case 0:
				level = getDifficultLevel();
				switch(level){
					case 0:
						cols=9;
						rows=9;
						bombs=10;
						break;
					case 1:
						cols=16;
						rows=16;
						bombs=40;
						break;
					case 2:
						cols=30;
						rows=16;
						bombs=99;
						break;
				}
				infinityGame = false;
				break;
			case 1:
				infinityGame = false;
			case 2:
				cols=Integer.parseInt(gridWidthEdit.getText());
				rows=Integer.parseInt(gridHeightEdit.getText());
				bombs=bombsValue=Integer.parseInt(bombsEdit.getText());
				time=Integer.parseInt(timeLimitEdit.getText());

				if (bombsInPercentChk.isSelected()){
					bombs = (cols*rows*bombs)/100;
				}
				if (bombs == 0){
					bombs = 1;
				}
				break;
		}
		setVisible(false);
		Sweeper sweeper = new Sweeper(cols, rows, bombs, skin, username, infinityGame);
		sweeper.setTimeLimit(time);
		sweeper.startGame();
	//	saveSettings();    ТУТУТУТУТУТУУУУУУУУУУУУУУУУУУУУУУУУУУУУУУУУУУУУУУУУТТТТТТТТТТТТТТТТТТТТТТТТТ
		dispose();
	}



	/**
	 * Функция установки скина {@link GameModeDialog#skinsComboBox}
	 */
	public void setSkinChoice(String s){
		skinsComboBox.select(s);
	}

	/**
	 * Функция загрузки всех заданных параметров для режимов "Обычная игра с опциями" и "Бескоенчное поле"
	 */
	public void loadSettings(){
		int gameMode;
		int cols = 9, rows = 9;
		int bombsValue = 10;
		int bombs = 10;
		int level = 0;
		int time = 0;
		boolean infinityGame = true;
		String username;
		String skin;

		boolean bombsInPercent;
		boolean needSaveMyCase;

		Preferences prefs = Preferences.userNodeForPackage(GameEngine.class);
		needSaveMyCase = prefs.getBoolean(NEED_SAVE_SETTINGS, true);
		String gamer = prefs.get(GAMER_NAME, "Аноним");

		skin = prefs.get(SKIN, "default");
		gameMode = prefs.getInt(MODE, 0);  //БЫЛО 2
		level = prefs.getInt(DIFFICULT_LEVEL, 0);
		rows = prefs.getInt(ROWS, 10);
		int columns = prefs.getInt(COLUMNS, 10);
		bombsValue = prefs.getInt(BOMBS_VALUE, 15);
		bombsInPercent = prefs.getBoolean(BOMBS_IN_PERCENT, false);

		time = prefs.getInt(TIME_LIMIT, time);
//		saveMyCaseChk.setSelected(needSaveMyCase);   ТУУУУУУУУУУУУУУУУУУУУУУУУУУУУУУУУУУУУУУУУУУУТТТ
		usernameEdit.setText(gamer);
		setSkinChoice(skin);
		setSelectedGameMode(gameMode);
		gridWidthEdit.setText(""+columns);
		gridHeightEdit.setText(""+rows);
		bombsEdit.setText(""+bombsValue);
		timeLimitEdit.setText(""+time);
		setDifficultLevel(level);
		setSelectedGameMode(gameMode);
		bombsInPercentChk.setSelected(bombsInPercent);


	}

}