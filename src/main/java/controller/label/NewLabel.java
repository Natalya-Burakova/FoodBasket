package controller.label;


import javafx.scene.control.Label;
import javafx.scene.text.Font;

public class NewLabel implements controller.label.Label{


    private Label label;

    public NewLabel(String title,int font, double layoutX, double layoutY) {
        label = new Label();
        label.setText(title);
        label.setFont(Font.font(font));
        label.setLayoutX(layoutX);
        label.setLayoutY(layoutY);
    }


    @Override
    public Label getLable() {
        return label;
    }
}
