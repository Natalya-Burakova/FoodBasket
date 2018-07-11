package controller.button;


public class NewButtonFactory {

    public static Button makeButton(String title, double layoutX, double layoutY, double width, double height) {
        return new NewButton(title, layoutX, layoutY, width, height);
    }

}
