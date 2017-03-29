package models;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Player extends Entity {

	private double glideJuice = 100;
	private Juice equippedJuice = null;
	private int maxLives = 1;
	private int lives = 1;
	private int maxAmmo = 0;
	private int ammo = 0;
	private boolean canStomp = false;
	private boolean canGlide = false;
	private int pickupRange = 0;
	private int numberOfJumps = 1;
	private double jumpHeight = 8.0;
	private double startingHeight = jumpHeight;
	private double fallFactor = .15;
	private double width = 48;
	private double height = 96;
	private double speed = 1.2;
	private Image image = new Image("file:pictures/frames/frame0.png");
	private ImageView imageView = new ImageView(image);
	
	public Player(double y) {
		imageView.setFitHeight(height);
		imageView.setFitWidth(width);
		imageView.setLayoutX(0);
		imageView.setLayoutY(y - height - 41);
	}
	
	public Player(double y, double speed, int jumps, int pickupRange, int gold, int ammo, int lives,
			boolean canGlide, boolean canStomp, Juice j) {
		imageView.setFitHeight(height);
		imageView.setFitWidth(width);
		imageView.setLayoutX(0);
		imageView.setLayoutY(y - height - 41);
		
		setSpeed(speed);
		setNumberOfJumps(jumps);
		setPickupRange(pickupRange);
		setGold(gold);
		setMaxAmmo(ammo);
		setMaxLives(lives);
		setCanGlide(canGlide);
		setCanStomp(canStomp);
		setEquippedJuice(j);
	}

	public double getJumpHeight() {
		return jumpHeight;
	}

	public void setJumpHeight(double jumpHeight) {
		this.jumpHeight = jumpHeight;
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

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public ImageView getImageView() {
		return imageView;
	}

	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}

	public double getStartingHeight() {
		return startingHeight;
	}

	public void setStartingHeight(double startingHeight) {
		this.startingHeight = startingHeight;
	}

	public double getFallFactor() {
		return fallFactor;
	}

	public void setFallFactor(double fallFactor) {
		this.fallFactor = fallFactor;
	}

	public int getNumberOfJumps() {
		return numberOfJumps;
	}

	public void setNumberOfJumps(int numberOfJumps) {
		this.numberOfJumps = numberOfJumps;
	}

	public int getPickupRange() {
		return pickupRange;
	}

	public void setPickupRange(int pickupRange) {
		this.pickupRange = pickupRange;
	}

	public boolean isCanGlide() {
		return canGlide;
	}

	public void setCanGlide(boolean canGlide) {
		this.canGlide = canGlide;
	}

	public boolean isCanStomp() {
		return canStomp;
	}

	public void setCanStomp(boolean canStomp) {
		this.canStomp = canStomp;
	}

	public int getMaxAmmo() {
		return maxAmmo;
	}

	public void setMaxAmmo(int maxAmmo) {
		this.maxAmmo = maxAmmo;
	}

	public int getAmmo() {
		return ammo;
	}

	public void setAmmo(int ammo) {
		this.ammo = ammo;
	}

	public int getLives() {
		return lives;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}

	public int getMaxLives() {
		return maxLives;
	}

	public void setMaxLives(int maxLives) {
		this.maxLives = maxLives;
	}

	public Juice getEquippedJuice() {
		return equippedJuice;
	}

	public void setEquippedJuice(Juice equippedJuice) {
		this.equippedJuice = equippedJuice;
	}

	public double getGlideJuice() {
		return glideJuice;
	}

	public void setGlideJuice(double glideJuice) {
		this.glideJuice = glideJuice;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Speed: ");
		
		sb.append(getSpeed());
		sb.append("\nJump Height: ");
		sb.append(getStartingHeight());
		sb.append("\nLives: ");
		sb.append(getLives());
		sb.append("/");
		sb.append(getMaxLives());
		sb.append("\nAmmo: ");
		sb.append(getAmmo());
		sb.append("/");
		sb.append(getMaxAmmo());
		
		return sb.toString();
	}
}
