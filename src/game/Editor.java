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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import models.Coin;
import models.Enemy;
import models.Entity;
import models.MeleeEnemy;
import models.Platform;
import models.RangedEnemy;

public class Editor {

	private static Scene scene;
	private static Group entities;
	private static Group toolbar;
	private static Rectangle slider;
	private static double startingX;
	private static int scrollMagnitude = 20;
	private static ArrayList<Platform> platforms = new ArrayList<>();
	private static ArrayList<Enemy> enemies = new ArrayList<>();
	private static ArrayList<Coin> coins = new ArrayList<>();
	private static ArrayList<Entity> allEntities = new ArrayList<>();
	private static ImageView selectedImage = new ImageView();
	private static Image platform = new Image("file:pictures/Brick.png");
	private static Image coin = new Image("file:pictures/Coin.png");
	private static Image dirtyCoin = new Image("file:pictures/DirtyCoin.png");
	private static Image melee = new Image("file:pictures/MeleeEnemy.png");
	private static Image ranged = new Image("file:pictures/RangedEnemy.png");
	private static Image delete = new Image("file:pictures/Delete.png");
	private static final Slider platformSize = new Slider(1, 100, 1);
	private static Menu menu;
	private static double width;
	private static double height;
	
	public static void createScene(double width, double height, Menu menu) {
		Editor.width = width;
		Editor.height = height;
		Editor.menu = menu;
		Group g = new Group();
		entities = new Group();
		toolbar = new Group();
		
		platformSize.setShowTickLabels(false);
		platformSize.setShowTickMarks(false);
		platformSize.setMajorTickUnit(19);
		platformSize.setMinorTickCount(5);
		platformSize.setBlockIncrement(10);
		
		Image i = new Image("file:pictures/Toolbar.png");
		ImageView iv = new ImageView(i);
		entities.getChildren().add(iv);
		iv.setFitWidth(width - 200);
		iv.setFitHeight(height - 25);
		
		slider = new Rectangle(150, 25, Paint.valueOf("#970"));
		slider.setArcHeight(20);
		slider.setArcWidth(20);
		slider.setLayoutY(height - 25);
		slider.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getSceneX() - startingX >= 0 &&
						event.getSceneX() - startingX + slider.getWidth() <= width - 200) {
					slider.setLayoutX(event.getSceneX() - startingX);
					for (int i = 0; i < allEntities.size(); i++) {
						if (allEntities.get(i).getClass().equals(Platform.class)) {
							((Platform) allEntities.get(i)).getPlatform().setLayoutX(allEntities.get(i).getActualX() - (event.getSceneX() - startingX) * scrollMagnitude);
						}
						else {
							allEntities.get(i).getImageView().setLayoutX(allEntities.get(i).getActualX() - (event.getSceneX() - startingX) * scrollMagnitude);
						}
					}
				}
			}
		});
		slider.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				startingX = event.getSceneX() - slider.getLayoutX();
			}
		});
		
		Image i2 = new Image("file:pictures/Toolbar.png");
		ImageView iv2 = new ImageView(i2);
		toolbar.getChildren().add(iv2);
		iv2.setLayoutX(width - 200);
		iv2.setLayoutY(0);
		iv2.setFitWidth(200);
		iv2.setFitHeight(height);
		
		ImageView pv = new ImageView(platform);
		pv.setLayoutX(803);
		pv.setLayoutY(13);
		pv.setFitWidth(50);
		pv.setFitHeight(50);
		ImageView cv = new ImageView(coin);
		cv.setLayoutX(788);
		cv.setLayoutY(93);
		cv.setFitWidth(25);
		cv.setFitHeight(25);
		ImageView dc = new ImageView(dirtyCoin);
		dc.setLayoutX(848);
		dc.setLayoutY(93);
		dc.setFitWidth(25);
		dc.setFitHeight(25);
		ImageView rv = new ImageView(ranged);
		rv.setLayoutX(763);
		rv.setLayoutY(143);
		rv.setFitWidth(50);
		rv.setFitHeight(50);
		ImageView mv = new ImageView(melee);
		mv.setLayoutX(843);
		mv.setLayoutY(143);
		mv.setFitWidth(50);
		mv.setFitHeight(50);
		ImageView dv = new ImageView(delete);
		dv.setLayoutX(813);
		dv.setLayoutY(580);
		dv.setFitWidth(50);
		dv.setFitHeight(50);
		
		Rectangle select = new Rectangle(100, 100);
		select.setStroke(Paint.valueOf("#09F"));
		select.setFill(Paint.valueOf("rgba(0,0,0,0)"));
		select.setArcWidth(20);
		select.setArcHeight(20);
		select.setStrokeWidth(3);
		select.getStrokeDashArray().addAll(3d, 12d);
		select.setStroke(Paint.valueOf("rgba(0,0,0,0)"));
		
		toolbar.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (intersects(event, pv)) {
					selectImage(pv, select);
				}
				else if (intersects(event, cv)) {
					selectImage(cv, select);
				}
				else if (intersects(event, dc)) {
					selectImage(dc, select);
				}
				else if (intersects(event, rv)) {
					selectImage(rv, select);
				}
				else if (intersects(event, mv)) {
					selectImage(mv, select);
				}
				else if (intersects(event, dv)) {
					selectImage(dv, select);
				}
				else {
					selectedImage = new ImageView();
					select.setStroke(Paint.valueOf("rgba(0,0,0,0)"));
				}
			}
		});
		
		toolbar.getChildren().addAll(pv, cv, dc, rv, mv, dv, select);
		
		g.getChildren().addAll(entities, toolbar, slider);
		
		scene = new Scene(g, width, height);
		entitiesAddition();
		addTools(iv);
	}
	
	private static void entitiesAddition() {
		entities.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (selectedImage.getImage() != null) {
					if (selectedImage.getImage().equals(platform)) {
						Platform p = new Platform((int) platformSize.getValue(), event.getSceneY(), event.getSceneX());
						p.getPlatform().setLayoutX(p.getPlatform().getLayoutX() - p.getWidth() / 2 - 20);
						p.getPlatform().setLayoutY(p.getPlatform().getLayoutY() + p.getHeight() / 2);
						p.setActualX(p.getPlatform().getLayoutX() + (slider.getLayoutX() * scrollMagnitude));
						platforms.add(p);
						allEntities.add(p);
						entities.getChildren().add(p.getPlatform());
					}
					else if (selectedImage.getImage().equals(coin)) {
						Coin c = new Coin(event.getSceneX(), event.getSceneY(), coin);
						c.getImageView().setLayoutX(c.getImageView().getLayoutX() - c.getImageView().getFitWidth() / 2);
						c.getImageView().setLayoutY(c.getImageView().getLayoutY() - c.getImageView().getFitHeight() / 2);
						c.setActualX(c.getImageView().getLayoutX() + (slider.getLayoutX() * scrollMagnitude));
						coins.add(c);
						allEntities.add(c);
						entities.getChildren().add(c.getImageView());
					}
					else if (selectedImage.getImage().equals(dirtyCoin)) {
						Coin c = new Coin(event.getSceneX(), event.getSceneY(), dirtyCoin);
						c.getImageView().setLayoutX(c.getImageView().getLayoutX() - c.getImageView().getFitWidth() / 2);
						c.getImageView().setLayoutY(c.getImageView().getLayoutY() - c.getImageView().getFitHeight() / 2);
						c.setActualX(c.getImageView().getLayoutX() + (slider.getLayoutX() * scrollMagnitude));
						coins.add(c);
						allEntities.add(c);
						entities.getChildren().add(c.getImageView());
					}
					else if (selectedImage.getImage().equals(ranged)) {
						RangedEnemy r = new RangedEnemy();
						r.getImageView().setFitWidth(64);
						r.getImageView().setFitHeight(64);
						r.getImageView().setLayoutX(event.getSceneX());
						r.getImageView().setLayoutY(event.getSceneY());
						r.getImageView().setLayoutX(r.getImageView().getLayoutX() - r.getImageView().getFitWidth() / 2);
						r.getImageView().setLayoutY(r.getImageView().getLayoutY() - r.getImageView().getFitHeight() / 2);
						r.setActualX(r.getImageView().getLayoutX() + (slider.getLayoutX() * scrollMagnitude));
						enemies.add(r);
						allEntities.add(r);
						entities.getChildren().add(r.getImageView());
					}
					else if (selectedImage.getImage().equals(melee)) {
						MeleeEnemy r = new MeleeEnemy();
						r.getImageView().setFitWidth(64);
						r.getImageView().setFitHeight(64);
						r.getImageView().setLayoutX(event.getSceneX());
						r.getImageView().setLayoutY(event.getSceneY());
						r.getImageView().setLayoutX(r.getImageView().getLayoutX() - r.getImageView().getFitWidth() / 2);
						r.getImageView().setLayoutY(r.getImageView().getLayoutY() - r.getImageView().getFitHeight() / 2);
						r.setActualX(r.getImageView().getLayoutX() + (slider.getLayoutX() * scrollMagnitude));
						enemies.add(r);
						allEntities.add(r);
						entities.getChildren().add(r.getImageView());
					}
					else if (selectedImage.getImage().equals(delete)) {
						for (int i = 0; i < allEntities.size(); i++) {
							if (allEntities.get(i).getClass().equals(Platform.class)) {
								if (intersects(event, ((Platform) allEntities.get(i)).getPlatform())) {
									platforms.remove(allEntities.get(i));
									entities.getChildren().remove(((Platform) allEntities.get(i)).getPlatform());
									allEntities.remove(i);
									break;
								}
							}
							else {
								if (intersects(event, allEntities.get(i).getImageView())) {
									if (coins.contains(allEntities.get(i))) {
										coins.remove(allEntities.get(i));
									}
									else if (enemies.contains(allEntities.get(i))) {
										enemies.remove(allEntities.get(i));
									}
									entities.getChildren().remove(allEntities.get(i).getImageView());
									allEntities.remove(i);
									break;
								}
							}
						}
					}
					else {
						
					}
				}
			}
		});
	}
	
	private static void addTools(ImageView iv) {
		MenuBar menuBar = new MenuBar();
		menuBar.setStyle("-fx-background-color: #df9c4f; -fx-font: 13 Arial;");
		
		javafx.scene.control.Menu clearItem = new javafx.scene.control.Menu("Clear Items");
		javafx.scene.control.Menu sizeItem = new javafx.scene.control.Menu("Item Sizes");
		javafx.scene.control.Menu fileItem = new javafx.scene.control.Menu("File");
		javafx.scene.control.Menu menuItem = new javafx.scene.control.Menu("Menu");
		
		MenuItem main = new MenuItem("Exit to Main Menu");
		main.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				menu.setScene(menu.getScene());
			}
		});
		
		menuItem.getItems().add(main);
		
		MenuItem save = new MenuItem("Save Level");
		save.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				FileChooser fc = new FileChooser();
				fc.setTitle("Save Your .xml Level");
				fc.setInitialDirectory(new File("levels/userCreatedLevels/"));
				fc.getExtensionFilters().addAll(
		                new FileChooser.ExtensionFilter("XML", "*.xml*")
		        );
				File f = fc.showSaveDialog(menu.getStage());
				SaveLevel(f.getAbsolutePath());
			}
		});
		
		MenuItem load = new MenuItem("Load Level");
		load.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				FileChooser fc = new FileChooser();
				fc.setTitle("Select a .xml Level");
				fc.setInitialDirectory(new File("levels/userCreatedLevels/"));
				fc.getExtensionFilters().addAll(
		                new FileChooser.ExtensionFilter("XML", "*.xml*")
		        );
				File f = fc.showOpenDialog(menu.getStage());
				loadLevel(f.getAbsolutePath());
			}
		});
		
		fileItem.getItems().addAll(save, load);
		
		MenuItem size = new MenuItem("Platform Size", platformSize);
		
		MenuItem clearAll = new MenuItem("Clear All");
		clearAll.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				enemies.clear();
				coins.clear();
				platforms.clear();
				allEntities.clear();
				entities.getChildren().clear();
				entities.getChildren().add(iv);
			}
		});
