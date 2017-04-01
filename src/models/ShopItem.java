package models;

import enums.ItemType;
import game.EntityHandler;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ShopItem {

	private static Image jump = new Image("file:pictures/Shop/Jump.png");
	private static Image speed = new Image("file:pictures/Shop/Speed.png");
	private static Image glide = new Image("file:pictures/Shop/Glide.png");
	private static Image stomp = new Image("file:pictures/Shop/Stomp.png");
	private static Image gold = new Image("file:pictures/Shop/Coins.png");
	private static Image lives = new Image("file:pictures/Shop/Lives.png");
	private static Image juice = new Image("file:pictures/Shop/Juice.png");
	private static Image gun = new Image("file:pictures/Shop/Gun.png");
	private ImageView imageView = new ImageView();
	private ImageView pBar = new ImageView();
	private int level = 0;
	private final int maxLevel;
	private int costForNextLevel;
	private boolean canPurchase = true;
	private static Player player;
	private static Label goldLabel;
	private static EntityHandler eh;
	
	public ShopItem(ItemType it, Player player, Label goldLabel, EntityHandler eh) {
		ShopItem.eh = eh;
		ShopItem.player = player;
		ShopItem.goldLabel = goldLabel;
		switch (it) {
		case SPEED:
			costForNextLevel = 100;
			getImageView().setImage(speed);
			maxLevel = 5;
			level = (int) ((player.getMaxSpeed() - 1.2) / .2) <= maxLevel ? (int) ((player.getMaxSpeed() - 1.2) / .2) : maxLevel;
			pBar.setImage(new Image("file:pictures/ProgressBars/5bars" + level + ".png"));
			if (level == maxLevel) {
				costForNextLevel = 0;
			}
			else {
				costForNextLevel += (100 * level);
			}
			break;
		case JUMP:
			costForNextLevel = 250;
			getImageView().setImage(jump);
			maxLevel = 3;
			level = player.getNumberOfJumps() - 1 <= maxLevel ? player.getNumberOfJumps() - 1 : maxLevel;
			pBar.setImage(new Image("file:pictures/ProgressBars/3bars" + level + ".png"));
			if (level == maxLevel) {
				costForNextLevel = 0;
			}
			else {
				for (int i = 0; i < level; i++) {
					costForNextLevel *= 2;
				}
			}
			break;
		case STOMP:
			costForNextLevel = 500;
			getImageView().setImage(stomp);
			maxLevel = 1;
			level = player.isCanStomp() ? 1 : 0;
			pBar.setImage(new Image("file:pictures/ProgressBars/1bar" + level + ".png"));
			if (level == 1) {
				costForNextLevel = 0;
			}
			break;
		case GLIDER:
			costForNextLevel = 750;
			getImageView().setImage(glide);
			maxLevel = 1;
			level = player.isCanGlide() ? 1 : 0;
			pBar.setImage(new Image("file:pictures/ProgressBars/1bar" + level + ".png"));
			if (level == 1) {
				costForNextLevel = 0;
			}
			break;
		case GUN:
			costForNextLevel = 150;
			getImageView().setImage(gun);
			maxLevel = 5;
			level = player.getMaxAmmo() / 2 <= maxLevel ? player.getMaxAmmo() / 2 : maxLevel;
			pBar.setImage(new Image("file:pictures/ProgressBars/5bars" + level + ".png"));
			if (level == maxLevel) {
				costForNextLevel = 0;
			}
			else {
				costForNextLevel += (150 * level);
			}
			break;
		case JUICE:
			costForNextLevel = 75;
			getImageView().setImage(juice);
			maxLevel = 1;
			level = player.getEquippedJuice() != null ? 1 : 0;
			pBar.setImage(new Image("file:pictures/ProgressBars/1bar" + level + ".png"));
			if (level == maxLevel) {
				costForNextLevel = 0;
			}
			break;
		case LIVES:
			costForNextLevel = 500;
			getImageView().setImage(lives);
			maxLevel = 3;
			level = player.getMaxLives() - 1 <= maxLevel ? player.getMaxLives() - 1 : maxLevel;
			pBar.setImage(new Image("file:pictures/ProgressBars/3bars" + level + ".png"));
			if (level == maxLevel) {
				costForNextLevel = 0;
			}
			else {
				for (int i = 0; i < level; i++) {
					costForNextLevel *= 2;
				}
			}
			break;
		case GOLD:
			costForNextLevel = 150;
			getImageView().setImage(gold);
			maxLevel = 5;
			level = player.getPickupRange() / 25 <= maxLevel ? player.getPickupRange() / 25 : maxLevel;
			pBar.setImage(new Image("file:pictures/ProgressBars/5bars" + level + ".png"));
			if (level == maxLevel) {
				costForNextLevel = 0;
			}
			else {
				costForNextLevel += (150 * level);
			}
			break;
		default:
			costForNextLevel = 100;
			getImageView().setImage(speed);
			pBar.setImage(new Image("file:pictures/ProgressBars/5bars0.png"));
			maxLevel = 5;
			break;
		}
	}
	
	public void positionCorrectly() {
		pBar.setLayoutX(getImageView().getBoundsInParent().getMinX() - 20);
		pBar.setLayoutY(getImageView().getBoundsInParent().getMinY() + 10);
	}
	
	public boolean purchaseItem() {
		if (player.getGold() < costForNextLevel || !canPurchase) {
			return false;
		}
		else {
			player.setGold(player.getGold() - costForNextLevel);
			
			if (getImageView().getImage().equals(speed)) {
				player.setMaxSpeed(player.getMaxSpeed() + .2);
				player.setSpeed(player.getMaxSpeed());
				costForNextLevel += 100;
				pBar.setImage(new Image("file:pictures/ProgressBars/5bars" + (level + 1) + ".png"));
			}
			else if (getImageView().getImage().equals(jump)) {
				player.setNumberOfJumps(player.getNumberOfJumps() + 1);
				costForNextLevel *= 2;
				pBar.setImage(new Image("file:pictures/ProgressBars/3bars" + (level + 1) + ".png"));
			}
			else if (getImageView().getImage().equals(stomp)) {
				player.setCanStomp(true);
				costForNextLevel += 0;
				pBar.setImage(new Image("file:pictures/ProgressBars/1bar" + (level + 1) + ".png"));
			} 
			else if (getImageView().getImage().equals(glide)) {
				player.setCanGlide(true);
				costForNextLevel = 0;
				pBar.setImage(new Image("file:pictures/ProgressBars/1bar" + (level + 1) + ".png"));
			} 
			else if (getImageView().getImage().equals(gun)) {
				player.setMaxAmmo(player.getMaxAmmo() + 2);
				costForNextLevel += 150;
				pBar.setImage(new Image("file:pictures/ProgressBars/5bars" + (level + 1) + ".png"));
			} 
			else if (getImageView().getImage().equals(juice)) {
				if (player.getEquippedJuice() != null) {
					player.setGold(player.getGold() + costForNextLevel);
					return false;
				}
				else {
					player.setEquippedJuice(new Juice());
					pBar.setImage(new Image("file:pictures/ProgressBars/1bar" + (level + 1) + ".png"));
					costForNextLevel = 75;
				}
			} 
			else if (getImageView().getImage().equals(lives)) {
				player.setMaxLives(player.getMaxLives() + 1);
				player.setLives(player.getMaxLives());
				costForNextLevel *= 2;
				pBar.setImage(new Image("file:pictures/ProgressBars/3bars" + (level + 1) + ".png"));
			} 
			else if (getImageView().getImage().equals(gold)) {
				eh.setCoinMultiplier(eh.getCoinMultiplier() + 1.5);
				player.setPickupRange(player.getPickupRange() + 25);
				costForNextLevel += 150;
				pBar.setImage(new Image("file:pictures/ProgressBars/5bars" + (level + 1) + ".png"));
			}
			
			level++;
			if (level >= maxLevel) {
				costForNextLevel = 0;
				canPurchase = false;
			}
			
			getGoldLabel().setText("" + player.getGold());
			
			return true;
		}
	}

	public ImageView getImageView() {
		return imageView;
	}

	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public int getCostForNextLevel() {
		return costForNextLevel;
	}

	public void setCostForNextLevel(int costForNextLevel) {
		this.costForNextLevel = costForNextLevel;
	}

	public boolean isCanPurchase() {
		return canPurchase;
	}

	public void setCanPurchase(boolean canPurchase) {
		this.canPurchase = canPurchase;
	}

	public Player getPlayer() {
		return player;
	}

	public Label getGoldLabel() {
		return goldLabel;
	}

	public ImageView getpBar() {
		return pBar;
	}

	public void setpBar(ImageView pBar) {
		this.pBar = pBar;
	}
}
