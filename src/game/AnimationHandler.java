package game;

import interfaces.Pausable;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class AnimationHandler implements Runnable, Pausable {
	
	private Timeline timeline;
	private Thread thread;
	private int index = 0;
	private int timestamp = 0;
	private int time = 0;
	private int animationTimer = 50;
	private Game game;
	private Image[] frames = new Image[] {
			new Image("file:pictures/frames/frame0.png"),
			new Image("file:pictures/frames/frame1.png"),
			new Image("file:pictures/frames/frame2.png"),
			new Image("file:pictures/frames/frame3.png"),
			new Image("file:pictures/frames/frame4.png"),
			new Image("file:pictures/frames/frame5.png"),
			new Image("file:pictures/frames/frame6.png"),
			new Image("file:pictures/frames/frame0.png"),
			new Image("file:pictures/frames/frame7.png"),
			new Image("file:pictures/frames/frame8.png"),
			new Image("file:pictures/frames/frame9.png"),
			new Image("file:pictures/frames/frame10.png"),
			new Image("file:pictures/frames/frame11.png"),
			new Image("file:pictures/frames/frame12.png")
	};
	
	public AnimationHandler(Game g) {
		game = g;
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		timeline = new Timeline(new KeyFrame(
		        Duration.millis(1),
		        ae -> timerTick()));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}
	
	private void timerTick() {
		time++;
		if (time - timestamp >= animationTimer) {
			game.getPlayer().getImageView().setImage(frames[index]);
			index++;
			if (index == frames.length) {
				index = 0;
			}
			timestamp = time;
		}
	}
	
	public void pauseTimer() {
		timeline.pause();
	}
	
	public void startTimer() {
		timeline.play();
	}
	
	public void stopTimer() {
		timeline.stop();
	}

	public int getAnimationTimer() {
		return animationTimer;
	}

	public void setAnimationTimer(int animationTimer) {
		this.animationTimer = animationTimer;
	}
}
