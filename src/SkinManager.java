import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import javax.swing.JButton;

import java.util.prefs.*;

import Sweeper.Box;
import Sweeper.*;
/**
 * Класс SkinManager - отвечает за загрузку новых изображения для изменения интерфейса игры.
 * @autor Фролов Аркадий
 */
public class SkinManager extends JFrame
{
	private final static String SKIN = "skin_image_";
	private final static String SKIN_NAME = "skinName_";
	private final static String SKINS_COUNT = "skinsCount";
	JPanel topPanel;
	JPanel bottomPanel;
	JTextField nameEdit;
	Choice oldNameComboBox;

	JTextField[] skinPathEditArray;
	JButton[] browseFileBtnArray;
	Image[] imagesArray;
	JPanel[] imagePanels;
	JButton okBtn;
	JButton cancelBtn;
	FileDialog fdlg;
	boolean quitOnApply = true;
	SetNewSkinListener listener = null;
	static final int BOX_COUNT = 16;
	static final String[] defaultValues = {
			"res://default/zero.png",
			"res://default/num1.png",
			"res://default/num2.png",
			"res://default/num3.png",
			"res://default/num4.png",
			"res://default/num5.png",
			"res://default/num6.png",
			"res://default/num7.png",
			"res://default/num8.png",
			"res://default/bomb.png",
			"res://default/opened.png",
			"res://default/closed.png",
			"res://default/flaged.png",
			"res://default/bombed.png",
			"res://default/nobomb.png",
			"res://default/icon.png"

	};

	static final String[] imageNames = {
			"zero",
			"num1",
			"num2",
			"num3",
			"num4",
			"num5",
			"num6",
			"num7",
			"num8",
			"bomb",
			"opened",
			"closed",
			"flaged",
			"bombed",
			"nobomb",
			"icon"

		};

