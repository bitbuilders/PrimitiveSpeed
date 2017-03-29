package models;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Bullet extends Entity {

	private double speed;
	private double width;
	private double height;
	private boolean movingRight = true;
	private boolean moving = false;
	private static Image pebble = new Image("file:pictures/Pebble.png");
	private static Image arrow = new Image("file:pictures/Arrow.png");
	
	public Bullet(boolean movingRight) {
		this(movingRight, 2);
	}
	
	public Bullet(boolean movingRight, double speed) {
		setMovingRight(movingRight);
		setSpeed(speed);
		setImage();
	}
	
	private void setImage() {
		if (isMovingRight()) {
			setImageView(new ImageView(pebble));
		}
		else {
			setImageView(new ImageView(arrow));
		}
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public boolean isMovingRight() {
		return movingRight;
	}

	public void setMovingRight(boolean movingRight) {
		this.movingRight = movingRight;
	}

	public boolean isMoving() {
		return moving;
	}

	public void setMoving(boolean moving) {
		this.moving = moving;
	}

	public static Image getPebble() {
		return pebble;
	}

	public static Image getArrow() {
		return arrow;
	}
}
