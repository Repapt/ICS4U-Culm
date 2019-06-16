package states;

import java.util.ArrayList;
import java.util.Arrays;

import beats.Conductor;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import main.Main;
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
	
	Text[] keySet = new Text[4];
	
	Text[] nav = new Text[3];
	
	Text[] songNames, artists;
	
	Rectangle[] buttons, keyButtons, songButtons;
	
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
		keyButtons = new Rectangle[4];
		
		nav[0].setY(325);
		nav[1].setY(400);
		nav[2].setY(525);
		
		for(int i=0;i<3;i++) {
			nav[i].setFont(Images.font2);
			nav[i].setFill(highlight);
			//nav[i].setBoundsType(TextBoundsType.VISUAL);
			nav[i].setX(200 - nav[i].getBoundsInLocal().getWidth()/2);
			nav[i].setStroke(Color.web("49a0be"));
			nav[i].setStrokeWidth(1);
			nav[i].setEffect(shadow);
			
			buttons[i] = new Rectangle(nav[i].getBoundsInLocal().getWidth()*1.2,nav[i].getBoundsInLocal().getHeight()*1.2,highlight);
			buttons[i].setX(200 - buttons[i].getWidth()/2);
			buttons[i].setY(nav[i].getY() -buttons[i].getHeight() + nav[i].getBoundsInLocal().getHeight()/2);
			buttons[i].setStroke(Color.web("49a0be"));
			buttons[i].setStrokeWidth(1);
			buttons[i].setEffect(shadow);
			buttons[i].setOpacity(0);
			
		}
		keySet[0] = new Text("Lane 1: " + keys[0]);
		keySet[1] = new Text("Lane 2: " + keys[0]);
		keySet[2] = new Text("Lane 3: " + keys[0]);
		keySet[3] = new Text("Pause: " + keys[0]);
		for(int i=0;i<4;i++) {

			keySet[i].setFont(Images.font2);
			keySet[i].setX(200 - keySet[i].getBoundsInLocal().getWidth()/2);
			keySet[i].setY(70*(i+3) + 40);
			keySet[i].setFill(highlight);
			keySet[i].setStroke(Color.web("49a0be"));
			keySet[i].setStrokeWidth(1);
			keySet[i].setEffect(shadow);
			
			keyButtons[i] = new Rectangle(keySet[i].getBoundsInLocal().getWidth()*1.2,keySet[i].getBoundsInLocal().getHeight()*1.2,highlight);
			keyButtons[i].setX(200 - keyButtons[i].getWidth()/2);
			keyButtons[i].setY(70*(i+3) + 40 -keyButtons[i].getHeight() + keySet[i].getBoundsInLocal().getHeight()/2);
			keyButtons[i].setStroke(Color.web("49a0be"));
			keyButtons[i].setStrokeWidth(1);
			keyButtons[i].setEffect(shadow);
			keyButtons[i].setOpacity(0);
		}
		
		
		//keySet[3] = new Print(40, 75*6, -1, Color.WHITE, "Pause: " + keys[3]);
		Color thing = Color.web("#370138");
		backGrad = new LinearGradient(1, 1, 1, 0, true, CycleMethod.NO_CYCLE, 
				new Stop[] {
				new Stop(gradStops[0], thing),
				new Stop(gradStops[1], Color.web("#000000")),
				new Stop(gradStops[2], thing),
	            new Stop(gradStops[3], Color.web("#000000"))    
        
		});
				
		background = new Rectangle(width, height*3, backGrad);
		
		
		numSongs = conductor.getSongList().length;
		songNames = new Text[numSongs];
		artists = new Text[numSongs];
		songButtons = new Rectangle[numSongs];
		
		for(int i=0;i<numSongs; i++) {
			String song = conductor.getSongList()[i];
			songNames[i] = new Text(song);
			songNames[i].setFont(Images.font5);
			songNames[i].setX(40);
			songNames[i].setY(70*(i+3) + 40);
			songNames[i].setFill(highlight);
			songNames[i].setStroke(Color.web("49a0be"));
			songNames[i].setStrokeWidth(1);
			songNames[i].setEffect(shadow);
			
			artists[i] = new Text(conductor.getArtistList()[i]);
			artists[i].setFont(Images.superSmall);
			artists[i].setX(40);
			artists[i].setY(70*(i+3) + songNames[i].getBoundsInLocal().getHeight() + 7);
			artists[i].setFill(highlight);
			artists[i].setStroke(Color.web("49a0be"));
			artists[i].setStrokeWidth(0.5);
			artists[i].setEffect(shadow);
			
			double buttonW = Math.max(songNames[i].getBoundsInLocal().getWidth()*1.1, artists[i].getBoundsInLocal().getWidth()*1.1);
			
			songButtons[i] = new Rectangle(buttonW,songNames[i].getBoundsInLocal().getHeight()*1.2,highlight);
			songButtons[i].setX(30 + buttonW/2.2 - songButtons[i].getWidth()/2);
			songButtons[i].setY(70*(i+3) + 40 + 7 -songButtons[i].getHeight() + songNames[i].getBoundsInLocal().getHeight()/2);
			songButtons[i].setStroke(Color.web("49a0be"));
			songButtons[i].setStrokeWidth(1);
			songButtons[i].setEffect(shadow);
			songButtons[i].setOpacity(0);
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
			if(curr.getX() > 400 || curr.getY() > 600 || curr.getY() + curr.getHeight() < 0 || curr.getX() + curr.getWidth() < 0) {
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
	
	public void dragged(MouseEvent event) {
		
	}
	public void mouseRelease(MouseEvent event) {
		
	}
	
	public void click(MouseEvent event){
		double y = event.getY();
		if(page == 1) {
			if(y > keyButtons[0].getY() && y < keyButtons[0].getY() + keyButtons[0].getHeight()) {
				currSet = 0;
			} else if (y > keyButtons[1].getY() && y < keyButtons[1].getY() + keyButtons[1].getHeight()) {
				currSet = 1;
			} else if (y > keyButtons[2].getY() && y < keyButtons[2].getY() + keyButtons[2].getHeight()) {
				currSet = 2;
			} else if (y > keyButtons[3].getY() && y < keyButtons[3].getY() + keyButtons[3].getHeight()) {
				currSet = 3;
			} else if(y > buttons[2].getY() && y < buttons[2].getY() + buttons[2].getHeight()) {
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
			
			if(y > buttons[2].getY() && y < buttons[2].getY() + buttons[2].getHeight()) {
				page = 0;
				mouseReset();
			} else {
				for(int i=0;i<numSongs;i++) {
					if(y > songButtons[i].getY() && y < songButtons[i].getY() + songButtons[i].getHeight()) {

						conductor.setSong(i);
						game.refreshCounter();
						game.changeState(new Playing(game, keys, conductor));
	
						System.out.println(conductor.getSongName());
						break;
					} 
				}
			}
			
		}
	}
	
	public void moved(MouseEvent event) {
		double y = event.getY();
		double x = event.getX();
		mouseReset();
		if(page == 0) {
			if(y > buttons[0].getY() && y < buttons[0].getY() + buttons[0].getHeight()) {
				mousedOver(page, 0);
			} else if(y > buttons[1].getY() && y < buttons[1].getY() + buttons[1].getHeight()) {
				mousedOver(page, 1);
			}
		} else if(page == 1) {
			if(y > buttons[2].getY() && y < buttons[2].getY() + buttons[2].getHeight()) {
				if(x > buttons[2].getX() && x < buttons[2].getX() + buttons[2].getWidth()) {
					mousedOver(page, 2);
				}
			} else if(y > keyButtons[0].getY() && y < keyButtons[0].getY() + keyButtons[0].getHeight()) {
				mousedOver(page, 3);
			} else if(y > keyButtons[1].getY() && y < keyButtons[1].getY() + keyButtons[1].getHeight()) {
				mousedOver(page, 4);
			} else if(y > keyButtons[2].getY() && y < keyButtons[2].getY() + keyButtons[2].getHeight()) {
				mousedOver(page, 5);
			} else if(y > keyButtons[3].getY() && y < keyButtons[3].getY() + keyButtons[3].getHeight()) {
				mousedOver(page, 6);
			}
		} else {
			if(y > buttons[2].getY() && y < buttons[2].getY() + buttons[2].getHeight()) {
				if(x > buttons[2].getX() && x < buttons[2].getX() + buttons[2].getWidth()) {
					mousedOver(page, 2);
				}
			}else if(y > songButtons[0].getY() && y < songButtons[0].getY() + songButtons[0].getHeight()) {
				mousedOver(page, 7);
			} else if(y > songButtons[1].getY() && y < songButtons[1].getY() + songButtons[1].getHeight()) {
				mousedOver(page, 8);
			} else if(y > songButtons[2].getY() && y < songButtons[2].getY() + songButtons[2].getHeight()) {
				mousedOver(page, 9);
			} else if(y > songButtons[3].getY() && y < songButtons[3].getY() + songButtons[3].getHeight()) {
				mousedOver(page, 10);
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
		
		group.getChildren().add(buttons[2]);
		
		if(page == 0) {

			group.getChildren().add(buttons[0]);
			group.getChildren().add(buttons[1]);
			
			group.getChildren().add(nav[0]);
			group.getChildren().add(nav[1]);
			
		} else if(page == 1) {
			for(int i=0;i<4;i++) {
				group.getChildren().add(keyButtons[i]);
				group.getChildren().add(keySet[i]);
			}
			group.getChildren().add(nav[2]);
		} else if (page == 2) {
			for(int i=0;i<numSongs; i++) {
				group.getChildren().add(songButtons[i]);
				group.getChildren().add(songNames[i]);
				group.getChildren().add(artists[i]);
			}
			
			group.getChildren().add(nav[2]);
		}
		
		group.getChildren().add(title);
	}

	@Override
	public void keyRelease(KeyEvent event) {
		
	}
	
	public void mousedOver(int page, int num) {
		if(page == 0) {
			nav[num].setFill(Color.web("013b53"));
			nav[num].setEffect(null);
			nav[num].setStrokeWidth(0);
			buttons[num].setOpacity(1);
		} else if (page == 1) {
			if(num == 2) {
				nav[num].setFill(Color.web("013b53"));
				nav[num].setEffect(null);
				nav[num].setStrokeWidth(0);
				buttons[num].setOpacity(1);
			} else {
				keySet[num - 3].setFill(Color.web("013b53"));
				keySet[num - 3].setEffect(null);
				keySet[num - 3].setStrokeWidth(0);
				keyButtons[num - 3].setOpacity(1);
				
			}
			
		} else if (page == 2) {
			if(num == 2) {
				nav[num].setFill(Color.web("013b53"));
				nav[num].setEffect(null);
				nav[num].setStrokeWidth(0);
				buttons[num].setOpacity(1);
			}else {
				songNames[num - 7].setFill(Color.web("013b53"));
				songNames[num - 7].setEffect(null);
				songNames[num - 7].setStrokeWidth(0);
				
				artists[num - 7].setFill(Color.web("013b53"));
				artists[num - 7].setEffect(null);
				artists[num - 7].setStrokeWidth(0);
				
				songButtons[num - 7].setOpacity(1);
				
			}
		}
	}
	
	public void mouseReset() {
		for(int i=0;i<3;i++) {

			nav[i].setStroke(Color.web("49a0be"));
			nav[i].setStrokeWidth(1);
			nav[i].setEffect(shadow);

			nav[i].setFill(highlight);
			buttons[i].setOpacity(0);
		}
		
		for(int i=0;i<4;i++) {
			keySet[i].setStroke(Color.web("49a0be"));
			keySet[i].setStrokeWidth(1);
			keySet[i].setEffect(shadow);

			keySet[i].setFill(highlight);
			keyButtons[i].setOpacity(0);
		}
		
		for(int i=0;i<numSongs;i++) {
			songNames[i].setStroke(Color.web("49a0be"));
			songNames[i].setStrokeWidth(1);
			songNames[i].setEffect(shadow);
			songNames[i].setFill(highlight);
			
			artists[i].setStroke(Color.web("49a0be"));
			artists[i].setStrokeWidth(0.5);
			artists[i].setEffect(shadow);
			artists[i].setFill(highlight);
			
			songButtons[i].setOpacity(0);
		}
	
	}

}
