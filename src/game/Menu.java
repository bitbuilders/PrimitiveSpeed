package game;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Menu extends Application {

	private Stage primaryStage;
	private Scene mainMenu;
	static Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
	public final double defaultWidth = primaryScreenBounds.getWidth() - 350;
	public final double defaultHeight = primaryScreenBounds.getHeight() - 40;
	private ModeSelection ms;
	private Image image = new Image("file:pictures/Cursor.png");
	private ImageCursor cursor = new ImageCursor(image);
	private Image image2 = new Image("file:pictures/CursorEaten.png");
	private ImageCursor cursorEaten = new ImageCursor(image2);
	private Image play = new Image("file:pictures/PlayButton.png");
	private Image playHover = new Image("file:pictures/PlayButtonHover.png");
	private Image edit = new Image("file:pictures/LevelEditorButton.png");
	private Image editHover = new Image("file:pictures/LevelEditorButtonHover.png");
	private Image quit = new Image("file:pictures/QuitButton.png");
	private Image quitHover = new Image("file:pictures/QuitButtonHover.png");
	
	@Override
	public void start(Stage arg0) throws Exception {
		primaryStage = new Stage();
		primaryStage.setTitle("Primative Speed");
		mainMenu = createScene();
		primaryStage.setMinWidth(750);
		primaryStage.setMinHeight(650);
		setScene(mainMenu);
		primaryStage.show();
		ms = new ModeSelection(this, getDefaultWidth(), getDefaultHeight());
		
		mainMenu.setCursor(cursor);
	}
	
	private Scene createScene() {
		BorderPane bp = new BorderPane();
		VBox vbox = new VBox();
		
		Image i = new Image("file:pictures/Title.png");
		ImageView iv = new ImageView(i);
		iv.setFitHeight(250);
		iv.setFitWidth(300);
		Image i2 = new Image("file:pictures/PlayButton.png");
		ImageView iv2 = new ImageView(i2);
		iv2.setFitHeight(100);
		iv2.setFitWidth(200);
		Image i3 = new Image("file:pictures/LevelEditorButton.png");
		ImageView iv3 = new ImageView(i3);
		iv3.setFitHeight(100);
		iv3.setFitWidth(200);
		Image i4 = new Image("file:pictures/QuitButton.png");
		ImageView iv4 = new ImageView(i4);
		iv4.setFitHeight(100);
		iv4.setFitWidth(200);
		
		vbox.setSpacing(20);
		vbox.getChildren().addAll(iv2, iv3, iv4);
		vbox.setAlignment(Pos.CENTER);
		addListener(vbox, iv2, iv3, iv4);
		
		bp.setCenter(vbox);
		bp.setTop(iv);
		BorderPane.setAlignment(iv, Pos.TOP_CENTER);
		BorderPane.setMargin(vbox, new Insets(-20,0,0,0));
		
		Scene s = new Scene(bp, defaultWidth, defaultHeight);
		
		mainMenu = s;
		return s;
	}
	
	private void addListener(VBox vbox, ImageView iv2, ImageView iv3, ImageView iv4) {
		vbox.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (intersects(event, iv2)) {
					iv2.setImage(playHover);
					vbox.setCursor(cursorEaten);
				}
				else if (intersects(event, iv3)) {
					iv3.setImage(editHover);
					vbox.setCursor(cursorEaten);
				}
				else if (intersects(event, iv4)) {
					iv4.setImage(quitHover);
					vbox.setCursor(cursorEaten);
				}
				else {
					vbox.setCursor(cursor);
					iv2.setImage(play);
					iv3.setImage(edit);
					iv4.setImage(quit);
				}
			}
		});
		iv2.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				setScene(ms.getScene());
			}
		});
		iv3.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				System.out.println("Level Editor");
			}
		});
		iv4.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				System.exit(0);
			}
		});
	}
	
	public boolean intersects(MouseEvent e, ImageView iv) {
		if (e.getX() >= iv.getBoundsInParent().getMinX() &&
				e.getX() <= iv.getBoundsInParent().getMaxX() &&
				e.getY() >= iv.getBoundsInParent().getMinY() &&
				e.getY() <= iv.getBoundsInParent().getMaxY()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void setScene(Scene s) {
		primaryStage.setScene(s);
	}
	
	public Scene getScene() {
		return mainMenu;
	}

	public double getDefaultWidth() {
		return defaultWidth;
	}

	public double getDefaultHeight() {
		return defaultHeight;
	}
}
