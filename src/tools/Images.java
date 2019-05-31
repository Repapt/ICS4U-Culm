package tools;

import java.io.InputStream;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

public class Images {
	
	
	public static Image title;
	public static ImageView titleV;
	
	public static InputStream fontStream;
	public static Font font;
	

	public static void load() {
		
		fontStream = Images.class.getResourceAsStream("TRON.TTF");
		font = Font.loadFont(fontStream, 30);
		
		title = new Image(Images.class.getResourceAsStream("Impulse.png"));
		titleV = new ImageView(title);
	}
}
