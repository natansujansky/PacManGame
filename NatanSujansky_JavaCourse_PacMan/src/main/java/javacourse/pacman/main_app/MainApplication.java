package javacourse.pacman.main_app;

import java.io.IOException;

import javacourse.pacman.controller.GameBoardController;
import javacourse.pacman.controller.MainMenuController;
import javacourse.pacman.general.ApplicationAction;
import javacourse.pacman.model.level.LevelLayout;
import javacourse.pacman.model.model.PacManModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Class used for starting the game of Pac-Man
 *
 * @author Natan
 *
 */
public class MainApplication extends Application {

    private PacManModel model;
    private LevelLayout levelLayout;
    private Stage stage;
    private Scene menuScene;
    private Scene gameScene;
    private MainMenuController menuController;
    private GameBoardController gameController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        initMainMenu();
        stage.setScene(menuScene);
        menuScene.getWindow().setX(0);
        menuScene.getWindow().setY(0);
        stage.setTitle("PacMan");
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Helper method that is used for initialization of the Main Menu
     *
     * @throws IOException in case there is an I/O error during the resource file
     *                     loading
     */
    private void initMainMenu() throws IOException {
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/fxml/pacman_menu.fxml"));
        menuController = new MainMenuController();
        menuLoader.setController(menuController);
        Parent root = menuLoader.load();
        menuScene = new Scene(root);
        menuController.registerApplicationControl(action -> performAction(action));
    }

    /**
     * Helper method that is used for initialization of the Pac-Man GameBoard
     *
     * @throws IOException in case there is an I/O error during the resource file
     *                     loading
     */
    private void initGameBoard() throws IOException {
        FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("/fxml/pacman_gameboard.fxml"));
        gameController = new GameBoardController();
        gameLoader.setController(gameController);
        Parent root = gameLoader.load();
        gameScene = new Scene(root);
        gameController.registerApplicationControl(action -> performAction(action));
        gameScene.setOnKeyPressed(event -> gameController.handle(event));
        stage.setOnCloseRequest(event -> performAction(ApplicationAction.EXIT_GAME));

        levelLayout = new LevelLayout(menuController.getSelectedLevel().getKey(),
                menuController.getSelectedLevel().getValue());
        model = new PacManModel();
        model.initializeNewGame(levelLayout);
        gameController.startGame(model);
    }

    /**
     * Helper method that is used for handling the callback when switching between
     * 'screens'
     *
     * @param action ApplicationAction enum value
     */
    private void performAction(ApplicationAction action) {
        switch (action) {
        case START_NEW_GAME:
            try {
                initGameBoard();
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.setScene(gameScene);
            break;
        case EXIT_GAME:
            stage.close();
            if (gameController != null) {
                gameController.closeGame();
            }
            break;
        case OPEN_MAIN_MENU:
            stage.setScene(menuScene);
            if (gameController != null) {
                gameController.closeGame();
            }
            break;
        default:
            break;
        }
    }
}
