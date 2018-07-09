package analysis.json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import product.Product;
import product.category.Category;
import product.category.MakeCategory;


public class Parser {
    private ObservableList<Product> listProduct;
    private String jsonLine;

    public Parser(String jsonLine) {
        this.jsonLine = jsonLine;
    }

    public void parce() {
        listProduct = FXCollections.observableArrayList();
        Gson gson = new Gson();
        JsonArray data = gson.fromJson(jsonLine, JsonObject.class).getAsJsonObject().get("products").getAsJsonObject().get("product").getAsJsonArray();
        for (int i = 0; i<data.size(); i++) {
            JsonObject jsonObject = data.get(i).getAsJsonObject();
            String name = jsonObject.get("name").getAsString();
            String category = jsonObject.get("category").getAsString();
            String info = jsonObject.get("info").getAsString();
            String price = jsonObject.get("price").getAsString();
            //определяем нужную категорию
            Category categoryProduct  = MakeCategory.makeCategory(category);
            //cоздаем объект - продукт
            Product product = new Product(name, categoryProduct, info, price);
            listProduct.add(product);
        }
    }

    public ObservableList<Product> getListProduct() {
        return listProduct;
    }

}