//		Label l2 = new Label("Clear All");
//		l2.setStyle("-fx-font: 15 Arial; -fx-text-fill: #A03; -fx-font-weight: bold;");
//		l2.setUnderline(true);
//		l2.setLayoutX(250);
//		
//		l2.setOnMouseMoved(new EventHandler<MouseEvent>() {
//			@Override
//			public void handle(MouseEvent event) {
//				l2.setCursor(Cursor.HAND);
//			}
//		});
//		l2.setOnMouseClicked(new EventHandler<MouseEvent>() {
//			@Override
//			public void handle(MouseEvent event) {
//				
//			}
//		});
		
		Label l = new Label("Platform Size: ");
		platformSize.setLayoutX(305);
		platformSize.setLayoutY(7);
		l.setLayoutX(200);
		l.setLayoutY(5);
		l.setStyle("-fx-font: 15 Arial; -fx-text-fill: #05A; -fx-font-weight: bold;");
		
		MenuItem clearCoins = new MenuItem("Clear Coins");
		clearCoins.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				for (int i = 0; i < coins.size(); i++) {
					if (allEntities.contains(coins.get(i))) {
						allEntities.remove(coins.get(i));
						entities.getChildren().remove(coins.get(i).getImageView());
					}
				}
				coins.clear();
			}
		});
