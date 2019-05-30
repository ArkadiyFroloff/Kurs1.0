package Sweeper;

public class Cell
{
	boolean hasMine = false;
	boolean minesCountDefined = false;
	int minesCount = 0;
	int id = 0;
	Cell left = null;
	Cell right = null;
	Cell top = null;
	Cell bottom = null;
	public boolean needRecheckSpaces = false;
	boolean hasFlag_ = false;
	boolean isBombed = false;
	public static final int FLAG_ID=12, MINE_ID=9, SPACE_ID=10, CLOSED_ID=11, BOMBED_ID=13; 
	boolean isOpen = false;
	
	public static boolean asserts1 = true;


	
	public Cell(boolean hasMine){
		this.hasMine = hasMine;
	}
	
	public Cell(boolean hasMine, int id){
		this.hasMine = hasMine;
		this.id = id;
	}
	
	public static Cell createCell(CellFactory factory){
		return new Cell(factory.defineMineExists(), factory.getCellsCount());
	}
	
	public boolean isMine(){
		return hasMine;
	}
	
	public int getId(){
		return id;
	}
	
	public boolean hasLeftChild(){
		return (left != null);
	}
	
	public boolean hasRightChild(){
		return (right != null);
	}
	
	public boolean hasTopChild(){
		return (top != null);
	}
	
	public boolean hasBottomChild(){
		return (bottom != null);
	}
	
	public boolean hasChildAt(Direction direction)
	{
		switch(direction){
			case LEFT:
				return (left != null);

			case RIGHT:
				return (right != null);

			case TOP:	
				return (top != null);

			case BOTTOM:
				return (bottom!=null);
		}
		return false;
	}
	
	public boolean isOpenned(){
		return isOpen;
	}
	
	public void setNeedRecheckSpaces(boolean needRecheck){
		needRecheckSpaces = needRecheck;
	}
	
	public void markOpenned(){
		isOpen=true;
	}
	
	public  void toggleFlag()
	{
		hasFlag_ = ! hasFlag_;
	}
	
	public boolean hasFlag(){
		return hasFlag_;
	} 
	public synchronized int getMinesCount(CellFactory factory)
	{
		if (minesCountDefined) return minesCount;
		minesCount = 0;
		if (getChildAt(Direction.LEFT, factory).isMine()) minesCount++;
		if (getChildAt(Direction.RIGHT, factory).isMine()) minesCount++;
		if (getChildAt(Direction.TOP, factory).isMine()) minesCount++;
		if (getChildAt(Direction.BOTTOM, factory).isMine()) minesCount++;
		if (getChildAt(Direction.TOP_LEFT, factory).isMine()) minesCount++;
		if (getChildAt(Direction.TOP_RIGHT, factory).isMine()) minesCount++;
		if (getChildAt(Direction.BOTTOM_RIGHT, factory).isMine()) minesCount++;
		if (getChildAt(Direction.BOTTOM_LEFT, factory).isMine()) minesCount++;
		minesCountDefined = true;
		return minesCount;
	}
	
	public void markBombed(){
		isBombed = true;
	}
	public int getImageId(CellFactory factory){
		if (hasFlag_ && !isOpen) return FLAG_ID;
		if (isBombed && isOpen) return BOMBED_ID;
		if (hasMine && isOpen) return MINE_ID;
		
		int mines =  getMinesCount(factory);
		if (!isOpen) return CLOSED_ID;
		return mines;
	}
	
	public Cell childAt(Direction direction,CellFactory factory){
		return getChildAt(direction, factory);
	}
	
	public Cell childAtRight(CellFactory factory){
		return getChildAt(Direction.RIGHT, factory);
	}
	
	public Cell childAtLeft(CellFactory factory){
		return getChildAt(Direction.LEFT, factory);
	}

	public Cell childAtTop(CellFactory factory){
		return getChildAt(Direction.TOP, factory);
	}
	
	public Cell childAtBottom(CellFactory factory){
		return getChildAt(Direction.BOTTOM, factory);
	}
	
	public Cell getBorder(Direction direction)
	{
		Cell current = this;
		switch(direction){
			case LEFT:
				while(current.left != null){
					current = current.left;
				}
				return current;

			case RIGHT:
				while(current.right != null){
					current = current.right;
				}
				return current;

			case TOP:	
				while(current.top != null){
					current = current.top;
				}
				return current;


			case BOTTOM:
				while(current.bottom != null){
					current = current.bottom;
				}
				return current;

		}
		return null;
	}
	
	public int getSize(Direction direction){
		Cell current = this;
		int size = 0;
		switch(direction){
			case LEFT:
				while(current.left != null){
					current = current.left;
					size++;
				}
				break;

			case RIGHT:
				while(current.right != null){
					current = current.right;
					size++;
				}
				break;

			case TOP:	
				while(current.top != null){
					current = current.top;
					size++;
				}
				break;


			case BOTTOM:
				while(current.bottom != null){
					current = current.bottom;
					size++;
				}
				break;
		}
		return size;
	}
	
