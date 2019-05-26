package states;

import javafx.scene.Group;
import javafx.scene.input.KeyEvent;
import main.Main;

public abstract class GameState {
	
	public double height;
	public double width;
	
	public GameState(Main g) {
		height = g.getHeight();
		width = g.getWidth();
	}
	
	
	
	
	public abstract void update(int counter);
	
	public abstract void keyPress(KeyEvent event);
	public abstract void keyRelease(KeyEvent event);
	
	public abstract void draw(Group group);

}
