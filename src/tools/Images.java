package tools;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Images {
	
	
	public static Image title;
	public static ImageView titleV;
	

	public static void load() {
		
		
		
		title = new Image(Images.class.getResourceAsStream("Impulse.png"));
		titleV = new ImageView(title);
	}
}
