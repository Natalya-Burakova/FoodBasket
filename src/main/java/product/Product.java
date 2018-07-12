package product;

import com.sun.javafx.binding.StringFormatter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import product.category.*;

public class Product {

    private StringProperty name;
    private StringProperty category;
    private StringProperty info;
    private StringProperty price;
    private StringProperty count;
    private StringProperty weight;

    private Category categoryProduct;


    public Product(String name, Category category, String info, String price) {
        this.name = new SimpleStringProperty(name);
        this.category = new SimpleStringProperty(category.toString());
        this.categoryProduct = category;
        this.info = new SimpleStringProperty(info);
        this.price = new SimpleStringProperty(price);
        this.count = new SimpleStringProperty();
        this.weight = new SimpleStringProperty();
    }

    public void setPrice(double price) {
        this.price = new SimpleStringProperty(String.format("%.2f",price).replace(',','.'));
    }

    public void setCount(String count) {
        this.count = new SimpleStringProperty(count);
    }

    public void setWeight(String weight) {
        this.weight = new SimpleStringProperty(weight);
    }

    public Category getCategoryProduct() { return categoryProduct; }

    public StringProperty getName() {
        return name;
    }

    public StringProperty getCategory() {
        return category;
    }

    public StringProperty getInfo() {
        return info;
    }

    public StringProperty getPrice() {
        return price;
    }

    public StringProperty getCount() {
        return count;
    }

    public StringProperty getWeight() {
        return weight;
    }

}
