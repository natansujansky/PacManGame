package javacourse.pacman.view;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javacourse.pacman.model.model.GhostSprite;
import javacourse.pacman.model.model.PacManModel;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class PacManGridView extends GridPane {

    private static final double CELL_DIMENSION = 30.0;

    private static final int GHOST_BLINKING_TIME_MS = 3_000;

    private static final String[] IMAGE_FILE_NAMES = { "big_dot.png", "blue_ghost.gif", "ghost_1.gif", "ghost_2.gif",
            "pacman_down.gif", "pacman_left.gif", "pacman_right.gif", "pacman_up.gif", "small_dot.png", "wall.png" };

    private PacManModel model;

    private Map<String, Image> imageMap;

    private ImageView pacManImage;

    private Map<GhostSprite, ImageView> ghostImageMap;

    private boolean isRedGhostImageLastUsed;

    /**
     * Constructor for PacManGridView class
     *
     * @throws IOException in case there is and I/O error during reading of image
     *                     resource file
     */
    public PacManGridView() throws IOException {
        imageMap = new HashMap<>();
        Image image;
        for (String imageName : IMAGE_FILE_NAMES) {
            try (InputStream inputStream = getClass().getResourceAsStream("/graphics/" + imageName)) {
                if (inputStream == null) {
                    throw new IllegalArgumentException(
                            "No resource found on the provided path: /graphics/" + imageName);
                }
                image = new Image(inputStream, CELL_DIMENSION, CELL_DIMENSION, false, false);
                imageMap.put(imageName, image);
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
        }
    }

    /**
     * Method that is used for reinitializing the view with the new model that has
     * been provided
     *
     * @param pacManModel object defining the model for PacMan game
     */
    public void initializeGrid(PacManModel pacManModel) {
        this.model = pacManModel;
        ghostImageMap = new HashMap<>();
        pacManImage = new ImageView();
        for (GhostSprite ghost : model.getGhosts()) {
            ImageView iv = new ImageView();
            if (!isRedGhostImageLastUsed) {
                iv.setImage(imageMap.get("ghost_1.gif"));
                isRedGhostImageLastUsed = true;
            } else {
                iv.setImage(imageMap.get("ghost_2.gif"));
                isRedGhostImageLastUsed = false;
            }
            ghostImageMap.put(ghost, iv);
        }
        update();
    }

    /**
     * Method used for updating the view to reflect the updated Pac-Man game model
     */
    public void update() {
        if (this.model == null) {
            throw new IllegalStateException("GridView hasn't been initialized yet!");
        }
        this.getChildren().clear();
        for (int i = 0; i < model.getLevelLayout().getLevelHeight(); i++) {
            for (int j = 0; j < model.getLevelLayout().getLevelWidth(); j++) {
                ImageView iv = new ImageView();
                switch (model.componentAt(i, j)) {
                case WALL:
                    iv.setImage(imageMap.get("wall.png"));
                    break;
                case SMALL_DOT:
                    iv.setImage(imageMap.get("small_dot.png"));
                    break;
                case BIG_DOT:
                    iv.setImage(imageMap.get("big_dot.png"));
                    break;
                default:
                    break;
                }
                this.getChildren().add(iv);
                GridPane.setConstraints(iv, j, i);
            }
        }
        if (model.getPacMan().getMovementDirection() == null) {
            pacManImage.setImage(imageMap.get("pacman_left.gif"));
        } else {
            switch (model.getPacMan().getMovementDirection()) {
            case UP:
                pacManImage.setImage(imageMap.get("pacman_up.gif"));
                break;
            case DOWN:
                pacManImage.setImage(imageMap.get("pacman_down.gif"));
                break;
            case RIGHT:
                pacManImage.setImage(imageMap.get("pacman_right.gif"));
                break;
            case LEFT:
                pacManImage.setImage(imageMap.get("pacman_left.gif"));
                break;
            default:
                break;
            }
        }
        this.getChildren().add(pacManImage);
        GridPane.setConstraints(pacManImage, model.getPacMan().getColumnIndex(), model.getPacMan().getRowIndex());

        for (int i = 0; i < ghostImageMap.size(); i++) {
            ImageView iv = new ImageView();
            if (model.getGhosts().get(i).isGhostDead()) {
                if (model.getGhosts().get(i).getRemainingDeathTime() <= GHOST_BLINKING_TIME_MS) {
                    if (model.getGhosts().get(i).getGhostDeathUpdateCounter() % 2 == 0) {
                        iv = ghostImageMap.get(model.getGhosts().get(i));
                    } else {
                        continue;
                    }
                }
            } else if (model.getPacMan().isGhostEatingActive()) {
                iv.setImage(imageMap.get("blue_ghost.gif"));
                if (model.getPacMan().getRemainingGhostEatingTime() <= GHOST_BLINKING_TIME_MS) {
                    if (model.getPacMan().getGhostEatingUpdatesCounter() % 2 == 0) {
                        iv.setVisible(true);
                    } else {
                        iv.setVisible(false);
                    }
                }
            } else {
                iv = ghostImageMap.get(model.getGhosts().get(i));
            }
            this.getChildren().add(iv);
            GridPane.setConstraints(iv, model.getGhosts().get(i).getColumnIndex(),
                    model.getGhosts().get(i).getRowIndex());
        }
    }
}
