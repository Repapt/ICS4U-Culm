package tools;
/*
 * Author: Samuel Liu
 * Teacher: Mr. Radulovic
 * 2019/06/18
 * Class for the 'flashes' that are generated every time a keypress is made. Creates a temporary
 * flash in the lane that the keypress was in.
 */
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;

public class Flash {
	
	public Rectangle flash;
	public Rectangle flash2;
	int dur = 0;
	
	public Flash(int lane, int streak) {
		LinearGradient grad1 = new LinearGradient(0, 1, 0, 0, true, CycleMethod.NO_CYCLE, 
				new Stop[]{
			            new Stop(0, Color.web("#000000", 0)),
			            new Stop(0.2, Color.web("#e5e5e5e5", 0.4)),
			            new Stop(1, Color.web("000000", 0)),
			});
		
		LinearGradient grad2 = new LinearGradient(0, 1, 0, 0, true, CycleMethod.NO_CYCLE, 
				new Stop[]{
			            new Stop(0, Color.web("#000000", 0)),
			            new Stop(0.2, Color.web("#d8c550", 0.75)),
			            new Stop(1, Color.web("000000", 0)),
			});
		//flash is slightly golden if the streak is 32+
		//flash is white otherwise
		if(streak > 31) {
			flash = new Rectangle(100, 300, grad2);
		} else {
			flash = new Rectangle(100, 300, grad1);
		}
		flash.setX(100*(0.5+lane));
		flash.setY(300);
	}
	
	public Rectangle showFlash() {
		return flash;
		
	}

	public int update() {
		//flash only lasts for 5 frames
		dur ++;
		if(dur > 5) {
			return -1;
		}
		return 1;
	}
}
