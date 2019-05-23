package states;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

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
import tools.Print;

public class Playing extends GameState{
	
	Main game;

	ArrayList<Beat>[] beats = new ArrayList[3];
	Rectangle endLine = new Rectangle(400, 3, Color.web("white"));
	Rectangle endLine2 = new Rectangle(400, 3, Color.web("white"));
	Rectangle background, lanes;
	
	LinearGradient grad1, grad2, gradGoal;
	int score = 0;
	int height, width;
	int streak = 0;
	int mult = 1;
	
	long gameStart;
	boolean started = false;
	
	Circle[] goals = new Circle[3];
	
	Conductor conductor;
	
	ArrayList<Print> toPrint = new ArrayList<Print>();
	Text scoreText;
	
	public Playing(Main g) {
		
		game = g;
		
		for(int i=0;i<3;i++) {
			beats[i] = new ArrayList<Beat>();
		}
		
		scoreText = new Text(150, 45, "Score: " + score);
		scoreText.setFont(Font.font("Roberto", FontWeight.BOLD, 30));
		scoreText.setFill(Color.WHITE);
		
		
		
		height = game.getHeight();
		width = game.getWidth();
		
		endLine.setX(0);
		endLine.setY(height-50);
		endLine2.setX(0);
		endLine2.setY(height-150);
	
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
		
		conductor = new Conductor(1, height);
		
		
		
		try {
			startAnim();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	
		
	}
	
	public void update(int counter) {
		
		if(System.nanoTime() - gameStart > conductor.getDelay()*1000000000 && !started) {
			conductor.play();
			started = true;
		}
		
		if(started) {
			if(conductor.update() == 1) {
				try {
					game.end();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		genBeats();
		
		scoreText.setText("Score: " + score);
		
		if(streak > 16) {
			background.setFill(grad2);
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
		
		for(int i=0;i<toPrint.size();i++) {
			Print curr = toPrint.get(i);
			if(curr.update() == -1) {
				toPrint.remove(i);
			} else {
				group.getChildren().add(curr.getText());
			}
		}
		
		scoreText.setTextAlignment(TextAlignment.CENTER);
		group.getChildren().add(scoreText);
		
		
		
	}
	
	public void keyPress(KeyEvent event) {
		
	}
	
	public void keyRelease(KeyEvent event) {
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
			if(y > 475 && y < 575) {
				beats[lane].remove(0);
				score += 100*mult;
				streak ++;
				toPrint.add(new Print(curr.getX()-40, curr.getY()-25, 15, Color.PURPLE, "perfect!"));
			} else {
				miss();
				toPrint.add(new Print(100*(lane+1), 440, 10, Color.RED, "miss"));
			}
		}
	}
	
	public void miss() {
		score -= 100;
		background.setFill(grad1);
		streak = 0;
		mult = 1;
	}
	
	double currPos = 0;
	int numBeats = 0;
	
	public void genBeats() {
				
		currPos = conductor.songPosition();
		
		if(currPos > conductor.getBeatTime()) {
			
			numBeats ++;
			
			//System.out.println(currPos - lastPos);
		
			int lane = (int)(Math.random()*3);
			beats[lane].add(new Beat(lane, conductor.getBeatSpeed()));

			conductor.addBeat();
			
			
		}
	}
	
	
	public void startAnim() throws InterruptedException {
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

