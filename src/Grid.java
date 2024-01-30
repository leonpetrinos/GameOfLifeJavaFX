import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.util.Random;

/**
 * The Grid class represents a grid of cells for a Game of Life simulation using JavaFX.
 */
public final class Grid {
    private final GridPane gridPane = new GridPane();
    private final int gridWidth;
    private final int gridHeight;
    private static final Paint ALIVE_COLOR = Color.ANTIQUEWHITE;
    private static final Paint DEAD_COLOR = Color.valueOf("1f262a");
    private boolean isStable = true;
    private boolean gameStarted = false;

    /**
     * Constructs a grid with the specified dimensions and cell size.
     *
     * @param gridWidth      The width of the grid.
     * @param gridHeight     The height of the grid.
     * @param cellDimension  The size of each cell.
     */
    public Grid(int gridWidth, int gridHeight, int cellDimension) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        for (int row = 0 ; row < gridHeight ; ++row) {
            for (int col = 0 ; col < gridWidth ; ++col) {
                Rectangle rectangle = new Rectangle(cellDimension, cellDimension);
                rectangle.setFill(DEAD_COLOR);
                rectangle.setStroke(DEAD_COLOR);
                rectangle.setStrokeWidth(0.5);
                gridPane.add(rectangle, col, row);
            }
        }