//		Label l3 = new Label("Clear Coins");
//		l3.setStyle("-fx-font: 15 Arial; -fx-text-fill: #A80; -fx-font-weight: bold;");
//		l3.setUnderline(true);
//		l3.setLayoutX(320);
//		
//		l3.setOnMouseMoved(new EventHandler<MouseEvent>() {
//			@Override
//			public void handle(MouseEvent event) {
//				l3.setCursor(Cursor.HAND);
//			}
//		});
//		l3.setOnMouseClicked(new EventHandler<MouseEvent>() {
//			@Override
//			public void handle(MouseEvent event) {
//				
//			}
//		});
		
		MenuItem clearPlatforms = new MenuItem("Clear Platforms");
		clearPlatforms.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				for (int i = 0; i < platforms.size(); i++) {
					if (allEntities.contains(platforms.get(i))) {
						allEntities.remove(platforms.get(i));
						entities.getChildren().remove(platforms.get(i).getPlatform());
					}
				}
				platforms.clear();
			}
		});
//		Label l4 = new Label("Clear Platforms");
//		l4.setStyle("-fx-font: 15 Arial; -fx-text-fill: #D80; -fx-font-weight: bold;");
//		l4.setUnderline(true);
//		l4.setLayoutX(410);
//		
//		l4.setOnMouseMoved(new EventHandler<MouseEvent>() {
//			@Override
//			public void handle(MouseEvent event) {
//				l4.setCursor(Cursor.HAND);
//			}
//		});
//		l4.setOnMouseClicked(new EventHandler<MouseEvent>() {
//			@Override
//			public void handle(MouseEvent event) {
//				
//			}
//		});
		
		MenuItem clearEnemies = new MenuItem("Clear Enemies");
		clearEnemies.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				for (int i = 0; i < enemies.size(); i++) {
					if (allEntities.contains(enemies.get(i))) {
						allEntities.remove(enemies.get(i));
						entities.getChildren().remove(enemies.get(i).getImageView());
					}
				}
				enemies.clear();
			}
		});
