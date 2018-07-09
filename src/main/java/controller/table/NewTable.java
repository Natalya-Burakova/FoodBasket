package controller.table;


import analysis.json.Parser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Font;
import product.Product;

public class NewTable implements Table {

    private static final int TABLE_SIZE_HEIGHT = 450;
    private static final int TABLE_COLUMN_WIDTH = 110;
    private TableView<Product> table;
    private ObservableList<Product> listProduct;

    public NewTable(){
        listProduct = FXCollections.observableArrayList();
    }

    public NewTable(Parser parser){
        listProduct = parser.getListProduct();
    }

    public void setTable(String col1, String col2, String col3, String col4, String tableNumber) {
        table = new TableView<Product>();

        table.setMinHeight(TABLE_SIZE_HEIGHT);
        table.setMaxHeight(TABLE_SIZE_HEIGHT);

        TableColumn<Product, String> column1 = new TableColumn<Product, String>(col1);
        column1.setMaxWidth(TABLE_COLUMN_WIDTH);
        column1.setMinWidth(TABLE_COLUMN_WIDTH);
        column1.setResizable(false);

        TableColumn<Product, String> column2 = new TableColumn<Product, String>(col2);
        column2.setMaxWidth(TABLE_COLUMN_WIDTH + 45);
        column2.setMinWidth(TABLE_COLUMN_WIDTH + 45);
        column2.setResizable(false);

        TableColumn<Product, String> column3 = new TableColumn<Product, String>(col3);
        column3.setMaxWidth(TABLE_COLUMN_WIDTH+100);
        column3.setMinWidth(TABLE_COLUMN_WIDTH+100);
        column3.setResizable(false);

        TableColumn<Product, String> column4 = new TableColumn<Product, String>(col4);
        column4.setMaxWidth(TABLE_COLUMN_WIDTH);
        column4.setMinWidth(TABLE_COLUMN_WIDTH);
        column4.setResizable(false);

        table.getColumns().addAll(column1, column2, column3, column4);

        if (tableNumber.equals("1")) {
            column1.setCellValueFactory((TableColumn.CellDataFeatures<Product, String> cellData) -> cellData.getValue().getName());
            column2.setCellValueFactory((TableColumn.CellDataFeatures<Product, String> cellData) -> cellData.getValue().getCategory());
            column3.setCellValueFactory((TableColumn.CellDataFeatures<Product, String> cellData) -> cellData.getValue().getInfo());
            column4.setCellValueFactory((TableColumn.CellDataFeatures<Product, String> cellData) -> cellData.getValue().getPrice());
        }
        else {
            column1.setCellValueFactory((TableColumn.CellDataFeatures<Product, String> cellData) -> cellData.getValue().getName());
            column2.setCellValueFactory((TableColumn.CellDataFeatures<Product, String> cellData) -> cellData.getValue().getCount());
            column3.setCellValueFactory((TableColumn.CellDataFeatures<Product, String> cellData) -> cellData.getValue().getWeight());
            column4.setCellValueFactory((TableColumn.CellDataFeatures<Product, String> cellData) -> cellData.getValue().getPrice());
        }

        table.setItems(listProduct);
    }

    @Override
    public TableView getTable() {
        return table;
    }


}