	/**
	 * Конструктор - вывод отдельного окна для загрузки своих изображеный
	 * @param listener
	 * @see  SkinManager#SkinManager(SetNewSkinListener)
	 */
	public SkinManager(SetNewSkinListener listener){
		super("Скин менеджер игры \"Сапер\"");
		this.listener = listener;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(640, 800);
		setLayout(new BorderLayout());
		topPanel = new JPanel();
		bottomPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		topPanel.add(new JLabel("Новое имя скина:"));
		nameEdit = new JTextField();
		oldNameComboBox = new Choice();
		int skinsCount = getSkinsCount();
		for (int i=0; i < skinsCount; i++){
			String name  = skinNameAt(i);
			oldNameComboBox.add(name);
		}
		oldNameComboBox.addItemListener( e -> {
			String currentName = oldNameComboBox.getSelectedItem();
			reloadSkinSettingsByName(currentName);
		});
		topPanel.add(nameEdit);
		topPanel.add(new JLabel("Или выбрать старое:"));
		topPanel.add(oldNameComboBox);
		bottomPanel.setLayout(new GridBagLayout());

		okBtn = new JButton("Сохранить");
		cancelBtn = new JButton("Отмена");
		int imagesCount = BOX_COUNT;
		skinPathEditArray = new JTextField[imagesCount];
		browseFileBtnArray = new JButton[imagesCount];
		FileDialog fdlg = new FileDialog(this, "Open image:", FileDialog.LOAD);
		imagesArray = new Image[imagesCount];
		imagePanels =new JPanel[imagesCount];
		GridBagConstraints constraints = new GridBagConstraints();
		int w=70, h=70;
		for (int i=0; i< imagesCount; i++){
			JPanel panel = new JPanel();
			//panel.setLayout(new );
			skinPathEditArray[i] = new JTextField();
			skinPathEditArray[i].setText(defaultValues[i]);
			browseFileBtnArray[i] = new JButton("...");
			browseFileBtnArray[i].setActionCommand(""+ i);
			browseFileBtnArray[i].addActionListener( e -> {
				String cmd = e.getActionCommand();
				int index = Integer.parseInt(cmd);

				fdlg.show();
				String filename  = fdlg.getFile();
				if (filename!= null){
					filename = fdlg.getDirectory() + filename;
					skinPathEditArray[index].setText(filename);
					reloadImageAt(index, filename);
				}
			});
			constraints.weighty= 0.06;

			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.anchor = GridBagConstraints.WEST;
			constraints.weightx = 0.2;
			constraints.gridy   = i;
			constraints.gridx   = 0;
			Label label = new Label(imageNames[i]);
			w=120; h=32;
			label.setSize(w, h);
			label.setPreferredSize( new Dimension(w, h));
			bottomPanel.add(label, constraints );

			constraints.weightx = 0.2;
			constraints.gridy   = i;
			constraints.gridx   = 1;
			w=32; h=32;
			browseFileBtnArray[i].setSize(w, h);
			browseFileBtnArray[i].setPreferredSize( new Dimension(w, h));
			bottomPanel.add(browseFileBtnArray[i], constraints);
			constraints.fill = GridBagConstraints.HORIZONTAL;
				//;
			constraints.weightx = 2.4;//0.8;//0.62;
			constraints.gridy   = i;
			constraints.gridx   = 2;
			w = 320;h =32;
			skinPathEditArray[i].setSize(w,  h);
			skinPathEditArray[i].setPreferredSize( new Dimension(w, h));
			constraints.gridwidth = 1;
			bottomPanel.add(skinPathEditArray[i], constraints);
			constraints.gridwidth = 1;
			imagePanels[i] =  new JPanel()
			{
				@Override
				protected void paintComponent(Graphics g)
				{

					super.paintComponent(g);
					int id = Integer.parseInt(getName());
					if (imagesArray[id] != null)
						g.drawImage(imagesArray[id], 0, 0, imagesArray[id].getWidth(this), imagesArray[id].getHeight(this), this);
				}
			};
			imagePanels[i].setName("" + i);
			imagePanels[i].setLayout(new FlowLayout(FlowLayout.CENTER));
			reloadImageAt(i, defaultValues[i]);
			w=70; h=70;
			imagePanels[i].setSize(w, h);
			imagePanels[i].setPreferredSize( new Dimension(w, h));
			constraints.fill = GridBagConstraints.BOTH;
			constraints.weightx = 0.35;
			constraints.gridy   = i;
			constraints.gridx   = 3;// + 4-1;
			bottomPanel.add(imagePanels[i], constraints );
		}

		constraints.anchor = GridBagConstraints.WEST;
		constraints.weightx = 0.3;
		constraints.gridy   = 17;
		constraints.gridx   = 0;

		bottomPanel.add(okBtn, constraints);
		okBtn.addActionListener( e -> {
			applySettings();
		});

		constraints.weightx = 0.3;
		constraints.gridy   = 17;
		constraints.gridx   = 1;
		cancelBtn.addActionListener( e -> {
			dispose();
		});
		bottomPanel.add(cancelBtn, constraints);

		add(topPanel, BorderLayout.NORTH);
		add(bottomPanel, BorderLayout.EAST);
		setVisible(true);

	}

	public static void main(String[] args){
		new SkinManager(null);
	}
	/**
	 * Функция перезагрузки настройки скинов по имени при добавлении нового
	 * @param name  - название
	 */
	public void reloadSkinSettingsByName(String name){
		String[] paths  = loadSkin(name);
		for (int i=0; i< BOX_COUNT; i++){
			skinPathEditArray[i].setText(paths[i]);
			reloadImageAt(i, paths[i]);
		}
		nameEdit.setText(name);
	}
	/**
	 * Функция получения изображений перед началом игры
	 */
	public static HashMap<String, Integer> getNamesHashMap(){
		HashMap<String, Integer> map =new HashMap<String, Integer>();
		int count = BOX_COUNT;
		for (int i=0; i < count; i++){
			map.put(imageNames[i], new Integer(i));

		}
		return map;
	}
	/**
	 * Функция перезагрузки загрузки картинок при выборе метода добавления скина
	 * @param filename - имя файла
	 *
	 */
	public void reloadImageAt(int i, String filename){
		try{
			imagesArray[i] = loadImage(filename);
			imagePanels[i].repaint();
		}catch(Exception err){
			err.printStackTrace();
		}
	}
	/**
	 * Функция сохранения настроек при загрузке новых картинок
	 */
	public void applySettings(){
		int skinsCount = getSkinsCount();

		String name = nameEdit.getText();

		int id = findSkinName(name);
		int imagesCount = BOX_COUNT;
		String[] imagePaths = new String[imagesCount];
		for (int i=0; i<imagesCount; i++){
			imagePaths[i] = skinPathEditArray[i].getText();
		}
		if (id < 0){
			id = skinsCount;
			setSkinsCount(skinsCount+1);//add new skin
		}else{

		}
		saveSkinName(id, name);
		saveSkin(name, imagePaths);
		setVisible(false);
		if (listener!=null) listener.selectNewSkin( id, name);
		if ( quitOnApply) dispose();
	}

