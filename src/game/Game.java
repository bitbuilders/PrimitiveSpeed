package game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import enums.ItemType;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import models.Platform;
import models.Player;
import models.ShopItem;

public class Game {
	
	private ShopItem juice;
	private boolean shopUp = false;
	private Group infoStuff;
	private Group shop;
	private Label seconds;
	private Label gold;
	private ImageView goldView;
	private ImageView clockView;
	private boolean isEndless;
	private Pane entities;
	private Scene gameScene;
	private Menu menu;
	private Player player;
	private ModeSelection ms;
	private ArrayList<ArrayList<Pane>> platforms = new ArrayList<ArrayList<Pane>>();
	private final double[] positions;
	private Group heartGroup = new Group();
	private ArrayList<ImageView> hearts = new ArrayList<>();
	private Rectangle glideJuice;
	
	public Game(Menu menu, boolean isEndless, ModeSelection ms, ArrayList<Platform> platforms, Player p) {
		this.ms = ms;
		this.isEndless = isEndless;
		this.menu = menu;
		
		positions = new double[] {
			menu.getDefaultHeight(),
			menu.getDefaultHeight() - 200,
			menu.getDefaultHeight() - 400
		};
		
		player = p;
		gameScene = createScene();
		if (platforms != null) {
			loadPlatforms(platforms);
		}
	}
	
	private void loadPlatforms(ArrayList<Platform> platforms) {
		for (int i = 0; i < platforms.size(); i++) {
			if (platforms.get(i).getPlatform().getLayoutY() == positions[0]) {
				this.platforms.get(2).add(platforms.get(i).getPlatform());
			}
			else if (platforms.get(i).getPlatform().getLayoutY() == positions[1]) {
				this.platforms.get(1).add(platforms.get(i).getPlatform());
			}
			else {
				this.platforms.get(0).add(platforms.get(i).getPlatform());
			}
			entities.getChildren().add(platforms.get(i).getPlatform());
		}
	}
	
	private Scene createScene() {
		infoStuff = new Group();
		entities = new Pane();
		
		Image i = new Image("file:pictures/Coin.png");
		goldView = new ImageView(i);
		goldView.setFitWidth(17);
		goldView.setFitHeight(17);
		gold = new Label("" + player.getGold());
		gold.setLayoutX(20);
		gold.setLayoutY(-2);
		gold.setStyle("-fx-font: 18 Arial; -fx-text-fill: #CA0;");
		infoStuff.getChildren().add(goldView);
		infoStuff.getChildren().add(gold);
		infoStuff.setLayoutX(2);
		infoStuff.setLayoutY(2);
		
		Image i2 = new Image("file:pictures/Clock.png");
		clockView = new ImageView(i2);
		clockView.setLayoutX(75);
		clockView.setFitWidth(17);
		clockView.setFitHeight(17);
		seconds = new Label("0");
		seconds.setLayoutX(95);
		seconds.setLayoutY(-2);
		seconds.setStyle("-fx-font: 18 Arial; -fx-text-fill: #777;");
		infoStuff.getChildren().add(clockView);
		infoStuff.getChildren().add(seconds);
		infoStuff.setLayoutX(2);
		infoStuff.setLayoutY(2);
		
		Image i3 = new Image("file:pictures/Wings.png");
		ImageView iv3 = new ImageView(i3);
		iv3.setFitHeight(17);
		iv3.setFitWidth(34);
		iv3.setLayoutX(200);
		glideJuice = new Rectangle(100, 15);
		glideJuice.setFill(Paint.valueOf("#09F"));
		if (!player.isCanGlide()) {
			glideJuice.setWidth(0);
		}
		glideJuice.setArcHeight(20);
		glideJuice.setArcWidth(20);
		glideJuice.setLayoutX(240);
		glideJuice.setLayoutY(1);
		infoStuff.getChildren().addAll(iv3, glideJuice);
		
		for (int x = 0; x < player.getMaxLives() - 1; x++) {
			Image i4 = new Image("file:pictures/Heart.png");
			ImageView iv4 = new ImageView(i4);
			iv4.setLayoutX(135 + (x * 18));
			iv4.setFitWidth(17);
			iv4.setFitHeight(17);
			hearts.add(iv4);
			heartGroup.getChildren().add(iv4);
		}
		infoStuff.getChildren().add(heartGroup);
		
		entities.getChildren().add(infoStuff);
		
		entities.getChildren().add(player.getImageView());
		Platform plat = new Platform((int) (menu.getScene().getWidth() / 20 + 10), menu.getDefaultHeight(), 0);
		entities.getChildren().add(plat.getPlatform());
		plat.getPlatform().toBack();
		
		ArrayList<Pane> p = new ArrayList<>();
		ArrayList<Pane> p2 = new ArrayList<>();
		ArrayList<Pane> p3 = new ArrayList<>();
		platforms.add(p);
		platforms.add(p2);
		platforms.add(p3);
		
		platforms.get(2).add(plat.getPlatform());
		
		Scene s = new Scene(entities, menu.getDefaultWidth(), menu.getDefaultHeight());
		gameScene = s;
		//System.out.println(gameScene.getWidth() + ";" + gameScene.getHeight());
		return s;
	}
	
