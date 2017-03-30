package game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import enums.HandlerType;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import models.Juice;
import models.Platform;
import models.Player;

public class ModeSelection {

	private Scene promptScene;
	private Scene modeScene;
	private Menu menu;
	private Image image = new Image("file:pictures/Cursor.png");
	private ImageCursor cursor = new ImageCursor(image);
	private Image image2 = new Image("file:pictures/CursorEaten.png");
	private ImageCursor cursorEaten = new ImageCursor(image2);
	private Image levels = new Image("file:pictures/LevelsButton.png");
	private Image levelsHover = new Image("file:pictures/LevelsButtonHover.png");
	private Image endless = new Image("file:pictures/EndlessButton.png");
	private Image endlessHover = new Image("file:pictures/EndlessButtonHover.png");
	private Image mainMenu = new Image("file:pictures/MainMenuButton.png");
	private Image mainMenuHover = new Image("file:pictures/MainMenuButtonHover.png");
	private Image newRun = new Image("file:pictures/NewGameButton.png");
	private Image newRunHover = new Image("file:pictures/NewGameButtonHover.png");
	private Image continueRun = new Image("file:pictures/ContinueRun.png");
	private Image continueRunHover = new Image("file:pictures/ContinueRunHover.png");
	private Image back = new Image("file:pictures/BackButton.png");
	private Image backHover = new Image("file:pictures/BackButtonHover.png");
	private AnimationHandler ah;
	private KeypressHandler kh;
	private PlatformHandler ph;
	private EntityHandler eh;
	
	public ModeSelection(Menu menu, double width, double height) {
		modeScene = createScene(width, height);
		createPromptScene(width, height);
		this.menu = menu;
		modeScene.setCursor(cursor);
	}
	
	private Scene createScene(double width, double height) {
		BorderPane bp = new BorderPane();
		VBox vbox = new VBox();
		
		ImageView iv = new ImageView(levels);
		iv.setFitHeight(100);
		iv.setFitWidth(200);
		ImageView iv2 = new ImageView(endless);
		iv2.setFitHeight(100);
		iv2.setFitWidth(200);
		ImageView iv3 = new ImageView(mainMenu);
		iv3.setFitHeight(100);
		iv3.setFitWidth(200);
		
		vbox.setSpacing(20);
		vbox.getChildren().addAll(iv, iv2, iv3);
		vbox.setAlignment(Pos.CENTER);
		addListener(vbox, iv, iv2, iv3);
		
		bp.setCenter(vbox);
		
		Scene s = new Scene(bp, width, height);
		return s;
	}
	
