package game;

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

public class ModeSelection {

	private Scene modeScene;
	private Menu menu;
	private Image image = new Image("file:pictures/Cursor.png");
	private ImageCursor cursor = new ImageCursor(image);
	private Image image2 = new Image("file:pictures/CursorEaten.png");
	private ImageCursor cursorEaten = new ImageCursor(image2);
	private Image play = new Image("file:pictures/PlayButton.png");
	private Image playHover = new Image("file:pictures/PlayButtonHover.png");
	private AnimationHandler ah;
	private KeypressHandler kh;
	private PlatformHandler ph;
	private EntityHandler eh;
	
	public ModeSelection(Menu menu, double width, double height) {
		modeScene = createScene(width, height);
		this.menu = menu;
		modeScene.setCursor(cursor);
	}
	
	private Scene createScene(double width, double height) {
		BorderPane bp = new BorderPane();
		VBox vbox = new VBox();
		
		Image i = new Image("file:pictures/PlayButton.png");
		ImageView iv = new ImageView(i);
		iv.setFitHeight(100);
		iv.setFitWidth(200);
		Image i2 = new Image("file:pictures/PlayButton.png");
		ImageView iv2 = new ImageView(i2);
		iv2.setFitHeight(100);
		iv2.setFitWidth(200);
		Image i3 = new Image("file:pictures/PlayButton.png");
		ImageView iv3 = new ImageView(i3);
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
					iv.setImage(playHover);
					vbox.setCursor(cursorEaten);
				}
				else if (menu.intersects(event, iv2)) {
					iv2.setImage(playHover);
					vbox.setCursor(cursorEaten);
				}
				else if (menu.intersects(event, iv3)) {
					iv3.setImage(playHover);
					vbox.setCursor(cursorEaten);
				}
				else {
					iv.setImage(play);
					iv2.setImage(play);
					iv3.setImage(play);
					vbox.setCursor(cursor);
				}
			}
		});
		iv.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				System.out.println("Level selection");
			}
		});
		iv2.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Game g = new Game(menu, true);
				ah = new AnimationHandler(g);
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
				}
				kh = new KeypressHandler(g, ah, g.getPlatforms(), menu.getDefaultHeight() - 50);
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
				}
				ph = new PlatformHandler(g.getPlatforms(), g);
				eh = new EntityHandler(g, ph);
				menu.setScene(g.getScene());
			}
		});
		iv3.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				menu.setScene(menu.getScene());
			}
		});
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
}
