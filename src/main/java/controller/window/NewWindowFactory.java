package controller.window;

public class NewWindowFactory {

    public static Window makeWindow(int width, int height) {
        return new NewWindow(width, height);
    }
}
