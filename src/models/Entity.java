package models;

import javafx.scene.image.ImageView;

public class Entity {

	private double speed;
	private ImageView imageView;
	private int gold;
	private double actualX;
	private double actualY;

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public ImageView getImageView() {
		return imageView;
	}

	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public double getActualX() {
		return actualX;
	}

	public void setActualX(double actualX) {
		this.actualX = actualX;
	}

	public double getActualY() {
		return actualY;
	}

	public void setActualY(double actualY) {
		this.actualY = actualY;
	}
	
}
