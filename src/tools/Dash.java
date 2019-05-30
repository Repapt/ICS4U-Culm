package tools;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Dash {
	
	Rectangle line;
	int dur;
	int count;
	double speed;
	int direction;
	
	public Dash() {
		
		dur = (int)(Math.random()*1000) + 100;
		
		direction = (int)(Math.random()*4);
		
		if(direction == 0 || direction == 1) {
			line = new Rectangle(Math.random()*5 + 2, Math.random()*150 + 20, Color.web("#39b7e5"));
		} else {
			line = new Rectangle(Math.random()*150 + 5, Math.random()*5 + 2, Color.web("#39b7e5"));
		}
		line.setX(Math.random()*400);
		line.setY(Math.random()*600);
		speed = Math.random()*20 + 10;
		
		
	}
	
	
	public Rectangle getRect() {
		return line;
	}
	
	public int update() {
		count ++;
		if(direction == 0) {
			line.setY(line.getY() + speed);
		} else if (direction == 1) {
			line.setY(line.getY() - speed);
		} else if (direction == 2) {
			line.setX(line.getX() - speed);
		} else {
			line.setX(line.getX() + speed);
		}
		if(count > dur) {
			return -1;
		}
		return 1;
	}
	
	public double getX() {
		return line.getX();
	}
	public double getY() {
		return line.getY();
	}
}