        installEvents();
    }

    /**
     * Installs mouse events for grid interaction (clicking or dragging the mouse).
     */
    public void installEvents() {
        gridPane.setOnMouseDragged(e -> {
            if (!gameStarted) {
                Node intersectedNode = e.getPickResult().getIntersectedNode();
                if (intersectedNode instanceof Rectangle r) {
                    int idx = getCellIndex(GridPane.getRowIndex(r), GridPane.getColumnIndex(r));
                    r.setFill(ALIVE_COLOR);
                    gridPane.getChildren().set(idx, r);
                }
            }
        });

        gridPane.setOnMousePressed(e -> {
            if (!gameStarted) {
                Node intersectedNode = e.getPickResult().getIntersectedNode();
                if (intersectedNode instanceof Rectangle r) {
                    int idx = getCellIndex(GridPane.getRowIndex(r), GridPane.getColumnIndex(r));
                    if (r.getFill() == DEAD_COLOR) {
                        r.setFill(ALIVE_COLOR);
                    } else {
                        r.setFill(DEAD_COLOR);
                    }
                    gridPane.getChildren().set(idx, r);
                }
            }
        });
    }

    /**
     * Gets the JavaFX GridPane representing the grid.
     *
     * @return The JavaFX GridPane.
     */
    public GridPane getPane() {
        return gridPane;
    }

    /**
     * Calculates the number of alive neighbors of the cell at position (row, col)
     *
     * @param row  The row of the cell in the grid
     * @param col  The column of the cell in the grid
     * @return The number of alive neighbours of the cell at position (row, col)
     */
    private int neighbours(int row, int col) {
        int nbOfNeighbours = 0;

        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = col - 1; c <= col + 1; c++) {
                if (r == row && c == col) {
                    continue;  // Skip the current cell
                }

                int wrappedRow = (r + gridHeight) % gridHeight;
                int wrappedCol = (c + gridWidth) % gridWidth;

                Rectangle cell = getCellAt(wrappedRow, wrappedCol);
                if (cell != null && cell.getFill() == ALIVE_COLOR) {
                    nbOfNeighbours++;
                }
            }
        }

        return nbOfNeighbours;
    }

    /**
     * Updates the JavaFX GridPane to the next generation according to the Game of Life rules
     */
    public void update() {
        GridPane temp = new GridPane();
        boolean hasChanged = false;

        for (int row = 0; row < gridHeight; ++row) {
            for (int col = 0; col < gridWidth; ++col) {
                int aliveNeighboursCount = neighbours(row, col);
                Rectangle currentCell = getCellAt(row, col);
                Rectangle newCell = new Rectangle(currentCell.getWidth(), currentCell.getHeight());
                newCell.setFill(currentCell.getFill());
                newCell.setStroke(DEAD_COLOR);
                newCell.setStrokeWidth(0.5);

                if (currentCell.getFill() == ALIVE_COLOR) {
                    if (aliveNeighboursCount < 2 || aliveNeighboursCount > 3) {
                        hasChanged = true;
                        newCell.setFill(DEAD_COLOR);
                    }
                } else {
                    if (aliveNeighboursCount == 3) {
                        hasChanged = true;
                        newCell.setFill(ALIVE_COLOR);
                    }
                }

                isStable = !hasChanged;
                temp.add(newCell, col, row);
            }
        }

        gridPane.getChildren().clear();
        gridPane.getChildren().addAll(temp.getChildren());
    }


    /**
     * Gets the JavaFX Rectangle at (row, col), representing a cell
     *
     * @param row  The row of the cell in the grid
     * @param col  The column of the cell in the grid
     * @return The cell at position (row, col) in the grid
     */
    private Rectangle getCellAt(int row, int col) {
        return (Rectangle) gridPane.getChildren().get(getCellIndex(row, col));
    }

    /**
     * Gets the index of a cell in the grid based on its row and column
     *
     * @param row  The row of the cell in the grid
     * @param col  The column of the cell in the grid
     * @return The index of the cell at position (row, col) in the grid
     */
    private int getCellIndex(int row, int col) {
        return row * gridWidth + col;
    }


    /**
     * Indicates if the Game of Life has reached a stable state
     * @return True if the Game of Life is in a stable state
     */
    public boolean isStable() {
        return isStable;
    }


    /**
     * Resets the JavaFX GridPane to a grid where every cell is dead
     */
    public void reset() {
        for (Node node : gridPane.getChildren()) {
            Rectangle rectangle = (Rectangle) node;
            rectangle.setFill(DEAD_COLOR);
        }
    }

    /**
     * Creates a random configuration for the JavaFX GridPane of cells
     */
    public void randomizeGrid() {
        Random random = new Random();
        for (Node node : gridPane.getChildren()) {
            Rectangle rectangle = (Rectangle) node;
            rectangle.setFill((random.nextBoolean()) ? DEAD_COLOR : ALIVE_COLOR);
        }
    }

    /**
     * Used to indicate that the Game of Life is running or not
     * @param gameStarted  Indicates if the game has started
     */
    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    /**
     * Gets the information whether the game is running or not
     * @return The variable gameStarted
     */
    public boolean gameStarted() {
        return gameStarted;
    }

    /**
     * Defines a few known patterns for the grid
     */
    public enum Pattern {
        GLIDER,
        GOSPER_GLIDER_GUN,
        SIMKIN_GLIDER_GUN,
        COOL_GLIDER;
    }

    /**
     * Sets the given pattern to the grid
     * @param pattern  Starting pattern for the grid
     */
    public void applyPattern(Pattern pattern) {
        reset();
        switch (pattern) {
            case GLIDER -> applyGliderPattern();
            case GOSPER_GLIDER_GUN -> applyGosperGliderGunPattern();
            case SIMKIN_GLIDER_GUN -> applySimkinGliderGunPattern();
            case COOL_GLIDER -> applyCoolGliderPattern();
        }
    }

    /**
     * Applies the glider pattern to the grid
     */
    private void applyGliderPattern() {
        int startX = 5;  // Starting X coordinate of the glider pattern
        int startY = 5;  // Starting Y coordinate of the glider pattern

        int[][] pattern = {
                {0, 1}, {1, 2}, {2, 0}, {2, 1}, {2, 2}
        };

        for (int[] coordinates : pattern) {
            int row = startY + coordinates[0];
            int col = startX + coordinates[1];
            Rectangle cell = getCellAt(row, col);
            if (cell != null) {
                cell.setFill(ALIVE_COLOR);
            }
        }
    }

    /**
     * Applies the Gosper Glider Gun pattern to the grid
     */
    private void applyGosperGliderGunPattern() {
        int startX = 1;  // Starting X coordinate of the glider gun pattern
        int startY = 1;  // Starting Y coordinate of the glider gun pattern

        int[][] pattern = {
                {5, 1}, {5, 2}, {6, 1}, {6, 2}, {3, 13}, {3, 14}, {4, 12}, {4, 16}, {5, 11}, {5, 17}, {6, 11}, {6, 15},
                {6, 17}, {6, 18}, {7, 11}, {7, 17}, {8, 12}, {8, 16}, {9, 13}, {9, 14}, {1, 25}, {2, 23}, {2, 25},
                {3, 21}, {3, 22}, {4, 21}, {4, 22}, {5, 21}, {5, 22}, {6, 23}, {6, 25}, {7, 25}, {3, 35}, {3, 36},
                {4, 35}, {4, 36}
        };

        for (int[] coordinates : pattern) {
            int row = startY + coordinates[0];
            int col = startX + coordinates[1];
            Rectangle cell = getCellAt(row, col);
            if (cell != null) {
                cell.setFill(ALIVE_COLOR);
            }
        }
    }

    /**
     * Applies the Simkin Glider Gun pattern to the grid
     */
    private void applySimkinGliderGunPattern() {
        int startX = 40;  // Starting X coordinate of the glider pattern
        int startY = 30;  // Starting Y coordinate of the glider pattern

        int[][] pattern = {
                {0,0}, {0, 1}, {1, 0}, {1,1},
                {4, 3}, {5, 3}, {4, 4}, {5, 4},
                {7, 0}, {8, 0}, {7, 1}, {8, 1},
                {22, 9}, {23, 9}, {25, 9}, {26,9},
                {21, 10}, {27, 10},
                {21, 11}, {28, 11}, {31, 11}, {32, 11},
                {21, 12}, {22, 12}, {23, 12}, {27,12}, {31, 12}, {32, 12},
                {26, 13},
                {20, 17}, {21, 17},
                {20, 18},
                {21, 19}, {22, 19}, {23, 19},
                {23, 20}
        };

        for (int[] coordinates : pattern) {
            int row = startY + coordinates[1];
            int col = startX + coordinates[0];
            Rectangle cell = getCellAt(row, col);
            if (cell != null) {
                cell.setFill(ALIVE_COLOR);
            }
        }
    }

    /**
     * Applies a cool glider pattern to the grid
     */
    private void applyCoolGliderPattern() {
        int startX = 50;  // Starting X coordinate of the glider pattern
        int startY = 20;  // Starting Y coordinate of the glider pattern

        int[][] pattern = {
                {0, 0}, {1, 0}, {2, 0}, {3, 0}, {4, 0},
                {0, 1}, {5, 1}, {13, 1}, {14, 1},
                {0, 2}, {12, 2}, {13, 2}, {15, 2}, {16, 2}, {17, 2},
                {1, 3}, {11, 3}, {12, 3}, {14, 3}, {15, 3}, {16, 3}, {17, 3},
                {3, 4}, {4, 4}, {8, 4}, {9, 4}, {11, 4}, {12, 4}, {15, 4}, {16, 4},
                {5, 5}, {10, 5}, {13, 5},
                {6, 6}, {8, 6}, {10, 6}, {12, 6},
                {7, 7},
                {7, 8},
                {6, 9}, {8, 9}, {10, 9}, {12, 9},
                {5, 10}, {10, 10}, {13, 10},
                {3, 11}, {4, 11}, {8, 11}, {9, 11}, {11, 11}, {12, 11}, {15, 11}, {16, 11},
                {1, 12}, {11, 12}, {12, 12}, {14, 12}, {15, 12}, {16, 12}, {17, 12},
                {0, 13}, {12, 13}, {13, 13}, {15, 13}, {16, 13}, {17, 13},
                {0, 14}, {5, 14}, {13, 14}, {14, 14},
                {0, 15}, {1, 15}, {2, 15}, {3, 15}, {4, 15}
        };

        for (int[] coordinates : pattern) {
            int row = startY + coordinates[1];
            int col = startX + coordinates[0];
            Rectangle cell = getCellAt(row, col);
            if (cell != null) {
                cell.setFill(ALIVE_COLOR);
            }
        }

    }


}
