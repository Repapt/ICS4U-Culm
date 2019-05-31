package states;

import java.util.ArrayList;
import java.util.Arrays;

import beats.Conductor;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import main.Main;
import tools.Flash;
import tools.Images;
import tools.Dash;
import tools.Print;

public class Menu extends GameState {
	
	Main game;
	Print startText;
	
	
	Rectangle background;
	LinearGradient backGrad;
	
	Conductor conductor;
	
	double[] gradStops;
	
	double backY = 0;
	
	int currSet = 0;
	
	int numSongs;
	
	String[] keys = new String[4];
	
	Print[] keySet = new Print[4];
	
	Print[] nav = new Print[3];
	
	Print[] songNames;
	
	ArrayList<Dash> lines = new ArrayList<Dash>();
	
	ImageView title;
	
	int page = 0;
	
	public Menu(Main g) throws Exception{
		super(g);
		game = g;
		
		conductor = new Conductor(0, height);
		
		gradStops = new double[4];
		gradStops[0] = 0;
		gradStops[1] = 1/3.0;
		gradStops[2] = 2/3.0;
		gradStops[3] = 1;
		
		keys[0] = "A";
		keys[1] = "S";
		keys[2] = "D";
		keys[3] = "P";
		
		title = Images.titleV;
		title.setPreserveRatio(true);
		title.setFitWidth(400);
		
		nav[0] = new Print(40, 500, -1, Color.WHITE, "back");
		nav[1] = new Print(40, 200, -1, Color.WHITE, "Controls");
		nav[2] = new Print(40, 300, -1, Color.WHITE, "Song Select");
		
		
		for(int i=0;i<3;i++) {
			keySet[i] = new Print(40, 75*(i+3), -1, Color.WHITE, "Lane 1: " + keys[i]);
		}
		keySet[3] = new Print(40, 75*6, -1, Color.WHITE, "Pause: " + keys[3]);
		startText = new Print(40, 500, -1, Color.WHITE, "Click here to begin");
		Color thing = Color.web("#7b0b7c");
		backGrad = new LinearGradient(1, 1, 1, 0, true, CycleMethod.NO_CYCLE, 
				new Stop[] {
				new Stop(gradStops[0], thing),
				new Stop(gradStops[1], Color.web("#000000")),
				new Stop(gradStops[2], thing),
	            new Stop(gradStops[3], Color.web("#000000"))    
        
		});
				
		background = new Rectangle(width, height*6, backGrad);
		
		numSongs = conductor.getSongList().length;
		songNames = new Print[numSongs];
		
		for(int i=0;i<numSongs; i++) {
			String song = conductor.getSongList()[i];
			songNames[i] = new Print(40, 75*(i+3), -1, Color.WHITE, song);
		}
	
				
	
	}

	@Override
	public void update(int counter) {
		
		if(Math.random() < 0.3) {
			lines.add(new Dash());
		}
		
		for(int i=0;i<lines.size();i++) {
			Dash curr = lines.get(i);
			curr.update();
			if(curr.getX() > 400 || curr.getY() > 600) {
				lines.remove(i);
			}
		}
		
		backY += 0.003;
		//System.out.println(Arrays.toString(gradStops));

		for(int i=0;i<3;i++) {
			keySet[i].setText("Lane 1: " + keys[i]);
		}
		keySet[3].setText("Pause: " + keys[3]);
		
		background.setY(1500*Math.sin(backY) - 1500);
		
	}

	@Override
	public void keyPress(KeyEvent event) {
		
		if(page == 1) {
			keys[currSet] = event.getText().toUpperCase();
		}
		
		
		
	}
	
	public void click(MouseEvent event){
		double y = event.getY();
		if(page == 1) {
			if(y > 175 && y < 250) {
				currSet = 0;
			} else if (y > 250 && y < 325) {
				currSet = 1;
			} else if (y > 325 && y < 400) {
				currSet = 2;
			} else if (y > 400 && y < 450) {
				currSet = 3;
			} else if(y > 450 && y < 550) {
				page = 0;
			}
		} else if (page == 0) {
			if(y > 150 && y < 250) {
				page = 1;
			} else if (y > 250 && y < 350) {
				page = 2;
			} else if (y > 450 && y < 550) {
				game.refreshCounter();
				game.changeState(new Playing(game, keys, conductor));
			}
		} else if(page ==2) {
			
			if(y > 175 && y < 250) {
				conductor.setSong(0);
			} else if (y > 250 && y < 325) {
				conductor.setSong(1);
			} else if(y > 325 && y < 400){ 
				conductor.setSong(2);
			} else if(y > 400 && y < 450) {
				conductor.setSong(3);
			} else if (y > 450 && y < 550) {
				page = 0;
			} 
			
			System.out.println(conductor.getSongName());
			
		}
	}

	@Override
	public void draw(Group group) {
		
		group.getChildren().add(background);
		
		for(int i=0;i<lines.size();i++) {
			group.getChildren().add(lines.get(i).getRect());
		}
		
		if(page == 0) {
			group.getChildren().add(startText.getText());
			group.getChildren().add(nav[1].getText());
			group.getChildren().add(nav[2].getText());
		} else if(page == 1) {
			for(int i=0;i<4;i++) {
				group.getChildren().add(keySet[i].getText());
			}
			group.getChildren().add(nav[0].getText());
		} else if (page == 2) {
			for(int i=0;i<numSongs; i++) {
				group.getChildren().add(songNames[i].getText());
			}
			
			group.getChildren().add(nav[0].getText());
		}
		
		group.getChildren().add(title);
	}

	@Override
	public void keyRelease(KeyEvent event) {
		
	}

}
