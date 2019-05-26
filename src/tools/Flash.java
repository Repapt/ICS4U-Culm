package tools;

import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;

public class Flash {
	
	public Rectangle flash;
	public Rectangle flash2;
	
	public Flash() {
		LinearGradient grad1 = new LinearGradient(0, 1, 0, 0, true, CycleMethod.NO_CYCLE, 
				new Stop[]{
			            new Stop(0, Color.web("#000000", 0.9)),
			            new Stop(0.3, Color.web("#e5e5e5e5", 0.7)),
			            new Stop(0.5, Color.web("#ffffff", 0.7)),
			            new Stop(0.7, Color.web("#e5e5e5e5", 0.7)),
			            new Stop(1, Color.web("000000", 0.9)),
			});
		LinearGradient grad2 = new LinearGradient(1, 0, 0, 0, true, CycleMethod.NO_CYCLE, 
				new Stop[]{
			            new Stop(0, Color.web("#000000", 1)),
			            new Stop(0.3, Color.web("#7f7f7f", 0.7)),
			            new Stop(0.5, Color.web("#d3d3d3", 0.3)),
			            new Stop(0.7, Color.web("#7f7f7f", 0.7)),
			            new Stop(1, Color.web("000000", 1)),
			});
	
		flash = new Rectangle(200, 400, grad1);
		flash2 = new Rectangle(200, 400, grad2);
	}

}
