package Sweeper;

public class InvalidDirectionException extends Exception
{
	public InvalidDirectionException(){
		super();
	}
	
	public InvalidDirectionException(String msg){
		super(msg);
	}
	
}