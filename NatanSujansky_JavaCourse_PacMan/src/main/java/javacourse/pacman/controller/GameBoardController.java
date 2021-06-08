package javacourse.pacman.controller;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import javacourse.pacman.general.ApplicationAction;
import javacourse.pacman.model.level.LevelLayout;
import javacourse.pacman.model.model.MovementDirection;
import javacourse.pacman.model.model.PacManModel;
import javacourse.pacman.view.PacManGridView;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

/**
 * Class that handles KeyEvents and moves Pac-Man in the requested direction. It
 * also handles periodic updates of the game model and will update the view with
 * the new model state, which gives the game the appearance of fluidity.
 *
 * @author Natan
 *
 */
public class GameBoardController implements EventHandler<KeyEvent> {

    @FXML
    private Label scoreLabel, resultLabel, nameLabel;

    @FXML
    private GridPane gridPane;

    @FXML
    private PacManGridView gridView;

    private long score;
    private String gameResult;
    private KeyCode key;
    private PacManModel model;
    private LevelLayout levelLayout;
    private final ScheduledExecutorService scheduler;

    private Consumer<ApplicationAction> applicationControl;

    /**
     * Constructor method for GameBoardController class
     */
    public GameBoardController() {
        scheduler = Executors.newScheduledThreadPool(1);
    }

    @Override
    public void handle(KeyEvent event) {
        key = event.getCode();
        event.consume();
    }

    /**
     * Method that prepares model and view for the new game, and starts the periodic
     * game update
     *
     * @param pacManModel
     * @throws IOException in case there is an I/O error when loading image
     *                     resources
     */
    public void startGame(PacManModel pacManModel) throws IOException {
        model = pacManModel;
        levelLayout = model.getLevelLayout();
        gridView.initializeGrid(model);
        nameLabel.setText(levelLayout.getLevelName());
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        updateGame();
                    }
                });
            }
        }, 0, 200, TimeUnit.MILLISECONDS);
    }

    /**
     * Method that stops the game and cleans-up resources that required cleaning up
     */
    public void closeGame() {
        scheduler.shutdownNow();
        gridView.update();
    }

    /**
     * Method used for the registration of callback method for controlling the flow
     * of the Pac-Man application
     *
     * @param applicationControl enum value
     */
    public void registerApplicationControl(Consumer<ApplicationAction> applicationControl) {
        this.applicationControl = applicationControl;
    }

    /**
     * Helper method used for updating the game
     */
    private void updateGame() {
        if (key != null) {
            switch (key) {
            case UP:
                if (!model.isGameOver()) {
                    model.updateGameStatus(MovementDirection.UP);
                    gridView.update();
                }
                break;
            case DOWN:
                if (!model.isGameOver()) {
                    model.updateGameStatus(MovementDirection.DOWN);
                    gridView.update();
                }
                break;
            case LEFT:
                if (!model.isGameOver()) {
                    model.updateGameStatus(MovementDirection.LEFT);
                    gridView.update();
                }
                break;
            case RIGHT:
                if (!model.isGameOver()) {
                    model.updateGameStatus(MovementDirection.RIGHT);
                    gridView.update();
                }
                break;
            case G:
                model.restartGame();
                resultLabel.setText(null);
                gridView.initializeGrid(model);
                break;
            case P:
                // Pause - do nothing and don't update model and view
                break;
            case ESCAPE:
                applicationControl.accept(ApplicationAction.EXIT_GAME);
                break;
            case M:
                applicationControl.accept(ApplicationAction.OPEN_MAIN_MENU);
                break;
            default:
                model.updateGameStatus(model.getPacMan().getMovementDirection());
                gridView.update();
            }
            score = model.getGameScore();
            scoreLabel.setText("Score: " + score);
            if (model.isGameOver()) {
                gameResult = model.isPlayerVictorious() ? "YOU WIN!" : "YOU LOSE!";
                resultLabel.setText(gameResult);
            }
        }
    }
}
