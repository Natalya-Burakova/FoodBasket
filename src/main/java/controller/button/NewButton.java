package controller.button;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.text.Font;
import product.Product;

public class NewButton implements controller.button.Button{

    private Button btn;

    public NewButton(String title, double layoutX, double layoutY, double width, double height) {
        btn = new Button();
        btn.setMaxWidth(width);
        btn.setMinWidth(width);
        btn.setMaxHeight(height);
        btn.setMinHeight(height);
        btn.setText(title);
        btn.setFont(Font.font(20));
        btn.setLayoutX(layoutX);
        btn.setLayoutY(layoutY);
    }

    @Override
    public Button getButton() {
        return btn;
    }


}
