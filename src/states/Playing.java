package states;
/*
 * Author: Samuel Liu
 * Teacher: Mr. Radulovic
 * 2019/06/18
 * Playing class handles events when the game is going on. Handles pause/unpause/start events,
 * uses the Conductor class to determine when to generate beats.
 */
import java.util.ArrayList;
import beats.Beat;
import beats.Conductor;
import beats.DrumSound;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import main.Main;
import tools.Flash;
import tools.Resources;
import tools.Print;

public class Playing extends GameState{
	
	Main game;
	
	//array of arraylists of beats where each arraylist represents a lane
	ArrayList<Beat>[] beats = new ArrayList[3];
	
	//Make everything look prettier variables
	ArrayList<DrumSound> drums = new ArrayList<DrumSound>();
	ArrayList<Flash> flashes = new ArrayList<Flash>();
	
	Rectangle background, lanes, backMult, backAcc, complete, total, inLines;
	LinearGradient grad1, grad2, gradGoal, gradGoal2, gradGoal22, gradPerf;
	double[] gradStops; 
	double backY = 0;

	Circle[] goals = new Circle[3];
	
	Circle[] smallGoals = new Circle[3];
	
	Conductor conductor;
	
	ArrayList<Print> toPrint = new ArrayList<Print>();
	
	Text numDraw;
	
	Text[] startSeq = new Text[4];
	
	Text songName, artistName;
	
	Rectangle pauseFilter;
	Text pauseText;

	DropShadow shadow;
	
	Rectangle quitButton, resumeButton;
	Rectangle optionBack;
	Rectangle[] slideBars = new Rectangle[2];
	Rectangle[] emptySlideBars = new Rectangle[2];
	Rectangle[] sliders = new Rectangle[2];
	Text[] pauseOptions = new Text[4];
	
	Text[] keys = new Text[3];
	String[] keyString;
	
	//Game feature variables
	int score = 0;
	int streak = 0;
	int mult = 1;
	double accuracy = 0;
	int missed = 0;
	int hits;
	
	//generating beats
	double currPos = 0;
	int numBeats = 0;
	double lastTick = 0;

	
	//game flow
	long gameStart;
	boolean started = false;
	boolean paused = false;
	boolean unpausing = false;
	int unpauseCounter;
	int dragging = 0;
	int buffer = 15;
	
	
	public Playing(Main g, String[] keys, Conductor cond) {
		
		super(g);
		
		game = g;
		
		//setting up scene composition
		songName = new Text(cond.getSongName());
		songName.setX(50);
		songName.setY(35);
		songName.setFill(Color.WHITE);
		songName.setFont(Resources.font5);
		
		artistName = new Text(cond.getArtistName());
		artistName.setX(50);
		artistName.setY(15 + songName.getY());
		artistName.setFill(Color.WHITE);
		
		//I used my own class, Print, for simplicity. These probably could all just be Texts
		Print scoreText = new Print(50, 120, -1, Color.WHITE, "Score: " + score);
		scoreText.center(200);
		Print multText = new Print(25, 435, -1, Color.WHITE, "X1");
		Print streakText = new Print(25, 465, -1, Color.WHITE, "" + streak);
		Print accText = new Print(305, 450, -1, Color.WHITE, "0%");
		toPrint.add(scoreText);
		toPrint.add(multText);
		toPrint.add(streakText);
		toPrint.add(accText);
		
		//setting up background gradient
		//Used an array for easy tweaking
		gradStops = new double[5];
		gradStops[0] = 0;
		gradStops[1] = 0.25;
		gradStops[2] = 0.5;
		gradStops[3] = 0.8;
		gradStops[4] = 1;
		
		grad1 = new LinearGradient(0f, 1f, 1f, 0f, true, CycleMethod.NO_CYCLE, 
				new Stop[]{
				            //new Stop(gradStops[0], Color.web("#64c2f8")),
				            new Stop(gradStops[0], Color.web("5f89b7")),
				            new Stop(gradStops[1], Color.web("#ed5fc2")),
				            new Stop(gradStops[2], Color.web("#be4af7")),
				           // new Stop(gradStops[2], Color.web ("#370138")),
				       
				            new Stop(gradStops[3], Color.web("ed5fc2")),
				           // new Stop(gradStops[3], Color.web("#517399")),
				            new Stop(gradStops[4], Color.web("#5f89b7")),
				});
		
		//The background is a rectangle 5x longer than the screen, and the rectangle just
		//moves up and down to simulate a moving gradient in the background
		background = new Rectangle(width, height*5, grad1);
		
		//various gradients used
		grad2 = new LinearGradient(1, 1, 0, 0, true, CycleMethod.NO_CYCLE, 
				new Stop[]{
				           
			            new Stop(0, Color.web("#ffe2fc")),
			            new Stop(0.2, Color.web("#db27c8")),
			            new Stop(0.4, Color.web("#d850c7")),
			            new Stop(1, Color.web("#ffe0fb")),
			});
		
		gradGoal = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, 
				new Stop[]{
				           
				            new Stop(0, Color.web("#ffe2fc")),
				            new Stop(0.2, Color.web("#db27c8")),
				            new Stop(0.4, Color.web("#d850c7")),
				            new Stop(1, Color.web("#ffe0fb")),
				});
		
