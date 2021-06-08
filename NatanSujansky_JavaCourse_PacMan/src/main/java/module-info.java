module NatanSujansky_HW6 {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;

    opens javacourse.tictactoe to javafx.graphics, javafx.controls;
    opens javacourse.pacman.main_app to javafx.graphics, javafx.fxml, javafx.controls;
    opens javacourse.pacman.view to javafx.graphics, javafx.fxml, javafx.controls;
    opens javacourse.pacman.controller to javafx.graphics, javafx.fxml, javafx.controls;
}