	public void dumpSyncRowParams(Direction direction,Cell cell1, Cell cell2){
		System.out.println("\nsyncRow:this=" + this);
		System.out.println("\nsyncRow:cell1=" + cell1);
		System.out.println("\nsyncRow:cell2=" + cell2);
		System.out.println("\nsyncRow:direction=" + direction);
	}
	
	public void syncRow(Direction direction,Cell cell1, Cell cell2,CellFactory factory)
	{
		Cell current = cell1;
		Cell newCell;
		Cell prevCell = null;
		switch(direction){
			case LEFT:
			while(current != null)
			{
				
				if (current.left == null){
					newCell = createCell(factory);
					current.linkChildAt(direction, newCell);
				}else{
					//assert current.left == this;
					if (asserts1) assert current == this; 
				}

				if (prevCell!= null){
					prevCell.left.linkChildAt(Direction.BOTTOM, current.left);
				}
				prevCell = current;
				current = current.bottom;
				
			}
			assert (prevCell == cell2);
			break;

			case RIGHT:
			while(current != null)
			{
				if (current.right == null){
					newCell = createCell(factory);
					current.linkChildAt(direction, newCell);

				}else{
					if (current!= this){
						System.out.println("\nsyncRowC:Assertation failed, current=" + current);
						dumpSyncRowParams(direction, cell1, cell2);
					}
					if (asserts1) assert current == this; 
				}
				if (prevCell!= null){
					prevCell.right.linkChildAt(Direction.BOTTOM, current.right);
				}
				prevCell = current;
				current = current.bottom;

				
			}
			assert (prevCell == cell2);
			break;

			case TOP:	
			while(current != null)
			{
				
				if (current.top == null){
					newCell = createCell(factory);
					current.linkChildAt(direction, newCell);
				}else{
					//assert current.top == this;
					if (asserts1) assert current == this;  
				}

				if (prevCell!= null){
					prevCell.top.linkChildAt(Direction.RIGHT, current.top);
				}
				prevCell = current;
				current = current.right;
				
			}
			assert (prevCell == cell2);
			break;


			case BOTTOM:
			while(current != null)
			{
				
				if (current.bottom == null){
					newCell = createCell(factory);
					current.linkChildAt(direction, newCell);
				}else{
					if (asserts1) assert current == this; 
				}
				if (prevCell!= null){
					prevCell.bottom.linkChildAt(Direction.RIGHT, current.bottom);
				}
				prevCell = current;
				current = current.right;
				
			}
			assert (prevCell == cell2);
			break;

		}
		assertIsSquare();
	}
	

	public boolean assertIsSquare(){
		Cell top = getBorder(Direction.TOP);
		Cell topLeft = top.getBorder(Direction.LEFT);
		int topWidth = topLeft.getSize(Direction.RIGHT);
		int topHeight = topLeft.getSize(Direction.BOTTOM);
		int altSize;
		int i;
		Cell current = topLeft.bottom;
		i=0;
		while (current!= null)
		{
			altSize =  current.getSize(Direction.RIGHT);
			if (topWidth != altSize){
				System.err.println("assertIsSquare Failed!:topWidth=" + topWidth + ", altSize=" + altSize + ", at row:" + i);
				System.err.println("assertIsSquare:current=" + current);
				dumpCellMap(4);
				assert false;
			}
			current = current.bottom;
			i++;
		}
		current = topLeft.right;
		while (current!= null)
		{
			altSize = current.getSize(Direction.BOTTOM);
			if (topHeight != altSize){
				System.err.println("assertIsSquare Failed!:topHeight="+ topHeight + ", altSize=" + altSize + ", at column:" + i);
				System.err.println("assertIsSquare:current=" + current);
				dumpCellMap(4);
				assert false;
			}
			current = current.right; 
		}
		Cell bottom = getBorder(Direction.BOTTOM);
		Cell bottomRight = top.getBorder(Direction.RIGHT);
		return true;
	}
	
	public String formatNum(String value, int fieldSize){
		String s = value;
		while (s.length() < fieldSize){
			s += " ";
		}
		return s;
	}
	
