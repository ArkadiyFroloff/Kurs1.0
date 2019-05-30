package Sweeper;

import java.util.Random;

public class CellFactory
{
	Random random;
	int minesPercent = 7;
	
	int allMinesCount = 0;
	int allCellsCount = 0;
	
	public CellFactory(){
		random = new Random(System.currentTimeMillis());
	}
	
	public void resetIds(){
		allMinesCount = 0;
		allCellsCount = 0;
	}
	
	public Cell createCell(){
		return new Cell(defineMineExists(), getCellsCount());
	}
	
	public void setMinesPercent(int percent){
		if (percent <= 0) percent = 1;
		if (percent > 99) percent = 99;
		minesPercent = percent;
	}
	
	public synchronized void setRandomSeeed(long seed){
		random.setSeed(seed);
	}
	
	public synchronized boolean defineMineExists(){
		int rnd = random.nextInt(100);
		allCellsCount++;
		boolean hasMine = (rnd < minesPercent);
		if (hasMine) allMinesCount++;
		return hasMine;
	}
	
	public int getMinesCount(){
		return allMinesCount;
	}
	
	public int getCellsCount(){
		return allCellsCount;
	}

}