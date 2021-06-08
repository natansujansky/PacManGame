package javacourse.pacman.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javacourse.pacman.general.ApplicationAction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Region;
import javafx.util.Pair;

/**
 * Class that handles the Pac-Man game main menu logic
 *
 * @author Natan
 *
 */
public class MainMenuController implements Initializable {

    @FXML
    private Button newGameButton, instructionsButton, exitButton;

    @FXML
    private ComboBox<String> levelSelectBox;

    private Map<String, String> levelPathMap;

    private static final String LEVEL_PATH_REGEX = "(.*) ### (.*)";

    private String selectedLevel;

    private Consumer<ApplicationAction> applicationControl;

    /**
     * Constructor method for MainMenuController class
     *
     * @throws IOException in case there is an error during reading of the
     *                     level_list.txt resource file
     */
    public MainMenuController() throws IOException {
        levelPathMap = new LinkedHashMap<>();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            getLevelPaths();
        } catch (IOException e) {
            e.printStackTrace();
        }
        levelSelectBox.setButtonCell(new ListCell<String>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item);
                    setAlignment(Pos.CENTER);
                    Insets old = getPadding();
                    setPadding(new Insets(old.getTop(), 0, old.getBottom(), 0));
                }
            }
        });
        levelSelectBox.getStyleClass().add("center-aligned");
        levelSelectBox.getItems().clear();
        levelSelectBox.getItems().addAll(levelPathMap.keySet());
        levelSelectBox.getSelectionModel().selectFirst();
        selectedLevel = levelSelectBox.getItems().get(0);
        levelSelectBox.setOnAction(event -> {
            selectedLevel = levelSelectBox.getSelectionModel().getSelectedItem();
        });
    }

    /**
     * Getter method for the pair of selected level name and its corresponding file
     * path
     *
     * @return selected level name paired with its file path in String format
     */
    public Pair<String, String> getSelectedLevel() {
        Pair<String, String> pair = new Pair<>(selectedLevel, levelPathMap.get(selectedLevel));
        return pair;
    }

    @FXML
    public void handleNewGame(ActionEvent event) {
        applicationControl.accept(ApplicationAction.START_NEW_GAME);
    }

    @FXML
    public void handleInstructions(ActionEvent event) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setTitle("GAME INSTRUCTIONS");
        alert.setHeaderText(null);
        alert.setContentText(
                "Player controls the Pac-Man with the arrow keys, while ghosts are controlled by the computer."
                        + "The goal of the game is to eat all of the dots (small and big) in the maze without getting caught by the ghosts."
                        + "When a big dot is eaten, Pac-Man gets powered-up and is able to eat ghosts for a short time (during this time "
                        + "ghosts turn blue and run away from Pac-Man). If a ghost touches Pac-Man while not powered-up, the game is over.\n"
                        + "Additional controls:\n" + "G     - game restart\n" + "P     - pause game\n"
                        + "Esc  - exit game\n" + "M    - open main menu");

        alert.showAndWait();
    }

    @FXML
    public void handleExit(ActionEvent event) {
        applicationControl.accept(ApplicationAction.EXIT_GAME);
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

    private void getLevelPaths() throws IOException {
        String fileContent;
        try (InputStream inputStream = getClass().getResourceAsStream("/level_list.txt")) {
            fileContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        String[] lines = fileContent.split("\r?\n");
        for (String line : lines) {
            if (line.isEmpty()) {
                continue;
            } else {
                Matcher levelPathMatcher = Pattern.compile(LEVEL_PATH_REGEX).matcher(line);
                if (!levelPathMatcher.matches()) {
                    throw new IllegalStateException("Level list resource file not in correct format!");
                } else {
                    levelPathMap.put(levelPathMatcher.group(1), levelPathMatcher.group(2));
                }
            }
        }
    }

}
