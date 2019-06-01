package states;

import java.util.ArrayList;
import java.util.Arrays;

import beats.Conductor;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import main.Main;
import tools.Flash;
import tools.Images;
import tools.Dash;
import tools.Print;

public class Menu extends GameState {
	
	Main game;
	Menu menu;
	
	Rectangle background;
	LinearGradient backGrad;
	
	Conductor conductor;
	
	double[] gradStops;
	
	double backY = 0;
	
	int currSet = 0;
	
	int numSongs;

	DropShadow shadow;
	//Color highlight = Color.web("#86cbd8");
	Color highlight = Color.web("b5d9e0");
	
	String[] keys = new String[4];
	
	Print[] keySet = new Print[4];
	
	Text[] nav = new Text[3];
	
	Print[] songNames;
	
	Rectangle[] buttons;
	
	ArrayList<Dash> lines = new ArrayList<Dash>();
	
	ImageView title;
	
	int page = 0;
	
	public Menu(Main g) throws Exception{
		
		super(g);
		game = g;	
		menu = this;
		
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
		title.setY(50);
		
		nav[2] = new Text("back");
		nav[0] = new Text("Controls");
		nav[1] = new Text("Song Select");
		
		shadow = new DropShadow();
		shadow.setColor(Color.web("49a0be"));
		
		buttons = new Rectangle[3];
		
		for(int i=0;i<3;i++) {
			nav[i].setFont(Images.font2);
			nav[i].setY(325 + 100*i);
			nav[i].setFill(highlight);
			//nav[i].setBoundsType(TextBoundsType.VISUAL);
			nav[i].setX(200 - nav[i].getBoundsInLocal().getWidth()/2);
			nav[i].setStroke(Color.web("49a0be"));
			nav[i].setStrokeWidth(1);
			nav[i].setEffect(shadow);
			
			buttons[i] = new Rectangle(nav[i].getBoundsInLocal().getWidth()*1.2,nav[i].getBoundsInLocal().getHeight()*1.2,highlight);
			buttons[i].setX(200 - buttons[i].getWidth()/2);
			buttons[i].setY(325 + 100*i -buttons[i].getHeight() + nav[i].getBoundsInLocal().getHeight()/2);
			buttons[i].setStroke(Color.web("49a0be"));
			buttons[i].setStrokeWidth(1);
			buttons[i].setEffect(shadow);
			buttons[i].setOpacity(0);
			
		}
		
		
		
		
		for(int i=0;i<3;i++) {
			keySet[i] = new Print(40, 75*(i+3), -1, Color.WHITE, "Lane 1: " + keys[i]);
		}
		keySet[3] = new Print(40, 75*6, -1, Color.WHITE, "Pause: " + keys[3]);
		Color thing = Color.web("#7b0b7c");
		backGrad = new LinearGradient(1, 1, 1, 0, true, CycleMethod.NO_CYCLE, 
				new Stop[] {
				new Stop(gradStops[0], thing),
				new Stop(gradStops[1], Color.web("#000000")),
				new Stop(gradStops[2], thing),
	            new Stop(gradStops[3], Color.web("#000000"))    
        
		});
				
		background = new Rectangle(width, height*3, backGrad);
		
		numSongs = conductor.getSongList().length;
		songNames = new Print[numSongs];
		
		for(int i=0;i<numSongs; i++) {
			String song = conductor.getSongList()[i];
			songNames[i] = new Print(40, 75*(i+3), -1, Color.WHITE, song);
		}
	
				
	
	}

