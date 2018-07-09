package product.category;


public class FruitsAndVegetablesProducts implements Category{

    private static final FruitsAndVegetablesProducts NEW_INSTANCE = new FruitsAndVegetablesProducts();
    private static final String NAME_CATEGORY = "Fruits and vegetables";

    private FruitsAndVegetablesProducts(){}

    public static Category getInstance() {
        return NEW_INSTANCE;
    }

    @Override
    public String getName() {
        return NAME_CATEGORY;
    }

}
