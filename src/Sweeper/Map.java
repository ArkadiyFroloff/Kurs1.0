package Sweeper;

public class Map extends CellFactory
{
	int mapWidth = 9;
	int mapHeight = 9; 
	Coord minTopLeft = new Coord(0,0); 
	Coord maxBottomRight = new Coord(0,0); 
	boolean mapModified;
	public boolean locked = false;
	int score = 0; 
	
	public Cell baseCell = null; //TOP_LEFT cell!!
	
	public Map()
	{
		super();
		reset();
	}
	int getScore(){
		return score;
	}
	void addScore(int value){
		score += value;
	}
	
	public boolean isModified(){
		return mapModified;
	}
	public int getGridWidth(){
		return mapWidth;
	}
	
	public int getGridHeight(){
		return mapHeight;
	}

	/**
	 * Конструктор - для получение данных для поля
	 * @param h - высота
	 * @param w - ширина
	 * @see Map()#Map(int, int)
	 */
	public Map(int w, int h)
	{
		super();
		mapWidth = w;
		mapHeight = h;
		reset();
	}
	
	public void reset(){
		score = 0;
		resetIds();
		Cell[] row1 = null; 
		Cell[] prevRow = null;
		
		for (int i = 0; i < mapHeight+2; i++){
			prevRow = row1;
			row1 = makeCellRow();

			
			if (prevRow != null){
				linkRows(prevRow, row1);
			}
			if (i == 1){

				baseCell = row1[0].childAt(Direction.RIGHT, this);
			}
		}

		minTopLeft.x=0;
		minTopLeft.y=0;
		maxBottomRight.x=mapWidth - 1;
		maxBottomRight.y=mapHeight - 1;
		
	}
	
	private Cell[] makeCellRow()
	{
		return makeCellRow(mapWidth + 2);
	}
	private Cell[] makeCellRow(int mapWidth)
	{
		Cell[] array = new Cell[mapWidth];
		//int highIndex= mapWidth - 1;
		for (int i=0; i < mapWidth; i++){
			array[i] = createCell();
			if (i != 0){
				array[i-1].linkChildAt(Direction.RIGHT, array[i]);
			}

		}
		return array;
	}
	
	public void linkRows(Cell[] topRow, Cell[] bottomRow){
		Cell top = topRow[0];
		Cell bottom = bottomRow[0];
		int maxLength = topRow.length;
		if (bottomRow.length > maxLength) maxLength = bottomRow.length;
		linkRows(top, bottom, maxLength);
	}
	
	public void linkRows(Cell top, Cell bottom, int rowLength){
		int rowHi = rowLength - 1;
		for (int i=0; i < rowLength; i++)
		{
			top.linkChildAt(Direction.BOTTOM, bottom);
			if (i != rowHi){
				top = top.childAt(Direction.RIGHT, this);
				bottom = bottom.childAt(Direction.RIGHT, this);
			}
		}
	}
	

	
	public void moveRelative(int relX, int relY){
		baseCell = getRelativeCell(relX, relY);
	}

	//получение ячеек
	public Cell getRelativeCell(int relX, int relY){
		Cell cell = baseCell;
		cell = getRelativeXCell(cell, relX);
		cell = getRelativeYCell(cell, relY);
		return cell;
	}
	//получаем ячейки относительно х
	public Cell getRelativeXCell(Cell cell, int relX){
		Cell expandPoint = null;
		Direction direction; 
		if (relX < 0){
			//direction left
			relX = -relX;
			direction = Direction.LEFT;

			
		}else{
			direction = Direction.RIGHT;
		}
		for (int i=0; i< relX; i++){
			if (expandPoint== null && (!cell.hasChildAt(direction)))
			{
				expandPoint = cell;
			}
			cell = cell.childAt(direction, this);
		}
		return cell;
	}

	//получаем ячейки относительно y
	public Cell getRelativeYCell(Cell cell, int relY){

		Cell expandPoint = null;
		Direction direction; 
		if (relY < 0){
			//direction up
			relY = -relY;
			direction = Direction.TOP;

			
		}else{
			direction = Direction.BOTTOM;
		}
		for (int i=0; i< relY; i++){
			if (expandPoint== null && (!cell.hasChildAt(direction)))
			{
				expandPoint = cell;
			}
			cell = cell.childAt(direction, this);
		}
		return cell;
	}
	
