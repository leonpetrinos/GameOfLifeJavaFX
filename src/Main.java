import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.text.Font;

/**
 * The Main class represents the entry point of the JavaFX application for Conway's Game of Life.
 */
public final class Main extends Application {
    /**
     * Width of the grid of cells
     */
    public static final int GRID_WIDTH = 80;
    /**
     * Height of the grid of cells
     */
    public static final int GRID_HEIGHT = 60;
    /**
     * Dimensions of each cell
     */
    public static final int CELL_DIMENSION = 10;

    private Grid grid;
    private AnimationTimer animationTimer;
    private final IntegerProperty generationCount = new SimpleIntegerProperty(0);

    /**
     * The entry point for the JavaFX application.
     *
     * @param args  Command-line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initializes the JavaFX application.
     *
     * @param primaryStage  The primary stage for the application.
     */
    @Override
    public void start(Stage primaryStage) {
        grid = new Grid(GRID_WIDTH, GRID_HEIGHT, CELL_DIMENSION);
        GridPane gridPane = grid.getPane();

        Button startButton = new Button("Simulate");
        startButton.setStyle
                ("-fx-font-size: 12px; -fx-padding: 10px 20px; -fx-background-color: #68AA55; -fx-text-fill: white;");
        startButton.setOnAction(e -> {
            startAnimation();
            grid.setGameStarted(true);
        });

        Button restartButton = new Button("Restart");
        restartButton.setStyle
                ("-fx-font-size: 12px; -fx-padding: 10px 20px; -fx-background-color: #3C99DC; -fx-text-fill: white;");
        restartButton.setOnAction(e -> {
            resetGrid();
            grid.setGameStarted(false);
        });

        Button randomizeButton = new Button("Randomize");
        randomizeButton.setStyle
                ("-fx-font-size: 12px; -fx-padding: 10px 20px; -fx-background-color: #717D8C; -fx-text-fill: white;");
        randomizeButton.setOnAction(e -> {
            if (!grid.gameStarted()) {
                grid.randomizeGrid();
            }
        });

        ContextMenu contextMenu = createContextMenu();
        Button showMenuButton = new Button("Basic Configurations");
        showMenuButton.setOnAction(e -> {
            contextMenu.show(showMenuButton, Side.BOTTOM, 0, 0);
        });

        HBox buttonBox = new HBox(5);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(startButton, restartButton, randomizeButton);

        Text text = new Text();
        text.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 18));
        text.textProperty().bind(Bindings.createStringBinding(() ->
                        String.format("Number of Generations : %s", generationCount.get()), generationCount));

        VBox textBox = new VBox(text);
        textBox.setStyle("-fx-background-color: #F0F0F0; -fx-padding: 5px;");

        HBox buttonContainer = new HBox(showMenuButton);
        buttonContainer.setAlignment(Pos.TOP_RIGHT);
        textBox.getChildren().add(buttonContainer);

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #F0F0F0; -fx-padding: 5px;");
        root.setTop(textBox);
        root.setCenter(gridPane);
        root.setBottom(buttonBox);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Conway's Game Of Life");
        primaryStage.show();
    }

    /**
     * Starts the animation timer to simulate the Game of Life.
     */
    private void startAnimation() {
        if (animationTimer != null) {
            animationTimer.stop();
        }

        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                grid.update();
                if (!grid.isStable()) {
                    generationCount.set(generationCount.getValue() + 1);
                }
            }
        };

        animationTimer.start();
    }

    /**
     * Resets the grid and stops the animation timer.
     */
    private void resetGrid() {
        if (animationTimer != null) {
            animationTimer.stop();
        }

        grid.reset();
        generationCount.set(0);
    }

    /**
     * Creates a context menu with options for predefined starting patterns.
     *
     * @return The context menu.
     */
    private ContextMenu createContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem glider = new MenuItem("Glider");
        MenuItem gosperGliderGun = new MenuItem("Gosper Glider Gun");
        MenuItem simkinGliderGun = new MenuItem("Simkin Glider Gun");
        MenuItem coolGlider = new MenuItem("Cool Glider");

        glider.setOnAction(e -> {
            if (!grid.gameStarted()) {
                grid.applyPattern(Grid.Pattern.GLIDER);
            }
        });

        gosperGliderGun.setOnAction(e -> {
            if (!grid.gameStarted()) {
                grid.applyPattern(Grid.Pattern.GOSPER_GLIDER_GUN);
            }
        });

        simkinGliderGun.setOnAction(e -> {
            if (!grid.gameStarted()) {
                grid.applyPattern(Grid.Pattern.SIMKIN_GLIDER_GUN);
            }
        });

        coolGlider.setOnAction(e -> {
            if (!grid.gameStarted()) {
                grid.applyPattern(Grid.Pattern.COOL_GLIDER);
            }
        });

        contextMenu.getItems().addAll(glider, gosperGliderGun, simkinGliderGun, coolGlider);

        return contextMenu;
    }

}
