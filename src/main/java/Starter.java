import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Starter extends Application {

    public static void main(String[] args){
        Application.launch(Starter.class, (java.lang.String[])null);
    }

    public void start (Stage stage){
        try {
            AnchorPane node = (AnchorPane)FXMLLoader.load(Starter.class.getResource("fxml/file.fxml"));
            Scene scene = new Scene(node);
            stage.setTitle("Appplication basket products.");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (Exception ex){
            System.out.println("Trouble viewing fxml file:"+ex);
            System.exit(-1);
        }
    }
}