	public void printMap(){
		baseCell.dumpCellMap(4);
	}
	//работа со всеми ячейками карты по x и y (по направлениям)
	public void markSpaces(int x, int y){
		int maxX =  x;
		int maxY = y;
		if (maxX < 0) maxX = -maxX;
		if (maxY < 0) maxY = -maxY;
		Cell cell = getRelativeCell(x, y);
		if (!cell.isOpenned())score += 1;
		cell.markOpenned();
		
		if (maxX > (mapWidth+2) || maxY > (mapHeight+2)){
			cell.setNeedRecheckSpaces(true);
			return;
		}
		System.out.println("mark spaces: cell Id ="+ cell.getId());
		
		// left
		int minesCount;
		int currentX = x-1;
		Cell left = getRelativeCell(currentX, y);
		assert ! left.isMine();
		if (!left.isOpenned()){
			score += 1;
			minesCount = left.getMinesCount(this);
			if (minesCount == 0) markSpaces(currentX, y);
			left.markOpenned();
		}
		
		
		// right
		currentX = x+1;
		Cell right = //cell.childAtRight(this);//
			getRelativeCell(currentX, y);
		assert ! right.isMine();
		if (!right.isOpenned()){
			score += 1;
			minesCount = right.getMinesCount(this);
			if (minesCount == 0) markSpaces(currentX, y);
			right.markOpenned();
			
		}
		
		//Top
		int currentY = y-1;
		Cell top = getRelativeCell(x, currentY);
		assert ! top.isMine();
		if (!top.isOpenned()){
			score += 1;
			minesCount = top.getMinesCount(this);
			if (minesCount == 0) markSpaces(x, currentY);
			top.markOpenned();
		}
		
		//bottom
		currentY = y+1;
		Cell bottom = getRelativeCell(x, currentY);
		assert !bottom.isMine();
		if (!bottom.isOpenned()){
			score += 1;
			minesCount = bottom.getMinesCount(this);
			if (minesCount == 0) markSpaces(x, currentY);
			bottom.markOpenned();
		}
		
		// TOP_LEFT
		Cell cell2;
		currentY = y-1;
		currentX = x-1;
		cell2 = getRelativeCell(currentX, currentY);
		assert !cell2.isMine();
		if (!cell2.isOpenned()){
			score += 1;
			minesCount = cell2.getMinesCount(this);
			if (minesCount == 0) markSpaces(currentX, currentY);
			cell2.markOpenned();
		}
		// TOP_RIGHT
		currentY = y-1;
		currentX = x+1;
		cell2 = getRelativeCell(currentX, currentY);
		assert !cell2.isMine();
		if (!cell2.isOpenned()){
			score += 1;
			minesCount = cell2.getMinesCount(this);
			if (minesCount == 0) markSpaces(currentX, currentY);
			cell2.markOpenned();
		}
	
		// Bottom_LEFT
		currentY = y+1;
		currentX = x-1;
		cell2 = getRelativeCell(currentX, currentY);
		assert !cell2.isMine();
		if (!cell2.isOpenned()){
			score += 1;
			minesCount = cell2.getMinesCount(this);
			if (minesCount == 0) markSpaces(currentX, currentY);
			cell2.markOpenned();
		}

		// bottom right
		currentY = y+1;
		currentX = x+1;
		cell2 = getRelativeCell(currentX, currentY);
		assert !cell2.isMine();
		if (!cell.isOpenned()){
			score += 1;
			minesCount = cell2.getMinesCount(this);
			if (minesCount == 0) markSpaces(currentX, currentY);
			cell2.markOpenned();
		}
		
	}
	//
	public boolean refreshVisualWindow(Cell[][] matrix, boolean visibleAll)
	{
		if (locked) return false;
		if (visibleAll){
			for (int x=0; x < mapWidth; x++){
				for (int y=0;y < mapHeight; y++){
					Cell cell = getRelativeCell(x, y );
					cell.markOpenned();
					matrix[x][y] = cell;
				}
			}
			return true;
		}
		for (int x=0; x < mapWidth; x++){
			for (int y=0;y < mapHeight; y++){
				Cell cell = getRelativeCell(x, y );
				
				if (cell.needRecheckSpaces){
					markSpaces(x, y);
					cell.needRecheckSpaces = false;
				}
				matrix[x][y] = cell;
			}
				
		}
		
		return true;
	}


}