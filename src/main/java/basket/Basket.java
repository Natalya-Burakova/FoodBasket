package basket;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import product.Product;

public class Basket {

    private ObservableList listBasket;

    public Basket() {
        listBasket = FXCollections.observableArrayList();
    }

    public void addBasket(Product product){
        listBasket.add(product);
    }

    public Product getObjectListBasket(int index){
        return (Product) listBasket.get(index);
    }

    public ObservableList getListBasket(){
        return listBasket;
    }
}