	private void addListener(VBox vbox, ImageView iv, ImageView iv2, ImageView iv3) {
		vbox.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (menu.intersects(event, iv)) {
					iv.setImage(levelsHover);
					vbox.setCursor(cursorEaten);
				}
				else if (menu.intersects(event, iv2)) {
					iv2.setImage(endlessHover);
					vbox.setCursor(cursorEaten);
				}
				else if (menu.intersects(event, iv3)) {
					iv3.setImage(mainMenuHover);
					vbox.setCursor(cursorEaten);
				}
				else {
					iv.setImage(levels);
					iv2.setImage(endless);
					iv3.setImage(mainMenu);
					vbox.setCursor(cursor);
				}
			}
		});
		iv.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				System.out.println("Level selection");
				loadLevel("levels/endlessRun/EndlessRun.txt");
			}
		});
		
		iv2.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				menu.setScene(promptScene);
			}
		});
		iv3.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				menu.setScene(menu.getScene());
			}
		});
	}
	
	private void startNewGame() {
		ModeSelection ms = this;
		Game g = new Game(menu, true, ms, null, new Player(menu.getDefaultHeight()));
		ah = new AnimationHandler(g);
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
		}
		kh = new KeypressHandler(g, ah, g.getPlatforms(), menu.getDefaultHeight() - 50, ms);
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
		}
		ph = new PlatformHandler(g.getPlatforms(), g);
		eh = new EntityHandler(g, ph, 2, ms);
		menu.setScene(g.getScene());
		g.createShop(eh);
	}
	
	private void createPromptScene(double width, double height) {
		BorderPane bp = new BorderPane();
		VBox vbox = new VBox();
		
		ImageView iv = new ImageView(newRun);
		iv.setFitHeight(100);
		iv.setFitWidth(200);
		ImageView iv2 = new ImageView(continueRun);
		iv2.setFitHeight(100);
		iv2.setFitWidth(200);
		ImageView iv3 = new ImageView(back);
		iv3.setFitHeight(75);
		iv3.setFitWidth(150);
		
		vbox.setSpacing(20);
		vbox.getChildren().addAll(iv, iv2, iv3);
		vbox.setAlignment(Pos.CENTER);
		addListeners(vbox, iv, iv2, iv3);
		
		bp.setCenter(vbox);
		
		promptScene = new Scene(bp, width, height);
	}
	
	private void addListeners(VBox vbox, ImageView iv, ImageView iv2, ImageView iv3) {
		vbox.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (menu.intersects(event, iv)) {
					iv.setImage(newRunHover);
					vbox.setCursor(cursorEaten);
				}
				else if (menu.intersects(event, iv2)) {
					iv2.setImage(continueRunHover);
					vbox.setCursor(cursorEaten);
				}
				else if (menu.intersects(event, iv3)) {
					iv3.setImage(backHover);
					vbox.setCursor(cursorEaten);
				}
				else {
					iv.setImage(newRun);
					iv2.setImage(continueRun);
					iv3.setImage(back);
					vbox.setCursor(cursor);
				}
			}
		});
		iv.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				startNewGame();
			}
		});
		
		iv2.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				loadPlayer("levels/endlessRun/EndlessRun.xml");
			}
		});
		
		iv3.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				menu.setScene(modeScene);
			}
		});
	}
	
	private void loadPlayer(String fileName) {
		ArrayList<Platform> platforms = new ArrayList<>();
		Player p = new Player(menu.getDefaultHeight());
		double multiplier = 2;
		try {
			//Create a document builder
			File inputFile = new File(fileName);
			
	         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	         
	         Document doc = dBuilder.parse(inputFile);
	         doc.getDocumentElement().normalize();
			
			//Extract root element
			Element root = doc.getDocumentElement();
			
			NodeList nlist = root.getElementsByTagName("Player");
			
			for (int i = 0; i < nlist.getLength(); i++) {
				Node node = nlist.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					//System.out.println(element.getElementsByTagName("xCoord").item(0).getTextContent());
					double speedLevel = Double.parseDouble(element.getElementsByTagName("SpeedLevel").item(0).getTextContent());
					int jumpLevel = Integer.parseInt(element.getElementsByTagName("JumpLevel").item(0).getTextContent());
					int glideLevel = Integer.parseInt(element.getElementsByTagName("GlideLevel").item(0).getTextContent());
					int stompLevel = Integer.parseInt(element.getElementsByTagName("StompLevel").item(0).getTextContent());
					int goldLevel = Integer.parseInt(element.getElementsByTagName("GoldLevel").item(0).getTextContent());
					int gunLevel = Integer.parseInt(element.getElementsByTagName("GunLevel").item(0).getTextContent());
					int livesLevel = Integer.parseInt(element.getElementsByTagName("LivesLevel").item(0).getTextContent());
					int juiceLevel = Integer.parseInt(element.getElementsByTagName("JuiceLevel").item(0).getTextContent());
					int gold = Integer.parseInt(element.getElementsByTagName("Gold").item(0).getTextContent());

					/*
					 * Speed per level: .2
					 * Jumps per level: 1
					 * Glide per level: 0
					 * Stomp per level: 0
					 * Gold per level: 25 range; 1.5 multiplier
					 * Gun per level: 2
					 * Lives per level: 1
					 * Juice per level: 0
					 */
					
					double speed = 1.2 + (speedLevel * .2);
					int jump = jumpLevel + 1;
					boolean glide = (glideLevel == 1) ? true : false;
					boolean stomp = (stompLevel == 1) ? true : false;
					multiplier = (goldLevel * 1.5) + 2;
					int pickupRange = (goldLevel * 25);
					int gun = (gunLevel * 2);
					int lives = livesLevel + 1;
					Juice juice = (juiceLevel == 1) ? new Juice() : null;
					
					p = new Player(menu.getDefaultHeight(), speed, jump, pickupRange, gold, gun, lives, glide, stomp, juice);
					p.setLives(p.getMaxLives());
					p.setAmmo(p.getMaxAmmo());
				}
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			System.out.println("DocumentBuilder failed");
		}
		continueGame(platforms, true, p, multiplier);
	}
	
	private void loadLevel(String fileName) {
		ArrayList<Platform> platforms = new ArrayList<>();
		Player player = new Player(menu.getDefaultHeight());
				try {
					//Create a document builder
					File inputFile = new File(fileName);
			         DocumentBuilderFactory dbFactory 
			            = DocumentBuilderFactory.newInstance();
			         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			         Document doc = dBuilder.parse(inputFile);
			         doc.getDocumentElement().normalize();
					
					//Create a document from a file or stream
//					xmlStringBuilder.append("<?xml version=\"1.0\"?> <class> </class>");
//					ByteArrayInputStream input =  
//							new ByteArrayInputStream(xmlStringBuilder.toString().getBytes("UTF-8"));
					
					//Extract root element
					Element root = doc.getDocumentElement();
					
					NodeList nlist = root.getElementsByTagName("Platform");
					
					for (int i = 0; i < nlist.getLength(); i++) {
						Node node = nlist.item(i);
						if (node.getNodeType() == Node.ELEMENT_NODE) {
							Element element = (Element) node;
							//System.out.println(element.getElementsByTagName("xCoord").item(0).getTextContent());
							Platform p = new Platform(
									Integer.parseInt(
											element.getElementsByTagName("width").item(0).getTextContent())
									, Integer.parseInt(
											element.getElementsByTagName("yCoord").item(0).getTextContent())
									, Integer.parseInt(
											element.getElementsByTagName("xCoord").item(0).getTextContent()));
							platforms.add(p);
						}
					}
					
					//returns specific attribute
					root.getAttribute("attributeName"); 
					//returns a Map (table) of names/values
					root.getAttributes(); 
					
					//returns a list of subelements of specified name
					root.getElementsByTagName("subelementName"); 
					//returns a list of all child nodes
					root.getChildNodes(); 
				} catch (ParserConfigurationException | SAXException | IOException e) {
					System.out.println("DocumentBuilder failed");
				}
				continueGame(platforms, false, player, 2);
	}
	
	private void continueGame(ArrayList<Platform> platforms, boolean endless, Player p, double multiplier) {
		ModeSelection ms = this;
		Game g = new Game(menu, endless, ms, platforms, p);
		ah = new AnimationHandler(g);
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
		}
		kh = new KeypressHandler(g, ah, g.getPlatforms(), menu.getDefaultHeight() - 50, ms);
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
		}
		ph = new PlatformHandler(g.getPlatforms(), g);
		eh = new EntityHandler(g, ph, multiplier, ms);
		menu.setScene(g.getScene());
		g.createShop(eh);
	}
	
	public KeypressHandler getKey() {
		return kh;
	}
	
	public EntityHandler getEntity() {
		return eh;
	}
	
	public Scene getScene() {
		return modeScene;
	}
	
	public void handleKeyListener(HandlerType ht) {
		switch (ht) {
		case PAUSE:
			kh.pauseTimer();
			break;
		case STOP:
			kh.stopTimer();
			break;
		case PLAY:
			kh.startTimer();
			break;
		default:
			break;
		}
	}
	
	public void handleAnimation(HandlerType ht) {
		switch (ht) {
		case PAUSE:
			ah.pauseTimer();
			break;
		case STOP:
			ah.stopTimer();
			break;
		case PLAY:
			ah.startTimer();
			break;
		default:
			break;
		}
	}
	
	public void handlePlatform(HandlerType ht) {
		switch (ht) {
		case PAUSE:
			ph.pauseTimer();
			break;
		case STOP:
			ph.stopTimer();
			break;
		case PLAY:
			ph.startTimer();
			break;
		default:
			break;
		}
	}
	
	public void handleEntity(HandlerType ht) {
		switch (ht) {
		case PAUSE:
			eh.pauseTimer();
			break;
		case STOP:
			eh.stopTimer();
			break;
		case PLAY:
			eh.startTimer();
			break;
		default:
			break;
		}
	}
	
	public void resumeAllTimers() {
		eh.startTimer();
		ph.startTimer();
		ah.startTimer();
		kh.startTimer();
	}
	
	public void pauseAllTimers() {
		eh.pauseTimer();
		ph.pauseTimer();
		ah.pauseTimer();
		kh.pauseTimer();
	}
}
