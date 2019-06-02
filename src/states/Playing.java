package states;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.sound.midi.InvalidMidiDataException;

import beats.Beat;
import beats.Conductor;
import beats.DrumSound;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;
import main.Main;
import tools.Flash;
import tools.Images;
import tools.LoadMidi;
import tools.Print;

public class Playing extends GameState{
	
	Main game;

	ArrayList<Beat>[] beats = new ArrayList[3];
	
	ArrayList<DrumSound> drums = new ArrayList<DrumSound>();
	
	ArrayList<Flash> flashes = new ArrayList<Flash>();
	
	Rectangle background, lanes, backMult, backAcc, complete, total, inLines;
	
	LinearGradient grad1, grad2, gradGoal, gradGoal2, gradGoal22, gradPerf;
	int score = 0;
	int streak = 0;
	int mult = 1;
	
	double accuracy = 0;
	int missed = 0;
	int hits;
	
	double currPos = 0;
	int numBeats = 0;
	
	Text[] keys = new Text[3];
	String[] keyString;
	
	long gameStart;
	boolean started = false;
	boolean paused = false;
	boolean unpausing = false;
	int unpauseCounter;
	
	Circle[] goals = new Circle[3];
	
	Circle[] smallGoals = new Circle[3];
	
	Conductor conductor;
	
	ArrayList<Print> toPrint = new ArrayList<Print>();
	Text scoreText;
	
	Text numDraw;
	
	Text[] startSeq = new Text[4];
	
	Rectangle pauseFilter;
	Text pauseText;

	DropShadow shadow;
	
	Rectangle quitButton;
	Rectangle optionBack;
	Rectangle[] slideBars = new Rectangle[2];
	Rectangle[] emptySlideBars = new Rectangle[2];
	Rectangle[] sliders = new Rectangle[2];
	Text[] pauseOptions = new Text[3];
	
	
	public Playing(Main g, String[] keys, Conductor cond) {
		
		super(g);
		
		game = g;
		
		Print scoreText = new Print(50, 45, -1, Color.WHITE, "Score: " + score);
		Print multText = new Print(25, 435, -1, Color.WHITE, "X1");
		Print streakText = new Print(25, 465, -1, Color.WHITE, "" + streak);
		Print accText = new Print(305, 450, -1, Color.WHITE, "0%");
		toPrint.add(scoreText);
		toPrint.add(multText);
		toPrint.add(streakText);
		toPrint.add(accText);
		
		grad1 = new LinearGradient(0f, 1f, 1f, 0f, true, CycleMethod.NO_CYCLE, 
				new Stop[]{
				            new Stop(0, Color.web("#64c2f8")),
				            new Stop(0.57, Color.web("#be4af7")),
				            new Stop(1, Color.web("#ed5fc2")),
				});
		
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
		
		gradGoal2 = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, 
				new Stop[]{
				           
			            new Stop(0, Color.web("#fffde2")),
			            new Stop(0.2, Color.web("#dbc627")),
			            new Stop(0.4, Color.web("#d8c550")),
			            new Stop(1, Color.web("#fffde0")),
			});
		
