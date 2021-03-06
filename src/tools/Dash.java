package tools;
/*
 * Author: Samuel Liu
 * Teacher: Mr. Radulovic
 * 2019/06/18
 * Creates the dashes in the background of the menu screen. Randomizes length, width, direction,
 * and speed of the dashes moving across the screen.
 */
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Dash {
	
	Rectangle line;
	double speed;
	int direction;
	
	public Dash() {
		
		direction = (int)(Math.random()*4);
		
		//0 or 1 are vertical
		//2 or 3 are horizontal
		
		if(direction == 0 || direction == 1) {
			line = new Rectangle(Math.random()*4 + 2, Math.random()*150 + 20, Color.web("#39b7e5"));
		} else {
			line = new Rectangle(Math.random()*150 + 5, Math.random()*4 + 2, Color.web("#39b7e5"));
		}
		//randomizes starting position
		line.setX(Math.random()*400);
		line.setY(Math.random()*600);
		//formatting to make it look pretty
		DropShadow s = new DropShadow();
		s.setColor(Color.web("#39b7e5"));
		s.setOffsetX(3);
		s.setOffsetY(3);
		line.setEffect(s);
		speed = Math.random()*20 + 5;
		
		
	}
	
	
	public Rectangle getRect() {
		return line;
	}
	
	public int update() {
		//changes the x or y position every frame
		if(direction == 0) {
			line.setY(line.getY() + speed);
		} else if (direction == 1) {
			line.setY(line.getY() - speed);
		} else if (direction == 2) {
			line.setX(line.getX() - speed);
		} else {
			line.setX(line.getX() + speed);
		}
		return 1;
	}
	
	public double getX() {
		return line.getX();
	}
	public double getY() {
		return line.getY();
	}
	public double getHeight() {
		return line.getHeight();
	}
	public double getWidth() {
		return line.getWidth();
	}
}

