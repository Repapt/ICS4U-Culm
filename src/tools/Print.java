package tools;
/*
 * Author: Samuel Liu
 * Teacher: Mr. Radulovic
 * 2019/06/18
 * Class for the text that flashes on the screen on key press. Since I wanted them to only
 * appear for a limited time, I have a dur variable that represents how many frames the
 * text will stay on the screen for
 */
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

public class Print {

	Text text;
	int dur;
	int count = 0;
	
	public Print(double x, double y, int dur, Paint color, String words) {
		
		//Setting up text
		text = new Text(words);
		text.setFill(color);
		text.setX(x);
		text.setY(y);
		text.setFont(Resources.font);
		this.dur = dur;
		
	}
	
	public int update() {
		count ++;
		//increases count until it is equal to duration
		if(dur == -1) { //-1 signifies that it should stay on screen forever
			return 1;
		} else if(count >= dur) {
			return -1;
		}
		return 1;
	}
	
	//get and set text
	public Text getText() {
		return text;
	}
	public void setText(String toSet) {
		text.setText(toSet);
	}
	//centers text around a given mid point
	public void center(double mid) {
		text.setBoundsType(TextBoundsType.VISUAL);
		text.setX(mid - text.getBoundsInLocal().getWidth()/2);
	}
	
}
