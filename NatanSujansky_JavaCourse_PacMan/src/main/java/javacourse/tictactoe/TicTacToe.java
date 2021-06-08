package javacourse.tictactoe;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Class used for creation and running the tic-tac-toe game
 *
 * @author esunata
 *
 */
public class TicTacToe extends Application {

    private boolean isXTurn;

    /**
     * Boolean flag denoting if player X has been the first player to start or not
     */
    private boolean isXFirst;

    /**
     * Integer variable that counts the number of moves so far
     */
    private int moveCount;

    /**
     * Constant denoting the number of tiles on the tic-tac-toe game board
     */
    private static final int NUM_OF_TILES = 9;

    /**
     * Constant denoting the number of tiles in each board row and column
     */
    private static final int BOARD_SIDE_LENGTH = 3;

    /**
     * Label that is used for displaying whose turn it is currently
     */
    private Label label;

    /**
     * GridPane variable used for the representation of the game board
     */
    private GridPane gridPane;

    /**
     * String variable that will be used for denoting the outcome of the game in the
     * Alert pop-up window
     */
    private String outcome;

    /**
     * String variable that will be used as text content of the Alert pop-up window
     */
    private String alertMsg;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Tic-tac-toe");
        primaryStage.setScene(createScene());
        primaryStage.setResizable(false);
        isXTurn = true;
        isXFirst = isXTurn;
        moveCount = 0;
        primaryStage.show();
    }

    /**
     * Inner class that represents one tile on the board game
     *
     * @author esunata
     *
     */
    private class Tile extends StackPane {
        /**
         * Text contained in the tile
         */
        private Text symbol;

        /**
         * Constructor for the tile class
         */
        public Tile() {
            Rectangle square = new Rectangle(200, 200);
            square.setFill(Color.BEIGE);
            square.setStroke(Color.BLACK);

            symbol = new Text();
            symbol.setFont(Font.font(50));
            setAlignment(Pos.CENTER);

            getChildren().addAll(square, symbol);

            setOnMouseClicked(e -> {
                if (isGameOver()) {
                    return;
                }
                if (isXTurn) {
                    drawX();
                } else {
                    drawO();
                }
            });
        }

        /**
         * Getter method for the tile symbol
         *
         * @return Text contained in the tile
         */
        public String getTileSymbol() {
            return symbol.getText();
        }

        /**
         * Method used for drawing the 'X' symbol in the tile
         */
        private void drawX() {
            if (!symbol.getText().isEmpty()) {
                return;
            }
            symbol.setText("X");
            moveCount++;
            isXTurn = false;
            if (!isGameOver()) {
                label.setText("It is O's turn!");
            }
        }

        /**
         * Method used for drawing the 'O' symbol in the tile
         */
        private void drawO() {
            if (!symbol.getText().isEmpty()) {
                return;
            }
            symbol.setText("O");
            moveCount++;
            isXTurn = true;
            if (!isGameOver()) {
                label.setText("It is X's turn!");
            }
        }

    }

    /**
     * Method used for creating the game board
     *
     * @return Scene where game will be played
     */
    private Scene createScene() {
        gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        Tile[] tiles = new Tile[NUM_OF_TILES];

        for (int i = 0; i < NUM_OF_TILES; i++) {
            tiles[i] = new Tile();
        }
        gridPane.getChildren().addAll(tiles);
        gridPane.setAlignment(Pos.CENTER);

        for (int i = 0; i < BOARD_SIDE_LENGTH; i++) {
            for (int j = 0; j < BOARD_SIDE_LENGTH; j++) {
                GridPane.setConstraints(tiles[i * BOARD_SIDE_LENGTH + j], i, j);
            }
        }

        label = new Label("It is X's turn");
        label.setStyle("-fx-font-size: 20;");
        HBox hbox = new HBox(label);
        hbox.setAlignment(Pos.CENTER);

        VBox vBox = new VBox(gridPane, hbox);
        Scene scene = new Scene(vBox);
        return scene;
    }

    /**
     * Helper method used for getting the tile on the given position in gridPane
     *
     * @param column index of the column
     * @param row    index of the row
     * @return Tile object on the given position in the gridPane, or null if it
     *         doesn't exist
     */
    private Tile getTileFromGridPane(int column, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == column && GridPane.getRowIndex(node) == row && node instanceof Tile) {
                return (Tile) node;
            }
        }
        return null;
    }

    /**
     * Helper method that checks the conditions for the game end
     *
     * @return true if game is over, false otherwise
     */
    private boolean isGameOver() {
        for (int i = 0; i < 3; i++) {
            if (!getTileFromGridPane(0, i).getTileSymbol().isEmpty()
                    && getTileFromGridPane(0, i).getTileSymbol().equals(getTileFromGridPane(1, i).getTileSymbol())
                    && getTileFromGridPane(0, i).getTileSymbol().equals(getTileFromGridPane(2, i).getTileSymbol())) {
                outcome = "Victory!";
                alertMsg = "Player " + getTileFromGridPane(0, i).getTileSymbol() + " has won!";
                showAlert();
                return true;
            }
            if (!getTileFromGridPane(i, 0).getTileSymbol().isEmpty()
                    && getTileFromGridPane(i, 0).getTileSymbol().equals(getTileFromGridPane(i, 1).getTileSymbol())
                    && getTileFromGridPane(i, 0).getTileSymbol().equals(getTileFromGridPane(i, 2).getTileSymbol())) {
                outcome = "Victory!";
                alertMsg = "Player " + getTileFromGridPane(i, 0).getTileSymbol() + " has won!";
                showAlert();
                return true;
            }
        }
        if (!getTileFromGridPane(0, 0).getTileSymbol().isEmpty()
                && getTileFromGridPane(0, 0).getTileSymbol().equals(getTileFromGridPane(1, 1).getTileSymbol())
                && getTileFromGridPane(0, 0).getTileSymbol().equals(getTileFromGridPane(2, 2).getTileSymbol())) {
            outcome = "Victory!";
            alertMsg = "Player " + getTileFromGridPane(0, 0).getTileSymbol() + " has won!";
            showAlert();
            return true;
        }
        if (!getTileFromGridPane(0, 2).getTileSymbol().isEmpty()
                && getTileFromGridPane(0, 2).getTileSymbol().equals(getTileFromGridPane(1, 1).getTileSymbol())
                && getTileFromGridPane(0, 2).getTileSymbol().equals(getTileFromGridPane(2, 0).getTileSymbol())) {
            outcome = "Victory!";
            alertMsg = "Player " + getTileFromGridPane(0, 2).getTileSymbol() + " has won!";
            showAlert();
            return true;
        }
        if (moveCount == 9) {
            outcome = "Draw!";
            alertMsg = "It's a draw, starting over...";
            showAlert();
            return true;
        }
        return false;
    }

    /**
     * Method used for showing the alert pop-up windows when game is over
     */
    private void showAlert() {
        Alert alert = new Alert(AlertType.NONE);
        alert.setTitle(outcome);
        alert.setContentText(alertMsg);
        ButtonType closeButton = ButtonType.CLOSE;
        alert.getButtonTypes().setAll(closeButton);
        alert.showAndWait().ifPresent(response -> restartGame());
    }

    /**
     * Method that restarts the game when it is over and switches the player to
     * start first
     */
    private void restartGame() {
        moveCount = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                getTileFromGridPane(i, j).symbol.setText("");
            }
        }
        isXFirst = !isXFirst;
        isXTurn = isXFirst;
        if (isXFirst) {
            label.setText("It is X's turn!");
        } else {
            label.setText("It is O's turn!");
        }
    }
}
