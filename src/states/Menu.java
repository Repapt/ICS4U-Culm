package states;

import java.util.Arrays;

import javafx.scene.Group;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import main.Main;
import tools.Flash;
import tools.Print;

public class Menu extends GameState {
	
	Main game;
	Print startText;
	
	
	Rectangle background;
	LinearGradient backGrad;
	
	double[] gradStops;
	
	double backX = 0;
	
	int currSet = 0;
	
	String[] keys = new String[3];
	
	Print[] keySet = new Print[3];
	
	public Menu(Main g) {
		super(g);
		game = g;
		
		/*
		gradStops = new double[8];
		gradStops[1] = -2;
		gradStops[2] = -0.66;
		gradStops[3] = -1.33;
		gradStops[4] = 0;
		gradStops[5] = 0.66;
		gradStops[6] = 1.33;
		gradStops[7] = 2;
		*/
		gradStops = new double[7];
		gradStops[0] = 0;
		gradStops[1] = 1.0/6;
		gradStops[2] = 2.0/6;
		gradStops[3] = 3.0/6;
		gradStops[4] = 4.0/6;
		gradStops[5] = 5.0/6;
		gradStops[6] = 1;
		
		keys[0] = "A";
		keys[1] = "S";
		keys[2] = "D";
		
		
		for(int i=0;i<3;i++) {
			keySet[i] = new Print(40, 100*(i+1) + 50, -1, Color.WHITE, "Lane 1: " + keys[i]);
		}
		startText = new Print(40, 450, -1, Color.WHITE, "Click here to begin");
		
		backGrad = new LinearGradient(1, 0, 0, 0, true, CycleMethod.NO_CYCLE, 
				
				new Stop[] {
				/*
				new Stop(gradStops[0], Color.web("#f8bd55")),
	            new Stop(gradStops[1], Color.web("#c0fe56")),
	            new Stop(gradStops[2], Color.web("#5dfbc1")),
	            new Stop(gradStops[3], Color.web("#64c2f8")),
	            new Stop(gradStops[4], Color.web("#be4af7")),
	            new Stop(gradStops[5], Color.web("#ed5fc2")),
	            new Stop(gradStops[6], Color.web("#ef504c")),
	            new Stop(gradStops[7], Color.web("#f2660f")),
	            */
				
				new Stop(gradStops[0], Color.web("#12d19e")),
	            new Stop(gradStops[1], Color.web("#1b9be5")),
	            new Stop(gradStops[2], Color.web("#141bd1")),
	            new Stop(gradStops[3], Color.web("#5112ce")),
	            new Stop(gradStops[4], Color.web("#c10de5")),
	            new Stop(gradStops[5], Color.web("#e50dbd")),
	            new Stop(gradStops[6], Color.web("#f20450"))
	            
        
		});
				
		
		background = new Rectangle(width*8, height, backGrad);
	
				
	
	}

	@Override
	public void update(int counter) {
		
		backX += 0.005;
		//System.out.println(Arrays.toString(gradStops));

		for(int i=0;i<3;i++) {
			keySet[i].setText("Lane 1: " + keys[i]);
		}
		
		background.setX(1400*Math.sin(backX) - 1400);
		
	}

	@Override
	public void keyPress(KeyEvent event) {
		
		
		keys[currSet] = event.getText().toUpperCase();
		
		
		
	}
	
	public void click(MouseEvent event) {
		double y = event.getY();
		if(y > 100 && y < 200) {
			currSet = 0;
		} else if (y > 200 && y < 300) {
			currSet = 1;
		} else if (y > 300 && y < 400) {
			currSet = 2;
		} else if (y > 400 && y < 500) {
			game.changeState(new Playing(game, keys));
		}
	}

	@Override
	public void draw(Group group) {
		
		group.getChildren().add(background);
		
		group.getChildren().add(startText.getText());
		
		for(int i=0;i<3;i++) {
			group.getChildren().add(keySet[i].getText());
		}
	}

	@Override
	public void keyRelease(KeyEvent event) {
		
	}

}
