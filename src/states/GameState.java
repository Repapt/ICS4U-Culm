package states;
/*
 * Author: Samuel Liu
 * Teacher: Mr. Radulovic
 * 2019/06/18
 * Parent class for various gamestates. Makes switching between the two gamestates in my
 *main class easier.
 *has all the event handling classes as well as the height and width of the game window
 */
import javafx.scene.Group;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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
	public abstract void click(MouseEvent event);
	public abstract void moved(MouseEvent event);
	public abstract void dragged(MouseEvent event);
	public abstract void mouseRelease(MouseEvent event);
	
	public abstract void draw(Group group);
	

}