		//gold
		gradGoal2 = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, 
				new Stop[]{
				           
			            new Stop(0, Color.web("#fffde2")),
			            new Stop(0.2, Color.web("#dbc627")),
			            new Stop(0.4, Color.web("#d8c550")),
			            new Stop(1, Color.web("#fffde0")),
			});
		
		//gold but slightly translucent
		gradGoal22 = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, 
				new Stop[]{
				           
			            new Stop(0, Color.web("#fffde2", 0.85)),
			            new Stop(0.2, Color.web("#dbc627", 0.85)),
			            new Stop(0.4, Color.web("#d8c550", 0.85)),
			            new Stop(1, Color.web("#fffde0", 0.85)),
			});
		
		//gold but for the texts
		gradPerf = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, 
				new Stop[]{
				           
			            new Stop(0, Color.web("#e5b30d")),
			            new Stop(0.2, Color.web("#f4de95")),
			            new Stop(1, Color.web("#edb809")),
			});
	
		//formatting stuff to look good
		keyString = new String[4];
	
		pauseText = new Text("PAUSED");
		startSeq[0] = new Text("3");
		startSeq[1] = new Text("2");
		startSeq[2] = new Text("1");
		startSeq[3] = new Text("GO!");

		numDraw = startSeq[0];

		pauseText.setFont(Resources.font3);
		colorText(pauseText);
		pauseText.setY(pauseText.getY() - 150);
	
		optionBack = new Rectangle(300, 225, Color.web("#4e2a66", 0.8));
		optionBack.setX(50);
		optionBack.setY(250);
		optionBack.setStroke(Color.web("#3c1951", 0.8));
		optionBack.setStrokeWidth(5);
		pauseOptions[0] = new Text("Music");
		pauseOptions[1] = new Text("Beats");
		pauseOptions[2] = new Text("Resume");
		pauseOptions[3] = new Text("Quit");
		
		for(int i=0;i<4;i++) {
			keyString[i] = keys[i];
			startSeq[i].setFont(Resources.font4);
			colorText(startSeq[i]);

			pauseOptions[i].setFont(Resources.font2);
			pauseOptions[i].setX(70);
			pauseOptions[i].setStroke(Color.web("49a0be"));
			pauseOptions[i].setFill(Color.web("b5d9e0"));
			pauseOptions[i].setBoundsType(TextBoundsType.VISUAL);
			pauseOptions[i].setStrokeWidth(1);
			pauseOptions[i].setEffect(shadow);
			pauseOptions[i].setY(300 + (i*75));
		}

		pauseOptions[2].setX(80);
		pauseOptions[3].setX(240);
		pauseOptions[3].setY(pauseOptions[2].getY());
		

		for(int i=0;i<3;i++) {
			goals[i] = new Circle(100*(i+1), 500, 50, null);
			goals[i].setStroke(gradGoal);
			goals[i].setStrokeWidth(5);
			goals[i].setStrokeType(StrokeType.INSIDE);
			
			smallGoals[i] = new Circle(100*i + 100, 500, 25, grad2);
			
			
			this.keys[i] = new Text(keyString[i]);
			this.keys[i].setBoundsType(TextBoundsType.VISUAL);
			this.keys[i].setFill(Color.web("#3d0f5b", 0.2));
			this.keys[i].setFont(Font.font("roberto", FontWeight.BOLD, 120));
			
			this.keys[i].setX(100*(i+1) -(this.keys[i].getBoundsInLocal().getWidth()/2));
			this.keys[i].setY(400 + (this.keys[i].getBoundsInLocal().getHeight()/2));
			
			beats[i] = new ArrayList<Beat>();
			
		}
		
		for(int i=0;i<2;i++) {
			slideBars[i] = new Rectangle(150, 5, gradGoal);
			slideBars[i].setX(165);
			slideBars[i].setY(290 + (i*75));
			emptySlideBars[i] = new Rectangle(150, 5, Color.web("#3e324c"));
			emptySlideBars[i].setX(165);
			emptySlideBars[i].setY(290 + (i*75));
			emptySlideBars[i].setStrokeType(StrokeType.INSIDE);
			emptySlideBars[i].setStroke(gradGoal);
			sliders[i] = new Rectangle(30, 30, Color.web("013b53"));
			sliders[i].setStroke(Color.web("b5d9e0"));
			sliders[i].setStrokeWidth(2);
			sliders[i].setEffect(shadow);
			sliders[i].setY(slideBars[i].getY() - sliders[i].getHeight()/2 + slideBars[i].getHeight()/2);
			sliders[i].setArcHeight(20);
			sliders[i].setArcWidth(20);
		}
		
		//still just javafx formatting to make the text look nice
		quitButton = new Rectangle(pauseOptions[3].getBoundsInLocal().getWidth()*1.2,pauseOptions[3].getBoundsInLocal().getHeight(),Color.web("b5d9e0"));
		quitButton.setX(pauseOptions[3].getX() + pauseOptions[3].getBoundsInLocal().getWidth()/2 - quitButton.getWidth()/2 - 5);
		quitButton.setY(pauseOptions[3].getY() -quitButton.getHeight() + pauseOptions[3].getBoundsInLocal().getHeight()/2 - 10);
		quitButton.setStroke(Color.web("49a0be"));
		quitButton.setStrokeWidth(1);
		quitButton.setEffect(shadow);
		quitButton.setOpacity(0);
		resumeButton = new Rectangle(pauseOptions[2].getBoundsInLocal().getWidth()*1.2,pauseOptions[2].getBoundsInLocal().getHeight(),Color.web("b5d9e0"));
		resumeButton.setX(pauseOptions[2].getX() + pauseOptions[2].getBoundsInLocal().getWidth()/2 - resumeButton.getWidth()/2 - 5);
		resumeButton.setY(pauseOptions[2].getY() -resumeButton.getHeight() + pauseOptions[2].getBoundsInLocal().getHeight()/2 - 10);
		resumeButton.setStroke(Color.web("49a0be"));
		resumeButton.setStrokeWidth(1);
		resumeButton.setEffect(shadow);
		resumeButton.setOpacity(0);
		
		
		//more javafx setup
		pauseFilter = new Rectangle(width, height, Color.web("#553c66", 0.7));
		
		//gray boxes
		backMult = new Rectangle(100, 75, Color.web("#3e324c", 0.8));
		backMult.setY(400);
		
		backAcc = new Rectangle(100, 75, Color.web("#3e324c", 0.8));
		backAcc.setY(400);
		backAcc.setX(width - 100);
		
		//progress bar at the top
		complete = new Rectangle(0, 10, gradGoal);
		total = new Rectangle(width, 10, Color.web("#3e324c"));
		
		//background where the beats fall
		lanes = new Rectangle(300, height + 50, Color.web("#773272", 0.65));
		lanes.setX(50);
		lanes.setStroke(Color.web("#513a5b", 0.8));
		lanes.setStrokeWidth(5);
		lanes.setStrokeType(StrokeType.OUTSIDE);
		
		inLines = new Rectangle(100, 700, null);
		inLines.setX(150);
		inLines.setStroke(Color.web("#513a5b", 0.5));
		inLines.setStrokeWidth(3);
		
		
				
		try {
			conductor = cond;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	
		
	}
	
	@Override
	public void update(int counter) {
		
		//changes progression bar size based on how long the song has been playing for
		complete.setWidth(width * (conductor.songPosition()/conductor.songLength()));
		
		//changes volume bar size on the pause screen
		//this can be optimized to only run when a change is made
		slideBars[0].setWidth(150 * conductor.getSongVol());
		slideBars[1].setWidth(150 * conductor.getBeatVol());
		sliders[0].setX(slideBars[0].getX() + slideBars[0].getWidth() - sliders[0].getWidth()/2);
		sliders[1].setX(slideBars[1].getX() + slideBars[1].getWidth() - sliders[0].getWidth()/2);
		
		//moves the background (Math.sin moves between 1 and -1)
		backY += 0.005;
		background.setY(1200*Math.sin(backY) - 1200);
		
		if(conductor.songPosition() > lastTick + 60/conductor.getBPM()) {
			lastTick += 60/conductor.getBPM();
			numBeats ++;
		}
		
		if(!started) {
			
			try {
				//starting countdown
				startAnim(counter);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} else if(paused) {
			//special method called when there's a pause
			pauseUpdate();
		} else {
			//method that handles the 'beats'
			genBeats();
			
			//updates accuracy
			if(missed + hits == 0) {
				accuracy = 0;
			} else {
				accuracy = ((double)(hits))/((missed + hits));
			}
			
			//multiplier increases based on streak
			if (streak > 31) {
				mult = 8;
				//after a 32 streak, gold accents are added to the screen
				goldMode();
			} else if(streak > 15) {
				mult = 4;
			} else if(streak > 7) {
				mult = 2;
			} 
			//updates score, multiplier, accuracy Texts
			toPrint.get(0).setText("Score: " + score);
			toPrint.get(0).center(200);
			toPrint.get(1).setText("X" + mult);
			toPrint.get(2).setText("" + streak);
			toPrint.get(3).setText(String.format("%.0f", accuracy*100)+ "%");
			
			
			//removes drum sounds from drums arraylist when they're done to avoid
			//the arraylist getting too big
			//we can ensure that the drum at index 0 will always finish first
			for(int i=0;i<drums.size();i++) {
				if(drums.get(0).isEnded()) {
					drums.remove(0);
				} else {
					break;
				}
			}
			
			//conductor.update() returns 1 when the song is finished
			if(conductor.update() == 1) {
				
				try {
					//adds score to highscores text file
					Resources.write(conductor.getSongName() + " - " + score + "\n");
					//returns to menu
					game.changeState(new Menu(game));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
		
		
		
		
	}
	@Override
	public void draw(Group group) {
		//just adding everything to the scene
		
		group.getChildren().add(background);
		group.getChildren().add(lanes);
		group.getChildren().add(inLines);
		
		//handles flashes that occur on key press
		for(int i=0;i<flashes.size();i++) {
			Flash flash = flashes.get(i);
			if(flash.update() == -1) {
				flashes.remove(i);
			} else {
				group.getChildren().add(flash.showFlash());
			}
		}

		for(int i=0;i<3;i++) {
			group.getChildren().add(keys[i]);
			group.getChildren().add(goals[i]);
			group.getChildren().add(smallGoals[i]);
			
			
		}
		group.getChildren().add(total);
		group.getChildren().add(complete);
		
		
		//runs through every lane, and every beat in that respective lane
		for(int i=0;i<3;i++) {
			ArrayList<Beat> curr = beats[i];
			for(int j=0;j<curr.size();j++) {
				Beat currBeat = curr.get(j);
				if(!paused) {
					if(currBeat.getY() > 490 && currBeat.getY() < 510) {
						//the beat briefly pauses during the 'perfect' spot to make the game
						//more lenient
						currBeat.move(false);
					} else {
						currBeat.move(true);
					}
				}
				
				group.getChildren().add(currBeat.circle);
				group.getChildren().add(currBeat.iView);
				if(currBeat.getY() > height-1) {
					//removes beat when they go off screen and considers it a 'miss'
					toPrint.add(new Print(currBeat.getX()-25, currBeat.getY()-25, 15, Color.RED, "miss"));
					curr.remove(j);

					miss();
				}
			}
		}

		group.getChildren().add(backAcc);
		group.getChildren().add(backMult);
		
		for(int i=0;i<toPrint.size();i++) {
			Print curr = toPrint.get(i);
			if(curr.update() == -1) {
				//update returns -1 when the duration is up
				toPrint.remove(i);
			} else {
				group.getChildren().add(curr.getText());
			}
		}
		group.getChildren().add(songName);
		group.getChildren().add(artistName);
		if(!started || paused) {
			//numDraw includes 'paused' text as well as countdown text
			group.getChildren().add(pauseFilter);
			
			group.getChildren().add(numDraw);
			
			if(paused && !unpausing) {
				//pause screen, only shows when it's paused
				//unpausing refers to when the starting sequence is playing after a pause
				group.getChildren().add(optionBack);
				group.getChildren().add(quitButton);
				group.getChildren().add(resumeButton);
				for(int i=0;i<4;i++) {
					group.getChildren().add(pauseOptions[i]);
					if(i<2) {
						group.getChildren().add(emptySlideBars[i]);
						group.getChildren().add(slideBars[i]);
						group.getChildren().add(sliders[i]);
					}
				}
			}
			
		}
		
		
		
		
	}
	
	@Override
	public void keyRelease(KeyEvent event) {
	}
	
	@Override
	public void dragged(MouseEvent event) {
		//only necessary for volume drag bars on the pause screen
		if(paused && !unpausing) {
			double x = event.getX();
			double y = event.getY();
			//the first time dragged is called, dragging is 0
			if(dragging == 0) {
				//buffer is there so you don't have to click exactly on the drag bar to drag it
				if(y + buffer > emptySlideBars[0].getY() && y - buffer < emptySlideBars[0].getY() + emptySlideBars[0].getHeight()) {
					if(x + buffer > emptySlideBars[0].getX() && x - buffer < emptySlideBars[0].getX() + emptySlideBars[0].getWidth()) {
						double newVol = (x-emptySlideBars[0].getX())/150;
						if(newVol < 0) {
							newVol = 0;
						} else if(newVol > 1) {
							newVol = 1;
						}
						conductor.setSongVol(newVol);
						slideBars[0].setWidth(newVol*150);
						dragging = 1;
					}
				}else if(y + buffer > emptySlideBars[1].getY() && y - buffer < emptySlideBars[1].getY() + emptySlideBars[1].getHeight()) {
					if(x + buffer > emptySlideBars[1].getX() && x - buffer < emptySlideBars[1].getX() + emptySlideBars[1].getWidth()) {
						double newVol = (x-emptySlideBars[1].getX())/150;
						if(newVol < 0) {
							newVol = 0;
						} else if(newVol > 1) {
							newVol = 1;
						}
						conductor.setBeatVol(newVol);
						slideBars[1].setWidth(newVol*150);
						dragging = 2;
					}
				}
				//subsequent dragged calls after the first one automatically refer to the
				//original drag target until the drag action is completed
				//this allows you to drag and hold on to a volume bar, then move around the
				//screen while still moving the volume bar
			} else if(dragging == 1) {
				double newVol = (x-emptySlideBars[0].getX())/150;
				if(newVol < 0) {
					newVol = 0;
				} else if(newVol > 1) {
					newVol = 1;
				}
				conductor.setSongVol(newVol);
				slideBars[0].setWidth(newVol*150);
				sliders[0].setFill(Color.web("b5d9e0"));
				sliders[0].setStroke(Color.web("49a0be"));
			} else if(dragging == 2) {
				double newVol = (x-emptySlideBars[1].getX())/150;
				if(newVol < 0) {
					newVol = 0;
				} else if(newVol > 1) {
					newVol = 1;
				}
				conductor.setBeatVol(newVol);
				slideBars[1].setWidth(newVol*150);
				sliders[1].setFill(Color.web("b5d9e0"));
				sliders[1].setStroke(Color.web("49a0be"));
			}
		}
	}
	
	@Override
	public void mouseRelease(MouseEvent event) {
		
	}
	@Override
	public void click(MouseEvent event) {
		double x = event.getX();
		double y = event.getY();
		if(paused && !unpausing) {
			//a click ends dragging if the action was still ongoing
			if(dragging == 1 || dragging == 2) {

				sliders[dragging-1].setFill(Color.web("013b53"));
				sliders[dragging-1].setStroke(Color.web("b5d9e0"));
				dragging = 0;
			} else {
				//checks if the click occurred within the quit/resume button
				//if so, it returns to main menu/starts the unpausing sequence
				if(y > quitButton.getY() && y < quitButton.getY() + quitButton.getHeight()) {
					if(x > quitButton.getX() && x < quitButton.getX() + quitButton.getWidth()) {
						try {
							drums.add(new DrumSound(conductor.getBeatVol(), 2));
							game.changeState(new Menu(game));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if(x > resumeButton.getX() && x < resumeButton.getX() + resumeButton.getWidth()) {
						if(paused && !unpausing) {
							drums.add(new DrumSound(conductor.getBeatVol(), 2));
							unpausing = true;
							unpauseCounter = 0;
						} else {
							paused = true;
						}
					}
					
					//clicking on the slide bars automatically moves it to where you clicked
				} else if(y + buffer> emptySlideBars[0].getY() && y -buffer < emptySlideBars[0].getY() + emptySlideBars[0].getHeight()) {
					if(x + buffer > emptySlideBars[0].getX() && x -buffer < emptySlideBars[0].getX() + emptySlideBars[0].getWidth()) {
						double newVol = (x-emptySlideBars[0].getX())/150;
						conductor.setSongVol(newVol);
						slideBars[0].setWidth(newVol*150);
					}
				}else if(y + buffer> emptySlideBars[1].getY() && y - buffer< emptySlideBars[1].getY() + emptySlideBars[1].getHeight()) {
					if(x + buffer> emptySlideBars[1].getX() && x - buffer < emptySlideBars[1].getX() + emptySlideBars[1].getWidth()) {
						double newVol = (x-emptySlideBars[1].getX())/150;
						conductor.setBeatVol(newVol);
						slideBars[1].setWidth(newVol*150);
					}
				}
			}
				
			
			
		}
	}
	
	@Override
	public void keyPress(KeyEvent event) {
		String key  = event.getText().toUpperCase();
		//checks the lane that corresponds with the key that was pressed
		if(key.equals(keys[0].getText())) {
			checkHit(0);
		} else if (key.equals(keys[1].getText())) {
			checkHit(1);
		} else if (key.equals(keys[2].getText())) {
			checkHit(2);
		} else if (key.equals(keyString[3])) {
			
			if(started) {
				if(paused && !unpausing) {
					//unpauses the game if it's already paused
					unpausing = true;
					unpauseCounter = 0; //resets the counter
				} else {
					//pauses the game if it is currently running
					paused = true;
				}
			}
		}
	}
	
	@Override
	public void moved(MouseEvent event) {
		//makes the buttons glow when you mouse over them
		double x = event.getX();
		double y = event.getY();
		if(paused && !unpausing) {
			pauseOptions[3].setFill(Color.web("b5d9e0"));
			pauseOptions[3].setStrokeWidth(1);
			pauseOptions[3].setEffect(shadow);
			quitButton.setOpacity(0);
			
			pauseOptions[2].setFill(Color.web("b5d9e0"));
			pauseOptions[2].setStrokeWidth(1);
			pauseOptions[2].setEffect(shadow);
			resumeButton.setOpacity(0);
			if(y > quitButton.getY() && y < quitButton.getY() + quitButton.getHeight()) {
				if(x > quitButton.getX() && x < quitButton.getX() + quitButton.getWidth()) {
					pauseOptions[3].setFill(Color.web("013b53"));
					pauseOptions[3].setEffect(null);
					pauseOptions[3].setStrokeWidth(0);
					quitButton.setOpacity(1);
				} else if(x > resumeButton.getX() && x < resumeButton.getX() + resumeButton.getWidth()) {
					pauseOptions[2].setFill(Color.web("013b53"));
					pauseOptions[2].setEffect(null);
					pauseOptions[2].setStrokeWidth(0);
					resumeButton.setOpacity(1);
				}
			}
		}
	}
	
	private void checkHit(int lane) {
		if(!paused) { //only works if the game is actually running
			int len = beats[lane].size();
			
			//adds a drum sound and a flash
			if(numBeats % 4 == 0) {

				drums.add(new DrumSound(conductor.getBeatVol(), 2));
			} else {
				drums.add(new DrumSound(conductor.getBeatVol(), 1));
			}
			flashes.add(new Flash(lane, streak));
	
	
			if(len == 0) { //if there's nothing in the lane
				miss();
				toPrint.add(new Print(100*(lane+1), 440, 10, Color.RED, "miss"));
			} else {
				
				Beat curr = beats[lane].get(0);
				double y = curr.getY();
				
				//goes through the array until it finds the closest one to the 
				//bottom of the screen that is still within the 'hit' range
				//we can ensure that each beat is higher up than the last
				for(int i=0;i<len;i++) {
					curr = beats[lane].get(i);
					y = curr.getY();
					if(!(curr.getY() > 590)) {
						break;
					}
				}
				//determines how good the hit was based on how far away it was from the
				//perfect spot
				if(y < 560 && y > 440) {
					
					if(y < 510 && y > 490) {
						score += 100*mult;
						toPrint.add(new Print(curr.getX()-40, curr.getY()-25, 15, gradPerf, "perfect!"));
	
					} else {
						score += 50*mult;
						toPrint.add(new Print(curr.getX()-40, curr.getY()-25, 15, Color.GREEN, "good!"));
	
					}
					//if it's close enough, it counts as a hit
					beats[lane].remove(0);
					streak ++;
					hits ++;
				} else {
					//if it's not close enough, it's a miss
					miss();
					toPrint.add(new Print(100*(lane+1), 440, 10, Color.RED, "miss"));
				}
			}
		}
	}
	
	public void miss() {
		//handles what happens when you miss a beat
		missed ++;
		purpleMode();
		streak = 0;
		mult = 1;
		
	}
	public void genBeats() {
		
		if(Resources.beats.size() == 0) {
		
		} else {
			//getting the information of the next beat
			String next = Resources.beats.get(0);
			int space = next.indexOf(" ");
			int time = Integer.parseInt(next.substring(0,space));
					
			currPos = conductor.songPosition();
			//generates a beat when the timestamp of the beat matches the timestamp of the song
			//minus travel time, since the beat begins at the top of the screen and needs
			//to travel to the bottom of the screen before it is supposed to be in sync with the
			//song
		
			if(currPos > time*conductor.getTickSize() - conductor.getTravelTime()) {
				
				//this for loop checks all subsequent beats in the array to check if there
				//are any beats that occur at the same time. 
				
				for(int i=0;i<Resources.beats.size();i++) {
					
					String next2 = Resources.beats.get(0);
					int space2 = next2.indexOf(" ");
					int time2 = Integer.parseInt(next2.substring(0,space2));
					int key2 = Integer.parseInt(next2.substring(space2 + 1));
					
					if(time2 == time) {
						
						int lane;
						
						//the way i formatted it, each key represents a specific lane
						if(key2 == 60) {
							lane = 0;
						} else if (key2 == 62) {
							lane = 1;
						} else if (key2 == 64){
							lane = 2;
						} else {
							//for bug fixing
							System.out.println("failure @" + time);
							lane = 0;
						}
						beats[lane].add(new Beat(lane, conductor.getBeatSpeed()));
			
						//removes the beat so that we don't add a beat twice
						Resources.beats.remove(0);
						
						
					} else {
						break;
					}
					
				}
				
				
				
			}
		}
	}
	
	public void goldMode() {
		//adds gold accents to the screen when you're on a 32 streak
		complete.setFill(gradGoal2);
		backAcc.setStroke(gradGoal2);
		backAcc.setStrokeWidth(3);
		backAcc.setStrokeType(StrokeType.OUTSIDE);
		
		backMult.setStroke(gradGoal2);
		backMult.setStrokeWidth(3);
		backMult.setStrokeType(StrokeType.OUTSIDE);
		
		lanes.setStroke(gradGoal22);
		lanes.setStrokeWidth(6);
	}
	
	public void purpleMode() {
		//removes gold accents when you lose your streak
		background.setFill(grad1);
		complete.setFill(gradGoal);
		
		backAcc.setStroke(null);
		backMult.setStroke(null);
		
		lanes.setStroke(Color.web("#513a5b", 0.8));
		lanes.setStrokeWidth(5);
		
		
		
	}
	public void colorText(Text text) {
		//text formatting code i use so my code doesn't get too repetitive
		//i could make better use of this by implementing it in more places
		
		shadow = new DropShadow();
		shadow.setColor(Color.web("b5d9e0"));
		shadow.setRadius(10);
		
		text.setBoundsType(TextBoundsType.VISUAL);
		text.setFill(Color.web("013b53"));
		text.setStroke(Color.web("b5d9e0"));
		text.setStrokeWidth(5);
		text.setBoundsType(TextBoundsType.VISUAL);
		text.setX(200 -(text.getBoundsInLocal().getWidth()/2));
		text.setY(400 - (text.getBoundsInLocal().getHeight()/2));
		text.setEffect(shadow);
	}
	
	//updates the numDraw variable every second so that it cycles through "3", "2", "1", "GO!"
	//and also adds a beat sound effect every second
	public void startAnim(int counter) throws Exception {
		if(counter == 1) {
			numDraw = startSeq[0];
			drums.add(new DrumSound(conductor.getBeatVol(), 1));
		} else if (counter == 60) {
			numDraw = startSeq[1];
			drums.add(new DrumSound(conductor.getBeatVol(), 1));
		} else if (counter == 120) {
			numDraw = startSeq[2];
			drums.add(new DrumSound(conductor.getBeatVol(), 1));
		} else if (counter == 180) {
			numDraw = startSeq[3];
			drums.add(new DrumSound(conductor.getBeatVol(), 2));
		} else if(counter > 240) {
			//at the end of the countdown, the song begins
			try {
				conductor.play();
			} catch(Exception e) {
				e.printStackTrace();
			}
			started = true;

			String next = Resources.beats.get(0);
			int space = next.indexOf(" ");
			int time = Integer.parseInt(next.substring(0,space));
			
			lastTick = time*conductor.getTickSize() - conductor.getTravelTime();
		}
		
		//marks the beginning of the song
		gameStart = System.nanoTime();

	}
	public void pauseUpdate() {
		if(unpauseCounter == 0) {
			conductor.pause();
			numDraw = pauseText;
		}
		if(unpausing) {
			
			unpauseCounter ++;
			//since this is called every 1/60th of a second, 60 updates is equal to one second
			//this updates the countdown once every second
			//at the end of the countdown, the game is resumed
			if(unpauseCounter == 1) {
				numDraw = startSeq[0];
				drums.add(new DrumSound(conductor.getBeatVol(), 1));
			} else if (unpauseCounter == 60) {
				numDraw = startSeq[1];
				drums.add(new DrumSound(conductor.getBeatVol(), 1));
			} else if(unpauseCounter == 120) {
				numDraw = startSeq[2];
				drums.add(new DrumSound(conductor.getBeatVol(), 1));
			} else if (unpauseCounter == 180) {
				numDraw = startSeq[3];
				drums.add(new DrumSound(conductor.getBeatVol(), 2));
			} else if (unpauseCounter > 239) {
				unpausing = false;
				paused = false;
				unpauseCounter = 0;
				conductor.unpause();
			}
		}
	}
}

