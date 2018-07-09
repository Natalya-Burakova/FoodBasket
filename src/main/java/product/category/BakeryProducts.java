package product.category;

//хлебобулочные
public class BakeryProducts implements Category {

    private static final BakeryProducts NEW_INSTANCE = new BakeryProducts();
    private static final String NAME_CATEGORY = "Bakery";

    private BakeryProducts(){}

    public static Category getInstance() {
        return NEW_INSTANCE;
    }

    @Override
    public String getName() {
        return NAME_CATEGORY;
    }

}
