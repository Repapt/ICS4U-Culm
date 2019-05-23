package beats;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

public class Beat {
	
	public static Group circles = new Group();
	
	double speed;
	public int lane;
	double x = 0;
	double y = 0;
	double pauseCount;
	
	LinearGradient gradient = new LinearGradient(0, 1, 1, 0, true, CycleMethod.NO_CYCLE,
			new Stop[] {
					new Stop(0, Color.web("#840779")),
					new Stop(0.7, Color.web("#f95eec")),
					new Stop(1, Color.web("#e20dd0")),
					
			});
	
	public Circle circle = new Circle(x, y, 25, gradient);	
	
	public Beat(int lane, double speed) {
		
		this.lane = lane;
		this.speed = speed;
		
		
		x = 100*(lane+1);
		circle.setCenterX(x);
		
		/*
		circle.setStrokeType(StrokeType.OUTSIDE);
		circle.setStroke(Color.web("black", 0.5));
		circle.setStrokeWidth(3);
		*/
		
		circles.getChildren().add(circle);
		
	}
	
	public void move(boolean moving) {
		if(!moving) {
			pauseCount ++;
		} else {
			y += speed;
			circle.setCenterY(y);
		}
		
		if(pauseCount > 6) {
			y += speed;
			circle.setCenterY(y);
		}
		
	}
	
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	
	
	
	
}
