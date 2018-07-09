package controller.window;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class NewWindow implements Window {

    private Stage stage;
    private Scene scene;
    private AnchorPane anchorPane;

    public NewWindow(int width, int height) {
        this.stage = new Stage();
        this.anchorPane = new AnchorPane();
        this.scene = new Scene(anchorPane, width, height);
        this.stage.setScene(scene);
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public Scene getScene(){
        return scene;
    }

    @Override
    public AnchorPane getAnchorPane() {
        return anchorPane;
    }

}
