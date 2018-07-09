package controller.table;


import analysis.json.Parser;
import javafx.scene.control.TableView;

public class NewTableFactory {

    public static TableView makeTable(Parser parser) {
        NewTable table = new NewTable(parser);
        table.setTable("Name", "Category", "Info", "Price", "1");
        return table.getTable();
    }

    public static TableView makeTable() {
        NewTable table = new NewTable();
        table.setTable("Name", "Count" , "Weight", "Price", "2");
        return table.getTable();
    }
}
