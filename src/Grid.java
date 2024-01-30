import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public final class Grid {
    private final GridPane gridPane = new GridPane();
    private final int gridWidth;
    private final int gridHeight;
    private static final Paint ALIVE_COLOR = /*Color.BLACK*/Color.ANTIQUEWHITE;
    private static final Paint DEAD_COLOR = /*Color.valueOf("f0f0f0")*/Color.valueOf("1f262a");
    private boolean isStable = true;
    private boolean gameStarted = false;

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

    public GridPane getPane() {
        return gridPane;
    }

    private Rectangle getCellAt(int row, int col) {
        return (Rectangle) gridPane.getChildren().get(getCellIndex(row, col));
    }

    private int getCellIndex(int row, int col) {
        return row * gridWidth + col;
    }

    private int aliveNeighbours(int row, int col) {
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

    public boolean isStable() {
        return isStable;
    }
    public void update() {
        GridPane temp = new GridPane();
        boolean hasChanged = false;

        for (int row = 0; row < gridHeight; ++row) {
            for (int col = 0; col < gridWidth; ++col) {
                int aliveNeighboursCount = aliveNeighbours(row, col);
                Rectangle currentCell = getCellAt(row, col);
                Rectangle newCell = new Rectangle(currentCell.getWidth(), currentCell.getHeight());
                newCell.setFill(currentCell.getFill());
                //newCell.setStroke(Color.BLACK);
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

    public void reset() {
        for (Node node : gridPane.getChildren()) {
            Rectangle rectangle = (Rectangle) node;
            rectangle.setFill(DEAD_COLOR);
        }
    }

    public void randomizeGrid() {
        Random random = new Random();
        for (Node node : gridPane.getChildren()) {
            Rectangle rectangle = (Rectangle) node;
            rectangle.setFill((random.nextBoolean()) ? DEAD_COLOR : ALIVE_COLOR);
        }
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    public boolean gameStarted() {
        return gameStarted;
    }

    public enum PredefinedStartingPosition {
        GLIDER,
        GOSPER_GLIDER_GUN,
        SIMKIN_GLIDER_GUN,
        COOL_GLIDER;
    }

    public void setPredefinedStartingPosition(PredefinedStartingPosition position) {
        reset();
        switch (position) {
            case GLIDER -> addGliderPattern();
            case GOSPER_GLIDER_GUN -> addGosperGliderGunPattern();
            case SIMKIN_GLIDER_GUN -> addSimkinGlierGunPattern();
            case COOL_GLIDER -> addCoolGliderPattern();
        }
    }
    private void addGliderPattern() {
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

    private void addGosperGliderGunPattern() {
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

    private void addSimkinGlierGunPattern() {
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

    private void addCoolGliderPattern() {
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