	public void createShop(EntityHandler eh) {
		shop = new Group();
		
		Image shopBlueprint = new Image("file:pictures/Shop/Shop.png");
		ImageView iv = new ImageView(shopBlueprint);
		
		ShopItem[] items = new ShopItem[8];
		
		Label goldCost = new Label("");
		goldCost.setLayoutX(638);
		goldCost.setLayoutY(305);
		goldCost.setStyle("-fx-text-fill: #CA0; -fx-font: 25 Arial;");
		
		ShopItem speed = new ShopItem(ItemType.SPEED, player, getLabel(), eh);
		speed.getImageView().setLayoutX(41);
		speed.getImageView().setLayoutY(156);
		items[0] = speed;
		ShopItem jump = new ShopItem(ItemType.JUMP, player, getLabel(), eh);
		jump.getImageView().setLayoutX(192);
		jump.getImageView().setLayoutY(156);
		items[1] = jump;
		ShopItem glide = new ShopItem(ItemType.GLIDER, player, getLabel(), eh);
		glide.getImageView().setLayoutX(343);
		glide.getImageView().setLayoutY(156);
		items[2] = glide;
		ShopItem stomp = new ShopItem(ItemType.STOMP, player, getLabel(), eh);
		stomp.getImageView().setLayoutX(494);
		stomp.getImageView().setLayoutY(156);
		items[3] = stomp;
		ShopItem juice = new ShopItem(ItemType.JUICE, player, getLabel(), eh);
		juice.getImageView().setLayoutX(41);
		juice.getImageView().setLayoutY(385);
		this.juice = juice;
		items[4] = juice;
		ShopItem coins = new ShopItem(ItemType.GOLD, player, getLabel(), eh);
		coins.getImageView().setLayoutX(192);
		coins.getImageView().setLayoutY(385);
		items[5] = coins;
		ShopItem lives = new ShopItem(ItemType.LIVES, player, getLabel(), eh);
		lives.getImageView().setLayoutX(343);
		lives.getImageView().setLayoutY(385);
		items[6] = lives;
		ShopItem gun = new ShopItem(ItemType.GUN, player, getLabel(), eh);
		gun.getImageView().setLayoutX(494);
		gun.getImageView().setLayoutY(385);
		items[7] = gun;
		
		Label itemName = new Label("Shop Info");
		itemName.setMaxWidth(165);
		itemName.setPrefWidth(165);
		itemName.setWrapText(false);
		itemName.setLayoutX(637);
		itemName.setLayoutY(26);
		itemName.setStyle("-fx-font: 19 Arial; -fx-text-fill: #08D; -fx-font-weight: bold;"
				+ " -fx-effect: dropshadow( gaussian , #049 , 0,0,1,1 );");
		
		Label info = new Label("Click on an icon to see what that upgrade does. Some upgrades may have five"
				+ " different tiers, while others will only have one. Also, not all items are permanent"
				+ ". To purchase the upgrade, use the gold button below, or double click the icon.");
		info.setMaxWidth(160);
		info.setPrefWidth(160);
		info.setWrapText(true);
		info.setLayoutX(637);
		info.setLayoutY(55);
		info.setStyle("-fx-font: 16 Arial; -fx-text-fill: #727;"
				+ " -fx-effect: dropshadow( gaussian , #404 , 0,0,0,1 );");
		
		Label title = new Label("Shop");
		title.setPrefWidth(200);
		title.setLayoutX(365);
		title.setLayoutY(8);
		title.setStyle("-fx-font: 30 Arial; -fx-text-fill: #0A4;"
				+ " -fx-effect: dropshadow(gaussian , #0B2 , 0,0,0,1 );");
		
		Image green = new Image("file:pictures/ContinueRunGreen.png");
		ImageView contRun = new ImageView(green);
		contRun.setLayoutX(635);
		contRun.setLayoutY(350);
		contRun.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				savePlayerData();
				toggleShopDisplay();
				newRound();
			}
		});
		contRun.setOnMouseMoved(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent event) {
				contRun.setCursor(Cursor.HAND);
			}
		});
		
		Image red = new Image("file:pictures/MainMenuRed.png");
		ImageView menu = new ImageView(red);
		menu.setLayoutX(635);
		menu.setLayoutY(439);
		menu.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				savePlayerData();
				returnToTitle();
			}
		});
		menu.setOnMouseMoved(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent event) {
				menu.setCursor(Cursor.HAND);
			}
		});
		
		shop.getChildren().addAll(iv, speed.getImageView(), jump.getImageView(), glide.getImageView(), 
				gun.getImageView(), stomp.getImageView(), juice.getImageView(), coins.getImageView(),
				lives.getImageView(), speed.getpBar(), jump.getpBar(), glide.getpBar(), gun.getpBar(),
				stomp.getpBar(), juice.getpBar(), coins.getpBar(), lives.getpBar(), 
				itemName, info, title, contRun, menu, goldCost);
		speed.positionCorrectly();
		jump.positionCorrectly();
		glide.positionCorrectly();
		gun.positionCorrectly();
		stomp.positionCorrectly();
		juice.positionCorrectly();
		coins.positionCorrectly();
		lives.positionCorrectly();
		shop.setLayoutX(50);
		shop.setLayoutY(50);
		
		addHandlers(items, itemName, info, goldCost);
		goldCost.setText("");
	}
	
	private void returnToTitle() {
		menu.setScene(menu.getScene());
	}
	
	private void newRound() {
		player.getImageView().setLayoutX(10);
		player.getImageView().setLayoutY(0);
		entities.getChildren().clear();
		entities.getChildren().add(player.getImageView());
		platforms.get(0).clear();
		platforms.get(1).clear();
		platforms.get(2).clear();
		Platform plat = new Platform((int) (menu.getScene().getWidth() / 20 + 10), menu.getDefaultHeight(), 0);
		entities.getChildren().addAll(plat.getPlatform(), infoStuff, gold, goldView);
		goldView.setLayoutX(0);
		goldView.setLayoutY(0);
		goldView.setFitWidth(17);
		goldView.setFitHeight(17);
		gold.setLayoutX(20);
		gold.setLayoutY(-2);
		gold.setStyle("-fx-font: 18 Arial; -fx-text-fill: #CA0;");
		plat.getPlatform().toBack();
		platforms.get(2).add(plat.getPlatform());
		ms.resumeAllTimers();
		player.setGlideJuice(100);
		player.setLives(player.getMaxLives());
		addHearts();
		if (player.isCanGlide()) {
			glideJuice.setWidth(100);
		}
	}
	
	private void addHearts() {
		infoStuff.getChildren().remove(heartGroup);
		for (int x = 0; x < player.getMaxLives() - 1; x++) {
			Image i4 = new Image("file:pictures/Heart.png");
			ImageView iv4 = new ImageView(i4);
			iv4.setLayoutX(135 + (x * 18));
			iv4.setFitWidth(17);
			iv4.setFitHeight(17);
			hearts.add(iv4);
			heartGroup.getChildren().add(iv4);
		}
		infoStuff.getChildren().add(heartGroup);
	}
	
	private void addHandlers(ShopItem[] items, Label itemName, Label itemInfo, Label goldCost) {
			items[0].getImageView().setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					if (event.getClickCount() >= 2) {
						items[0].purchaseItem();
					}
					else {
						itemInfo.setText("Upgrades your character's speed slightly to get you over those"
								+ " large gaps. This upgrade has five tiers.");
					}
					if (items[0].getLevel() != items[0].getMaxLevel()) {
						itemName.setText("Speed (" + (items[0].getLevel()) + ")");
					}
					else {
						itemName.setText("Speed (MAX)");
					}
					goldCost.setText(items[0].getCostForNextLevel() + " Gold");
				}
			});
			items[1].getImageView().setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					if (event.getClickCount() >= 2) {
						items[1].purchaseItem();
					}
					if (items[1].getLevel() != items[1].getMaxLevel()) {
						itemName.setText("Jump (" + (items[1].getLevel()) + ")");
					}
					else {
						itemName.setText("Jump (MAX)");
					}
					itemInfo.setText("Upgrades the number of jumps your"
							+ " character is able to perform before touching"
							+ " the ground again. Currently, you can only jump"
							+ " " + player.getNumberOfJumps() + " time(s). This upgrade has three tiers.");
					goldCost.setText(items[1].getCostForNextLevel() + " Gold");
				}
			});
			items[2].getImageView().setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					if (event.getClickCount() >= 2) {
						items[2].purchaseItem();
					}
					else {
						itemInfo.setText("Gives your character the ability to glide"
								+ ", by pressing the shift key, for a limited amount of time"
								+ ". This upgrade only has one tier.");
					}
					if (items[2].getLevel() != items[2].getMaxLevel()) {
						itemName.setText("Glide (" + (items[2].getLevel()) + ")");
					}
					else {
						itemName.setText("Glide (MAX)");
					}
					goldCost.setText(items[2].getCostForNextLevel() + " Gold");
				}
			});
			items[3].getImageView().setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					if (event.getClickCount() >= 2) {
						items[3].purchaseItem();
					}
					else {
						itemInfo.setText("Gives your character the ability to eliminate enemies"
								+ " from above, by jumping on them, leaving you unharmed."
								+ " Remind you of certain Italian "
								+ " Plumber? This upgrade only has one tier.");
					}
					if (items[3].getLevel() != items[3].getMaxLevel()) {
						itemName.setText("Stomp (" + (items[3].getLevel()) + ")");
					}
					else {
						itemName.setText("Stomp (MAX)");
					}
					goldCost.setText(items[3].getCostForNextLevel() + " Gold");
				}
			});
			items[4].getImageView().setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					if (event.getClickCount() >= 2) {
						items[4].purchaseItem();
					}
					else {
						itemInfo.setText("A consumable that dramatically increases your character's"
								+ " physical abilites, by pressing the 'F' key, for a short duration. "
								+ " This upgrade only has one teir, and will have to be"
								+ " re-purchased after usage.");
					}
					if (items[4].getLevel() != items[4].getMaxLevel()) {
						itemName.setText("Juice (" + (items[4].getLevel()) + ")");
					}
					else {
						itemName.setText("Juice (MAX)");
					}
					goldCost.setText(items[4].getCostForNextLevel() + " Gold");
				}
			});
			items[5].getImageView().setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					if (event.getClickCount() >= 2) {
						items[5].purchaseItem();
					}
					else {
						itemInfo.setText("Increases the amount of gold generated. Also, it slightly increases"
								+ " the range in which you collect the gold. The gold will come to you, "
								+ "instead of the other way around. This upgrade has five tiers.");
					}
					if (items[5].getLevel() != items[5].getMaxLevel()) {
						itemName.setText("Gold (" + (items[5].getLevel()) + ")");
					}
					else {
						itemName.setText("Gold (MAX)");
					}
					goldCost.setText(items[5].getCostForNextLevel() + " Gold");
				}
			});
			items[6].getImageView().setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					if (event.getClickCount() >= 2) {
						items[6].purchaseItem();
					}
					else {
						itemInfo.setText("Upgrades the number of lives you have. With each life,"
								+ " you can fall off the map, or get hit by an enemy and"
								+ " come out unharmed with the sacrifice of one of your lives"
								+ ". This upgrade has three tiers.");
					}
					if (items[6].getLevel() != items[6].getMaxLevel()) {
						itemName.setText("Lives (" + (items[6].getLevel()) + ")");
					}
					else {
						itemName.setText("Lives (MAX)");
					}
					goldCost.setText(items[6].getCostForNextLevel() + " Gold");
				}
			});
			items[7].getImageView().setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					if (event.getClickCount() >= 2) {
						items[7].purchaseItem();
					}
					else {
						itemInfo.setText("Upgrades the amount of ammo your trusty slingshot can fire."
								+ " The tides have turned, enemies will fear you now!"
								+ " This upgrade has five tiers.");
					}
					if (items[7].getLevel() != items[7].getMaxLevel()) {
						itemName.setText("Gun (" + (items[7].getLevel()) + ")");
					}
					else {
						itemName.setText("Gun (MAX)");
					}
					goldCost.setText(items[7].getCostForNextLevel() + " Gold");
				}
			});
			shop.setOnMouseMoved(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					if (intersects(items[0], event) || intersects(items[1], event) || 
							intersects(items[2], event) || intersects(items[3], event) ||
							intersects(items[4], event) || intersects(items[5], event) ||
							intersects(items[6], event) || intersects(items[7], event)) {
						shop.setCursor(Cursor.HAND);
					}
					else {
						shop.setCursor(Cursor.DEFAULT);
					}
				}
			});
	}
	
	private boolean intersects(ShopItem si, MouseEvent cursor) {
		if (cursor.getX() >= si.getImageView().getBoundsInParent().getMinX() &&
				cursor.getX() <= si.getImageView().getBoundsInParent().getMaxX() &&
				cursor.getY() >= si.getImageView().getBoundsInParent().getMinY() &&
				cursor.getY() <= si.getImageView().getBoundsInParent().getMaxY()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void toggleShopDisplay() {
		if (shopUp) {
			entities.getChildren().remove(shop);
			shop.getChildren().removeAll(gold, goldView);
			shopUp = false;
		}
		else {
			checkCount();
			entities.getChildren().add(shop);
			gold.toFront();
			gold.setLayoutX(42);
			gold.setLayoutY(10);
			gold.setStyle("-fx-font: 30 Arial; -fx-text-fill: #CA0;");
			goldView.toFront();
			goldView.setLayoutX(15);
			goldView.setLayoutY(16);
			goldView.setFitWidth(25);
			goldView.setFitHeight(25);
			shop.getChildren().addAll(goldView, gold);
			shopUp = true;
		}
	}
	
	private void savePlayerData() {
		try {
			File inputFile = new File("levels/endlessRun/EndlessRun.xml");
		      DocumentBuilderFactory docFactory =
		      DocumentBuilderFactory.newInstance();
		      DocumentBuilder docBuilder = 
		      docFactory.newDocumentBuilder();
		      Document doc = docBuilder.parse(inputFile);
		      //Node cars = doc.getFirstChild();
		      Node player = doc.getElementsByTagName("Player").item(0);
		      // update supercar attribute
//		      NamedNodeMap attr = player.getAttributes();
//		      Node nodeAttr = attr.getNamedItem("company");
//		      nodeAttr.setTextContent("Lamborigini");

		      // loop the supercar child node
		      NodeList list = player.getChildNodes();
		      for (int temp = 0; temp < list.getLength(); temp++) {
		         Node node = list.item(temp);
		         if (node.getNodeType() == Node.ELEMENT_NODE) {
		            Element eElement = (Element) node;
		            if ("LivesLevel".equals(eElement.getNodeName())){
		            	eElement.setTextContent("" + (this.player.getMaxLives() - 1));
		            }
		            else if ("GunLevel".equals(eElement.getNodeName())) {
		            	eElement.setTextContent("" + (this.player.getMaxAmmo() / 2));
		            }
		            else if ("StompLevel".equals(eElement.getNodeName())) {
		            	eElement.setTextContent("" + (this.player.isCanStomp() ? 1 : 0));
		            }
		            else if ("GlideLevel".equals(eElement.getNodeName())) {
		            	eElement.setTextContent("" + (this.player.isCanGlide() ? 1 : 0));
		            }
		            else if ("GoldLevel".equals(eElement.getNodeName())) {
		            	eElement.setTextContent("" + (this.player.getPickupRange() / 25));
		            }
		            else if ("JumpLevel".equals(eElement.getNodeName())) {
		            	eElement.setTextContent("" + (this.player.getNumberOfJumps() - 1));
		            }
		            else if ("SpeedLevel".equals(eElement.getNodeName())) {
		            	eElement.setTextContent("" + ((int) ((this.player.getSpeed() - 1.2) / .2)));
		            }
		            else if ("JuiceLevel".equals(eElement.getNodeName())) {
		            	eElement.setTextContent("" + (this.player.getEquippedJuice() != null ? 1 : 0));
		            }
		            else if ("Gold".equals(eElement.getNodeName())) {
		            	eElement.setTextContent("" + (this.player.getGold()));
		            }
		         }
		      }
//		      NodeList childNodes = cars.getChildNodes();
//		      for(int count = 0; count < childNodes.getLength(); count++){
//		         Node node = childNodes.item(count);
//		         if("luxurycars".equals(node.getNodeName()))
//		            cars.removeChild(node);
//		         }
		         TransformerFactory transformerFactory = TransformerFactory.newInstance();
		         Transformer transformer = transformerFactory.newTransformer();
		         DOMSource source = new DOMSource(doc);
		         StreamResult consoleResult = new StreamResult(inputFile);
		         transformer.transform(source, consoleResult);
	      } catch (DOMException | TransformerException |
	    		  ParserConfigurationException | SAXException | IOException e) {
	         System.out.println("Error saving player data");
	      }
	}
	
	private void checkCount() {
		if (player.getEquippedJuice() == null) {
			juice.getpBar().setImage(new Image("file:pictures/ProgressBars/1bar0.png"));
			juice.setLevel(0);
		}
		else {
			juice.getpBar().setImage(new Image("file:pictures/ProgressBars/1bar1.png"));
			juice.setLevel(juice.getMaxLevel());
		}
	}
	
	public ArrayList<ImageView> getHearts() {
		return hearts;
	}
	
	public Group getHeartGroup() {
		return heartGroup;
	}
	
	public Rectangle getGlideMeter() {
		return glideJuice;
	}
	
	public Label getSeconds() {
		return seconds;
	}
	
	public Label getLabel() {
		return gold;
	}
	
	public ArrayList<ArrayList<Pane>> getPlatforms() {
		return platforms;
	}
	
	public Scene getScene() {
		return gameScene;
	}
	
	public Pane getEntities() {
		return entities;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public boolean isEndless() {
		return isEndless;
	}
	
	public double[] getPositions() {
		return positions;
	}
}