	@Override
	public void update(int counter) {
		
		backY += 0.01;
		background.setY(600*Math.sin(backY) - 600);
		
		if(Math.random() < 0.3) {
			lines.add(new Dash());
		}
		
		for(int i=0;i<lines.size();i++) {
			Dash curr = lines.get(i);
			curr.update();
			if(curr.getX() > 400 || curr.getY() > 600 || curr.getY() < 0 || curr.getX() < 0) {
				lines.remove(i);
			}
		}
		
		//System.out.println(Arrays.toString(gradStops));
		if(page == 1) {
			for(int i=0;i<3;i++) {
				keySet[i].setText("Lane " + (i+1) + ": " + keys[i]);
			}
			keySet[3].setText("Pause: " + keys[3]);
		}
		
		
		
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
				mouseReset();
			}
		} else if (page == 0) {
			if(page == 0) {
				if(y > buttons[0].getY() && y < buttons[0].getY() + buttons[0].getHeight()) {
					page = 1;
					mouseReset();
				} else if(y > buttons[1].getY() && y < buttons[1].getY() + buttons[1].getHeight()) {
					page = 2;
					mouseReset();
				}
			}
			
		} else if(page ==2) {
			
			if(y > 175 && y < 250) {
				conductor.setSong(0);
				game.refreshCounter();
				game.changeState(new Playing(game, keys, conductor));
			} else if (y > 250 && y < 325) {
				conductor.setSong(1);
				game.refreshCounter();
				game.changeState(new Playing(game, keys, conductor));
			} else if(y > 325 && y < 400){ 
				conductor.setSong(2);
				game.refreshCounter();
				game.changeState(new Playing(game, keys, conductor));
			} else if(y > 400 && y < 450) {
				conductor.setSong(3);
				game.refreshCounter();
				game.changeState(new Playing(game, keys, conductor));
			} else if (y > 450 && y < 550) {
				page = 0;
				mouseReset();
			} 
			
			System.out.println(conductor.getSongName());
			
		}
	}
	
	public void moved(MouseEvent event) {
		double y = event.getY();
		double x = event.getX();
		mouseReset();
		if(page == 0) {
			if(y > buttons[0].getY() && y < buttons[0].getY() + buttons[0].getHeight()) {
				mousedOver(0);
			} else if(y > buttons[1].getY() && y < buttons[1].getY() + buttons[1].getHeight()) {
				mousedOver(1);
			}
		} else if(page == 1) {
			if(y > buttons[2].getY() && y < buttons[2].getY() + buttons[2].getHeight()) {
				mousedOver(2);
			}
		} else {
			if(y > buttons[2].getY() && y < buttons[2].getY() + buttons[2].getHeight()) {
				mousedOver(2);
			}
		}
	}

	@Override
	public void draw(Group group) {
		
		group.getChildren().add(background);
		
		
		Rectangle temp = new Rectangle(400, 6, Color.WHITE);
		temp.setY(100);
		//group.getChildren().add(temp);
		
		for(int i=0;i<lines.size();i++) {
			group.getChildren().add(lines.get(i).getRect());
		}

		for(int i=0;i<3;i++) {
			group.getChildren().add(buttons[i]);
		}
		
		if(page == 0) {
			group.getChildren().add(nav[0]);
			group.getChildren().add(nav[1]);
		} else if(page == 1) {
			for(int i=0;i<4;i++) {
				group.getChildren().add(keySet[i].getText());
			}
			group.getChildren().add(nav[2]);
		} else if (page == 2) {
			for(int i=0;i<numSongs; i++) {
				group.getChildren().add(songNames[i].getText());
			}
			
			group.getChildren().add(nav[2]);
		}
		
		group.getChildren().add(title);
	}

	@Override
	public void keyRelease(KeyEvent event) {
		
	}
	
	public void mousedOver(int num) {
		nav[num].setFill(Color.web("013b53"));
		nav[num].setEffect(null);
		nav[num].setStrokeWidth(0);
		buttons[num].setOpacity(1);
	}
	
	public void mouseReset() {
		for(int i=0;i<3;i++) {

			nav[i].setStroke(Color.web("49a0be"));
			nav[i].setStrokeWidth(1);
			nav[i].setEffect(shadow);

			nav[i].setFill(highlight);
			buttons[i].setOpacity(0);
		}
	}

}
