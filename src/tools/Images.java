package tools;

import java.io.InputStream;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

public class Images {
	
	
	public static Image title;
	public static ImageView titleV;
	
	public static InputStream fontStream, fontStream2, fontStream3, fontStream4;
	public static Font font, font2, font3, font4;
	

	public static void load() {

		fontStream = Images.class.getResourceAsStream("Prototype.ttf");

		fontStream2 = Images.class.getResourceAsStream("Prototype.ttf");
		fontStream3 = Images.class.getResourceAsStream("Prototype.ttf");
		fontStream4 = Images.class.getResourceAsStream("Prototype.ttf");
		

		font2 = Font.loadFont(fontStream2, 30);
		font = Font.loadFont(fontStream, 40);
		font3 = Font.loadFont(fontStream3, 100);
		font4 = Font.loadFont(fontStream4, 150);
		
		title = new Image(Images.class.getResourceAsStream("Impulse3.png"));
		titleV = new ImageView(title);
	}
	
}