		gradGoal22 = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, 
				new Stop[]{
				           
			            new Stop(0, Color.web("#fffde2", 0.85)),
			            new Stop(0.2, Color.web("#dbc627", 0.85)),
			            new Stop(0.4, Color.web("#d8c550", 0.85)),
			            new Stop(1, Color.web("#fffde0", 0.85)),
			});
		
		gradPerf = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, 
				new Stop[]{
				           
			            new Stop(0, Color.web("#e5b30d")),
			            new Stop(0.2, Color.web("#f4de95")),
			            new Stop(1, Color.web("#edb809")),
			});
	
		keyString = new String[4];
	
		pauseText = new Text("PAUSED");
		startSeq[0] = new Text("3");
		startSeq[1] = new Text("2");
		startSeq[2] = new Text("1");
		startSeq[3] = new Text("GO!");

		pauseText.setFont(Images.font3);
		colorText(pauseText);
		pauseText.setY(pauseText.getY() - 150);
		
		for(int i=0;i<4;i++) {
			keyString[i] = keys[i];
			startSeq[i].setFont(Images.font4);
			colorText(startSeq[i]);
		}
		
		numDraw = startSeq[0];
		
		optionBack = new Rectangle(300, 225, Color.web("#4e2a66", 0.8));
		optionBack.setX(50);
		optionBack.setY(250);
		optionBack.setStroke(Color.web("#3c1951", 0.8));
		optionBack.setStrokeWidth(5);
		pauseOptions[0] = new Text("Music");
		pauseOptions[1] = new Text("Beats");
		pauseOptions[2] = new Text("Quit");
		
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
			
			pauseOptions[i].setFont(Images.font2);
			pauseOptions[i].setX(75);
			pauseOptions[i].setStroke(Color.web("49a0be"));
			pauseOptions[i].setFill(Color.web("b5d9e0"));
			pauseOptions[i].setStrokeWidth(1);
			pauseOptions[i].setEffect(shadow);
			pauseOptions[i].setY(300 + (i*75));
			
		}
		pauseOptions[2].setX(200 - pauseOptions[2].getBoundsInLocal().getWidth()/2);

		for(int i=0;i<2;i++) {
			slideBars[i] = new Rectangle(150, 5, gradGoal);
			slideBars[i].setX(165);
			slideBars[i].setY(290 + (i*75));
			emptySlideBars[i] = new Rectangle(150, 5, Color.web("#3e324c"));
			emptySlideBars[i].setX(165);
			emptySlideBars[i].setY(290 + (i*75));
			emptySlideBars[i].setStrokeType(StrokeType.INSIDE);
			emptySlideBars[i].setStroke(gradGoal);
		}
		
		
		quitButton = new Rectangle(pauseOptions[2].getBoundsInLocal().getWidth()*1.2,pauseOptions[2].getBoundsInLocal().getHeight(),Color.web("b5d9e0"));
		quitButton.setX(200 - quitButton.getWidth()/2);
		quitButton.setY(pauseOptions[2].getY() -quitButton.getHeight() + pauseOptions[2].getBoundsInLocal().getHeight()/2 - 10);
		quitButton.setStroke(Color.web("49a0be"));
		quitButton.setStrokeWidth(1);
		quitButton.setEffect(shadow);
		quitButton.setOpacity(0);
		
		pauseFilter = new Rectangle(width, height, Color.web("#553c66", 0.7));
		
		background = new Rectangle(width, height, grad1);
		
		backMult = new Rectangle(100, 75, Color.web("#3e324c", 0.8));
		backMult.setY(400);
		
		backAcc = new Rectangle(100, 75, Color.web("#3e324c", 0.8));
		backAcc.setY(400);
		backAcc.setX(width - 100);
		
		complete = new Rectangle(0, 10, gradGoal);
		total = new Rectangle(width, 10, Color.web("#3e324c"));
		
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
	
	public void update(int counter) {
		
		complete.setWidth(width * (conductor.songPosition()/conductor.songLength()));
		slideBars[0].setWidth(150 * conductor.getSongVol());
		slideBars[1].setWidth(150 * conductor.getBeatVol());
		
		if(!started) {
			
			try {
				startAnim(counter);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} else if(paused) {
			pauseUpdate();
		} else {
		
			genBeats();
			if(missed + hits == 0) {
				accuracy = 0;
			} else {
				accuracy = ((double)(hits))/((missed + hits));
			}
			
			
			toPrint.get(0).setText("Score: " + score);
			toPrint.get(1).setText("X" + mult);
			toPrint.get(2).setText("" + streak);
			toPrint.get(3).setText(String.format("%.0f", accuracy*100)+ "%");
		
			if (streak > 31) {
				mult = 8;
				goldMode();
			} else if(streak > 15) {
				mult = 4;
			} else if(streak > 7) {
				mult = 2;
			} 
			
			for(int i=0;i<drums.size();i++) {
				if(drums.get(0).isEnded()) {
					drums.remove(0);
				}
			}
			
			if(conductor.update() == 1) {
				
				try {
					game.changeState(new Menu(game));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
		
		
		
		
	}
	public void draw(Group group) {
		
		group.getChildren().add(background);
		group.getChildren().add(lanes);
		group.getChildren().add(inLines);
		
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
		
		
		
		for(int i=0;i<3;i++) {
			ArrayList<Beat> curr = beats[i];
			for(int j=0;j<curr.size();j++) {
				Beat currBeat = curr.get(j);
				if(!paused) {
					if(currBeat.getY() > 490 && currBeat.getY() < 510) {
						
						currBeat.move(false);
					} else {
						currBeat.move(true);
					}
				}
				
				group.getChildren().add(currBeat.circle);
				group.getChildren().add(currBeat.iView);
				if(currBeat.getY() > height-1) {
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
				toPrint.remove(i);
			} else {
				group.getChildren().add(curr.getText());
			}
		}
		if(!started || paused) {
			group.getChildren().add(pauseFilter);
			
			group.getChildren().add(numDraw);
			
			if(paused) {
				group.getChildren().add(optionBack);
				group.getChildren().add(quitButton);
				for(int i=0;i<3;i++) {
					group.getChildren().add(pauseOptions[i]);
					if(i<2) {
						group.getChildren().add(emptySlideBars[i]);
						group.getChildren().add(slideBars[i]);
					}
				}
			}
			
		}
		
		
		
		
	}
	
	public void keyRelease(KeyEvent event) {
		
	}
	public void click(MouseEvent event) {
		double x = event.getX();
		double y = event.getY();
		if(paused) {
			if(y > quitButton.getY() && y < quitButton.getY() + quitButton.getHeight()) {
				if(x > quitButton.getX() && x < quitButton.getX() + quitButton.getWidth()) {
					try {
						game.changeState(new Menu(game));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else if(y > emptySlideBars[0].getY() && y < emptySlideBars[0].getY() + emptySlideBars[0].getHeight()) {
				if(x > emptySlideBars[0].getX() && x < emptySlideBars[0].getX() + emptySlideBars[0].getWidth()) {
					double newVol = (x-emptySlideBars[0].getX())/150;
					conductor.setSongVol(newVol);
					slideBars[0].setWidth(newVol*150);
				}
			}else if(y > emptySlideBars[1].getY() && y < emptySlideBars[1].getY() + emptySlideBars[1].getHeight()) {
				if(x > emptySlideBars[1].getX() && x < emptySlideBars[1].getX() + emptySlideBars[1].getWidth()) {
					double newVol = (x-emptySlideBars[1].getX())/150;
					conductor.setBeatVol(newVol);
					slideBars[1].setWidth(newVol*150);
				}
			}
			
		}
	}
	
	public void keyPress(KeyEvent event) {
		String key  = event.getText().toUpperCase();
		if(key.equals(keys[0].getText())) {
			checkHit(0);
		} else if (key.equals(keys[1].getText())) {
			checkHit(1);
		} else if (key.equals(keys[2].getText())) {
			checkHit(2);
		} else if (key.equals(keyString[3])) {
			if(paused) {
				unpausing = true;
				unpauseCounter = 0;
			} else {
				paused = true;
			}
		}
	}
	
	public void moved(MouseEvent event) {
		double x = event.getX();
		double y = event.getY();
		if(paused) {
			pauseOptions[2].setFill(Color.web("b5d9e0"));
			pauseOptions[2].setStrokeWidth(1);
			pauseOptions[2].setEffect(shadow);
			quitButton.setOpacity(0);
			if(y > quitButton.getY() && y < quitButton.getY() + quitButton.getHeight()) {
				if(x > quitButton.getX() && x < quitButton.getX() + quitButton.getWidth()) {
					pauseOptions[2].setFill(Color.web("013b53"));
					pauseOptions[2].setEffect(null);
					pauseOptions[2].setStrokeWidth(0);
					quitButton.setOpacity(1);
				}
			}
		}
	}
	
	private void checkHit(int lane) {
		if(!paused) {
			int len = beats[lane].size();
			
			drums.add(new DrumSound(conductor.getBeatVol()));
			flashes.add(new Flash(lane, streak));
	
	
			if(len == 0) {
				miss();
				toPrint.add(new Print(100*(lane+1), 440, 10, Color.RED, "miss"));
			} else {
				
				Beat curr = beats[lane].get(0);
				double y = curr.getY();
				
				for(int i=0;i<len;i++) {
					curr = beats[lane].get(i);
					y = curr.getY();
					if(!(curr.getY() > 590)) {
						break;
					}
				}
				if(y < 560 && y > 440) {
					
					if(y < 510 && y > 490) {
						score += 100*mult;
						toPrint.add(new Print(curr.getX()-40, curr.getY()-25, 15, gradPerf, "perfect!"));
	
					} else {
						score += 50*mult;
						toPrint.add(new Print(curr.getX()-40, curr.getY()-25, 15, Color.GREEN, "good!"));
	
					}
					beats[lane].remove(0);
					streak ++;
					hits ++;
				} else {
					miss();
					toPrint.add(new Print(100*(lane+1), 440, 10, Color.RED, "miss"));
				}
			}
		}
	}
	
	public void miss() {
		missed ++;
		purpleMode();
		streak = 0;
		mult = 1;
		
	}
	public void genBeats() {
		
		if(LoadMidi.beats.size() == 0) {
		} else {
			
			String next = LoadMidi.beats.get(0);
			int space = next.indexOf(" ");
			int time = Integer.parseInt(next.substring(0,space));
					
			currPos = conductor.songPosition();
			
			if(currPos > time*conductor.getTickSize() - conductor.getTravelTime()) {
				
				for(int i=0;i<LoadMidi.beats.size();i++) {
					
					String next2 = LoadMidi.beats.get(0);
					int space2 = next2.indexOf(" ");
					int time2 = Integer.parseInt(next2.substring(0,space2));
					int key2 = Integer.parseInt(next2.substring(space2 + 1));
					
					if(time2 == time) {
						
						numBeats ++;
						
						//System.out.println(currPos - lastPos);
						
						int lane;
					
						if(key2 == 60) {
							lane = 0;
						} else if (key2 == 62) {
							lane = 1;
						} else if (key2 == 64){
							lane = 2;
						} else {
							System.out.println("failure @" + time);
							lane = 0;
						}
						beats[lane].add(new Beat(lane, conductor.getBeatSpeed()));
			
						//conductor.addBeat();
						
						LoadMidi.beats.remove(0);
						
						
					} else {
						break;
					}
					
				}
				
				
				
			}
		}
	}
	
	public void goldMode() {
		//background.setFill(grad2);
		//lanes.setFill(Color.web("#212020", 0.9));
		complete.setFill(gradGoal2);
		backAcc.setStroke(gradGoal2);
		backAcc.setStrokeWidth(3);
		backAcc.setStrokeType(StrokeType.OUTSIDE);
		
		backMult.setStroke(gradGoal2);
		backMult.setStrokeWidth(3);
		backMult.setStrokeType(StrokeType.OUTSIDE);
		
		lanes.setStroke(gradGoal22);
		lanes.setStrokeWidth(6);
		
	
		/*
		for(Circle c : goals) {
			c.setStroke(gradGoal22);
			c.setStrokeWidth(3);
			c.setStrokeType(StrokeType.INSIDE);
		}
		*/
	}
	
	public void purpleMode() {
		background.setFill(grad1);
		complete.setFill(gradGoal);
		
		backAcc.setStroke(null);
		backMult.setStroke(null);
		
		lanes.setStroke(Color.web("#513a5b", 0.8));
		lanes.setStrokeWidth(5);
		
		/*
		for(Circle c : goals) {
			c.setStroke(null);
		}
		*/
		
		
	}
	public void colorText(Text text) {
		
		shadow = new DropShadow();
		shadow.setColor(Color.web("b5d9e0"));
		//shadow.setSpread(0.5);
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
	
	
	public void startAnim(int counter) throws Exception {
		if(counter < 60) {
			numDraw = startSeq[0];
		} else if (counter == 60) {
			numDraw = startSeq[1];
		} else if (counter == 120) {
			numDraw = startSeq[2];
		} else if (counter == 180) {
			numDraw = startSeq[3];
		} else if(counter > 240) {
			try {
				conductor.play();
			} catch(Exception e) {
				e.printStackTrace();
			}
			started = true;
		}
		
		gameStart = System.nanoTime();

	}
	public void pauseUpdate() {
		if(unpauseCounter == 0) {
			conductor.pause();
			numDraw = pauseText;
		}
		if(unpausing) {
			
			unpauseCounter ++;
			if(unpauseCounter < 60) {
				numDraw = startSeq[0];
			} else if (unpauseCounter == 60) {
				numDraw = startSeq[1];
			} else if(unpauseCounter == 120) {
				numDraw = startSeq[2];
			} else if (unpauseCounter == 180) {
				numDraw = startSeq[3];
			} else if (unpauseCounter > 239) {
				unpausing = false;
				paused = false;
				unpauseCounter = 0;
				conductor.unpause();
			}
		}
	}
}

