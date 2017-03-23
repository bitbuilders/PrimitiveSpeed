package models;

import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Platform {

	private Pane platform = new Pane();
	private static Image brick = new Image("file:pictures/Brick.png");
	private static Image mossyBrick = new Image("file:pictures/BrickMossy.png");
	private static Image flowerBrick = new Image("file:pictures/BrickMossyFlower.png");
	private int size;
	private static Random rand = new Random();
	private int width = 20;
	private int height = 95;
	private int offCenter = 40;
	private double y;
	
	public Platform(double y, double x) {
		this(randomSize(), y, x);
	}
	
	public Platform(int size, double y, double x) {
		setSize(size);
		for (int i = (int) (getSize() - 1); i >= 0; i--) {
			ImageView iv = new ImageView();
			int z = rand.nextInt(5);
			if (z <= 2) {
				iv.setImage(brick);
			}
			else if (z == 3) {
				iv.setImage(mossyBrick);
			}
			else {
				iv.setImage(flowerBrick);
			}
			
			getPlatform().getChildren().add(iv);
			iv.setLayoutX(i * width);
		}
		if (x == 0) {
			getPlatform().setLayoutX(getPlatform().getLayoutX() - offCenter + x);
		}
		else {
			getPlatform().setLayoutX(getPlatform().getLayoutX() + x);
		}
		getPlatform().setLayoutY(y - getHeight());
		setY(y - height / 2);
	}
	
	private static int randomSize() {
		int size = 10;
		size += rand.nextInt(5) + 1;
		size += rand.nextInt(5) + 1;
		
		return size;
	}

	public Pane getPlatform() {
		return platform;
	}

	public void setPlatform(Pane platform) {
		this.platform = platform;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
}