//		Label l5 = new Label("Clear Enemies");
//		l5.setStyle("-fx-font: 15 Arial; -fx-text-fill: #444; -fx-font-weight: bold;");
//		l5.setUnderline(true);
//		l5.setLayoutX(528);
//		
//		l5.setOnMouseMoved(new EventHandler<MouseEvent>() {
//			@Override
//			public void handle(MouseEvent event) {
//				l5.setCursor(Cursor.HAND);
//			}
//		});
//		l5.setOnMouseClicked(new EventHandler<MouseEvent>() {
//			@Override
//			public void handle(MouseEvent event) {
//				
//			}
//		});
		
		menuBar.getMenus().addAll(fileItem, menuItem, clearItem);
		sizeItem.getItems().add(size);
		clearItem.getItems().addAll(clearAll, clearCoins, clearPlatforms, clearEnemies);
		toolbar.getChildren().addAll(l, platformSize, menuBar);
	}
	
	private static void selectImage(ImageView iv, Rectangle r) {
		selectedImage = iv;
		r.setStroke(Paint.valueOf("#09F"));
		r.setWidth(iv.getFitWidth() + 10);
		r.setHeight(iv.getFitHeight() + 10);
		r.setLayoutX(iv.getLayoutX() - 5);
		r.setLayoutY(iv.getLayoutY() - 5);
	}
	
	private static boolean intersects(MouseEvent event, ImageView iv) {
		Bounds bounds = iv.getBoundsInParent();
		if (event.getX() >= bounds.getMinX() && event.getX() <= bounds.getMaxX() &&
				event.getY() >= bounds.getMinY() && event.getY() <= bounds.getMaxY()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private static boolean intersects(MouseEvent event, Pane iv) {
		Bounds bounds = iv.getBoundsInParent();
		if (event.getX() >= bounds.getMinX() && event.getX() <= bounds.getMaxX() &&
				event.getY() >= bounds.getMinY() && event.getY() <= bounds.getMaxY()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private static void loadLevel(String fileName) {
		Editor.entities.getChildren().clear();
		Image im = new Image("file:pictures/Toolbar.png");
		ImageView iv = new ImageView(im);
		entities.getChildren().add(iv);
		iv.setFitWidth(width - 200);
		iv.setFitHeight(height - 25);
		Editor.enemies.clear();
		Editor.coins.clear();
		Editor.platforms.clear();
		Group all = new Group();
		ArrayList<Entity> entities = new ArrayList<>();
				try {
					File inputFile = new File(fileName);
			         DocumentBuilderFactory dbFactory 
			            = DocumentBuilderFactory.newInstance();
			         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			         Document doc = dBuilder.parse(inputFile);
			         doc.getDocumentElement().normalize();
					
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
									, Double.parseDouble(
											element.getElementsByTagName("yCoord").item(0).getTextContent())
									, Double.parseDouble(
											element.getElementsByTagName("xCoord").item(0).getTextContent()));
							p.getPlatform().setLayoutY(Double.parseDouble(
									element.getElementsByTagName("yCoord").item(0).getTextContent()));
							p.setActualX(p.getPlatform().getLayoutX());
							Editor.platforms.add(p);
							all.getChildren().add(p.getPlatform());
							Editor.entities.getChildren().add(p.getPlatform());
							allEntities.add(p);
						}
					}
					
					NodeList nlist2 = root.getElementsByTagName("Coin");
					
					for (int i = 0; i < nlist2.getLength(); i++) {
						Node node = nlist2.item(i);
						if (node.getNodeType() == Node.ELEMENT_NODE) {
							Element element = (Element) node;
							//System.out.println(element.getElementsByTagName("xCoord").item(0).getTextContent());
							Coin c = new Coin(Double.parseDouble(
									element.getElementsByTagName("xCoord").item(0).getTextContent()), 
									Double.parseDouble(
											element.getElementsByTagName("yCoord").item(0).getTextContent()));
							c.setActualX(c.getImageView().getLayoutX());
							Editor.coins.add(c);
							entities.add(c);
							all.getChildren().add(c.getImageView());
							Editor.entities.getChildren().add(c.getImageView());
							allEntities.add(c);
						}
					}
					
					NodeList nlist3 = root.getElementsByTagName("Enemy");
					
					for (int i = 0; i < nlist3.getLength(); i++) {
						Node node = nlist3.item(i);
						if (node.getNodeType() == Node.ELEMENT_NODE) {
							Element element = (Element) node;
							//System.out.println(element.getElementsByTagName("xCoord").item(0).getTextContent());
							String type = element.getElementsByTagName("Type").item(0).getTextContent();
							Enemy e = null;
							if (type.equalsIgnoreCase("melee")) {
								e = new MeleeEnemy();
							}
							else {
								e = new RangedEnemy();
							}
							e.getImageView().setLayoutX(Double.parseDouble(
									element.getElementsByTagName("xCoord").item(0).getTextContent()));
							e.getImageView().setLayoutY(Double.parseDouble(
									element.getElementsByTagName("yCoord").item(0).getTextContent()));
							e.setActualX(e.getImageView().getLayoutX());
							e.getImageView().setFitHeight(64);
							e.getImageView().setFitWidth(64);
							Editor.enemies.add(e);
							entities.add(e);
							all.getChildren().add(e.getImageView());
							Editor.entities.getChildren().add(e.getImageView());
							allEntities.add(e);
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
	}
	
	private static void SaveLevel(String fileName) {
				try {
			         DocumentBuilderFactory dbFactory 
			            = DocumentBuilderFactory.newInstance();
			         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			         Document doc = dBuilder.newDocument();
					
					Element root = doc.createElement("Entities");
					doc.appendChild(root);
					
					for (int i = 0; i < allEntities.size(); i++) {
						if (allEntities.get(i).getClass().equals(Coin.class)) {
							Element subRoot = doc.createElement("Coin");
							
							Element x = doc.createElement("xCoord");
							
							x.appendChild(
							doc.createTextNode("" + allEntities.get(i).getActualX()));
							subRoot.appendChild(x);
							Element y = doc.createElement("yCoord");
							subRoot.appendChild(y);
							y.appendChild(
									doc.createTextNode("" + allEntities.get(i).getImageView().getLayoutY()));
							root.appendChild(subRoot);
						}
						else if (allEntities.get(i).getClass().equals(Platform.class)) {
							Element subRoot = doc.createElement("Platform");
							
							Element width = doc.createElement("width");
							subRoot.appendChild(width);
							width.appendChild(
							doc.createTextNode("" + 
							(int) (((Platform) allEntities.get(i)).getPlatform().getWidth() / 20 - 2)));
							Element x = doc.createElement("xCoord");
							subRoot.appendChild(x);
							x.appendChild(
							doc.createTextNode("" + allEntities.get(i).getActualX()));
							Element y = doc.createElement("yCoord");
							subRoot.appendChild(y);
							y.appendChild(
									doc.createTextNode("" + ((Platform)allEntities.get(i)).getPlatform().getLayoutY()));
							root.appendChild(subRoot);
						}
						else if (allEntities.get(i).getClass().equals(MeleeEnemy.class)) {
							Element subRoot = doc.createElement("Enemy");
							
							Element type = doc.createElement("Type");
							subRoot.appendChild(type);
							type.appendChild(
							doc.createTextNode("Melee"));
							Element x = doc.createElement("xCoord");
							subRoot.appendChild(x);
							x.appendChild(
							doc.createTextNode("" + allEntities.get(i).getActualX()));
							Element y = doc.createElement("yCoord");
							subRoot.appendChild(y);
							y.appendChild(
									doc.createTextNode("" + allEntities.get(i).getImageView().getLayoutY()));
							root.appendChild(subRoot);
						}
						else if (allEntities.get(i).getClass().equals(RangedEnemy.class)) {
							Element subRoot = doc.createElement("Enemy");
							
							Element type = doc.createElement("Type");
							subRoot.appendChild(type);
							type.appendChild(
							doc.createTextNode("Ranged"));
							Element x = doc.createElement("xCoord");
							subRoot.appendChild(x);
							x.appendChild(
							doc.createTextNode("" + allEntities.get(i).getActualX()));
							Element y = doc.createElement("yCoord");
							subRoot.appendChild(y);
							y.appendChild(
							doc.createTextNode("" + allEntities.get(i).getImageView().getLayoutY()));
							root.appendChild(subRoot);
						}
					}
					
					// write the content into xml file
			         TransformerFactory transformerFactory =
			         TransformerFactory.newInstance();
			         Transformer transformer =
			         transformerFactory.newTransformer();
			         DOMSource source = new DOMSource(doc);
			         File f = new File(fileName);
			         f.createNewFile();
			         StreamResult result =
			         new StreamResult(f);
			         transformer.transform(source, result);
			         // Output to console for testing
//			         StreamResult consoleResult =
//			         new StreamResult(System.out);
//			         transformer.transform(source, consoleResult);
				} catch (ParserConfigurationException | IOException | TransformerException e) {
					System.out.println(e.getMessage());
				}
	}
	
	public static Scene getScene() {
		return scene;
	}
}
