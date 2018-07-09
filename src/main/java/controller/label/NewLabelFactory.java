package controller.label;



public class NewLabelFactory {

    public static Label makeLabel(String title, int font, double layoutX, double layoutY) {
        return new NewLabel(title, font, layoutX, layoutY);
    }
}
