package models;

import enums.ItemType;
import game.EntityHandler;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ShopItem {

	private static Image jump = new Image("file:picutres/Jump.png");
	private static Image speed = new Image("file:picutres/Speed.png");
	private static Image glide = new Image("file:picutres/Glide.png");
	private static Image stomp = new Image("file:picutres/Stomp.png");
	private static Image gold = new Image("file:picutres/Gold.png");
	private static Image lives = new Image("file:picutres/Lives.png");
	private static Image juice = new Image("file:picutres/Juice.png");
	private static Image gun = new Image("file:picutres/Gun.png");
	private ImageView imageView = new ImageView();
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
			break;
		case JUMP:
			costForNextLevel = 150;
			getImageView().setImage(jump);
			maxLevel = 5;
			break;
		case STOMP:
			costForNextLevel = 600;
			getImageView().setImage(stomp);
			maxLevel = 1;
			break;
		case GLIDER:
			costForNextLevel = 750;
			getImageView().setImage(glide);
			maxLevel = 1;
			break;
		case GUN:
			costForNextLevel = 150;
			getImageView().setImage(gun);
			maxLevel = 5;
			break;
		case JUICE:
			costForNextLevel = 75;
			getImageView().setImage(juice);
			maxLevel = 1;
			break;
		case LIVES:
			costForNextLevel = 500;
			getImageView().setImage(lives);
			maxLevel = 3;
			break;
		case GOLD:
			costForNextLevel = 200;
			getImageView().setImage(gold);
			maxLevel = 5;
			break;
		default:
			costForNextLevel = 100;
			getImageView().setImage(speed);
			maxLevel = 5;
			break;
		}
	}
	
	public boolean purchaseItem(ItemType it) {
		if (player.getGold() < costForNextLevel || !canPurchase) {
			return false;
		}
		else {
			player.setGold(player.getGold() - costForNextLevel);
			switch (it) {
			case SPEED:
				player.setSpeed(player.getSpeed() + .2);
				costForNextLevel += 100;
				break;
			case JUMP:
				player.setNumberOfJumps(player.getNumberOfJumps() + 1);
				costForNextLevel += 150;
				break;
			case STOMP:
				player.setCanStomp(true);
				costForNextLevel += 0;
				break;
			case GLIDER:
				player.setCanGlide(true);
				costForNextLevel = 0;
				break;
			case GUN:
				player.setMaxAmmo(player.getMaxAmmo() + 2);
				costForNextLevel += 150;
				break;
			case JUICE:
				if (player.getEquippedJuice() != null) {
					player.setGold(player.getGold() + costForNextLevel);
					return false;
				}
				else {
					player.setEquippedJuice(new Juice());
				}
				costForNextLevel = 0;
				break;
			case LIVES:
				player.setMaxLives(player.getMaxLives() + 1);
				costForNextLevel *= 2;
				break;
			case GOLD:
				eh.setCoinMultiplier(eh.getCoinMultiplier() + 1.5);
				player.setPickupRange(player.getPickupRange() + 25);
				costForNextLevel += 200;
				break;
			default:
				costForNextLevel += 100;
				break;
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
}
