package game;

import java.util.ArrayList;
import java.util.Random;

import interfaces.Pausable;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import models.Platform;

public class PlatformHandler implements Runnable, Pausable {

	private double topCreation = 0;
	private double middleCreation = 0;
	//private double spawnTime = 0.0;
	private int lastIncrease = 0;
	private double lastSpawnTime = 0;
	private int bottomSpawnTime = 3;
	private int middleSpawnTime = 4;
	private int topSpawnTime = 5;
	private double seconds = 0;
	private Game game;
	private double scrollSpeed = 1;
	private Timeline endlessTimeline;
	private Timeline timeline;
	private Thread thread;
	private Random rand = new Random();
	private ArrayList<ArrayList<Pane>> positions = new ArrayList<ArrayList<Pane>>();
	
	public PlatformHandler(ArrayList<ArrayList<Pane>> plats, Game g) {
		this.game = g;
		positions = plats;
		thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void run() {
		if (game.isEndless()) {
			endlessTimeline = new Timeline(new KeyFrame(
			        Duration.millis(10),
			        ae -> timerTickEndless()));
			endlessTimeline.setCycleCount(Animation.INDEFINITE);
			endlessTimeline.play();
		}
		else {
			timeline = new Timeline(new KeyFrame(
			        Duration.millis(10),
			        ae -> timerTick()));
			timeline.setCycleCount(Animation.INDEFINITE);
			timeline.play();
		}
	}
	
	private void timerTickEndless() {
		seconds += .01;
		
		if (seconds % 1 >= 0 && seconds % 1 <= .01) {
			int s = (int) seconds;
			game.getSeconds().setText("" + s);
		}
		
		movePlatforms();
		
		if (seconds > 3) {
			spawnPlatform();
			if (seconds > 4.5) {
				spawnMiddle();
				if (seconds > 7) {
					spawnTop();
				}
			}
		}
		
		if ((int) seconds % 4 == 0 && lastIncrease != (int) seconds) {
			scrollSpeed += .05;
			lastIncrease = (int) seconds;
			//spawnTime = scrollSpeed - 1;
		}
	}
	
	private void timerTick() {
		seconds += .01;
		
		if (seconds % 1 >= 0 && seconds % 1 <= .01) {
			int s = (int) seconds;
			game.getSeconds().setText("" + s);
		}
		
		movePlatforms();
	}
	
	private void movePlatforms() {
		for (int i = 0; i < positions.size(); i++) {
			for (int x = 0; x < positions.get(i).size(); x++) {
				if (positions.get(i).get(x) != null) {
					positions.get(i).get(x).setLayoutX(positions.get(i).get(x).getLayoutX() - scrollSpeed);
					if (positions.get(i).get(x).getBoundsInParent().getMaxX() < 0) {
						game.getEntities().getChildren().remove(positions.get(i).get(x));
						positions.get(i).remove(x);
					}
				}
			}
		}
	}
	
	private void spawnMiddle() {
		if (seconds - middleCreation >= 4) {
			Platform p3 = null;
			int z = rand.nextInt(4);
			if (z <= 2 || seconds - middleCreation > 4) {
				p3 = new Platform(game.getPositions()[1], game.getScene().getWidth());
				positions.get(1).add(p3.getPlatform());
				game.getEntities().getChildren().add(p3.getPlatform());
				p3.getPlatform().toBack();
				middleCreation = seconds;
			}
			
			int x = rand.nextInt(middleSpawnTime - (middleSpawnTime - 1) + 1) + middleSpawnTime - 1;
			double y = rand.nextDouble();
			middleCreation = seconds + (x + y);
		}
	}
	
	private void spawnTop() {
		if (seconds - topCreation >= 4) {
			Platform p3 = null;
			int z = rand.nextInt(3);
			if (z <= 1 || seconds - topCreation > 5) {
				p3 = new Platform(game.getPositions()[2], game.getScene().getWidth());
				positions.get(0).add(p3.getPlatform());
				game.getEntities().getChildren().add(p3.getPlatform());
				p3.getPlatform().toBack();
			}

			int x = rand.nextInt(topSpawnTime - (topSpawnTime - 1) + 1) + topSpawnTime - 1;
			double y = rand.nextDouble();
			topCreation = seconds + (x + y);
		}
	}
	
	private void spawnPlatform() {
		if (seconds >= lastSpawnTime && positions.get(2).size() < 3) {
			Platform p = null;

			p = new Platform(game.getPositions()[0], game.getScene().getWidth());
			positions.get(2).add(p.getPlatform());
			game.getEntities().getChildren().add(p.getPlatform());
			p.getPlatform().toBack();

			int x = rand.nextInt(bottomSpawnTime - (bottomSpawnTime - 1) + 1) + bottomSpawnTime - 1;
			double y = rand.nextDouble() + 1;
			lastSpawnTime = seconds + (x + y);
		}
	}
	
	public ArrayList<ArrayList<Pane>> getPositions() {
		return this.positions;
	}
	
	public double getScrollSpeed() {
		return scrollSpeed;
	}
	
	public void stopTimer() {
		if (game.isEndless()) {
			endlessTimeline.stop();
		}
		else {
			timeline.stop();
		}
	}
	
	public void startTimer() {
		scrollSpeed = 1;
		middleCreation = 0;
		topCreation = 0;
		lastSpawnTime = 0;
		seconds = 0;
		if (game.isEndless()) {
			endlessTimeline.play();
		}
		else {
			timeline.play();
		}
	}
	
	public void pauseTimer() {
		if (game.isEndless()) {
			endlessTimeline.pause();
		}
		else {
			timeline.pause();
		}
	}
}