	/**
	 * Функция загрузки изображений
	 * @param name  - название
	 */
	public static Image loadImage(String name){
		String s = name;
		ImageIcon icon = null;
		try{
			do{
				if (s.startsWith("res:/")){
					s = s.substring(5 );
					icon = new ImageIcon(Sweeper.class.getResource(s));
					break;
				}
				if (s.indexOf('/') < 0  && s.indexOf('.') < 0){
					s = "/default/" + s + ".png";
					icon = new ImageIcon(Sweeper.class.getResource(s));
					break;
				}

				icon = new ImageIcon(s);
			}while(false);

			System.err.println("OK open image:" + s);
			return icon.getImage();
		}catch(Exception err){
			System.err.println("Unable to open:" + name +"\n" + s);
		}
		return null;
	}
	/**
	 * Функция для получения скинов
	 */
	public static  Preferences getPreferences(){
		return Preferences.userNodeForPackage(CellFactory.class);
	}
	/**
	 * Функция для получения количества скинов
	 */
	public static int getSkinsCount(){
		Preferences prefs = getPreferences();// ("Sweeper.SkinManager.skins" );
		return prefs.getInt(SKINS_COUNT, 0);
	}
	/**
	 * Функция установки количества созданных скинов
	 * @param v - количество созданных скиинов
	 */
	public static void setSkinsCount(int v){
		Preferences prefs = getPreferences();//("Sweeper.SkinManager.skins" );
		prefs.putInt(SKINS_COUNT, v);
		try{
			prefs.flush();
		}catch(Exception err){
			err.printStackTrace();
		}
	}

	/**
	 * Функция сохранения имени скина
	 * @param i - кол-во изображений
	 */
	public static String skinNameAt(int i){
		Preferences prefs = getPreferences();//("Sweeper.SkinManager.skins" );
		return prefs.get( SKIN_NAME + i, "");
	}
    //Поиск имени скина
	public static int findSkinName(String name){
		int count = getSkinsCount();
		for (int i=0; i < count; i++){
			String name2 = skinNameAt(i);
			if (name.equals(name2)) return i;
		}
		return -1;
	}

	/**
	 * Функция сохранения имени скина
	 * @param name - название
	 */
	public static void saveSkinName(int i, String name){
		Preferences prefs = getPreferences();//("Sweeper.SkinManager.skins" );
		prefs.put( SKIN_NAME + i, name);
		try{
			prefs.flush();
		}catch(Exception err){
			err.printStackTrace();
		}
	}


	/**
	 * Функция загрузка скина
	 * @param name - название
	 * @return все картинки
	 */
	public static String[] loadSkin(String name){
		Preferences prefs = getPreferences();//("Sweeper.SkinManager.skin_" + name);
		int count = BOX_COUNT;
		String[] array = new String[count];

		for (int i=0; i < count; i++){
			array[i] = prefs.get(SKIN + name +"_" + i, defaultValues[i]);
		}
		return array;
	}

	/**
	 * Функция сохранения скина
	 * @param name - название
	 * @param imagePaths - ссылки на картинки
	 */
	public static boolean saveSkin(String name, String imagePaths[]){
		Preferences prefs = getPreferences();//("Sweeper.SkinManager.skin_" + name);
		int count = BOX_COUNT;
		for (int i=0; i < count; i++){
			prefs.put(SKIN + name +"_" + i, imagePaths[i]);
		}
		try{
			prefs.flush();
			return true;
		}catch(Exception err){
			err.printStackTrace();
		}
		return false;
	}
}
