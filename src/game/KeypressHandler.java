package game;

import java.util.ArrayList;

import interfaces.Pausable;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import models.Player;

public class KeypressHandler implements Runnable, Pausable {

	private int jumps = 0;
	private Player player;
	private AnimationHandler ah;
	private Scene gameScene;
	private boolean jumping = false;
	private boolean falling = false;
	private boolean movingLeft = false;
	private boolean movingRight = false;
	private final double lowestPoint;
	private ArrayList<ArrayList<Pane>> platforms = new ArrayList<ArrayList<Pane>>();
	private Thread thread;
	private Timeline timeline;
	private Timeline glideTimer = new Timeline(new KeyFrame(Duration.millis(100), ae -> glideTick()));
	
	public KeypressHandler(Game g, AnimationHandler ah, ArrayList<ArrayList<Pane>> platforms, double point) {
		glideTimer.setCycleCount(Animation.INDEFINITE);
		lowestPoint = point;
		this.platforms = platforms;
		this.ah = ah;
		//game = g;
		player = g.getPlayer();
		gameScene = g.getScene();
		thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void run() {
		gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.SPACE) && player.getNumberOfJumps() > jumps && !jumping &&
						player.getJumpHeight() > -player.getStartingHeight() / 1) {
					player.setFallFactor(.15);
					player.setJumpHeight(player.getStartingHeight() - ((jumps) * 1));
					jumping = true;
					falling = true;
					jumps++;
				}
				if (event.getCode().equals(KeyCode.A)) {
					movingLeft = true;
					if (movingRight) {
						ah.setAnimationTimer(50);
					}
				}
				if (event.getCode().equals(KeyCode.D)) {
					movingRight = true;
					if (movingLeft) {
						ah.setAnimationTimer(50);
					}
				}
				if (event.getCode().equals(KeyCode.S)) {
					if (player.getJumpHeight() == player.getStartingHeight() &&
							player.getImageView().getBoundsInParent().getMaxY() < lowestPoint) {
						if (player.getJumpHeight() > 0) {
							player.getImageView().setLayoutY(player.getImageView().getLayoutY() + 8);
							player.setJumpHeight(-1);
						}
						falling = true;
					}
				}
				if (event.getCode().equals(KeyCode.F)) {
					if (player.getEquippedJuice() != null) {
						player.getEquippedJuice().use(player);
						player.setEquippedJuice(null);
					}
				}
				if (event.getCode().equals(KeyCode.SHIFT)) {
					if (player.isCanGlide()) {
						if (player.getGlideJuice() > 0) {
							glideTimer.play();
						}
						else {
							glideTimer.stop();
							player.setFallFactor(.15);
						}
					}
				}
			}
		});
		gameScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.SPACE)) {
					jumping = false;
				}
				if (event.getCode().equals(KeyCode.A)) {
					movingLeft = false;
					if (!movingRight) {
						ah.setAnimationTimer(50);
					}
				}
				if (event.getCode().equals(KeyCode.D)) {
					movingRight = false;
					if (!movingLeft) {
						ah.setAnimationTimer(50);
					}
				}
				if (event.getCode().equals(KeyCode.SHIFT)) {
					glideTimer.pause();
					player.setFallFactor(.15);
				}
			}
		});
		
		timeline = new Timeline(new KeyFrame(
		        Duration.millis(10),
		        ae -> timerTick()));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}
	
	private void glideTick() {
		player.setGlideJuice(player.getGlideJuice() - 1);
		if (player.getJumpHeight() < 0) {
			player.setFallFactor(.01);
		}
		if (player.getGlideJuice() <= 0) {
			glideTimer.stop();
			player.setFallFactor(.15);
		}
	}
	
	private void jump() {
		ah.setAnimationTimer(100);
		jump(player.getFallFactor());
	}
	
	private void fall(double factor) {
		if (player.getJumpHeight() > -8) {
			player.setJumpHeight(player.getJumpHeight() - player.getFallFactor());
		}
	}
	
	private void jump(double value) {
		player.getImageView().setLayoutY(player.getImageView().getLayoutY() - player.getJumpHeight());
	}
	
	private void moveLeft() {
		if (player.getImageView().getLayoutX() > 1) {
			
			if (!isJumping()) {
				ah.setAnimationTimer(60);
			}
			ImageView iv = player.getImageView();
			iv.setLayoutX(iv.getLayoutX() - (player.getSpeed() - .2));
			player.setImageView(iv);
		}
	}
	
	private void moveRight() {
		if (player.getImageView().getLayoutX() + player.getWidth() < gameScene.getWidth()) {
			
			if (!isJumping()) {
				ah.setAnimationTimer(40);
			}
			ImageView iv = player.getImageView();
			iv.setLayoutX(iv.getLayoutX() + player.getSpeed());
			player.setImageView(iv);
		}
	}
	
	private void checkBelow(Pane p) {
		if (p.getBoundsInParent().getMinX() - 25 <= player.getImageView().getBoundsInParent().getMaxX() &&
				p.getBoundsInParent().getMaxX() + 25 >= player.getImageView().getBoundsInParent().getMinX()) {
			if ((player.getImageView().getBoundsInParent().getMinX() + 25 > p.getBoundsInParent().getMaxX() ||
					player.getImageView().getBoundsInParent().getMaxX() - 25 < p.getBoundsInParent().getMinX() )
					&& player.getImageView().getBoundsInParent().getMaxY() <=
					p.getBoundsInParent().getMinY() + p.getBoundsInParent().getHeight() / 2 + 9 &&
					player.getImageView().getBoundsInParent().getMaxY() >=
					p.getBoundsInParent().getMinY() + p.getBoundsInParent().getHeight() / 2) {
				if (player.getJumpHeight() == player.getStartingHeight()) {
					player.setJumpHeight(-.5);
					falling = true;
					//TODO solve falling through platform issue
				}
			}
		}
		if (player.getImageView().getBoundsInParent().getMaxY() <=
				p.getBoundsInParent().getMinY() + p.getBoundsInParent().getHeight() / 2 + 9 &&
				player.getImageView().getBoundsInParent().getMaxY() >=
				p.getBoundsInParent().getMinY() + p.getBoundsInParent().getHeight() / 2  &&
				p.getBoundsInParent().getMinX() <= player.getImageView().getBoundsInParent().getMaxX() - 20 &&
				p.getBoundsInParent().getMaxX() >= player.getImageView().getBoundsInParent().getMinX() + 25 &&
				player.getJumpHeight() < 0) {
			resetJump();
		}
	}
	
	private void checkRight(Pane p) {
		
	}
	
	private void checkLeft(Pane p) {
		
	}
	
	private void checkAbove(Pane p) {
		
	}
	
	public void timerTick() {
		//System.out.println(player.getJumpHeight());
		if (movingLeft) {
			moveLeft();
		}
		if (movingRight) {
			moveRight();
		}
		if (jumping || player.getJumpHeight() != player.getStartingHeight()) {
			jump();
			//System.out.println("hi");
		}
		if (falling) {
			fall(1);
			//System.out.println("hi");
		}
		for (int i = 0; i < platforms.size(); i++) {
			for (int x = 0; x < platforms.get(i).size(); x++) {
			//System.out.println(platforms.get(i).getBoundsInParent().getMinY() + platforms.get(i).getBoundsInParent().getHeight() / 2);
			checkBelow(platforms.get(i).get(x));
			checkRight(platforms.get(i).get(x));
			checkLeft(platforms.get(i).get(x));
			checkAbove(platforms.get(i).get(x));
			}
		}
	}
		
	public boolean isJumping() {
		return jumping;
	}
		
	public void resetJump() {
		player.setJumpHeight(player.getStartingHeight());
		falling = false;
		if (isJumping()) {
			player.getImageView().setLayoutY(player.getImageView().getLayoutY() - 10);
			falling = true;
		}
		jumps = 0;
	}
	
	public void stopTimer() {
		timeline.stop();
	}
	
	public void startTimer() {
		timeline.play();
	}
	
	public void pauseTimer() {
		timeline.pause();
	}
}
