package Sweeper;
/**
 * Класс GameEngine - отвечает за управление процессами игры в бесконечном поле
 * @autor Фролов Аркадий
 */
public class GameEngine 
{
	GameStat state;
	public Map map;
	int percentScrollX = 11;
	int gridWidth, gridHeight;

	/**
	 * Конструктор - для получение данных перед началом игры бесконечного уровня
	 * @param h - высота
	 * @param w - ширина
	 * @see GameEngine()#GameEngine(int, int)
	 */
	public GameEngine(int w, int h){
		state = GameStat.PLAYED;
		map = new Map(w, h);
		gridWidth = w;
		gridHeight = h;
	}
	/**
	 * Функция получающая статус игры {@link GameEngine#state}
	 * @return - возвращает статус
	 */
	public GameStat getGameState(){
		return state;
	}

	/**
	 * Функция установки статуса игры {@link GameEngine#state}
	 */
	public void setGameState(GameStat state){
		this.state = state;
	}

	/**
	 * Функция устанавливает статуст игры - проигрыш  {@link GameEngine#state}
	 */
	public void setBombed()
    {
        state = GameStat.BOMBED;
    }

	/**
	 * Функция запуска игры
	 *
	 */
	public void start(){
		map.locked = true;
		state = GameStat.PLAYED;
		map.reset();
		map.locked = false;
	}
	/**
	 * Функция остановки
	 *
	 */
	public void stop(boolean win){
		if (win){
			state = GameStat.WINNER;
		}else{
			state = GameStat.BOMBED;
		}
	}

	/**
	 * Функция отвечающая за перемещение
	 * @param direction - направление
	 */
	public void scroll(Direction direction)
	{
		map.locked = true;
		int shift = (int)Math.floor( map.getGridWidth()*percentScrollX/100.0);
		if (shift < 1) shift = 1;
		switch(direction){
			case LEFT:
				map.moveRelative(-shift, 0);
				break;

			case RIGHT:
				map.moveRelative(shift, 0);
				break;

			case TOP:	
				map.moveRelative(0, -shift);
				break;
				
			case BOTTOM:
				map.moveRelative(0, shift);
				break;
		}
		map.locked = false;
	}

	/**
	 * Функция открытия ячейки
	 * @param x - коорд x
	 * @param y - y
	 */
	public void openCell(int x, int y){
		if (x < 0 || y < 0) return;
		map.locked = true;
		Cell cell = map.getRelativeCell(x, y);
		if (cell.isMine()){
			state = GameStat.BOMBED;
			cell.markOpenned();
			map.locked = false;
			return;
		}
		if (cell.isOpenned()){
			map.locked = false;
			return;
		}
		
		int count = cell.getMinesCount(map);
		if (count == 0){
			//Find space
			map.markSpaces(x, y);
		}else{
			map.addScore(1);
		}
		cell.markOpenned();
		map.locked = false;
	}
	/**
	 * Функция позволяющая переключить флаги (поставить, убрать флаг)
	 * @param x - коорд x
	 * @param y - y
	 */
	public void toggleFlag(int x, int y){
		map.locked = true;
		if (x < 0 || y < 0) return;
		Cell cell = map.getRelativeCell(x, y);
		cell.toggleFlag();
		map.locked = false;
	}
	
	public int getScore(){
	return map.getScore();
}

}