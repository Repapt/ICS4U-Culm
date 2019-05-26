package states;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.sound.midi.InvalidMidiDataException;

import beats.Beat;
import beats.Conductor;
import javafx.scene.Group;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import main.Main;
import tools.LoadMidi;
import tools.Print;

public class Playing extends GameState{
	
	Main game;

	ArrayList<Beat>[] beats = new ArrayList[3];
	
	Rectangle background, lanes, backMult, backAcc, complete, total;
	
	LinearGradient grad1, grad2, gradGoal;
	int score = 0;
	int streak = 0;
	int mult = 1;
	
	double accuracy = 1;
	int missed = 0;
	int hits;
	
	double currPos = 0;
	int numBeats = 0;
	
	long gameStart;
	boolean started = false;
	
	Circle[] goals = new Circle[3];
	
	Conductor conductor;
	
	ArrayList<Print> toPrint = new ArrayList<Print>();
	Text scoreText;
	
	public Playing(Main g) {
		
		super(g);
		
		
		game = g;
		
		for(int i=0;i<3;i++) {
			beats[i] = new ArrayList<Beat>();
		}
		
		Print scoreText = new Print(50, 45, -1, Color.WHITE, "Score: " + score);
		Print multText = new Print(25, 435, -1, Color.WHITE, "X1");
		Print streakText = new Print(25, 465, -1, Color.WHITE, "" + streak);
		Print accText = new Print(315, 450, -1, Color.WHITE, "100%");
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
		
		grad2 = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, 
				new Stop[]{
				           
				            new Stop(0, Color.web("#fff716")),
				            new Stop(0.3, Color.web("#fcffaf")),
				            new Stop(0.57, Color.web("#ffd016")),
				            new Stop(0.78, Color.web("#fcffaf")),
				            new Stop(1, Color.web("#ffea99")),
				});
		
		gradGoal = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, 
				new Stop[]{
				           
				            new Stop(0, Color.web("#ffe2fc")),
				            new Stop(0.2, Color.web("#db27c8")),
				            new Stop(0.4, Color.web("#d850c7")),
				            new Stop(1, Color.web("#ffe0fb")),
				});
		

		
		goals[0] = new Circle(100 ,500,50, gradGoal);
		goals[1] = new Circle(200 ,500,50, gradGoal);
		goals[2] = new Circle(300 ,500,50, gradGoal);
		
		
		background = new Rectangle(width, height, grad1);
		
		lanes = new Rectangle(300, height, Color.web("#773272", 0.7));
		lanes.setX(50);
		
		backMult = new Rectangle(100, 75, Color.web("#3e324c", 0.8));
		backMult.setY(400);
		
		backAcc = new Rectangle(100, 75, Color.web("#3e324c", 0.8));
		backAcc.setY(400);
		backAcc.setX(width - 100);
		
		complete = new Rectangle(0, 10, gradGoal);
		total = new Rectangle(width, 10, Color.web("#3e324c"));
				
		try {
			conductor = new Conductor(3, height);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		try {
			startAnim();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	
		
	}
	
	public void update(int counter) {
		
		complete.setWidth(width * (conductor.songPosition()/conductor.songLength()));
		
		if(System.nanoTime() - gameStart > conductor.getDelay()*1000000000 && !started) {
			conductor.play();
			started = true;
		}
		
		if(started) {
			if(conductor.update() == 1) {
				game.changeState(new Menu(game));
			}
		}
		
		genBeats();
		accuracy = ((double)(hits) + 1)/((missed + hits) + 1);
		
		
		toPrint.get(0).setText("Score: " + score);
		toPrint.get(1).setText("X" + mult);
		toPrint.get(2).setText("" + streak);
		toPrint.get(3).setText(String.format("%.0f", accuracy*100)+ "%");
	
		if (streak > 31) {
			mult = 8;
			background.setFill(grad2);
		} else if(streak > 15) {
			mult = 4;
		} else if(streak > 7) {
			mult = 2;
		} 
		
		//System.out.println(conductor.songPosition());
		
	}
	
	public void draw(Group group) {
		
		group.getChildren().add(background);
		group.getChildren().add(lanes);

		for(Circle c : goals) {
			group.getChildren().add(c);
		}
		
		group.getChildren().add(total);
		group.getChildren().add(complete);
		
		
		
		for(int i=0;i<3;i++) {
			ArrayList<Beat> curr = beats[i];
			for(int j=0;j<curr.size();j++) {
				Beat currBeat = curr.get(j);
				if(currBeat.getY() > 490 && currBeat.getY() < 510) {
					currBeat.move(false);
				} else {
					currBeat.move(true);
				}
				
				group.getChildren().add(currBeat.circle);
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
		
		
		
		
	}
	
	public void keyRelease(KeyEvent event) {
		
	}
	
	public void keyPress(KeyEvent event) {
		String key  = event.getCode().toString();
		if(key.equals("A")) {
			checkHit(0);
		} else if (key.equals("S")) {
			checkHit(1);
		} else if (key.equals("D")) {
			checkHit(2);
			
		}
	}
	
	private void checkHit(int lane) {
		int len = beats[lane].size();


		if(len == 0) {
			miss();
			toPrint.add(new Print(100*(lane+1), 440, 10, Color.RED, "miss"));
		} else {
			
			Beat curr = beats[lane].get(0);
			double y = curr.getY();
			
			for(int i=0;i<len;i++) {
				curr = beats[lane].get(i);
				y = curr.getY();
				if(!(curr.getY() > 575)) {
					break;
				}
			}
			if(y < 575 && y > 450) {
				
				if(y < 510 && y > 490) {
					score += 100*mult;
					toPrint.add(new Print(curr.getX()-40, curr.getY()-25, 15, Color.PURPLE, "perfect!"));

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
	
	public void miss() {
		missed ++;
		score -= 100;
		background.setFill(grad1);
		streak = 0;
		mult = 1;
		
	}
	
	public void genBeats() {
		
		if(LoadMidi.beats.size() == 0) {
			
		} else {
			
			String next = LoadMidi.beats.get(0);
			int space = next.indexOf(" ");
			int time = Integer.parseInt(next.substring(0,space));

			//System.out.println(time + " " + key);
					
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
			
						conductor.addBeat();
						
						LoadMidi.beats.remove(0);
						
					} else {
						break;
					}
					
				}
				
				
				
			}
		}
	}
	
	
	public void startAnim() throws Exception {
		long start = System.nanoTime();
		TimeUnit.SECONDS.sleep(1);
		long end = System.nanoTime();
		System.out.println(end - start);
		
		start = System.nanoTime();
		TimeUnit.SECONDS.sleep(1);
		end = System.nanoTime();
		System.out.println(end - start);
		
		start = System.nanoTime();
		TimeUnit.SECONDS.sleep(1);
		end = System.nanoTime();
		System.out.println(end - start);
		
		gameStart = System.nanoTime();

	}
}