	public String formatNum(int value, int fieldSize, char link){
		String s = "" + value;
		s = formatNum(s, fieldSize);
		return s + link;
	}
	
	
	public void dumpCellMap(int fieldSize){
		Cell top = getBorder(Direction.TOP);
		Cell topLeft = top.getBorder(Direction.LEFT);
		int topWidth = topLeft.getSize(Direction.RIGHT);
		int topHeight = topLeft.getSize(Direction.BOTTOM);
		int altSize;
		int i;
		Cell current = topLeft;
		i=0;
		String ret = "";
		char link = '-';
		while (current.bottom!= null)
		{
			Cell current2 = current;//.right;
			String row = "";
			Cell prev = null;
			while (current2.right!= null)
			{
				link = '-';
				if (current2  == current2.right.left){
					link = '=';
				}
				String s = formatNum(current2.id, fieldSize, link);
				ret += s;
				link = ' ';
				if (prev != null){
					
				}
				if (current2.bottom != null){
					if (current2  == current2.bottom.top){
						link = '=';
					}else{
						link = '-';
					}
				}
				row += formatNum(" " + link +" ", fieldSize+1);
				prev = current2;
				current2 = current2.right;
				
				
			}
			ret += formatNum(current2.id, fieldSize, ' ');
			ret += "\n";
			link = ' ';
			if (current2.bottom != null){
					if (current2  == current2.bottom.top){
						link = '=';
					}else{
						link = '-';
					}
				}
			row += formatNum(" " + link +" ", fieldSize+1);
			ret += row + "\n";
			current = current.bottom;
			i++;
		}

		System.err.println(ret);
	}
	
	
	public Cell getChildAt(Direction direction,CellFactory factory)
	{
		Cell newCell = null;
		Cell cell1 = null;
		Cell cellA,cellB;
		//Cell cell2 = null;
		switch(direction){
			case LEFT:
				if (left != null) return left;
				newCell = createCell(factory);
				linkChildAt(direction, newCell);
				cellA = getBorder(Direction.TOP);
				cellB = getBorder(Direction.BOTTOM);
				syncRow(direction, cellA, cellB, factory);

				return newCell;
			case RIGHT:
				if (right != null) return right;
				newCell = createCell(factory);
				linkChildAt(direction, newCell);
				cellA = getBorder(Direction.TOP);
				cellB = getBorder(Direction.BOTTOM);
				syncRow(direction, cellA, cellB, factory);

				return newCell;
			case TOP:	
				if (top != null) return top;
				newCell = createCell(factory);
				linkChildAt(direction, newCell);
				cellA = getBorder(Direction.LEFT);
				cellB = getBorder(Direction.RIGHT);
				syncRow(direction, cellA, cellB, factory);

				return newCell;
			case BOTTOM:
				if (bottom != null) return bottom;
				newCell = createCell(factory);
				linkChildAt(direction, newCell);
				cellA = getBorder(Direction.LEFT);
				cellB = getBorder(Direction.RIGHT);
				syncRow(direction, cellA, cellB, factory);

				return newCell;
			case TOP_LEFT:
				cell1 = getChildAt(Direction.TOP, factory);
				return cell1.getChildAt(Direction.LEFT, factory);
			case TOP_RIGHT:
				cell1 = getChildAt(Direction.TOP, factory);
				return cell1.getChildAt(Direction.RIGHT, factory);
			case BOTTOM_LEFT:
				cell1 = getChildAt(Direction.BOTTOM, factory);
				return cell1.getChildAt(Direction.LEFT, factory);
			case BOTTOM_RIGHT:
				cell1 = getChildAt(Direction.BOTTOM, factory);
				return cell1.getChildAt(Direction.RIGHT, factory);
		}
		return null;
	}
	
	
	public void linkChildAt(Direction direction, Cell child)
	{
		switch(direction){	
			case LEFT:
				assert (left==null) || (left == child);
				left = child;
				child.right = this;
				
				break;
			case RIGHT:
				assert (right==null) || (right == child);
				right = child;
				child.left = this;
				break;
			case TOP:
				assert (top==null) || (top == child);
				top = child;
				child.bottom = this;
				break;
			case BOTTOM:
				assert (bottom==null) || (bottom == child);
				bottom = child;
				child.top = this;
				break;
			case TOP_LEFT:
			
				break;
			case TOP_RIGHT:
				break;
			case BOTTOM_LEFT:
				break;
			case BOTTOM_RIGHT:
				break;
		}
	}
	public String toString0(){
		return "Cell:id=" +id+",hasMine="+hasMine+",isOpen="+isOpen+", minesCount="+ minesCount;
	}
	public String toString(){
		String s = toString0();
		s += ",left=`";
		if (left!=null){
			s+=left.toString0();
		}else{
			s+= "null";
		}
		s += "`,";
		s += ",right=`";
		if (right!=null){
			s+=right.toString0();
		}else{
			s+= "null";
		}
		s += "`,";

		s += ",top=`";
		if (top!=null){
			s+=top.toString0();
		}else{
			s+= "null";
		}
		s += "`,";
		
		s += ",bottom=`";
		if (bottom!=null){
			s+=bottom.toString0();
		}else{
			s+= "null";
		}
		s += "`,";
		return s;
	}
	
}