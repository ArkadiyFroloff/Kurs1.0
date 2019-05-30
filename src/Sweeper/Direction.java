package Sweeper;

public enum Direction
{
	LEFT, RIGHT, 
	TOP, BOTTOM, 
	TOP_LEFT, TOP_RIGHT, 
	BOTTOM_LEFT, BOTTOM_RIGHT;
	
    public int getOrdinal()
    {
        return this.ordinal();
    }
	
	public int toInt()
    {
        return this.ordinal();
    }
	
	
}