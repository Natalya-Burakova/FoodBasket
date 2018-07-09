package controller.window;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public interface Window {
    public Stage getStage();
    public Scene getScene();
    public AnchorPane getAnchorPane();
}
