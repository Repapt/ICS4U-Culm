package tools;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Print {

	
	Text text;
	int dur;
	int count = 0;
	
	public Print(double x, double y, int dur, Paint color, String words) {
		
		text = new Text(words);
		text.setFill(color);
		text.setX(x);
		text.setY(y);
		text.setFont(Font.font("roberto", FontWeight.BOLD, 30));
		this.dur = dur;
		
	}
	
	public int update() {
		count ++;
		
		if(dur == -1) {
			return 1;
		} else if(count >= dur) {
			return -1;
		}
		return 1;
	}
	
	public Text getText() {
		return text;
	}
	
	public void setText(String toSet) {
		text.setText(toSet);
	}
	
}
