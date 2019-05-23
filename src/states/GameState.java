package states;

import javafx.scene.Group;
import javafx.scene.input.KeyEvent;

public abstract class GameState {
	
	
	
	
	
	
	public abstract void update(int counter);
	
	public abstract void keyPress(KeyEvent event);
	public abstract void keyRelease(KeyEvent event);
	
	public abstract void draw(Group group);

}
