package beats;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;

public class Beat {
		
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
	
	public Image image = new Image(this.getClass().getResourceAsStream("beat.png"));
	public ImageView iView = new ImageView(image);
	
	
	public Beat(int lane, double speed) {
		
		this.lane = lane;
		this.speed = speed;
		
	
		
		x = 100*(lane+1);
		circle.setCenterX(x);
		
		iView.setX(x-25);
		iView.setY(y-25);
		
		/*
		circle.setStrokeType(StrokeType.OUTSIDE);
		circle.setStroke(Color.web("black", 0.5));
		circle.setStrokeWidth(3);
		*/
				
	}
	
	public void move(boolean moving) {
		if(!moving) {
			pauseCount ++;
			iView.setY(y-25);
			circle.setCenterY(y);
			
		} else {
			y += speed;
			circle.setCenterY(y);
			iView.setY(y-25);
		}
		
		if(pauseCount > 5) {
			y += speed;
			circle.setCenterY(y);
			iView.setY(y-25);
		} else if (pauseCount == 0) {
			
		} else {
			y = 500;
			circle.setCenterY(y);
			iView.setY(y-25);
			
		}
		
	}
	
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	
	
	
	
}
